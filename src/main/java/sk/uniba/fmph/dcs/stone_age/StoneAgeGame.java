package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.*;

/**
 * The StoneAgeGame class manages the core game logic and interaction between the game phases, players, and the game board.
 * It implements the InterfaceStoneAgeGame interface and provides methods to execute various game actions,
 * transition between game phases, and notify observers about changes in the game state.
 */
public class StoneAgeGame implements InterfaceStoneAgeGame{

    /** A map of player IDs to Player objects, representing all players in the game. */
    private final Map<Integer, Player> players;

    /** A list of players used for initializing game phases. */
    private final List<Player> forCreatingGamePhase;

    /** The observable object used to notify changes to external observers. */
    private final StoneAgeObservable observable;

    /** The game phase controller responsible for managing and transitioning between game phases. */
    private final InterfaceGamePhaseController gamePhaseController;

    /** The game board used to manage the game locations and resources. */
    private final InterfaceGetState gameBoard;

    public StoneAgeGame(final int amountOfPlayers, final StoneAgeObservable observer) {
        //Create map of players
        players = new HashMap<>();
        for (int i = 0; i < amountOfPlayers; i++) {
            players.put(i, new Player(new PlayerOrder(i, amountOfPlayers), new PlayerBoardGameBoardFacade(new PlayerBoard())));
        }

        //Create GameBoard
        forCreatingGamePhase = new ArrayList<>();
        for(Integer key: players.keySet()){
            forCreatingGamePhase.add(players.get(key));
        }
        this.gameBoard = new GameBoard(forCreatingGamePhase);

        //Create GamePhaseController
        this.gamePhaseController = new GamePhaseController(createController(), players.get(0).getPlayerOrder());

        this.observable = observer;
    }


    private Map<GamePhase, InterfaceGamePhaseState> createController(){
        GameBoard board = (GameBoard) gameBoard;
        Map<GamePhase, InterfaceGamePhaseState> map = new HashMap<>();
        //Game end state
        map.put(GamePhase.GAME_END, new GameEndState(forCreatingGamePhase));
        //All players take reward state
        map.put(GamePhase.ALL_PLAYERS_TAKE_A_REWARD, new AllPlayersTakeARewardState());
        //Waiting for tool use state
        map.put(GamePhase.WAITING_FOR_TOOL_USE, new WaitingForToolUseState(null));
        //New round state
        map.put(GamePhase.NEW_ROUND, new NewRoundState(board.getAllLocations(), forCreatingGamePhase));
        //FeedTribe state
        map.put(GamePhase.FEED_TRIBE, new FeedTribeState(forCreatingGamePhase));
        //Make action state
        map.put(GamePhase.MAKE_ACTION, new MakeActionState(board.getAllLocations()));
        //PlaceFigure state
        map.put(GamePhase.PLACE_FIGURES, new PlaceFiguresState(board.getAllLocations(), forCreatingGamePhase));
        return map;
    }

    /**
     * Notifies the observer of the current game state, including the game board, game phase controller, and players' states.
     */
    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        for(Integer key: players.keySet()){
            PlayerBoardGameBoardFacade plaBrd = (PlayerBoardGameBoardFacade) players.get(key).getPlayerBoard();
            observable.notify(plaBrd.getPlayerBoard().state());
        }
    }

    /**
     * Places a specified number of figures for a player at a given location on the game board.
     *
     * @param playerId The ID of the player making the action.
     * @param location The location where the figures will be placed.
     * @param figuresCount The number of figures to place.
     * @return true if the figures were placed successfully, false otherwise.
     */
    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean figuresResult = gamePhaseController.placeFigures(players.get(playerId).getPlayerOrder(), location, figuresCount);
        notifyObserver();
        return figuresResult;
    }

    /**
     * Executes an action for a player at a given location using specified resources to gain desired resources.
     *
     * @param playerId The ID of the player performing the action.
     * @param location The location where the action is to be performed.
     * @param usedResources The resources used for the action.
     * @param desiredResources The resources the player wants to gain.
     * @return true if the action was successfully performed, false otherwise.
     */
    @Override
    public boolean makeAction(int playerId, Location location, Collection<Effect> usedResources, Collection<Effect> desiredResources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean actionResult = gamePhaseController.makeAction(players.get(playerId).getPlayerOrder(), location, usedResources, desiredResources);
        notifyObserver();
        return actionResult;
    }

    /**
     * Skips the action at a given location for a specified player.
     *
     * @param playerId The ID of the player skipping the action.
     * @param location The location where the action would have been performed.
     * @return true if the action was skipped successfully, false otherwise.
     */
    @Override
    public boolean skipAction(int playerId, Location location) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean skipActionResult = gamePhaseController.skipAction(players.get(playerId).getPlayerOrder(), location);
        notifyObserver();
        return skipActionResult;
    }

    /**
     * Allows a player to use a tool from their available tools.
     *
     * @param playerId The ID of the player using the tool.
     * @param toolIndex The index of the tool being used.
     * @return true if the tool was used successfully, false otherwise.
     */
    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean useToolsResult = gamePhaseController.useTools(players.get(playerId).getPlayerOrder(), toolIndex);
        notifyObserver();
        return useToolsResult;
    }

    /**
     * Indicates that the player no longer wishes to use tools during the current throw.
     *
     * @param playerId The ID of the player making the decision.
     * @return true if the decision was registered successfully, false otherwise.
     */
    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean noMoreToolsResult = gamePhaseController.noMoreToolsThisThrow(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return noMoreToolsResult;
    }

    /**
     * Feeds the tribe by providing a collection of resources.
     *
     * @param playerId The ID of the player feeding the tribe.
     * @param resources The resources provided to feed the tribe.
     * @return true if the tribe was fed successfully, false otherwise.
     */
    @Override
    public boolean feedTribe(int playerId, Collection<Effect> resources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean feedTribeResult = gamePhaseController.feedTribe(players.get(playerId).getPlayerOrder(), resources);
        notifyObserver();
        return feedTribeResult;
    }

    /**
     * Indicates that the player will not feed their tribe during this turn.
     *
     * @param playerId The ID of the player making the decision.
     * @return true if the decision was registered successfully, false otherwise.
     */
    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean doNotFeedResult = gamePhaseController.doNotFeedThisTurn(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return doNotFeedResult;
    }

    /**
     * Makes all players take a reward choice based on the specified effect.
     *
     * @param playerId The ID of the player initiating the reward choice.
     * @param reward The reward that all players must choose from.
     * @return true if all players successfully took a reward choice, false otherwise.
     */
    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean allTakeRewardResult = gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId).getPlayerOrder(), reward);
        notifyObserver();
        return allTakeRewardResult;
    }
}


