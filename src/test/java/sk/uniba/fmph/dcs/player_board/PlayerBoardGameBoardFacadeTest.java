package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PlayerBoardGameBoardFacadeTest {
    @Test
    public void doNotFeedThisTurnTest() {
        PlayerResourcesAndFood prf = new PlayerResourcesAndFood();
        PlayerFigures figures = new PlayerFigures();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(figures, prf);
        PlayerBoard pb = new PlayerBoard(new PlayerCivilizationCards(), figures, prf, new PlayerTools(), tribeFedStatus);
        PlayerBoardGameBoardFacade PBGBF = new PlayerBoardGameBoardFacade(pb);
        for(int i = 0; i < 12; i++){
            pb.getPlayerResourcesAndFood().takeResources(List.of(Effect.FOOD));
        }
        pb.getPlayerResourcesAndFood()
                .giveResources(List.of(new Effect[]{Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD}));
        pb.getPlayerResourcesAndFood()
                .giveResources(List.of(new Effect[]{Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD, Effect.WOOD}));
        Collection<Effect> mats = Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD);
        boolean ans = PBGBF.doNotFeedThisTurn();
        assert ans;

        PBGBF.feedTribeIfEnoughFood();
        ans = PBGBF.doNotFeedThisTurn();
        assert !ans;
        int points = pb.addPoints(0);
        assert points == -10;

        PBGBF.newTurn();
        ans = PBGBF.feedTribeIfEnoughFood();
        assert ans;
        ans = PBGBF.doNotFeedThisTurn();
        assert !ans;
        points = pb.addPoints(0);
        assert points == -10;

        PBGBF.newTurn();
        ans = PBGBF.feedTribeIfEnoughFood();
        assert !ans;
        ans = PBGBF.doNotFeedThisTurn();
        assert ans;
        points = pb.addPoints(0);
        assert points == -20;

        PBGBF.newTurn();
        ans = PBGBF.feedTribeIfEnoughFood();
        assert !ans;
        ans = PBGBF.feedTribe(mats);
        assert !ans;
        ans = PBGBF.doNotFeedThisTurn();
        assert ans;
        points = pb.addPoints(0);
        assert points == -30;

    }
}