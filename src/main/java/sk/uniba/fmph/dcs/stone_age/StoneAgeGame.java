package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.FigureLocationAdaptor;
import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.*;

public class StoneAgeGame implements InterfaceStoneAgeGame{
    private final Map<Integer, Player> players;
    private final List<Player> forCreatingGamePhase;
    private final StoneAgeObservable observable;
    private final InterfaceGamePhaseController gamePhaseController;
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
        map.put(GamePhase.PLACE_FIGURES, new PlaceFiguresState(board.getAllLocations()));
        return map;
    }
    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        for(Integer key: players.keySet()){
            PlayerBoardGameBoardFacade plaBrd = (PlayerBoardGameBoardFacade) players.get(key).getPlayerBoard();
            observable.notify(plaBrd.getPlayerBoard().state());
        }
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean figuresResult = gamePhaseController.placeFigures(players.get(playerId).getPlayerOrder(), location, figuresCount);
        notifyObserver();
        return figuresResult;
    }

    @Override
    public boolean makeAction(int playerId, Location location, Collection<Effect> usedResources, Collection<Effect> desiredResources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean actionResult = gamePhaseController.makeAction(players.get(playerId).getPlayerOrder(), location, usedResources, desiredResources);
        notifyObserver();
        return actionResult;
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean skipActionResult = gamePhaseController.skipAction(players.get(playerId).getPlayerOrder(), location);
        notifyObserver();
        return skipActionResult;
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean useToolsResult = gamePhaseController.useTools(players.get(playerId).getPlayerOrder(), toolIndex);
        notifyObserver();
        return useToolsResult;
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean noMoreToolsResult = gamePhaseController.noMoreToolsThisThrow(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return noMoreToolsResult;
    }

    @Override
    public boolean feedTribe(int playerId, Collection<Effect> resources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean feedTribeResult = gamePhaseController.feedTribe(players.get(playerId).getPlayerOrder(), resources);
        notifyObserver();
        return feedTribeResult;
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean doNotFeedResult = gamePhaseController.doNotFeedThisTurn(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return doNotFeedResult;
    }

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


