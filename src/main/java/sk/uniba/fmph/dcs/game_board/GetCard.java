package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.List;
import java.util.Optional;

public class GetCard implements  EvaluateCivilizationCardImmediateEffect{
    private final CivilizationCardDeck deck;

    public GetCard(CivilizationCardDeck deck){
        this.deck = deck;
    }
    @Override
    public boolean performEffect(Player player, Effect effect){
        Optional<CivilizationCard> card = deck.getTop();
        if(card.isPresent()) {
            List<EndOfGameEffect> list = card.get().getEndOfGameEffectType();
            player.getPlayerBoard().giveEndOfGameEffect(list);
            return true;
        }else{
            return false;
        }
    }
}

