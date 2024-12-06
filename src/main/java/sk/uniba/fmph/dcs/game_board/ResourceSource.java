/**
 * Represents a resource location in the game, where players can place figures to collect resources.
 *
 * This class implements the `InterfaceFigureLocationInternal` and `InterfaceGetState` interfaces,
 * providing methods to place figures, make actions, and retrieve the current state of the resource source.
 *
 * Each `ResourceSource` instance is associated with a specific resource type (e.g., food, wood, clay).
 * The class manages the placement of figures, ensures rules regarding figure placement are followed,
 * and handles the resource collection process.
 */
package sk.uniba.fmph.dcs.game_board;

import java.util.*;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

public final class ResourceSource implements InterfaceFigureLocationInternal, InterfaceGetState {

    // Fields
    private String name;  // Name of the resource location
    private final Effect resource;  // Resource type associated with this location
    private int maxFigures;  // Maximum number of figures that can be placed
    private int maxFigureColors;  // Maximum number of unique players that can place figures
    private ArrayList<PlayerOrder> figures;  // List of figures currently placed

    private CurrentThrow currentThrow;  // Current throw associated with resource collection
    private Player currentPlayer;  // Player currently performing actions

    private static final int MAX_PLAYERS = 4;  // Maximum number of players in the game
    private static final int NUMBER_OF_PLACES = 7;  // Default number of figure slots for most resources

    /**
     * Constructor for ResourceSource.
     *
     * @param resource       The resource type associated with this location.
     * @param countOfPlayers Number of players in the game.
     * @throws IllegalArgumentException if the resource is not food or a valid resource type.
     */
    public ResourceSource(Effect resource, int countOfPlayers) {
        if (!resource.isResourceOrFood()) {
            throw new IllegalArgumentException("Resource must be food or resource");
        }

        this.resource = resource;
        this.currentThrow = null;
        this.currentPlayer = null;

        // Initialize properties based on the resource type
        switch (resource) {
            case FOOD -> {
                this.name = "Hunting";
                this.maxFigures = Integer.MAX_VALUE;
                this.maxFigureColors = MAX_PLAYERS;
            }
            case WOOD, CLAY, STONE, GOLD -> {
                this.name = switch (resource) {
                    case WOOD -> "Forest";
                    case CLAY -> "Clay mound";
                    case STONE -> "Quarry";
                    case GOLD -> "River";
                    default -> throw new IllegalStateException("Unexpected resource type");
                };
                this.maxFigures = NUMBER_OF_PLACES;
                this.maxFigureColors = Math.min(countOfPlayers - 1, MAX_PLAYERS);
            }
        }
        this.figures = new ArrayList<>();
    }
    /**
     * Places figures on the resource location for a player.
     *
     * @param player      The player placing figures.
     * @param figureCount Number of figures to place.
     * @return true if the placement was successful; false otherwise.
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (!player.getPlayerBoard().hasFigures(figureCount) || figures.contains(player.getPlayerOrder())) {
            return false;
        }
        if (maxFigures - figures.size() < figureCount) {
            return false;
        }
        Set<PlayerOrder> usedColors = new HashSet<>(figures);
        if (usedColors.size() == maxFigureColors) {
            return false;
        }

        for (int i = 0; i < figureCount; i++) {
            figures.add(player.getPlayerOrder());
        }
        player.getPlayerBoard().takeFigures(figureCount);
        return true;
    }

    /**
     * Attempts to place figures, returning the appropriate action status.
     *
     * @param player The player attempting to place figures.
     * @param count  The number of figures to place.
     * @return A `HasAction` enum indicating the result.
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(placeFigures(player, count)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Executes an action to collect resources.
     *
     * @param player           The player performing the action.
     * @param inputResources   Resources used as input for the action (e.g., tools).
     * @param outputResources  Resources produced by the action.
     * @return An `ActionResult` indicating the result of the action.
     */
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

    /**
     * Skips the action for a player. Currently unsupported.
     *
     * @param player The player attempting to skip.
     * @return false (skip not supported).
     */
    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    /**
     * Attempts to initiate an action for a player.
     *
     * @param player The player attempting an action.
     * @return A `HasAction` enum indicating the result.
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.contains(player.getPlayerOrder())){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        figures.removeIf(playerOrder -> playerOrder == player.getPlayerOrder());
        currentPlayer = null;
        currentThrow = null;
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Prepares the resource source for a new turn.
     *
     * @return Always returns false.
     */
    @Override
    public boolean newTurn() {
        this.currentThrow = null;
        this.currentPlayer = null;
        return false;
    }

    /**
     * Retrieves the current state of the resource source as a JSON string.
     *
     * @return JSON string representing the state of the resource source.
     */
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
