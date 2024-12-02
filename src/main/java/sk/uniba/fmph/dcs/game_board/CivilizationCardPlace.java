package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal{
    private final int requiredResources;
    private final ArrayList<PlayerOrder> figures = new ArrayList<>();
    private Optional<CivilizationCard> cardOfThisPlace = Optional.empty();
    private final CivilizationCardDeck cardDeck;
    private final CivilizationCardPlace next;
    private EvaluateCivilizationCardImmediateEffect performer;
    public CivilizationCardPlace(final CivilizationCardPlace next ,final CivilizationCardDeck cardDeck, int requiredResources){
        this.cardDeck = cardDeck;
        this.requiredResources = requiredResources;
        this.next = next;
        this.performer = null;
        this.cardOfThisPlace = placeCard();
    }

    public int getRequiredResources() {
        return requiredResources;
    }

    public Optional<CivilizationCard> placeCard(){
        if(cardOfThisPlace.isPresent()){
            Optional<CivilizationCard> toReturn = this.cardOfThisPlace;
            this.cardOfThisPlace = Optional.empty();
            this.cardOfThisPlace =  this.placeCard();
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

        if(!player.playerBoard().hasFigures(figureCount)){
            return false;
        }

        if(!this.figures.isEmpty()){
            return false;
        }

        player.playerBoard().takeFigures(1);
        figures.add(player.playerOrder());
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
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if(!figures.contains(player.playerOrder())){
            return ActionResult.FAILURE;
        }
        if(inputResources.length != requiredResources){
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

        if(!player.playerBoard().takeResources(List.of(inputResources))){
            return ActionResult.FAILURE;
        }

        if(cardOfThisPlace.isPresent()) {
            List<ImmediateEffect> immediateEffects = cardOfThisPlace.get().getImmediateEffectType();
            for(ImmediateEffect immediateEffect: immediateEffects){
                boolean result = true;
                switch (immediateEffect){
                    case ThrowGold -> {
                        performer = new GetSomethingThrow(Effect.GOLD);
                        result = performer.performEffect(player, Effect.GOLD);
                    }
                    case ThrowStone -> {
                        performer = new GetSomethingThrow(Effect.STONE);
                        result = performer.performEffect(player, Effect.STONE);
                    }
                    case ThrowClay -> {
                        performer = new GetSomethingThrow(Effect.CLAY);
                        result = performer.performEffect(player, Effect.CLAY);
                    }
                    case ThrowWood -> {
                        performer = new GetSomethingThrow(Effect.WOOD);
                        result = performer.performEffect(player, Effect.WOOD);
                    }
                    case POINT -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.POINT);
                    }
                    case WOOD -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.WOOD);
                    }
                    case CLAY -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.CLAY);
                    }
                    case STONE -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.STONE);
                    }
                    case GOLD -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.GOLD);
                    }
                    case CARD -> {
                        performer = new GetCard(cardDeck);
                        result = performer.performEffect(player, null);
                    }
                    case FOOD -> {
                        performer = new GetSomethingFixed();
                        result = performer.performEffect(player, Effect.FOOD);
                    }
                    case ArbitraryResource -> {
                        performer = new GetSomethingChoice(2);
                        if(outputResources.length == 0){
                            return ActionResult.FAILURE;
                        }
                        result = performer.performEffect(player, outputResources[0]);
                    }
                    case AllPlayersTakeReward -> {
                        performer = new AllPlayersTakeReward();
                        result = performer.performEffect(player, null);
                    }
                    case Tool -> player.playerBoard().giveEffect(List.of(Effect.TOOL));
                    case Field -> player.playerBoard().giveEffect(List.of(Effect.FIELD));
                    case OneTimeTool2 -> player.playerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL2));
                    case OneTimeTool3 -> player.playerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL3));
                    case OneTimeTool4 -> player.playerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL4));
                }
                if(!result){
                    return ActionResult.FAILURE;
                }
            }
            player.playerBoard().giveEndOfGameEffect(cardOfThisPlace.get().getEndOfGameEffectType());
        }

        cardOfThisPlace = Optional.empty();
        figures.remove(player.playerOrder());
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if(!figures.contains(player.playerOrder())){
            return false;
        }

        figures.remove(player.playerOrder());
        return true;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(!figures.contains(player.playerOrder())){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        if(!figures.isEmpty()){
            return false;
        }

        cardOfThisPlace = this.placeCard();
        return true;
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

    public Optional<CivilizationCard> getCardOfThisPlace() {
        return cardOfThisPlace;
    }
}
