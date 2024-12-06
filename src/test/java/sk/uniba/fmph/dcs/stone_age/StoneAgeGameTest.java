package sk.uniba.fmph.dcs.stone_age;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.game_board.*;

import java.lang.reflect.Field;
import java.util.*;

public class StoneAgeGameTest extends TestCase {
    private StoneAgeGame game;
    private GameBoard testBoard;
    private Map<Integer, Player> playersMap;
    private StoneAgeObservable observable;
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        observable= new StoneAgeObservable();
        game = new StoneAgeGame(4, observable);
        //Set accessibility to GameBoard
        Field gameBoard = StoneAgeGame.class.getDeclaredField("gameBoard");
        gameBoard.setAccessible(true);
        testBoard = (GameBoard) gameBoard.get(game);
        //Set accessibility to players
        Field gamePlayers = StoneAgeGame.class.getDeclaredField("players");
        gamePlayers.setAccessible(true);
        playersMap = (Map<Integer, Player>) gamePlayers.get(game);
    }

    public void testInitializationOfStoneAge() throws NoSuchFieldException, IllegalAccessException {

        Field gamePlayerForCreatingGamePhase = StoneAgeGame.class.getDeclaredField("forCreatingGamePhase");
        Field gameController = StoneAgeGame.class.getDeclaredField("gamePhaseController");
        Field observer = StoneAgeGame.class.getDeclaredField("observable");
        gamePlayerForCreatingGamePhase.setAccessible(true);
        gameController.setAccessible(true);
        observer.setAccessible(true);

        List<Player> toTestPlayers = (List<Player>) gamePlayerForCreatingGamePhase.get(game);
        InterfaceGamePhaseController testGameController = (InterfaceGamePhaseController) gameController.get(game);
        StoneAgeObservable testObserver = (StoneAgeObservable) observer.get(game);

        //Map and list must have players
        assertFalse(playersMap.isEmpty());
        assertFalse(toTestPlayers.isEmpty());
        //Controller, observer and gameBoard can't be null
        assertNotNull(testGameController);
        assertNotNull(testBoard);
        assertNotNull(testObserver);

        //toTestPlayer must contain all players from Map
        List<Player> playersFromMap = new ArrayList<>();
        for(Integer key: playersMap.keySet()){
            playersFromMap.add(playersMap.get(key));
        }
        assertEquals(toTestPlayers, playersFromMap);
    }

    public void testPlaceFigures() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        //Set accessibility to locations
        Field locations = GameBoard.class.getDeclaredField("allLocations");
        locations.setAccessible(true);
        Map<Location, InterfaceFigureLocation> testLocations = (Map<Location, InterfaceFigureLocation>) locations.get(testBoard);

        //Place figures of one player on location HuntingGrounds
        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(0,Location.HUNTING_GROUNDS, 2));
        //Place figures on another location in the same turn
        assertFalse(game.placeFigures(0, Location.BUILDING_TILE1, 1));
        //Try to place figures of player3 who should not place now
        assertFalse(game.placeFigures(2, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(2, Location.BUILDING_TILE1, 1));
        //Try to place figures of player2 who should place now
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 3));

        assertFalse(game.placeFigures(3, Location.HUNTING_GROUNDS, 2));
        assertTrue(game.placeFigures(2,Location.HUNTING_GROUNDS, 2));

        assertTrue(game.placeFigures(3, Location.HUNTING_GROUNDS, 3));
    }

    public void testIntegrationTest() throws NoSuchFieldException, IllegalAccessException {
        //Set accessibility to locations
        Field locations = GameBoard.class.getDeclaredField("allLocations");
        locations.setAccessible(true);
        Map<Location, InterfaceFigureLocation> testLocations = (Map<Location, InterfaceFigureLocation>) locations.get(testBoard);

        /*Place figures, try to make action will return WAITING_FOR_PLAYER_ACTION,
        * because player need to decide how many figures he wants to place.
        * Next phase Make Action phase will begin only, after all figures from all players
        * will be placed on GameBoard, so we will continue place them.
        *
        * Then player with ID == 0 will try to make AutomaticAction, but he has
        * his figures placed on ResourceSource instance, so GamePhase will return
        * WAITING_FOR_PLAYER_ACTION, because player need to decide to use tools
        * or no.
        * */

        //First figure place state
        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(1, Location.QUARRY, 3));
        assertTrue(game.placeFigures(2, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(3, Location.CIVILISATION_CARD1, 1));
        //Second figure place state
        assertTrue(game.placeFigures(0,Location.CLAY_MOUND, 2));
        assertTrue(game.placeFigures(1,Location.HUNTING_GROUNDS, 2));
        assertTrue(game.placeFigures(2,Location.QUARRY, 4));
        assertTrue(game.placeFigures(3,Location.FOREST, 4));

        /*
        * GamePhaseController will return WAITING_FOR_PLAYER_ACTION, because
        * player need to think use or not use tools
        *
        * player with ID == 0 can't use tools in location HUNTING_GROUNDS(doesn't have any).
        *
        * player with ID == 1 also put his figures on ResourceSource instance, so again
        * GamePhase will return WAITING_FOR_PLAYER_ACTION, and he also doesn't have any tool
        *
        * player with ID == 2 will receive one automatically tool and will use it in location QUARRY next time
        * when there will be his turn
        *
        *player with ID == 3 want to buy card on place 1, but first he will get resources in CLAY_MOUND
        * and again won't use any tool, because he doesn't have any.
        * */

        //player(ID == 0) make his action without tools in HUNTING_GROUNDS
        assertTrue(game.makeAction(0, Location.HUNTING_GROUNDS, List.of(new Effect[0]), List.of(new Effect[0])));
        //player (ID == 1) make his action without tools in QUARRY
        assertTrue(game.makeAction(1, Location.QUARRY, List.of(new Effect[0]), List.of(new Effect[0])));
        //player (ID == 2) can make his action automatically, because he decided to make action in TOOL_MAKER
        //player(ID == 3) firs decided to get some resources, then buy Civ.card, again his action will be without tools
        assertTrue(game.makeAction(3, Location.CLAY_MOUND, List.of(new Effect[0]), List.of(new Effect[0])));

        //New circle
        //player(ID == 0) now will collect resources from CLAY_MOUND, again without tools
        assertTrue(game.makeAction(0, Location.CLAY_MOUND, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 1) now will collect food from HUNTING_GROUNDS, again without tools
        assertFalse(game.makeAction(1, Location.HUNTING_GROUNDS, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 2) now will collect food from QUARRY, and use his new tool
        assertTrue(game.makeAction(2, Location.QUARRY, List.of(Effect.TOOL), List.of(new Effect[0])));
        //player(ID == 3) decided to buy this card
        assertTrue(game.makeAction(3, Location.CIVILISATION_CARD1, List.of(Effect.CLAY), List.of(new Effect[0])));
        /*First round all players have 12 starter food, so every player fed his tribe,
        * which was done automatically.
        * */
        /*New round begins and players again decide for what location put their figures:
        * player(ID == 1) - put his figure on Hut and HUNTING_GROUNDS
        * player(ID == 2) - put his figure on Field and RIVER
        * player(ID == 3) - put his figure on ToolMaker and QUARRY
        * player(ID == 4) - put his figure on Building3 and FOREST
        * also player order has changed, now starter player == player(ID == 1)
        * */
        //First phase
        assertTrue(game.placeFigures(1, Location.HUT,1));
        assertTrue(game.placeFigures(2, Location.FIELD,1));
        assertTrue(game.placeFigures(3, Location.TOOL_MAKER,1));
        assertTrue(game.placeFigures(0, Location.BUILDING_TILE3,1));
        //Second phase
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS,1));
        assertTrue(game.placeFigures(2, Location.RIVER,4));
        assertTrue(game.placeFigures(3, Location.QUARRY,4));
        assertTrue(game.placeFigures(0, Location.FOREST,4));

        //player(ID == 1) decided to make action on HUT and take one figure
        assertTrue(game.makeAction(1, Location.HUT, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 2) decided firs to make some GOLD with his tools
        assertTrue(game.makeAction(2, Location.RIVER, List.of(Effect.TOOL), List.of(new Effect[0])));
        //player(ID == 3) make action in TOOL_MAKER to get one tool
        assertTrue(game.makeAction(3, Location.TOOL_MAKER, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 0) make action in FOREST to get some wood
        assertTrue(game.makeAction(0, Location.TOOL_MAKER, List.of(new Effect[0]), List.of(new Effect[0])));

        //Now player (ID == 1) want to make food in HUNTING_GROUNDS, without tools, because he doesn't have any
        assertTrue(game.makeAction(1, Location.HUNTING_GROUNDS, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 2) make action field, which can be done automatically
        //player(ID == 3) want to make action in QUARRY with his new tool
        assertTrue(game.makeAction(3, Location.QUARRY,List.of(Effect.TOOL),List.of(new Effect[0])));
        /*After the action of player(ID == 0) FeedTribe state will automatically begin,
        * so we assume that:
        * one player doesn't have enough food to feed his tribe, and decided not to feed.
        * another also doesn't have enough food, but will food with his resources.
        * */
        for(int i = 0; i < 4; i++){
            playersMap.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD));
            playersMap.get(2).getPlayerBoard().takeResources(List.of(Effect.FOOD));
        }
        //player(ID == 0) decided to buy building, on which he placed his figure
        Field loc = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
        loc.setAccessible(true);
        FigureLocationAdaptor fromWhich = (FigureLocationAdaptor) testLocations.get(Location.BUILDING_TILE3);
        BuildingTile testTile = (BuildingTile) loc.get(fromWhich);
        Building building = testTile.getBuilding();
        if(building instanceof SimpleBuilding){
            Field resources = SimpleBuilding.class.getDeclaredField("requiredResources");
            resources.setAccessible(true);
            List<Effect> needToPay = (List<Effect>)resources.get(building);
            playersMap.get(0).getPlayerBoard().giveEffect(needToPay);
            assertTrue(game.makeAction(0, Location.BUILDING_TILE3, needToPay, List.of(new Effect[0])));
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
            playersMap.get(0).getPlayerBoard().giveEffect(effects);
            assertTrue(game.makeAction(0, Location.BUILDING_TILE3, effects, List.of(new Effect[0])));
        }else{
            Field maxCount = ArbitraryBuilding.class.getDeclaredField("maxNumberOfResources");
            maxCount.setAccessible(true);

            int maxResources = (int) maxCount.get(building);

            List<Effect> effects = new ArrayList<>();
            for(int i = 0; i < maxResources; i++){
                effects.add(Effect.WOOD);
            }
            playersMap.get(0).getPlayerBoard().giveEffect(effects);
            assertTrue(game.makeAction(0, Location.BUILDING_TILE3, effects, List.of(new Effect[0])));
        }

        /*All players fed their tribes, player(ID == 1) decided not  to feed and lost 10 points*/
        assertTrue(game.doNotFeedThisTurn(1));
        //Before last action we will make one place for buildings empty, to trigger EndOfGame state
        Field stackOfBuildings = BuildingTile.class.getDeclaredField("buildings");
        stackOfBuildings.setAccessible(true);
        Stack<Building> testBuilding = (Stack<Building>) stackOfBuildings.get(testTile);
        while (!testBuilding.isEmpty()){
            testBuilding.pop();
        }
        //Game ended and we count for each player his points.
        assertTrue(game.feedTribe(2, List.of(Effect.STONE,Effect.STONE,Effect.STONE,Effect.STONE,Effect.STONE)));
    }

}