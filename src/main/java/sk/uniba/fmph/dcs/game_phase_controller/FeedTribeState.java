/**
 * Represents the "Feed Tribe" phase in the game, where players must ensure their tribe has enough resources to survive.
 *
 * This state is part of the game phase controller and implements `InterfaceGamePhaseState`.
 * It handles feeding tribes, skipping feeding, and other actions during this phase.
 * Most actions unrelated to feeding tribes are invalid in this state and return failure.
 */
package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Collection;
import java.util.List;


public final class FeedTribeState implements InterfaceGamePhaseState {

    private final List<Player> playerTribes;

    /**
     * Constructs a `FeedTribeState` with a list of player tribes.
     *
     * @param playerTribes The list of players whose tribes need to be fed during this phase.
     */
    public FeedTribeState(List<Player> playerTribes) {
        this.playerTribes = playerTribes;
    }

    /**
     * Places figures during the "Feed Tribe" phase. This action is not valid in this phase.
     *
     * @param player       The player attempting to place figures.
     * @param location     The location to place the figures.
     * @param figuresCount The number of figures to place.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        return ActionResult.FAILURE;
    }
    /**
     * Makes an action during the "Feed Tribe" phase. This action is not valid in this phase.
     *
     * @param player          The player attempting the action.
     * @param location        The location of the action.
     * @param inputResources  The resources used for the action.
     * @param outputResources The resources produced by the action.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult makeAction(final PlayerOrder player, final Location location,
                                   final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    /**
     * Skips an action during the "Feed Tribe" phase. This action is not valid in this phase.
     *
     * @param player   The player attempting to skip an action.
     * @param location The location of the action.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult skipAction(final PlayerOrder player, final Location location) {
        return ActionResult.FAILURE;

    }

    /**
     * Uses tools during the "Feed Tribe" phase. This action is not valid in this phase.
     *
     * @param player    The player attempting to use tools.
     * @param toolIndex The index of the tool to use.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult useTools(final PlayerOrder player, final int toolIndex) {
        return ActionResult.FAILURE;
    }

    /**
     * Indicates no more tools will be used during this throw. This action is not valid in this phase.
     *
     * @param player The player attempting the action.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult noMoreToolsThisThrow(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    /**
     * Attempts to feed the tribe for the given player using the provided resources.
     *
     * @param player    The player attempting to feed their tribe.
     * @param resources The resources to use for feeding.
     * @return `ActionResult.ACTION_DONE` if successful, otherwise `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.feedTribe(resources)) {
                    return ActionResult.ACTION_DONE;
                }
                break;
            }
        }
        return ActionResult.FAILURE;
    }

    /**
     * Allows the player to skip feeding their tribe for the current turn.
     *
     * @param player The player attempting to skip feeding.
     * @return `ActionResult.ACTION_DONE` if successful, otherwise `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult doNotFeedThisTurn(final PlayerOrder player) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.doNotFeedThisTurn()) {
                    return ActionResult.ACTION_DONE;
                }
                break;
            }
        }
        return ActionResult.FAILURE;
    }

    /**
     * Initiates a reward choice for all players. This action is not valid in this phase.
     *
     * @param player  The player attempting the action.
     * @param reward  The reward to choose.
     * @return Always returns `ActionResult.FAILURE`.
     */
    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        return ActionResult.FAILURE;
    }

    /**
     * Tries to automatically perform an action for the player during this phase.
     * Automatically feeds the tribe if enough food is available, or waits for player input.
     *
     * @param player The player attempting the automatic action.
     * @return `HasAction.AUTOMATIC_ACTION_DONE` if the tribe is automatically fed,
     *         `HasAction.WAITING_FOR_PLAYER_ACTION` if the player needs to act,
     *         or `HasAction.NO_ACTION_POSSIBLE` if no action is needed.
     */
    @Override
    public HasAction tryToMakeAutomaticAction(final PlayerOrder player) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.isTribeFed()) {
                    return HasAction.NO_ACTION_POSSIBLE;
                }
                if (pl.feedTribeIfEnoughFood()) {
                    return HasAction.AUTOMATIC_ACTION_DONE;
                }
                break;
            }
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}