package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import static org.junit.Assert.*;

public class ResourceSourceTest {
    private ResourceSource resourceSource;

    private Player player1;
    private Player player2;
    private  Player player3;
    private Player player4;

    @Before
    public void setUp() {
        player1 = new Player(new PlayerOrder(0, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player2 = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player3 = new Player(new PlayerOrder(2, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player4 = new Player(new PlayerOrder(3,4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
    }

    @Test
    public void testHunting(){

        resourceSource = new ResourceSource(Effect.FOOD, 4);

        assertTrue(resourceSource.placeFigures(player1, 5));
        assertTrue(resourceSource.placeFigures(player2, 5));
        assertTrue(resourceSource.placeFigures(player3, 5));
        assertTrue(resourceSource.placeFigures(player4, 5));

        assertFalse(resourceSource.placeFigures(player1, 1));
        assertFalse(resourceSource.placeFigures(player2, 1));
        assertFalse(resourceSource.placeFigures(player3, 1));
        assertFalse(resourceSource.placeFigures(player4, 1));

        assertFalse(resourceSource.newTurn());

        //Give players tools
        player1.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player2.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player3.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player4.getPlayerBoard().giveEffect(List.of(Effect.TOOL));

        //Players actions
        List <Effect> output = new ArrayList<>();
        List<Effect> input = List.of(Effect.TOOL);

        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player1, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player1, input, output));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player2, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player2, output, output));
        assertEquals(ActionResult.FAILURE, resourceSource.makeAction(player1, input, output));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player3, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player3, input, output));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player4, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player4, input, output));

        assertFalse(resourceSource.newTurn());
        //More complex tests for ResourceSource used in GameBoardComponentTest
    }

    @Test
    public void testRiverAndLessPlayers(){
        resourceSource = new ResourceSource(Effect.GOLD, 3);

        assertTrue(resourceSource.placeFigures(player1, 3));
        assertTrue(resourceSource.placeFigures(player2, 2));


        assertFalse(resourceSource.placeFigures(player3, 3));
        assertFalse(resourceSource.placeFigures(player3, 2));

        assertFalse(resourceSource.newTurn());

        //Players actions
        List <Effect> output = new ArrayList<>();
        List <Effect> input = List.of(Effect.TOOL);

        //Give player tools
        player1.getPlayerBoard().giveEffect(List.of(Effect.TOOL));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, resourceSource.tryToMakeAction(player1));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player1, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player1, input, output));

        assertFalse(resourceSource.newTurn());

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, resourceSource.tryToMakeAction(player2));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(player2, input, output));
        assertEquals(ActionResult.FAILURE, resourceSource.makeAction(player2, input, output));

        assertFalse(resourceSource.newTurn());
        //More complex tests for ResourceSource used in GameBoardComponentTest
    }
}
