package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.List;
import java.util.ArrayList;

public class GetSomethingChoice implements EvaluateCivilizationCardImmediateEffect{
    private int totalNumber;
    private PlayerOrder playerOrder;

    public GetSomethingChoice(int totalNumber){
        this.totalNumber = totalNumber;
    }
    @Override
    public boolean performEffect(Player player, Effect choice) {
        if(playerOrder ==null){
            playerOrder = player.getPlayerOrder();
        }
        if(!playerOrder.equals(player.getPlayerOrder())){
            return false;
        }
        if (!choice.isResource() || totalNumber == 0) {
            return false;
        }
        totalNumber--;

        List<Effect> effectToGive = new ArrayList<>();
        effectToGive.add(choice);

        player.getPlayerBoard().giveEffect(effectToGive);
        return true;
    }

}