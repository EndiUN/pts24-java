package sk.uniba.fmph.dcs.game_board;

import java.util.*;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

public final class ResourceSource implements InterfaceFigureLocationInternal, InterfaceGetState {
    private  String name;
    private final Effect resource;
    private  int maxFigures;
    private  int maxFigureColors;
    private  ArrayList<PlayerOrder> figures;

    private CurrentThrow currentThrow;
    private Player currentPlayer;

    private final int MAX_PLAYERS = 4;
    private final int NUMBER_OF_PLACES = 7;
    public ResourceSource(Effect resource, int countOfPlayers) {
        if (!resource.isResourceOrFood()) {
            throw new IllegalArgumentException("Resource must be food or resource");
        }

        this.resource = resource;
        this.currentThrow = null;
        this.currentPlayer = null;
        switch (resource){
            case FOOD -> {
                this.name = "Hunting";
                this.maxFigures = Integer.MAX_VALUE;
                this.maxFigureColors = MAX_PLAYERS;
                this.figures = new ArrayList<>();
            }
            case WOOD -> {
                this.name = "Forest";
                this.maxFigures = NUMBER_OF_PLACES;
                if(countOfPlayers < MAX_PLAYERS){
                    this.maxFigureColors = countOfPlayers - 1;
                }else {
                    this.maxFigureColors = MAX_PLAYERS;
                }
                this.figures = new ArrayList<>();
            }
            case CLAY -> {
                this.name = "Clay mound";
                this.maxFigures = NUMBER_OF_PLACES;
                if(countOfPlayers < MAX_PLAYERS){
                    this.maxFigureColors = countOfPlayers - 1;
                }else {
                    this.maxFigureColors = MAX_PLAYERS;
                }
                this.figures = new ArrayList<>();
            }
            case STONE -> {
                this.name = "Quarry";
                this.maxFigures = NUMBER_OF_PLACES;
                if(countOfPlayers < MAX_PLAYERS){
                    this.maxFigureColors = countOfPlayers - 1;
                }else {
                    this.maxFigureColors = MAX_PLAYERS;
                }
                this.figures = new ArrayList<>();
            }
            case GOLD -> {
                this.name = "River";
                this.maxFigures = NUMBER_OF_PLACES;
                if(countOfPlayers < MAX_PLAYERS){
                    this.maxFigureColors = countOfPlayers - 1;
                }else {
                    this.maxFigureColors = MAX_PLAYERS;
                }
                this.figures = new ArrayList<>();
            }
        }
    }
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(!player.getPlayerBoard().hasFigures(figureCount)){
            return false;
        }
        if(figures.contains(player.getPlayerOrder())){
            return false;
        }
        if(maxFigures - figures.size() < figureCount){
            return false;
        }

        Set<PlayerOrder> usedColors = new HashSet<>(figures);
        if(usedColors.size() == maxFigureColors){
            return false;
        }

        for(int i = 0; i < figureCount; i++){
            figures.add(player.getPlayerOrder());
        }
        player.getPlayerBoard().takeFigures(figureCount);
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(placeFigures(player, count)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {

        if(currentPlayer == null && currentThrow == null){
            int countPlayerFigures = 0;
            for(PlayerOrder playerOrder: figures){
                if(playerOrder == player.getPlayerOrder()){
                    countPlayerFigures++;
                }
            }

            if(countPlayerFigures == 0){
                return ActionResult.FAILURE;
            }

            currentPlayer = player;
            currentThrow = new CurrentThrow();
            currentThrow.initiate(player,resource, countPlayerFigures);
            return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
        }

        if(player != currentPlayer){
            return ActionResult.FAILURE;
        }

        if(inputResources.isEmpty()){
            currentThrow.finishUsingTools();
            currentThrow = null;
            currentPlayer = null;
            figures.removeIf(playerOrder -> playerOrder == player.getPlayerOrder());
            return ActionResult.ACTION_DONE;
        }

        for(Effect effect: inputResources){
            if(!effect.equals(Effect.TOOL)){
                return ActionResult.FAILURE;
            }
        }

        int usedToolCount = 0;
        for(Effect effect: inputResources){
            for(int i = 0; i < 6; i++){
                if(currentThrow.useTool(i)){
                    usedToolCount++;
                    break;
                }
            }
        }
        if(usedToolCount != inputResources.size()){
            return ActionResult.FAILURE;
        }

        figures.removeIf(playerOrder -> playerOrder == player.getPlayerOrder());
        currentThrow.finishUsingTools();
        currentPlayer = null;
        currentThrow = null;
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(currentPlayer == null && figures.contains(player.getPlayerOrder())){
            makeAction(player, List.of(new Effect[0]), List.of(new Effect[0]));
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        if(currentThrow != null && currentThrow.canUseTools() && this.currentPlayer == player){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        figures.removeIf(playerOrder -> playerOrder == player.getPlayerOrder());
        currentPlayer = null;
        currentThrow = null;
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        this.currentThrow = null;
        this.currentPlayer = null;
        return false;
    }

    public String state() {
        Map<String, Object> state = Map.of(
                "name", name,
                "resource", resource,
                "maxFigures", maxFigures,
                "maxFigureColors", maxFigureColors,
                "figures", figures.stream().map(PlayerOrder::getOrder).toList(),
                "currentThrow", currentThrow,
                "currentPlayer", currentPlayer
        );
        return new JSONObject(state).toString();
    }

}
