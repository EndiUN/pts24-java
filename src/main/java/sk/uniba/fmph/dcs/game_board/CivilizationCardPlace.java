package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal, InterfaceGetState{
    private final int requiredResources;
    private final ArrayList<PlayerOrder> figures = new ArrayList<>();
    private Optional<CivilizationCard> cardOfThisPlace = Optional.empty();
    private final CivilizationCardDeck cardDeck;
    private final CivilizationCardPlace next;
    private EvaluateCivilizationCardImmediateEffect performer;
    private boolean first = false;
    private int sum;
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

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(this.placeFigures(player, count)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

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

    @Override
    public boolean skipAction(Player player) {
        if(!figures.contains(player.getPlayerOrder())){
            return false;
        }

        figures.remove(player.getPlayerOrder());
        return true;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(!figures.contains(player.getPlayerOrder())){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        cardOfThisPlace = this.placeCard();
        if(cardOfThisPlace.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


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
