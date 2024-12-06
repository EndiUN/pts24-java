package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

/**
 * Represents a location on the game board where players can place figures
 * to obtain a civilization card. This class manages actions such as placing figures,
 * executing player actions, and cycling through new cards.
 */
public class CivilizationCardPlace implements InterfaceFigureLocationInternal, InterfaceGetState{
    private final int requiredResources;
    private final ArrayList<PlayerOrder> figures = new ArrayList<>();
    private Optional<CivilizationCard> cardOfThisPlace = Optional.empty();
    private final CivilizationCardDeck cardDeck;
    private final CivilizationCardPlace next;
    private EvaluateCivilizationCardImmediateEffect performer;
    private boolean first = false;

    /**
     * Constructs a new {@link CivilizationCardPlace}.
     *
     * @param next            The next card place in the sequence, or {@code null} if this is the last place.
     * @param cardDeck        The deck of civilization cards to draw cards from.
     * @param requiredResources The number of resources required to obtain a card from this place.
     */
    public CivilizationCardPlace(final CivilizationCardPlace next, final CivilizationCardDeck cardDeck, final int requiredResources){
        this.cardDeck = cardDeck;
        this.requiredResources = requiredResources;
        if(this.requiredResources == 1){
            first = true;
        }
        this.next = next;
        this.performer = null;
        this.cardOfThisPlace = placeCard();
    }

    /**
     * Attempts to place a new card at this location.
     *
     * @return The card previously at this location, if present, or an empty {@link Optional}.
     */
    public Optional<CivilizationCard> placeCard(){
        if(cardOfThisPlace.isPresent()){
            Optional<CivilizationCard> toReturn = this.cardOfThisPlace;
            if(!first) {
                this.cardOfThisPlace = Optional.empty();
                this.cardOfThisPlace = this.placeCard();
            }
            return toReturn;
        }
        if(next != null){
            return next.placeCard();
        }else{
            return cardDeck.getTop();
        }
    }

    /**
     * Places figures at this card place.
     *
     * @param player      The player placing the figures.
     * @param figureCount The number of figures to place (must be 1 for this location).
     * @return {@code true} if the figures were successfully placed, {@code false} otherwise.
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(figureCount != 1){
            return false;
        }

        if(!player.getPlayerBoard().hasFigures(figureCount)){
            return false;
        }

        if(!this.figures.isEmpty()){
            return false;
        }

        player.getPlayerBoard().takeFigures(1);
        figures.add(player.getPlayerOrder());
        return true;
    }

    /**
     * Attempts to place figures and returns the result as {@link HasAction}.
     *
     * @param player The player attempting to place figures.
     * @param count  The number of figures to place.
     * @return {@link HasAction#AUTOMATIC_ACTION_DONE} if successful,
     *         {@link HasAction#NO_ACTION_POSSIBLE} otherwise.
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(this.placeFigures(player, count)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Executes the player's action at this card place.
     *
     * @param player          The player taking the action.
     * @param inputResources  The resources provided by the player.
     * @param outputResources Additional resources required for certain effects.
     * @return {@link ActionResult#ACTION_DONE} if successful, {@link ActionResult#FAILURE} otherwise.
     */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {

        if(!figures.contains(player.getPlayerOrder())){
            return ActionResult.FAILURE;
        }
        if(inputResources.size() != requiredResources){
            return ActionResult.FAILURE;
        }

        int effectResourcesCount = 0;
        for(Effect effect: inputResources){
            if(effect.isResource()){
                effectResourcesCount++;
            }
        }

        if(effectResourcesCount != requiredResources){
            return ActionResult.FAILURE;
        }

        if(!player.getPlayerBoard().takeResources(inputResources)){
            return ActionResult.FAILURE;
        }

        if(cardOfThisPlace.isPresent()) {
            List<ImmediateEffect> immediateEffects = cardOfThisPlace.get().getImmediateEffectType();
            for(ImmediateEffect immediateEffect: immediateEffects){
                switch (immediateEffect){
                    case ThrowGold -> {
                        performer = new GetSomethingThrow(Effect.GOLD);
                        performer.performEffect(player, Effect.GOLD);
                    }
                    case ThrowStone -> {
                        performer = new GetSomethingThrow(Effect.STONE);
                        performer.performEffect(player, Effect.STONE);
                    }
                    case ThrowClay -> {
                        performer = new GetSomethingThrow(Effect.CLAY);
                        performer.performEffect(player, Effect.CLAY);
                    }
                    case ThrowWood -> {
                        performer = new GetSomethingThrow(Effect.WOOD);
                        performer.performEffect(player, Effect.WOOD);
                    }
                    case POINT -> {
                        PlayerBoardGameBoardFacade playerBoard = (PlayerBoardGameBoardFacade) player.getPlayerBoard();
                        playerBoard.addPoints(1);
                    }
                    case WOOD -> {
                        performer = new GetSomethingFixed();
                        performer.performEffect(player, Effect.WOOD);
                    }
                    case CLAY -> {
                        performer = new GetSomethingFixed();
                        performer.performEffect(player, Effect.CLAY);
                    }
                    case STONE -> {
                        performer = new GetSomethingFixed();
                        performer.performEffect(player, Effect.STONE);
                    }
                    case GOLD -> {
                        performer = new GetSomethingFixed();
                        performer.performEffect(player, Effect.GOLD);
                    }
                    case CARD -> {
                        performer = new GetCard(cardDeck);
                        performer.performEffect(player, null);
                    }
                    case FOOD -> {
                        performer = new GetSomethingFixed();
                        performer.performEffect(player, Effect.FOOD);
                    }
                    case ArbitraryResource -> {
                        performer = new GetSomethingChoice(2);
                        if(outputResources.isEmpty()){
                            return ActionResult.FAILURE;
                        }
                        performer.performEffect(player, outputResources.stream().toList().get(0));
                    }
                    case AllPlayersTakeReward -> {
                        performer = new AllPlayersTakeReward();
                        performer.performEffect(player, null);
                    }
                    case Tool -> player.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
                    case Field -> player.getPlayerBoard().giveEffect(List.of(Effect.FIELD));
                    case OneTimeTool2 -> player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL2));
                    case OneTimeTool3 -> player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL3));
                    case OneTimeTool4 -> player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL4));
                }
            }
            player.getPlayerBoard().giveEndOfGameEffect(cardOfThisPlace.get().getEndOfGameEffectType());
        }

        cardOfThisPlace = Optional.empty();
        figures.remove(player.getPlayerOrder());
        return ActionResult.ACTION_DONE;
    }

    /**
     * Skips the player's action at this card place.
     *
     * @param player The player skipping their action.
     * @return {@code true} if the action was successfully skipped, {@code false} otherwise.
     */
    @Override
    public boolean skipAction(Player player) {
        if(!figures.contains(player.getPlayerOrder())){
            return false;
        }

        figures.remove(player.getPlayerOrder());
        return true;
    }

    /**
     * Checks if the player can make an action at this card place.
     *
     * @param player The player to check.
     * @return {@link HasAction#WAITING_FOR_PLAYER_ACTION} if an action can be made,
     *         {@link HasAction#NO_ACTION_POSSIBLE} otherwise.
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if(!figures.contains(player.getPlayerOrder())){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * Prepares this card place for a new turn by placing a new card if available
     * and clearing any figures from the previous turn.
     *
     * <p><strong>Note:</strong> The {@code newTurn()} method for card places must be called in
     * the following sequence: first for {@code cardPlace1}, then {@code cardPlace2},
     * {@code cardPlace3}, and finally {@code cardPlace4}. This ensures that card
     * placement follows the correct order of the game board.</p>
     *
     * @return {@code true} if no card is available for this card place (deck is empty),
     *         {@code false} otherwise.
     */
    @Override
    public boolean newTurn() {
        cardOfThisPlace = this.placeCard();
        figures.clear();
        if(cardOfThisPlace.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


    /**
     * Returns the state of this card place as a JSON string.
     *
     * @return A string representation of the state in JSON format.
     */
    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "requiredResources", requiredResources,
                "figures", figures,
                "cardOfThisPlace", cardOfThisPlace,
                "cardDeck", cardDeck,
                "civilizationCardPlaceNext", next

        );
        return new JSONObject(state).toString();
    }
}
