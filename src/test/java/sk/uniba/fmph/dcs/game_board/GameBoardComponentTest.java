package sk.uniba.fmph.dcs.game_board;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.assertTrue;

public class GameBoardComponentTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private GameBoard gameBoard;

    public void setUp(){
        player1 = new Player(new PlayerOrder(0, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player2 = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player3 = new Player(new PlayerOrder(2, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player4 = new Player(new PlayerOrder(3, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        gameBoard = new GameBoard(List.of(player1, player2, player3, player4));
    }

    public void testInitializationOfGameBoardAndState() throws NoSuchFieldException, IllegalAccessException {

        assertNotNull(gameBoard.getAllLocations().get(Location.FIELD));
        assertNotNull(gameBoard.getAllLocations().get(Location.HUT));
        assertNotNull(gameBoard.getAllLocations().get(Location.TOOL_MAKER));

        assertNotNull(gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS));
        assertNotNull(gameBoard.getAllLocations().get(Location.FOREST));
        assertNotNull(gameBoard.getAllLocations().get(Location.CLAY_MOUND));
        assertNotNull(gameBoard.getAllLocations().get(Location.QUARRY));
        assertNotNull(gameBoard.getAllLocations().get(Location.RIVER));

        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE1));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE2));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE3));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE4));

        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4));

    }

    public void testFieldsInteractions(){
        InterfaceFigureLocation field = gameBoard.getAllLocations().get(Location.FIELD);
        InterfaceFigureLocation toPlaceFigures = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Trying to place more than one figure on fields
        assertEquals(HasAction.NO_ACTION_POSSIBLE, field.tryToPlaceFigures(player1.getPlayerOrder(), 2));
        //Player have 0 figures to place
        assertTrue(toPlaceFigures.placeFigures(player2.getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, field.tryToPlaceFigures(player2.getPlayerOrder(), 1));
        //Fields already have figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, field.tryToPlaceFigures(player1.getPlayerOrder(), 1));
        assertFalse(field.placeFigures(player1.getPlayerOrder(), 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, field.tryToPlaceFigures(player3.getPlayerOrder(), 1));
        assertFalse(field.placeFigures(player3.getPlayerOrder(), 1));

        //Player receive one fieldPoint
        assertEquals(ActionResult.ACTION_DONE, field.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FIELD)));
        assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FIELD)));
    }

    public void testHutInteractions(){
        InterfaceFigureLocation hut = gameBoard.getAllLocations().get(Location.HUT);
        InterfaceFigureLocation toPlaceFigures = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Trying to place more than one figure on hut
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(player1.getPlayerOrder(), 6));
        //Player have 0 figures to place
        assertTrue(toPlaceFigures.placeFigures(player2.getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(player2.getPlayerOrder(), 2));
        //Hut already have figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hut.tryToPlaceFigures(player1.getPlayerOrder(), 2));
        assertFalse(hut.placeFigures(player1.getPlayerOrder(), 2));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(player3.getPlayerOrder(), 2));
        assertFalse(hut.placeFigures(player3.getPlayerOrder(), 1));

        //Player receive one figure
        assertEquals(ActionResult.ACTION_DONE, hut.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertFalse(((PlayerBoardGameBoardFacade) player1.getPlayerBoard()).newTurn());
        assertTrue(player1.getPlayerBoard().hasFigures(6));
        assertTrue(player1.getPlayerBoard().takeFigures(6));
        assertFalse(player1.getPlayerBoard().hasFigures(1));
    }

    public void testToolMakerInteractions(){
        InterfaceFigureLocation toolMaker = gameBoard.getAllLocations().get(Location.TOOL_MAKER);
        InterfaceFigureLocation toPlaceFigures = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Trying to place more than one figure on toolMaker
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(player1.getPlayerOrder(), 2));
        //Player have 0 figures to place
        assertTrue(toPlaceFigures.placeFigures(player2.getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(player2.getPlayerOrder(), 1));
        //Hut already have figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, toolMaker.tryToPlaceFigures(player1.getPlayerOrder(), 1));
        assertFalse(toolMaker.placeFigures(player1.getPlayerOrder(), 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(player3.getPlayerOrder(), 1));
        assertFalse(toolMaker.placeFigures(player3.getPlayerOrder(), 1));

        //Player receive one tool
        assertEquals(ActionResult.ACTION_DONE, toolMaker.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertTrue(player1.getPlayerBoard().hasSufficientTools(1));
        assertEquals(Optional.of(1), player1.getPlayerBoard().useTool(0));
        assertFalse(player1.getPlayerBoard().hasSufficientTools(1));
    }

    public void testHuntingFields() throws NoSuchFieldException, IllegalAccessException {
        InterfaceFigureLocation hunting_grounds = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Try to place figures, which player doesn't have
        assertFalse(hunting_grounds.placeFigures(player3.getPlayerOrder(), 6));

        //Try to place figures after player has already placed some
        assertTrue(hunting_grounds.placeFigures(player1.getPlayerOrder(), 3));
        assertFalse(hunting_grounds.placeFigures(player1.getPlayerOrder(), 1));

        //Try to place almost all figures, because hunting grounds doesn't have limit
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hunting_grounds.tryToPlaceFigures(player2.getPlayerOrder(), 5));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hunting_grounds.tryToPlaceFigures(player3.getPlayerOrder(), 5));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hunting_grounds.tryToPlaceFigures(player4.getPlayerOrder(), 5));

        //Try to make action without tools
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, hunting_grounds.tryToMakeAction(player1.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, hunting_grounds.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        //We get sum which have player on his dices
        Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
        location.setAccessible(true);

        Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
        currentThrow.setAccessible(true);

        Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
        dicesResults.setAccessible(true);

        ResourceSource rs = (ResourceSource)location.get(hunting_grounds);
        CurrentThrow round =  (CurrentThrow) currentThrow.get(rs);
        int results = (int) dicesResults.get(round);
        int countOfFood = results/Effect.FOOD.points();
        //-------------------------------------------------------------------------------------
        //First take all starter food(12 points)
        for(int i = 0; i < 12; i++){
            assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        //Then check if player get specify countOfFood food points
        assertEquals(ActionResult.ACTION_DONE, hunting_grounds.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hunting_grounds.tryToMakeAction(player1.getPlayerOrder()));
        for(int i = 0; i < countOfFood; i++){
            assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));

        //Player want to use some tools
        player2.getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,hunting_grounds.tryToMakeAction(player2.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, hunting_grounds.makeAction(player2.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        rs = (ResourceSource)location.get(hunting_grounds);
        round =  (CurrentThrow) currentThrow.get(rs);
        results = (int) dicesResults.get(round);
        countOfFood = (results + 2)/Effect.FOOD.points();
        //First take all starter food(12 points)
        for(int i = 0; i < 12; i++){
            assertTrue(player2.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        assertFalse(player2.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        //Then check if player get specify countOfFood food points
        assertEquals(ActionResult.ACTION_DONE, hunting_grounds.makeAction(player2.getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE,hunting_grounds.tryToMakeAction(player2.getPlayerOrder()));
        for(int i = 0; i < countOfFood; i++){
            assertTrue(player2.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        assertFalse(player2.getPlayerBoard().takeResources(List.of(Effect.FOOD)));

        //We still have figures of player3 and player4, try newTurn()
        assertFalse(hunting_grounds.newTurn());
        //Just move them from the board
        assertFalse(hunting_grounds.skipAction(player3.getPlayerOrder()));
        assertFalse(hunting_grounds.skipAction(player4.getPlayerOrder()));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,hunting_grounds.tryToMakeAction(player3.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, hunting_grounds.makeAction(player3.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(ActionResult.ACTION_DONE, hunting_grounds.makeAction(player3.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE,hunting_grounds.tryToMakeAction(player3.getPlayerOrder()));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,hunting_grounds.tryToMakeAction(player4.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, hunting_grounds.makeAction(player4.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(ActionResult.ACTION_DONE, hunting_grounds.makeAction(player4.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE,hunting_grounds.tryToMakeAction(player4.getPlayerOrder()));

        assertFalse(hunting_grounds.newTurn());
    }

    public void testQuarry() throws NoSuchFieldException, IllegalAccessException {
        InterfaceFigureLocation quarry = gameBoard.getAllLocations().get(Location.QUARRY);

        //Try to place figures, which player doesn't have
        assertFalse(quarry.placeFigures(player3.getPlayerOrder(), 6));

        //Try to place figures after player has already placed some
        assertTrue(quarry.placeFigures(player1.getPlayerOrder(), 3));
        assertFalse(quarry.placeFigures(player1.getPlayerOrder(), 1));

        //Try to place almost all figures, quarry like others have limit, which is 7 figures
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, quarry.tryToPlaceFigures(player2.getPlayerOrder(), 4));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToPlaceFigures(player3.getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToPlaceFigures(player4.getPlayerOrder(), 5));

        //Try to make action without tools
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, quarry.tryToMakeAction(player1.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, quarry.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

        //We get sum which have player on his dices
        Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
        location.setAccessible(true);

        Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
        currentThrow.setAccessible(true);

        Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
        dicesResults.setAccessible(true);

        ResourceSource rs = (ResourceSource)location.get(quarry);
        CurrentThrow round =  (CurrentThrow) currentThrow.get(rs);
        int results = (int) dicesResults.get(round);
        int countOfStone = results/Effect.STONE.points();
        //-------------------------------------------------------------------------------------

        //Then check if player get specify countOfStone stone points
        assertEquals(ActionResult.ACTION_DONE, quarry.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToMakeAction(player1.getPlayerOrder()));
        for(int i = 0; i < countOfStone; i++){
            assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.STONE)));
        }
        assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.STONE)));

        //Player want to use some tools
        player2.getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, quarry.tryToMakeAction(player2.getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, quarry.makeAction(player2.getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

        rs = (ResourceSource)location.get(quarry);
        round =  (CurrentThrow) currentThrow.get(rs);
        results = (int) dicesResults.get(round);
        countOfStone = (results + 2)/Effect.STONE.points();
        //Then check if player get specify countOfFood stone points
        assertEquals(ActionResult.ACTION_DONE, quarry.makeAction(player2.getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE,quarry.tryToMakeAction(player2.getPlayerOrder()));
        for(int i = 0; i < countOfStone; i++){
            assertTrue(player2.getPlayerBoard().takeResources(List.of(Effect.STONE)));
        }
        assertFalse(player2.getPlayerBoard().takeResources(List.of(Effect.STONE)));

        //We don't have any figure left, and can't place more
        assertFalse(quarry.newTurn());
    }

    public void testCivilizationCardPlaceInteraction() throws NoSuchFieldException, IllegalAccessException {
        InterfaceFigureLocation cardPl1 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1);
        InterfaceFigureLocation cardPl2 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2);
        InterfaceFigureLocation cardPl3 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3);
        InterfaceFigureLocation cardPl4 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4);

        //Try to place figures, which player doesn't have
        assertFalse(cardPl1.placeFigures(player3.getPlayerOrder(), 6));

        //Try to place figures more than one
        assertFalse(cardPl1.placeFigures(player1.getPlayerOrder(), 3));

        //Try to place figures after player has already placed some
        assertTrue(cardPl1.placeFigures(player1.getPlayerOrder(), 1));
        assertFalse(cardPl1.placeFigures(player1.getPlayerOrder(), 1));

        //Try another player to place figure
        assertFalse(cardPl1.placeFigures(player2.getPlayerOrder(), 1));

        //Place one figure of player2 on cardPlace 2
        assertTrue(cardPl2.placeFigures(player2.getPlayerOrder(), 1));

        assertEquals(HasAction.NO_ACTION_POSSIBLE, cardPl1.tryToMakeAction(player2.getPlayerOrder()));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, cardPl1.tryToMakeAction(player1.getPlayerOrder()));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, cardPl2.tryToMakeAction(player3.getPlayerOrder()));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, cardPl2.tryToMakeAction(player2.getPlayerOrder()));


        player1.getPlayerBoard().giveEffect(List.of(Effect.STONE, Effect.GOLD));

        //player2 try to make action while he doesn't have any figure
        assertEquals(ActionResult.FAILURE, cardPl1.makeAction(player2.getPlayerOrder(), List.of(new Effect[0]), List.of(Effect.STONE, Effect.WOOD)));
        //player1 try to make action with empty and bad input resources
        assertEquals(ActionResult.FAILURE, cardPl1.makeAction(player1.getPlayerOrder(), List.of(new Effect[0]), List.of(Effect.STONE, Effect.WOOD)));
        assertEquals(ActionResult.FAILURE, cardPl1.makeAction(player1.getPlayerOrder(), List.of(Effect.FOOD), List.of(Effect.STONE, Effect.WOOD)));
        //player1 have good input but doesn't have that resources in his PlayerBoard
        assertEquals(ActionResult.FAILURE, cardPl1.makeAction(player1.getPlayerOrder(), List.of(Effect.WOOD), List.of(Effect.STONE, Effect.WOOD)));
        //player1 make action with everything good
        assertEquals(ActionResult.ACTION_DONE, cardPl1.makeAction(player1.getPlayerOrder(), List.of(Effect.STONE),List.of(Effect.STONE, Effect.WOOD)));

        //Player1 try to skip action after he has already made it
        assertFalse(cardPl1.skipAction(player1.getPlayerOrder()));
        //Player2 can skip action
        assertTrue(cardPl2.skipAction(player2.getPlayerOrder()));

        //Try to make new Turn()
        assertFalse(cardPl1.newTurn());
        assertFalse(cardPl2.newTurn());
        assertFalse(cardPl3.newTurn());
        assertFalse(cardPl4.newTurn());
    }

    public void testBuildingInteractions() throws NoSuchFieldException, IllegalAccessException {
        InterfaceFigureLocation buildingTile1 = gameBoard.getAllLocations().get(Location.BUILDING_TILE1);
        InterfaceFigureLocation buildingTile2 = gameBoard.getAllLocations().get(Location.BUILDING_TILE2);
        InterfaceFigureLocation buildingTile3 = gameBoard.getAllLocations().get(Location.BUILDING_TILE3);
        InterfaceFigureLocation buildingTile4 = gameBoard.getAllLocations().get(Location.BUILDING_TILE4);

        //We need to place figures somewhere
        InterfaceFigureLocation hunting_grounds = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);
        assertTrue(hunting_grounds.placeFigures(player1.getPlayerOrder(), 5));

        //Try to place figures, which player doesn't have
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player1.getPlayerOrder(), 1));

        //Try to place more than max. figures
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player2.getPlayerOrder(), 2));
        assertFalse(buildingTile4.placeFigures(player4.getPlayerOrder(), 2));

        //Try to place figure, when somebody has already placed one
        assertTrue(buildingTile1.placeFigures(player2.getPlayerOrder(), 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player3.getPlayerOrder(), 1));
        assertFalse(buildingTile1.placeFigures(player3.getPlayerOrder(), 1));

        //Good conditions
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, buildingTile2.tryToPlaceFigures(player3.getPlayerOrder(), 1));

        //Try to make action while player doesn't have figure on this buildingTile
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player3.getPlayerOrder()));
        //BuildingTile with no figures
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile3.tryToMakeAction(player3.getPlayerOrder()));

        //Good conditions
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToMakeAction(player2.getPlayerOrder()));

        //Test making actions
        Field intf = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
        intf.setAccessible(true);
        InterfaceFigureLocationInternal getBuildingInterface = (InterfaceFigureLocationInternal) intf.get(buildingTile1);

        BuildingTile bl1 = (BuildingTile) getBuildingInterface;
        Building building = bl1.getBuilding();

        //Set accessibility for points
        PlayerBoardGameBoardFacade playerBoard = (PlayerBoardGameBoardFacade) player2.getPlayerBoard();
        Field plPrivateBoard = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
        plPrivateBoard.setAccessible(true);
        PlayerBoard fromWhichBoard = (PlayerBoard) plPrivateBoard.get(playerBoard);
        Field needPoints = PlayerBoard.class.getDeclaredField("points");
        needPoints.setAccessible(true);


        //Test make action of buildings
        if(building instanceof SimpleBuilding){
            Field resources = SimpleBuilding.class.getDeclaredField("requiredResources");
            resources.setAccessible(true);
            //Add player2 resources which he needs to pay
            List<Effect> needToPay = (List<Effect>)resources.get(building);
            player2.getPlayerBoard().giveEffect(needToPay);
            OptionalInt points = building.build(needToPay);

            assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player2.getPlayerOrder(), needToPay, List.of(new Effect[0])));
            int playerPointsAfterAction = (int) needPoints.get(fromWhichBoard);
            assertEquals(points.getAsInt(), playerPointsAfterAction);

        } else if (building instanceof VariableBuilding) {
                Field resourcesCount = VariableBuilding.class.getDeclaredField("numberOfResources");
                resourcesCount.setAccessible(true);

                int numberOfResources = (int) resourcesCount.get(building);

                List<Effect> effects = new ArrayList<>();
                for(int i = 0; i < numberOfResources; i++){
                    if(i < 1){
                        effects.add(Effect.WOOD);
                    }else{
                        effects.add(Effect.STONE);
                    }
                }
                player2.getPlayerBoard().giveEffect(effects);
                OptionalInt points = building.build(effects);
                assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player2.getPlayerOrder(), effects,List.of(new Effect[0])));
                int variablePoints = (int) needPoints.get(fromWhichBoard);
                assertEquals(points.getAsInt(), variablePoints);
        }else{
            Field maxCount = ArbitraryBuilding.class.getDeclaredField("maxNumberOfResources");
            maxCount.setAccessible(true);

            int maxResources = (int) maxCount.get(building);

            List<Effect> effects = new ArrayList<>();
            for(int i = 0; i < maxResources; i++){
                effects.add(Effect.WOOD);
            }
            player2.getPlayerBoard().giveEffect(effects);
            OptionalInt points = building.build(effects);
            assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player2.getPlayerOrder(), effects,List.of(new Effect[0])));
            int variablePoints = (int) needPoints.get(fromWhichBoard);
            assertEquals(points.getAsInt(), variablePoints);
        }

        //Test skip action, when player doesn't have figure after action
        assertFalse(buildingTile1.skipAction(player2.getPlayerOrder()));
        //Test skip action, when there is no any figure
        assertFalse(buildingTile3.skipAction(player3.getPlayerOrder()));
        //Test skip action when there is a figure
        assertTrue(buildingTile4.placeFigures(player3.getPlayerOrder(), 1));
        assertTrue(buildingTile4.skipAction(player3.getPlayerOrder()));

        //Test newTurn
        assertFalse(buildingTile1.newTurn());
        assertFalse(buildingTile2.newTurn());
        assertFalse(buildingTile3.newTurn());
        assertFalse(buildingTile4.newTurn());

    }



}