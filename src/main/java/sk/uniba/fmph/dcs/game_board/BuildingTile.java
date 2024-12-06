package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import org.json.JSONObject;

public class BuildingTile implements InterfaceFigureLocationInternal, InterfaceGetState {
    private final Stack<Building> buildings;
    private final ArrayList<PlayerOrder> figures;
    private Building building;
    private static final int MAX_FIGURES = 1;

    public BuildingTile(List<Building> buildings) {
        this.buildings = new Stack<>();
        this.buildings.addAll(buildings);
        this.building = this.buildings.pop();
        this.figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (figureCount != MAX_FIGURES || !figures.isEmpty()) {
            return false;
        }
        figures.add(player.getPlayerOrder());
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (count != MAX_FIGURES || !figures.isEmpty()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!player.getPlayerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!placeFigures(player, count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return ActionResult.FAILURE;
        }

        if(buildings.isEmpty()){
            return ActionResult.FAILURE;
        }
        if(!player.getPlayerBoard().takeResources(inputResources)){
            return ActionResult.FAILURE;
        }

        player.getPlayerBoard().takeResources(inputResources);

        OptionalInt points = building.build(inputResources);

        if(points.isEmpty()){
            return ActionResult.FAILURE;
        }
        PlayerBoardGameBoardFacade playerBoard = (PlayerBoardGameBoardFacade) player.getPlayerBoard();
        playerBoard.addPoints(points.getAsInt());

        building = null;
        figures.remove(player.getPlayerOrder());
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return false;
        }
        figures.clear();
        return true;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        if(buildings.isEmpty()){
            return true;
        }
        figures.clear();
        if(building == null){
            building = buildings.pop();
        }
        return false;
    }

    public Building getBuilding() {
        return building;
    }

    public String state() {
        Map<String, Object> state = Map.of(
                "building", buildings,
                "figures", figures.stream().map(PlayerOrder::getOrder).toList()
        );
        return new JSONObject(state).toString();
    }
}