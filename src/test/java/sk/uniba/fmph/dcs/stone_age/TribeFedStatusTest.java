package sk.uniba.fmph.dcs.stone_age;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerFigures;
import sk.uniba.fmph.dcs.player_board.PlayerResourcesAndFood;
import sk.uniba.fmph.dcs.player_board.TribeFedStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TribeFedStatusTest {
    private TribeFedStatus status;

    @Before
    public void setUp() {
        status = new TribeFedStatus(new PlayerFigures(), new PlayerResourcesAndFood());
    }

    @Test
    public void testInitialState() {
        assertFalse(status.isTribeFed());
        assertEquals(0, status.getFields());
    }

    @Test
    public void testAddField() {
        status.addField();
        assertEquals(1, status.getFields());
    }

    @Test
    public void testAddFieldLimit() {
        for(int i = 0; i < 12; i++) {
            status.addField();
        }
        assertEquals(10, status.getFields());
    }

    @Test
    public void testFeedTribeWithFood() {
        Collection<Effect> food = List.of(Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD);
        assertTrue(status.feedTribe(food));
        assertTrue(status.isTribeFed());
    }

    @Test
    public void testFeedTribeWithInsufficientFood() {
        Collection<Effect> food = new ArrayList<>();
        assertFalse(status.feedTribe(food));
        assertFalse(status.isTribeFed());
    }

    @Test
    public void testNewTurnResetsFedStatus() {
        Collection<Effect> food = List.of(Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD,Effect.FOOD);
        status.feedTribe(food);
        assertTrue(status.isTribeFed());

        status.newTurn();
        assertFalse(status.isTribeFed());
    }

    @Test
    public void testDoNotFeedThisTurn() {
        assertTrue(status.doNotFeedThisTurn());
        assertTrue(status.isTribeFed());
    }

    @Test
    public void testFeedTribeIfEnoughFood() {
        // This would need integration with PlayerResourcesAndFood
        // to test properly, as it depends on available food resources
        assertTrue(status.feedTribeIfEnoughFood());
    }
}
