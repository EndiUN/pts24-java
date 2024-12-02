package sk.uniba.fmph.dcs.game_board;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class CivilizationCardPlaceTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private CivilizationCardPlace civilizationCardPlaces4;
    private CivilizationCardPlace civilizationCardPlace3;
    private CivilizationCardPlace civilizationCardPlace2;
    private CivilizationCardPlace civilizationCardPlace1;
    private final CivilizationCard[] allCard = {
            //Dice roll ( 10 cards )
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.WRITING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.POTTERY)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),
            // FOOD (7 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.MEDICINE)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.POTTERY)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.WEAVING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.WEAVING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            // Resource (5 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE, ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.CLAY),
                    Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.GOLD),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
            // Resources with dice roll (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowStone),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowGold),
                    Arrays.asList(EndOfGameEffect.ART)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.WOOD),
                    Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),
            // Victory POINTs (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.MUSIC)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.MUSIC)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            // Extra.Tool tile (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.Tool),
                    Arrays.asList(EndOfGameEffect.ART)),
            // Agriculture (2 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.Field),
                    Arrays.asList(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.Field),
                    Arrays.asList(EndOfGameEffect.FARMER)),
            // Civilization card for the final scoring (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.CARD),
                    Arrays.asList(EndOfGameEffect.WRITING)),
            // One-use.tool (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool2),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool3),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool4),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            // Any 2 resources (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource),
                    Arrays.asList(EndOfGameEffect.MEDICINE)),
    };

    private class MockPlayerBoardGameBoard implements InterfacePlayerBoardGameBoard {

        private int availableFigures;
        private List<EndOfGameEffect> endOfGameEffects = new ArrayList<>();
        private List<Effect> resources = new ArrayList<>();

        public MockPlayerBoardGameBoard(int availableFigures){
            this.availableFigures = availableFigures;
        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {
            resources.addAll(stuff);
        }

        @Override
        public void giveFigure() {
            availableFigures++;
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
            endOfGameEffects.addAll(stuff);
        }

        @Override
        public void giveCard(CivilizationCard card) {
            return;
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            resources.remove(stuff);
            return true;
        }

        @Override
        public boolean takeFigures(int count) {
            if (count <= availableFigures) {
                availableFigures -= count;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean hasFigures(int count) {
            return availableFigures >= count;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.empty();
        }

        public List<Effect> getResources() {
            return resources;
        }

        public List<EndOfGameEffect> getEndOfGameEffects() {
            return endOfGameEffects;
        }
    }

    private Player playerMaker(Player player, int orderNum, int maxPlayers, InterfacePlayerBoardGameBoard board) {
        PlayerOrder order = new PlayerOrder(orderNum, maxPlayers);
        player = new Player() {
            @Override
            public PlayerOrder playerOrder() {
                return order;
            }

            @Override
            public InterfacePlayerBoardGameBoard playerBoard() {
                return board;
            }
        };
        return player;
    }

    public void setUp(){
        CivilizationCardDeck cardDeck = new CivilizationCardDeck(Arrays.asList(allCard));
        civilizationCardPlaces4 = new CivilizationCardPlace(null, cardDeck, 4);
        civilizationCardPlace3 = new CivilizationCardPlace(civilizationCardPlaces4, cardDeck, 3);
        civilizationCardPlace2 = new CivilizationCardPlace(civilizationCardPlace3,cardDeck,2);
        civilizationCardPlace1 = new CivilizationCardPlace(civilizationCardPlace2, cardDeck, 1);

        player1 = playerMaker(player1, 1, 3, new MockPlayerBoardGameBoard(1));
        player2 = playerMaker(player2, 2, 3, new MockPlayerBoardGameBoard(0));
        player3 = playerMaker(player3, 3, 3, new MockPlayerBoardGameBoard(2));
    }
    public void testInitializationOfCardPlaces(){



        assertEquals(4, civilizationCardPlaces4.getRequiredResources());
        assertEquals(3, civilizationCardPlace3.getRequiredResources());
        assertEquals(2, civilizationCardPlace2.getRequiredResources());
        assertEquals(1, civilizationCardPlace1.getRequiredResources());

        assertNotSame(civilizationCardPlaces4.getCardOfThisPlace(), civilizationCardPlace3.getCardOfThisPlace());
        assertNotSame(civilizationCardPlaces4.getCardOfThisPlace(), civilizationCardPlace2.getCardOfThisPlace());
        assertNotSame(civilizationCardPlaces4.getCardOfThisPlace(), civilizationCardPlace1.getCardOfThisPlace());

        assertNotSame(civilizationCardPlace3.getCardOfThisPlace(), civilizationCardPlace2.getCardOfThisPlace());
        assertNotSame(civilizationCardPlace3.getCardOfThisPlace(), civilizationCardPlace1.getCardOfThisPlace());

        assertNotSame(civilizationCardPlace2.getCardOfThisPlace(), civilizationCardPlace1.getCardOfThisPlace());
    }

    public void testPlaceFigures() {

        assertFalse(civilizationCardPlaces4.placeFigures(player1, 2));
        assertTrue(civilizationCardPlaces4.placeFigures(player1, 1));
        assertFalse(civilizationCardPlace3.placeFigures(player1, 1));

        assertFalse(civilizationCardPlaces4.placeFigures(player2, 2));
        assertFalse(civilizationCardPlaces4.placeFigures(player2, 1));

        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace3.tryToPlaceFigures(player3,1));
        assertFalse(civilizationCardPlace3.placeFigures(player3, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlaces4.tryToPlaceFigures(player3, 1));
        assertTrue(civilizationCardPlace2.placeFigures(player3, 1));
        assertFalse(civilizationCardPlace1.placeFigures(player3, 1));
    }

    public void testMakeAction(){

        Optional<CivilizationCard> cardBefore = civilizationCardPlaces4.getCardOfThisPlace();

        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlaces4.tryToMakeAction(player1));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlaces4.tryToPlaceFigures(player1,1));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, civilizationCardPlaces4.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlaces4.tryToMakeAction(player3));

        Effect[] badInput1 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.FOOD};
        Effect[] badInput2 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE};
        Effect[] input = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.CLAY};

        assertEquals(ActionResult.FAILURE, civilizationCardPlaces4.makeAction(player3, input, new Effect[0]));
        assertEquals(ActionResult.FAILURE, civilizationCardPlaces4.makeAction(player1, badInput1, new Effect[0]));
        assertEquals(ActionResult.FAILURE, civilizationCardPlaces4.makeAction(player1, badInput2, new Effect[0]));

        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlaces4.makeAction(player1, input, new Effect[0]));
        assertFalse(civilizationCardPlaces4.skipAction(player3));
        assertFalse(civilizationCardPlaces4.skipAction(player1));

        assertTrue(civilizationCardPlace3.placeFigures(player3, 1));
        assertFalse(civilizationCardPlace3.skipAction(player1));
        assertTrue(civilizationCardPlace3.skipAction(player3));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace3.tryToMakeAction(player3));

        assertTrue(civilizationCardPlace2.placeFigures(player3, 1));
        assertFalse(civilizationCardPlace2.newTurn());

        assertTrue(civilizationCardPlaces4.newTurn());

        assertNotSame(cardBefore, civilizationCardPlaces4.getCardOfThisPlace());
    }

}