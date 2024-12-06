/**
 * The {@code PlayerCivilizationCards} class manages a collection of civilization cards
 * and calculates their end-of-game points based on specific effects. It also provides
 * a JSON representation of the internal state for game state serialization.
 *
 * <p>This class implements the {@link InterfaceGetState} interface.
 * The cards are represented using a {@link Map} where each {@link EndOfGameEffect}
 * is mapped to its count.</p>
 *
 * <p>The class supports adding new effects, calculating total points based on
 * game-specific parameters, and retrieving the state of the card collection.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * PlayerCivilizationCards playerCards = new PlayerCivilizationCards();
 * playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.FARMER, EndOfGameEffect.BUILDER));
 * int points = playerCards.calculateEndOfGameCivilizationCardPoints(3, 2, 5, 4);
 * System.out.println("Total Points: " + points);
 * }</pre>
 *
 * @see EndOfGameEffect
 * @see InterfaceGetState
 */
package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerCivilizationCards implements InterfaceGetState {

    /**
     * A mapping of {@link EndOfGameEffect} to the count of cards with that effect.
     */
    private final Map<EndOfGameEffect, Integer> endOfGameEffectMap;

    /**
     * Initializes an empty map of civilization cards.
     */
    public PlayerCivilizationCards() {
        endOfGameEffectMap = new HashMap<>();
    }

    /**
     * Adds a collection of {@link EndOfGameEffect} to the map, incrementing the count
     * for each effect present in the collection.
     *
     * @param effects the collection of {@code EndOfGameEffect} to add.
     */
    public void addEndOfGameEffects(Collection<EndOfGameEffect> effects) {
        for (EndOfGameEffect effect : effects) {
            endOfGameEffectMap.merge(effect, 1, Integer::sum);
        }
    }

    /**
     * Calculates the total points from civilization cards based on the number of buildings,
     * tools, fields, and figures, as well as unique green card sets.
     *
     * <p>Points are calculated as follows:</p>
     * <ul>
     *     <li>Farmers: Points are equal to the number of fields multiplied by the count of farmer cards.</li>
     *     <li>Tool Makers: Points are equal to the number of tools multiplied by the count of tool maker cards.</li>
     *     <li>Builders: Points are equal to the number of buildings multiplied by the count of builder cards.</li>
     *     <li>Shamans: Points are equal to the number of figures multiplied by the count of shaman cards.</li>
     *     <li>Green Cards: Unique sets of green cards contribute points as the square of the set size.</li>
     * </ul>
     *
     * @param buildings the number of buildings.
     * @param tools the number of tools.
     * @param fields the number of fields.
     * @param figures the number of figures.
     * @return the total points from all civilization cards.
     */
    public int calculateEndOfGameCivilizationCardPoints(int buildings, int tools, int fields, int figures){
        int sumOfEndOfGameEffects = 0;
        Map<EndOfGameEffect, Integer> greenCards = new HashMap<>();
        for(EndOfGameEffect effect: endOfGameEffectMap.keySet()){
            switch (effect){
                case FARMER -> sumOfEndOfGameEffects += fields * endOfGameEffectMap.get(effect);
                case TOOL_MAKER -> sumOfEndOfGameEffects += tools * endOfGameEffectMap.get(effect);
                case BUILDER -> sumOfEndOfGameEffects += buildings * endOfGameEffectMap.get(effect);
                case SHAMAN -> sumOfEndOfGameEffects += figures * endOfGameEffectMap.get(effect);
                default -> greenCards.put(effect, endOfGameEffectMap.get(effect));
            }
        }

        while (true){
            int countCurrentCards = 0;
            for(EndOfGameEffect effect: greenCards.keySet()){
                int numberOfCards = greenCards.get(effect);
                if(numberOfCards > 0){
                    countCurrentCards++;
                    greenCards.put(effect, numberOfCards - 1);
                }
            }
            if(countCurrentCards == 0){
                break;
            }
            int toCheck = (int) Math.pow(countCurrentCards, 2);
            sumOfEndOfGameEffects += toCheck;
        }

        return sumOfEndOfGameEffects;
    }


    /**
     * Returns a JSON string representation of the internal state.
     *
     * @return the JSON string representation of the state.
     */
    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "endOfGameEffectMap", endOfGameEffectMap
        );

        return new JSONObject(state).toString();
    }

    /**
     * Retrieves the map of end-of-game effects and their respective counts.
     *
     * @return the map of {@link EndOfGameEffect} to their counts.
     */
    public Map<EndOfGameEffect, Integer> getEndOfGameEffectMap() {
        return endOfGameEffectMap;
    }
}
