package sk.uniba.fmph.dcs.game_board;

import java.util.Collection;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Player;

public class PlaceOnHutAdaptor implements InterfaceFigureLocationInternal{
    private ToolMakerHutFields huts;
    public PlaceOnHutAdaptor(ToolMakerHutFields toolMakerHutFields){
        this.huts = toolMakerHutFields;
    }
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        return false;
    }

    @Override
    public String state() {
        return "{}"; // Minimal implementation
    }
}
