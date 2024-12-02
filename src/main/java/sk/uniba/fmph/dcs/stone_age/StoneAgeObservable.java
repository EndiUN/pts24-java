package sk.uniba.fmph.dcs.stone_age;

import java.util.*;

public class StoneAgeObservable implements InterfaceStoneAgeObservable {
    private final HashMap<Integer, InterfaceStoneAgeObserver> observersMap = new HashMap<>();

    @Override
    public void registerObserver(int playerId, InterfaceStoneAgeObserver observer) {
        observersMap.put(playerId, observer);
    }

    public void notify(String gameState) {
        for (InterfaceStoneAgeObserver obs : observersMap.values()) {
            obs.update(gameState);
        }
    }
}