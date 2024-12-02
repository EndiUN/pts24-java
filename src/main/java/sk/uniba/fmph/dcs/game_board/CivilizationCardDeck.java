package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;


import java.util.*;

public class CivilizationCardDeck implements InterfaceGetState {
    private final Stack<CivilizationCard> cardDeck = new Stack<>();

    public CivilizationCardDeck(final List<CivilizationCard> allCards) {
        for (CivilizationCard civilizationCard : allCards) {
            cardDeck.push(civilizationCard);
        }

    }

    public Optional<CivilizationCard> getTop(){
        if(cardDeck.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(cardDeck.pop());
    }

    @Override
    public String state(){
        Map<String, Object> state = Map.of(
                "cardDeck", cardDeck
        );
        return new JSONObject(state).toString();
    }
}
