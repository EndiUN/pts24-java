package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.player_board.PlayerFigures;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import sk.uniba.fmph.dcs.player_board.PlayerFigures;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceFeedTribe;
import sk.uniba.fmph.dcs.stone_age.InterfaceNewTurn;

public final class TribeFedStatus implements InterfaceFeedTribe, InterfaceNewTurn {
    private static final int MAX_FIELDS = 10;
    private static final int FOOD_PER_FIGURE = 1;
    
    private final PlayerFigures figures;
    private final PlayerResourcesAndFood resourcesAndFood;
    private boolean tribeFed;
    private int fields;

    public TribeFedStatus(PlayerFigures figures, PlayerResourcesAndFood resourcesAndFood) {
        this.figures = figures;
        this.resourcesAndFood = resourcesAndFood;
        this.tribeFed = false;
        this.fields = 0;
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        if (tribeFed) {
            return false;
        }

        int requiredFood = calculateRequiredFood();
        int foodFromFields = Math.min(fields, requiredFood);
        int remainingFood = requiredFood - foodFromFields;

        // If we have enough fields to feed everyone, mark as fed
        if (remainingFood == 0) {
            tribeFed = true;
            return true;
        }else{
            for(int i = 0; i < remainingFood; i++){
                if(!resourcesAndFood.takeResources(List.of(Effect.FOOD))){
                    return false;
                }
            }
        }
        tribeFed = true;
        return true;
    }

    @Override
    public boolean feedTribe(Collection<Effect> resources) {
        if (tribeFed) {
            return false;
        }

        int requiredFood = calculateRequiredFood();
        int foodFromFields = Math.min(fields, requiredFood);
        int remainingFood = requiredFood - foodFromFields;

        // Count food resources provided
        int foodProvided = 0;
        for (Effect resource : resources) {
            if (!resource.isResourceOrFood()) {
                return false;
            }
            foodProvided++;
        }

        // Check if enough food was provided
        if (foodProvided == remainingFood) {
            tribeFed = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean doNotFeedThisTurn() {
        if (tribeFed) {
            return false;
        }
        tribeFed = true;
        return true;
    }

    @Override
    public boolean isTribeFed() {
        return tribeFed;
    }

    @Override
    public boolean newTurn() {
        tribeFed = false;
        return false;
    }

    public void addField() {
        if (fields < MAX_FIELDS) {
            fields++;
        }
    }

    public int getFields() {
        return fields;
    }

    private int calculateRequiredFood() {
        return figures.getTotalFigures() * FOOD_PER_FIGURE;
    }

    public String state() {
        Map<String, Object> state = Map.of(
            "tribeFed", tribeFed,
            "fields", fields
        );
        return new JSONObject(state).toString();
    }
}
