package sk.uniba.fmph.dcs.game_phase_controller;

import junit.framework.TestCase;
import org.junit.Test;

import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class FeedTribeStateTest extends TestCase {
private List<Player> tribes;
private Player player1;
private Player player2;
private Player player3;
    public void setUp(){
        tribes = new ArrayList<>();
        player1 = new Player(new PlayerOrder(0, 3),new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player2 = new Player(new PlayerOrder(1, 3),new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player3 = new Player(new PlayerOrder(2, 3),new PlayerBoardGameBoardFacade(new PlayerBoard()));
        tribes.add(player1);
        tribes.add(player2);
        tribes.add(player3);
    }

    public void testFeedTribeSuccess(){

        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        //Feed tribe with given resources
        ActionResult result1 = feedTribeState.feedTribe(player1.getPlayerOrder(), List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD));
        assertEquals(ActionResult.ACTION_DONE, result1);

        ActionResult result2 = feedTribeState.feedTribe(player2.getPlayerOrder(), List.of(Effect.FOOD));
        assertEquals(ActionResult.FAILURE, result2);

        ActionResult result3 = feedTribeState.feedTribe(player3.getPlayerOrder(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));
        assertEquals(ActionResult.ACTION_DONE, result3);
    }

    public void testDoNotFeedThisTurn(){

        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        //DoNotFeedThisTurn when tribe has been already fed
        assertEquals(ActionResult.ACTION_DONE, feedTribeState.feedTribe(player1.getPlayerOrder(),List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD)));
        ActionResult result1 = feedTribeState.doNotFeedThisTurn(player1.getPlayerOrder());
        assertEquals(ActionResult.FAILURE, result1);

        //DoNotFeedThisTurn action
        for(int i = 0; i < 9; i++){
            //Delete starter food
            assertTrue(player3.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        feedTribeState.feedTribe(player2.getPlayerOrder(), List.of(Effect.FOOD));
        ActionResult result2 = feedTribeState.doNotFeedThisTurn(player2.getPlayerOrder());
        assertEquals(ActionResult.ACTION_DONE, result2);

        //Feed tribe with other resources, because player doesn't have enough food
        feedTribeState.feedTribe(player3.getPlayerOrder(), List.of(Effect.FOOD, Effect.CLAY, Effect.STONE, Effect.WOOD, Effect.GOLD));
        ActionResult result3 = feedTribeState.doNotFeedThisTurn(player3.getPlayerOrder());
        assertEquals(ActionResult.FAILURE, result3);
    }

    public void testAutomaticAction(){
        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        //Try to make automatic action
        HasAction result1 = feedTribeState.tryToMakeAutomaticAction(player1.getPlayerOrder());
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, result1);

        //Try to make automatic action when tribe has been already fed
        assertEquals(ActionResult.ACTION_DONE, feedTribeState.feedTribe(player2.getPlayerOrder(),List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD)));
        HasAction result2 = feedTribeState.tryToMakeAutomaticAction(player2.getPlayerOrder());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result2);

        //Try to do automatic action while tribe doesn't have enough food
        for(int i = 0; i < 9; i++){
            //Delete starter food
            assertTrue(player3.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
        }
        HasAction result3 = feedTribeState.tryToMakeAutomaticAction(player3.getPlayerOrder());
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, result3);

        //Try to make automatic action after player fed his tribe
        assertEquals(ActionResult.ACTION_DONE, feedTribeState.feedTribe(player3.getPlayerOrder(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD)));
        HasAction result4 = feedTribeState.tryToMakeAutomaticAction(player3.getPlayerOrder());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result4);
    }

}