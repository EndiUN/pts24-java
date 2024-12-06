package sk.uniba.fmph.dcs.game_phase_controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import sk.uniba.fmph.dcs.stone_age.*;

public class PlaceFiguresStateTest {
    private PlaceFiguresState state;
    private MockFigureLocation mockLocation;
    private PlayerOrder player;
    
    private class MockFigureLocation implements InterfaceFigureLocation {
        private HasAction nextTryResponse = HasAction.NO_ACTION_POSSIBLE;
        private boolean nextPlaceResponse = false;
        
        @Override
        public boolean placeFigures(PlayerOrder player, int figureCount) {
            return nextPlaceResponse;
        }
        
        @Override
        public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
            return nextTryResponse;
        }
        
        @Override
        public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
            return ActionResult.FAILURE;
        }
        
        @Override
        public boolean skipAction(PlayerOrder player) {
            return false;
        }
        
        @Override
        public HasAction tryToMakeAction(PlayerOrder player) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        
        @Override
        public boolean newTurn() {
            return false;
        }
        
        public void setNextTryResponse(HasAction response) {
            this.nextTryResponse = response;
        }
        
        public void setNextPlaceResponse(boolean response) {
            this.nextPlaceResponse = response;
        }
    }

    @Before
    public void setUp() {
        mockLocation = new MockFigureLocation();
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.HUNTING_GROUNDS, mockLocation);
        state = new PlaceFiguresState(places);
        player = new PlayerOrder(0, 2);
    }

    @Test
    public void testPlaceFiguresSuccess() {
        mockLocation.setNextPlaceResponse(true);
        assertEquals(ActionResult.ACTION_DONE, 
            state.placeFigures(player, Location.HUNTING_GROUNDS, 1));
    }

    @Test
    public void testPlaceFiguresFailure() {
        mockLocation.setNextPlaceResponse(false);
        assertEquals(ActionResult.FAILURE,
            state.placeFigures(player, Location.HUNTING_GROUNDS, 1));
    }

    @Test
    public void testPlaceFiguresInvalidLocation() {
        assertEquals(ActionResult.FAILURE,
            state.placeFigures(player, Location.TOOL_MAKER, 1));
    }

    @Test
    public void testTryToMakeAutomaticActionWaiting() {
        mockLocation.setNextTryResponse(HasAction.WAITING_FOR_PLAYER_ACTION);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,
            state.tryToMakeAutomaticAction(player));
    }

//    @Test
//    public void testTryToMakeAutomaticActionNoAction() {
//        mockLocation.setNextTryResponse(HasAction.NO_ACTION_POSSIBLE);
//        assertEquals(HasAction.NO_ACTION_POSSIBLE,
//            state.tryToMakeAutomaticAction(player));
//    }

    @Test
    public void testInvalidActionsReturnFailure() {
        assertEquals(ActionResult.FAILURE,
            state.makeAction(player, Location.HUNTING_GROUNDS, new ArrayList<>(), new ArrayList<>()));
        assertEquals(ActionResult.FAILURE,
            state.skipAction(player, Location.HUNTING_GROUNDS));
        assertEquals(ActionResult.FAILURE,
            state.useTools(player, 0));
        assertEquals(ActionResult.FAILURE,
            state.noMoreToolsThisThrow(player));
        assertEquals(ActionResult.FAILURE,
            state.feedTribe(player, new ArrayList<>()));
        assertEquals(ActionResult.FAILURE,
            state.doNotFeedThisTurn(player));
        assertEquals(ActionResult.FAILURE,
            state.makeAllPlayersTakeARewardChoice(player, Effect.WOOD));
    }
}
