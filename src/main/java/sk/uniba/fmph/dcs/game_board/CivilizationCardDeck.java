/**
 * Represents a deck of Civilization Cards in the game.
 *
 * This class manages a stack of Civilization Cards and provides methods to retrieve the top card.
 * It also implements `InterfaceGetState` to provide a JSON representation of the deck's state.
 */
package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;


import java.util.*;


public class CivilizationCardDeck implements InterfaceGetState {
    private final Stack<CivilizationCard> cardDeck = new Stack<>(); // Stack to hold Civilization Cards.

    /**
     * Constructs a `CivilizationCardDeck` using a list of all available cards.
     *
     * @param allCards The list of Civilization Cards to populate the deck.
     */
    public CivilizationCardDeck(final List<CivilizationCard> allCards) {
        for (CivilizationCard civilizationCard : allCards) {
            cardDeck.push(civilizationCard);
        }
    }

    /**
     * Retrieves and removes the top card from the deck.
     *
     * @return An `Optional` containing the top card if the deck is not empty, or an empty `Optional` otherwise.
     */
    public Optional<CivilizationCard> getTop(){
        if(cardDeck.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(cardDeck.pop());
    }

    /**
     * Returns the current state of the Civilization Card Deck as a JSON string.
     *
     * @return A JSON string representing the deck, including the current cards.
     */
    @Override
    public String state(){
        Map<String, Object> state = Map.of(
                "cardDeck", cardDeck
        );
        return new JSONObject(state).toString();
    }
}
