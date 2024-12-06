package sk.uniba.fmph.dcs.stone_age;

import junit.framework.TestCase;
import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.*;
import sk.uniba.fmph.dcs.game_board.InterfaceFigureLocationInternal;

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
        *
        * Then player with ID == 0 will try to make AutomaticAction, but he has
        * his figures placed on ResourceSource instance, so GamePhase will return
        * WAITING_FOR_PLAYER_ACTION, because player need to decide to use tools
        * or no.
        * */

        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(1, Location.QUARY, 3));
        assertTrue(game.placeFigures(2, Location.BUILDING_TILE1, 1));
        assertTrue(game.placeFigures(3, Location.CIVILISATION_CARD1, 1));

        /*
        * player with ID == 0 decided not to use any tools.
        *
        * player with ID == 1 also put his figures on ResourceSource instance, so again
        * GamePhase will return WAITING_FOR_PLAYER_ACTION, because player need to decide to use tools
        * or no. player with ID == 1 decided to use two tools from his PlayerBoard
        *
        * For player with ID == 2 we try to make action, but we failed because he doesn't have enough
        * resources to pay for building, so we need to skip this player action
        *
        *player with ID == 3 buy card on place 1
        * */
        //player(ID == 0) make his action without tools
        assertTrue(game.makeAction(0, Location.HUNTING_GROUNDS, List.of(new Effect[0]), List.of(new Effect[0])));
        //player(ID == 1) make his action and used his tools
        playersMap.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL, Effect.TOOL, Effect.TOOL));
        assertTrue(game.makeAction(1, Location.QUARY, List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
        //player(ID == 2) try to make action, but doesn't have enough resources, so we skip his action
        assertFalse(game.makeAction(2, Location.BUILDING_TILE1, List.of(new Effect[0]), List.of(new Effect[0])));
        assertTrue(game.skipAction(2, Location.BUILDING_TILE1));
        //player(ID == 3) decided to buy this card
        playersMap.get(3).getPlayerBoard().giveEffect(List.of(Effect.STONE));
        assertTrue(game.makeAction(3, Location.CIVILISATION_CARD1, List.of(Effect.STONE), List.of(new Effect[0])));
        /*First round all players have 12 starter food, so every player fed his tribe,
        * which was done automatically.
        * */
        /*New round begins and players again decide for what location put their figures:
        * player(ID == 1) - put his figure on Hut
        * player(ID == 2) - put his figure on Field
        * player(ID == 3) - put his figure on ToolMaker
        * player(ID == 4) - put his figure on Building3
        * also player order has changed, now starter player == player(ID == 1)
        *
        * player(ID == 0), player(ID == 1), player(ID == 2) all actions must be performed
        * automatically.
        * */
        assertTrue(game.placeFigures(1, Location.HUT,1));
        assertTrue(game.placeFigures(2, Location.FIELD,1));
        assertTrue(game.placeFigures(3, Location.TOOL_MAKER,1));
        assertTrue(game.placeFigures(0, Location.BUILDING_TILE3,1));
        //player(ID == 0) want to buy that building, but after that players will
        //feed their tribes, so assume two of them doesn't have enough food
        //one won't feed his tribe, another will feed with other resources
        for(int i = 0; i < 7; i++){
            playersMap.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD));
            playersMap.get(2).getPlayerBoard().takeResources(List.of(Effect.FOOD));
        }
        for(int i = 0; i < 5; i++){
            playersMap.get(2).getPlayerBoard().giveEffect(List.of(Effect.STONE));
        }
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
        //Before last action we will make one place for buildings empty
        Field stackOfBuildings = BuildingTile.class.getDeclaredField("buildings");
        stackOfBuildings.setAccessible(true);
        Stack<Building> testBuilding = (Stack<Building>) stackOfBuildings.get(testTile);
        while (!testBuilding.isEmpty()){
            testBuilding.pop();
        }
        //Game ended and we add EndOfGamePoints for all players
        assertTrue(game.feedTribe(2, List.of(Effect.STONE,Effect.STONE,Effect.STONE,Effect.STONE,Effect.STONE)));
    }

}