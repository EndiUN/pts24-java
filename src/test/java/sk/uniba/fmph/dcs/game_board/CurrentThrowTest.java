package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CurrentThrowTest {

    private CurrentThrow currentThrow;
    private Player mockPlayer;
    private Effect mockEffect;

    private static class MockPlayerBoard implements InterfacePlayerBoardGameBoard {
        private boolean hasFiguresResponse = true;
        private Collection<Effect> resources;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.empty();
        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {

        }

        @Override
        public void giveFigure() {

        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {

        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return false;
        }

        @Override
        public boolean takeFigures(int count) {
            return false;
        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }
    @Before
    public void setUp() {
        currentThrow = new CurrentThrow();

        //MockPlayerBoard mockPlayerBoard = new MockPlayerBoard();
        PlayerBoard playerBoard = new PlayerBoard();
        InterfacePlayerBoardGameBoard board = new PlayerBoardGameBoardFacade(playerBoard);
        // Mocking a player
        mockPlayer = new Player(new PlayerOrder(0, 3), board);
        mockPlayer.getPlayerBoard().giveEffect(List.of(new Effect[]{Effect.ONE_TIME_TOOL2}));
        // Mocking an effect (assuming Effect is an enum or a class)
        mockEffect = Effect.WOOD;
    }

    @Test
    public void testInitiate() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);

        String state = currentThrow.state();
        assertTrue(state.contains("throwsFor"));
        assertTrue(state.contains("WOOD"));
        assertTrue(state.contains("player"));
        assertTrue(state.contains("finishedUseTool"));
    }

    @Test
    public void testUseToolSuccess() {
        // Assuming the player starts with tools available
        currentThrow.initiate(mockPlayer, mockEffect, 3);

        boolean result = currentThrow.useTool(3); // Tool index 3
        assertTrue(result);
    }

    @Test
    public void testUseToolFailureWhenFinished() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        currentThrow.finishUsingTools();

        boolean result = currentThrow.useTool(1);
        assertFalse(result);
    }

    @Test
    public void testCanUseTools() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        boolean result = currentThrow.canUseTools();
        assertTrue(result);
    }

    @Test
    public void testFinishUsingToolsSuccess() {
        currentThrow.initiate(mockPlayer, Effect.WOOD, 3);

        boolean result = currentThrow.finishUsingTools();
        assertTrue(result);
    }

    @Test
    public void testFinishUsingToolsFailureWhenNotResourceOrFood() {
        // Assuming there's an Effect that is not a resource or food
        Effect invalidEffect = Effect.ONE_TIME_TOOL2; // Example invalid effect
        currentThrow.initiate(mockPlayer, invalidEffect, 3);

        boolean result = currentThrow.finishUsingTools();
        assertFalse(result);
    }

    @Test
    public void testState() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        String state = currentThrow.state();

        assertTrue(state.contains("throwsFor"));
        assertTrue(state.contains("WOOD"));
        assertTrue(state.contains("player"));
        assertTrue(state.contains("finishedUseTool"));
    }
}
