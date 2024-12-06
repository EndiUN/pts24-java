/**
 * Represents the current throw action during the game.
 *
 * This class handles dice throws, tool usage, and resource allocation for a player.
 * It implements `InterfaceToolUse` to manage the use of tools during the action.
 */
package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Optional;

import java.util.*;

public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;           // The resource type for which the throw is made.
    private int throwResult;            // Total result of the dice throw.
    private Player player;              // Player performing the throw.
    private boolean finishedUseTool;    // Whether the player has finished using tools.

    /**
     * Initiates a new throw for the given player, resource, and number of dice.
     *
     * @param player The player performing the throw.
     * @param effect The resource type targeted by the throw.
     * @param dices  The number of dice to throw.
     */
    public void initiate(Player player, Effect effect, int dices){
        this.throwsFor = effect;
        this.player = player;
        // Perform the dice throw and calculate the total result.
        int[] dicesResults =  Throw.throw_(dices);
        this.throwResult = Arrays.stream(dicesResults).reduce(0, Integer::sum);
        this.finishedUseTool = false; // Reset tool usage flag.
    }

    /**
     * Attempts to use a tool by its index.
     *
     * @param idx The index of the tool to use.
     * @return `true` if the tool was successfully used, `false` otherwise.
     */
    @Override
    public boolean useTool(int idx) {
        if(finishedUseTool){
            return false; // Tools cannot be used after finishing the action.
        }

        Optional<Integer> toolValue = player.getPlayerBoard().useTool(idx);
        if(toolValue.isPresent()){
            throwResult += toolValue.get(); // Add tool value to the throw result.
            return true;
        }

        return false; // Tool usage failed.
    }

    /**
     * Checks if the player can use tools.
     *
     * @return `true` if tools are available, `false` otherwise.
     */
    @Override
    public boolean canUseTools() {
        return player.getPlayerBoard().hasSufficientTools(1);
    }

    /**
     * Finalizes tool usage and allocates resources based on the throw result.
     *
     * @return `true` if the action was successfully finalized, `false` otherwise.
     */
    @Override
    public boolean finishUsingTools() {
        if(finishedUseTool){
            return false;
        }

        if(!throwsFor.isResourceOrFood()){
            return false;
        }

        List<Effect> obtainedResources = new ArrayList<>();
        switch (throwsFor){
            case FOOD ->{
                for(int i = 0; i < throwResult/Effect.FOOD.points(); i++){
                    obtainedResources.add(Effect.FOOD);
                }
            }
            case WOOD -> {
                for (int i = 0; i < throwResult/Effect.WOOD.points(); i++){
                    obtainedResources.add(Effect.WOOD);
                }
            }
            case CLAY -> {
                for(int i = 0; i < throwResult/Effect.CLAY.points(); i++){
                    obtainedResources.add(Effect.CLAY);
                }
            }
            case STONE -> {
                for(int i = 0; i < throwResult/Effect.STONE.points(); i++){
                    obtainedResources.add(Effect.STONE);
                }
            }
            case GOLD -> {
                for(int i = 0; i < throwResult/Effect.GOLD.points(); i++){
                    obtainedResources.add(Effect.GOLD);
                }
            }
        }

        finishedUseTool = true;
        player.getPlayerBoard().giveEffect(obtainedResources);
        return true;
    }

    /**
     * Returns the current state of the throw as a JSON string.
     *
     * @return A JSON string representing the state of the current throw.
     */
    public String state(){
        Map<String, Object> state = Map.of(
                "throwsFor", throwsFor,
                "throwResult", throwResult,
                "player", player,
                "finishedUseTool", finishedUseTool
        );

        return new JSONObject(state).toString();
    }
}
