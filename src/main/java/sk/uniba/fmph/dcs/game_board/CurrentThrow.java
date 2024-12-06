package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Optional;

import java.util.*;

public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;
    private Player player;
    private boolean finishedUseTool;
    private int[] dicesResults;

    public void initiate(Player player, Effect effect, int dices){
        this.throwsFor = effect;
        this.player = player;
        dicesResults =  Throw.throw_(dices);
        this.throwResult = Arrays.stream(dicesResults).reduce(0, Integer::sum);
        this.finishedUseTool = false;
    }

    @Override
    public boolean useTool(int idx) {
        if(finishedUseTool){
            return false;
        }

        Optional<Integer> toolValue = player.getPlayerBoard().useTool(idx);
        if(toolValue.isPresent()){
            throwResult += toolValue.get();
            return true;
        }

        return false;
    }

    @Override
    public boolean canUseTools() {
        return player.getPlayerBoard().hasSufficientTools(1);
    }

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
