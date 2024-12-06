package sk.uniba.fmph.dcs.game_board;

import junit.framework.TestCase;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.player_board.PlayerCivilizationCards;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.*;

public class CivilizationCardPlaceTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;
    private CivilizationCardDeck specialDeck;
    private CivilizationCardPlace civilizationCardPlace4;
    private CivilizationCardPlace civilizationCardPlace3;
    private CivilizationCardPlace civilizationCardPlace2;
    private CivilizationCardPlace civilizationCardPlace1;
    private Field cardOfThisPlace;
    private final CivilizationCard[] allCard = {
            //Dice roll ( 10 cards )
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.WRITING)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.POTTERY)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.BUILDER)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),
            new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                    List.of(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),
            // FOOD (7 cards)
            new CivilizationCard(List.of(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.MEDICINE)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.POTTERY)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.WEAVING)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.WEAVING)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(List.of(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    List.of(EndOfGameEffect.BUILDER)),
            // Resource (5 cards)
            new CivilizationCard(List.of(ImmediateEffect.STONE, ImmediateEffect.STONE),
                    List.of(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(List.of(ImmediateEffect.STONE),
                    List.of(EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.STONE),
                    List.of(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(List.of(ImmediateEffect.CLAY),
                    List.of(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),
            new CivilizationCard(List.of(ImmediateEffect.GOLD),
                    List.of(EndOfGameEffect.SHAMAN)),
            // Resources with dice roll (3 cards)
            new CivilizationCard(List.of(ImmediateEffect.ThrowStone),
                    List.of(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(List.of(ImmediateEffect.ThrowGold),
                    List.of(EndOfGameEffect.ART)),
            new CivilizationCard(List.of(ImmediateEffect.WOOD),
                    List.of(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),
            // Victory Points (3 cards)
            new CivilizationCard(List.of(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    List.of(EndOfGameEffect.MUSIC)),
            new CivilizationCard(List.of(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    List.of(EndOfGameEffect.MUSIC)),
            new CivilizationCard(List.of(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    List.of(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            // Extra.Tool tile (1 card)
            new CivilizationCard(List.of(ImmediateEffect.Tool),
                    List.of(EndOfGameEffect.ART)),
            // Agriculture (2 cards)
            new CivilizationCard(List.of(ImmediateEffect.Field),
                    List.of(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(List.of(ImmediateEffect.Field),
                    List.of(EndOfGameEffect.FARMER)),
            // Civilization card for the final scoring (1 card)
            new CivilizationCard(List.of(ImmediateEffect.CARD),
                    List.of(EndOfGameEffect.WRITING)),
            // One-use tool (3 cards)
            new CivilizationCard(List.of(ImmediateEffect.OneTimeTool2),
                    List.of(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(List.of(ImmediateEffect.OneTimeTool3),
                    List.of(EndOfGameEffect.BUILDER)),
            new CivilizationCard(List.of(ImmediateEffect.OneTimeTool4),
                    List.of(EndOfGameEffect.BUILDER)),
            // Any 2 resources (1 card)
            new CivilizationCard(List.of(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource),
                    List.of(EndOfGameEffect.MEDICINE)),
    };


    public void setUp() throws NoSuchFieldException {
        //Create simple cardDeck to create card places and special cardDecks to test EndOfGameEffects
        List<CivilizationCard> toShuffle = new ArrayList<>(List.of(allCard));
        Collections.shuffle(toShuffle);
        CivilizationCardDeck cardDeck = new CivilizationCardDeck(toShuffle);

        //Set last card in specialCardDeck to card with Immediate effect CARD
        toShuffle.set(toShuffle.size() - 1, new CivilizationCard(List.of(ImmediateEffect.CARD),
                        List.of(EndOfGameEffect.WRITING)));
        specialDeck = new CivilizationCardDeck(toShuffle);

        //Creating 4 card places for game
        civilizationCardPlace4 = new CivilizationCardPlace(null, cardDeck, 4);
        civilizationCardPlace3 = new CivilizationCardPlace(civilizationCardPlace4, cardDeck, 3);
        civilizationCardPlace2 = new CivilizationCardPlace(civilizationCardPlace3, cardDeck,2);
        civilizationCardPlace1 = new CivilizationCardPlace(civilizationCardPlace2, cardDeck, 1);

        //Setting accesses to the card which is at the given card place
        cardOfThisPlace = CivilizationCardPlace.class.getDeclaredField("cardOfThisPlace");
        cardOfThisPlace.setAccessible(true);

        //Creating 3 players to imitate their actions
        player1 = new Player(new PlayerOrder(0, 3), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player2 = new Player(new PlayerOrder(1, 3),  new PlayerBoardGameBoardFacade(new PlayerBoard()));
        player3 = new Player(new PlayerOrder(2, 3),  new PlayerBoardGameBoardFacade(new PlayerBoard()));
    }
    public void testInitializationOfCardPlaces() throws NoSuchFieldException, IllegalAccessException {
        //Setting accesses to the required resources of the card places to know if they have been initialized right
        Field resources = CivilizationCardPlace.class.getDeclaredField("requiredResources");
        resources.setAccessible(true);

        int resourcesOfPlace4 = (int) resources.get(civilizationCardPlace4);
        int resourcesOfPlace3 = (int) resources.get(civilizationCardPlace3);
        int resourcesOfPlace2 = (int) resources.get(civilizationCardPlace2);
        int resourcesOfPlace1 = (int) resources.get(civilizationCardPlace1);

        assertEquals(4, resourcesOfPlace4);
        assertEquals(3, resourcesOfPlace3);
        assertEquals(2, resourcesOfPlace2);
        assertEquals(1, resourcesOfPlace1);

        //Checking that all cards are different
        Optional<CivilizationCard> cardOnPlace4 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace4);
        Optional<CivilizationCard> cardOnPlace3 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace3);
        Optional<CivilizationCard> cardOnPlace2 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace2);
        Optional<CivilizationCard> cardOnPlace1 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace1);

        assertNotSame(cardOnPlace4, cardOnPlace3);
        assertNotSame(cardOnPlace4, cardOnPlace2);
        assertNotSame(cardOnPlace4, cardOnPlace1);

        assertNotSame(cardOnPlace3, cardOnPlace2);
        assertNotSame(cardOnPlace3, cardOnPlace1);

        assertNotSame(cardOnPlace2, cardOnPlace1);
    }

    public void testPlaceFigures() {
        //Create one HuntingFields to store unnecessary figures
        ResourceSource rs = new ResourceSource(Effect.FOOD, 3);

        //Try to place on card more than one figure
        assertFalse(civilizationCardPlace4.placeFigures(player1, 2));
        assertTrue(civilizationCardPlace4.placeFigures(player1, 1));

        //Place figure which player doesn't have
        assertTrue(rs.placeFigures(player1, 4));
        assertFalse(civilizationCardPlace3.placeFigures(player1, 1));

        //Try to place figure on card when there is a figure of another player
        assertFalse(civilizationCardPlace4.placeFigures(player2, 2));
        assertFalse(civilizationCardPlace4.placeFigures(player2, 1));

        //Test TryToPlaceFigures
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace3.tryToPlaceFigures(player3,1));
        assertFalse(civilizationCardPlace3.placeFigures(player3, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace4.tryToPlaceFigures(player3, 1));
        assertTrue(civilizationCardPlace2.placeFigures(player3, 1));
    }

    public void testMakeAction() throws NoSuchFieldException, IllegalAccessException {

        //Try to make action without any figure
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace4.tryToMakeAction(player1));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace4.tryToPlaceFigures(player1,1));

        //Test tryToMakeAction, test it when there is a figure of another player
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, civilizationCardPlace4.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace4.tryToMakeAction(player3));

        //Try to skip action when player have figure on the cardPlace
        assertFalse(civilizationCardPlace4.skipAction(player3));
        assertTrue(civilizationCardPlace4.skipAction(player1));

        assertTrue(civilizationCardPlace3.placeFigures(player3, 1));
        assertFalse(civilizationCardPlace3.skipAction(player1));
        assertTrue(civilizationCardPlace3.skipAction(player3));
        //Try to make action after we skipped action
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace3.tryToMakeAction(player3));

        assertTrue(civilizationCardPlace2.placeFigures(player3, 1));

        //Check if all card places will return false, because cardDeck isn't empty
        assertFalse(civilizationCardPlace1.newTurn());
        assertFalse(civilizationCardPlace2.newTurn());
        assertFalse(civilizationCardPlace3.newTurn());
        assertFalse(civilizationCardPlace4.newTurn());

        //Get cardDeck to make it empty and check if newTurn() will return true
        Field deck = CivilizationCardPlace.class.getDeclaredField("cardDeck");
        deck.setAccessible(true);
        CivilizationCardDeck toMakeEmpty = (CivilizationCardDeck) deck.get(civilizationCardPlace2);
        Optional<CivilizationCard> last = toMakeEmpty.getTop();
        while (last.isPresent()){
            last = toMakeEmpty.getTop();
        }
        player3.getPlayerBoard().giveEffect(List.of(Effect.STONE, Effect.WOOD));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace2.tryToPlaceFigures(player3, 1));
        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace2.makeAction(player3, List.of(Effect.STONE, Effect.WOOD), List.of(Effect.WOOD, Effect.WOOD)));
        //NewTurn()
        assertFalse(civilizationCardPlace1.newTurn());
        assertFalse(civilizationCardPlace2.newTurn());
        assertFalse(civilizationCardPlace3.newTurn());
        assertTrue(civilizationCardPlace4.newTurn());
    }

    public void testPayForCard() throws NoSuchFieldException, IllegalAccessException {
        //Place player1 figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace4.tryToPlaceFigures(player1, 1));

        //Test payment with different inputs
        Effect[] badInput1 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.FOOD};
        Effect[] badInput2 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE};
        Effect[] input = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.CLAY};

        //Add player1 resources
        player1.getPlayerBoard().giveEffect(List.of(input));

        assertEquals(ActionResult.FAILURE, civilizationCardPlace4.makeAction(player3, List.of(input), List.of(new Effect[0])));
        assertEquals(ActionResult.FAILURE, civilizationCardPlace4.makeAction(player1, List.of(badInput1), List.of(new Effect[0])));
        assertEquals(ActionResult.FAILURE, civilizationCardPlace4.makeAction(player1, List.of(badInput2), List.of(new Effect[0])));

        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace4.makeAction(player1, List.of(input), List.of(Effect.WOOD, Effect.WOOD)));

        //Test if we there won't be any card, after player bought it
        Optional<CivilizationCard> cardOnPlace4 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace4);
        assertEquals(Optional.empty(), cardOnPlace4);

        //Place player3 figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace3.tryToPlaceFigures(player3, 1));

        //Good input, but player3 have only one gold
        Effect[] goodInput = new Effect[]{Effect.GOLD, Effect.STONE, Effect.GOLD};
        player3.getPlayerBoard().giveEffect(List.of(Effect.STONE, Effect.GOLD));

        assertEquals(ActionResult.FAILURE, civilizationCardPlace3.makeAction(player3, List.of(goodInput), List.of(new Effect[0])));
    }

    public void testIfCardShiftsProperly() throws NoSuchFieldException, IllegalAccessException {

        //Get cards to compare after new turn
        Optional<CivilizationCard> cardBeforeOnPlace4 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace4);
        Optional<CivilizationCard> cardBeforeOnPlace3 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace3);
        Optional<CivilizationCard> cardBeforeOnPlace1 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace1);

        //Buy card 2
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace2.tryToPlaceFigures(player1, 1));
        Effect[] input = new Effect[]{Effect.STONE,Effect.STONE};
        player1.getPlayerBoard().giveEffect(List.of(input));
        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace2.makeAction(player1, List.of(input), List.of(Effect.WOOD, Effect.WOOD)));

        //Buy card 3
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, civilizationCardPlace3.tryToPlaceFigures(player3, 1));
        Effect[] input2 = new Effect[]{Effect.STONE,Effect.STONE,Effect.WOOD};
        player3.getPlayerBoard().giveEffect(List.of(input2));
        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace3.makeAction(player3, List.of(input2), List.of(Effect.WOOD, Effect.WOOD)));

        //Check which cardPlaces needs new cards
        assertFalse(civilizationCardPlace1.newTurn());
        assertFalse(civilizationCardPlace2.newTurn());
        assertFalse(civilizationCardPlace3.newTurn());
        assertFalse(civilizationCardPlace4.newTurn());

        //Compare new cards with old cards
        Optional<CivilizationCard> cardOnPlace4 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace4);
        Optional<CivilizationCard> cardOnPlace3 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace3);
        Optional<CivilizationCard> cardOnPlace1 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace1);
        Optional<CivilizationCard> cardOnPlace2 = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace2);


        assertEquals(cardBeforeOnPlace4, cardOnPlace2);
        assertEquals(cardBeforeOnPlace1, cardBeforeOnPlace1);
        assertNotSame(cardBeforeOnPlace3, cardOnPlace3);
        assertNotSame(cardBeforeOnPlace4, cardOnPlace4);
    }

    public void testGettingImmediateEffect() throws NoSuchFieldException, IllegalAccessException {

        //Place figures on cardPlace 1
        assertTrue(civilizationCardPlace1.placeFigures(player1, 1));
        player1.getPlayerBoard().giveEffect(List.of(Effect.WOOD));

        Optional<CivilizationCard> card = (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace1);

        List<ImmediateEffect> immediateEffect = card.get().getImmediateEffectType();
        ImmediateEffect toUnderstand = immediateEffect.get(0);

        switch (toUnderstand){
            case FOOD:{
                //Remove all starter food (12 points)
                for(int i = 0; i < 12; i++){
                    assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                }
                assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                for(int i = 0; i < immediateEffect.size(); i++){
                    assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                }
                assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                break;
            }
            case WOOD, CLAY, STONE, GOLD:{
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                Effect whichResourceToTest = null;
                switch (toUnderstand){
                    case WOOD -> whichResourceToTest = Effect.WOOD;
                    case CLAY -> whichResourceToTest = Effect.CLAY;
                    case STONE -> whichResourceToTest = Effect.STONE;
                    case GOLD -> whichResourceToTest = Effect.GOLD;

                }
                for(int i = 0; i < immediateEffect.size(); i++){
                    assertTrue(player1.getPlayerBoard().takeResources(List.of(whichResourceToTest)));
                }
                assertFalse(player1.getPlayerBoard().takeResources(List.of(whichResourceToTest)));
                break;
            }
            case OneTimeTool2, OneTimeTool3, OneTimeTool4:{
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                switch (toUnderstand){
                    case OneTimeTool2 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(2));
                    case OneTimeTool3 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(3));
                    case OneTimeTool4 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(4));
                }
                break;
            }
            case POINT:{
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                PlayerBoardGameBoardFacade fromWhichFacade = (PlayerBoardGameBoardFacade) player1.getPlayerBoard();

                //Set accessibility for points
                Field playerBoard = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
                playerBoard.setAccessible(true);
                Field points = PlayerBoard.class.getDeclaredField("points");
                points.setAccessible(true);

                PlayerBoard forPoints = (PlayerBoard) playerBoard.get(fromWhichFacade);
                int pointsWeGot = (int) points.get(forPoints);
                assertEquals(3,pointsWeGot);
                break;
            }
            case ThrowGold, ThrowStone, ThrowWood, ThrowClay:{
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));

                //Set accessibility to performer and sum
                Field performer = CivilizationCardPlace.class.getDeclaredField("performer");
                performer.setAccessible(true);
                Field sum = GetSomethingThrow.class.getDeclaredField("sum");
                sum.setAccessible(true);

                GetSomethingThrow getSmthPrf = (GetSomethingThrow) performer.get(civilizationCardPlace1);
                int whatSumWeGot = (int) sum.get(getSmthPrf);
                Effect whatWeGot = null;
                switch (toUnderstand){
                    case ThrowWood -> whatWeGot = Effect.WOOD;
                    case ThrowClay -> whatWeGot = Effect.CLAY;
                    case ThrowStone -> whatWeGot = Effect.STONE;
                    case ThrowGold -> whatWeGot = Effect.GOLD;
                }
                //Check if player1 get specify amount of resources
                for(int i = 0; i < whatSumWeGot/whatWeGot.points(); i++){
                    assertTrue(player1.getPlayerBoard().takeResources(List.of(whatWeGot)));
                }
                assertFalse(player1.getPlayerBoard().takeResources(List.of(whatWeGot)));
                break;
            }
            case ArbitraryResource:{
                assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player1, List.of(Effect.WOOD), List.of(Effect.GOLD, Effect.GOLD)));
                for(int i = 0; i < 2; i++){
                    assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.GOLD)));
                }
                assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.GOLD)));
                break;
            }
            case AllPlayersTakeReward:{
                break;
            }
        }
    }

    public void testGettingEndOfGameEffectsAndImmediateEffectCARD() throws NoSuchFieldException, IllegalAccessException {

        //Create new cardPlaces to reach ImmediateEffect CARD
        civilizationCardPlace4 = new CivilizationCardPlace(null, specialDeck, 4);
        civilizationCardPlace3 = new CivilizationCardPlace(civilizationCardPlace4, specialDeck, 3);
        civilizationCardPlace2 = new CivilizationCardPlace(civilizationCardPlace3, specialDeck,2);
        civilizationCardPlace1 = new CivilizationCardPlace(civilizationCardPlace2, specialDeck, 1);

        assertTrue(civilizationCardPlace1.placeFigures(player2, 1));
        player2.getPlayerBoard().giveEffect(List.of(Effect.WOOD));

        //Get card from cardPlace1
        Optional<CivilizationCard> card =  (Optional<CivilizationCard>) cardOfThisPlace.get(civilizationCardPlace1);
        //-----------------------------------------------
        //Now we need Map od EndOfGameEffects
        Field plBoard = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
        plBoard.setAccessible(true);
        Field playerCivCards = PlayerBoard.class.getDeclaredField("playerCivilisationCards");
        playerCivCards.setAccessible(true);
        Field mapOfEndOfGame = PlayerCivilizationCards.class.getDeclaredField("endOfGameEffectMap");
        mapOfEndOfGame.setAccessible(true);
        Field stack = CivilizationCardDeck.class.getDeclaredField("cardDeck");
        stack.setAccessible(true);

        PlayerBoardGameBoardFacade fromWhichFacade = (PlayerBoardGameBoardFacade) player2.getPlayerBoard();
        PlayerBoard fromWhichBoard = (PlayerBoard) plBoard.get(fromWhichFacade);
        PlayerCivilizationCards plCards = (PlayerCivilizationCards) playerCivCards.get(fromWhichBoard);
        Map<EndOfGameEffect, Integer> effects = (Map<EndOfGameEffect, Integer>) mapOfEndOfGame.get(plCards);
        Stack<CivilizationCard> cards = (Stack<CivilizationCard>) stack.get(specialDeck);
        CivilizationCard top = cards.peek();

        ArrayList<EndOfGameEffect> endEffects = new ArrayList<>(card.get().getEndOfGameEffectType());
        endEffects.addAll(top.getEndOfGameEffectType());
        //Add endOfGameEffects to player board
        assertTrue(effects.isEmpty());
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, civilizationCardPlace1.tryToMakeAction(player2));
        assertEquals(ActionResult.ACTION_DONE, civilizationCardPlace1.makeAction(player2, List.of(Effect.WOOD), List.of(Effect.WOOD, Effect.WOOD)));
        effects = (Map<EndOfGameEffect, Integer>) mapOfEndOfGame.get(plCards);
        assertFalse(effects.isEmpty());
        //Check if all endOfGameEffects were added + endOfGameEffects from the second card
        for(EndOfGameEffect end: endEffects){
            int val = effects.get(end);
            if(val - 1 == 0){
                effects.remove(end);
            }else{
                effects.put(end, val - 1);
            }
        }
        assertTrue(effects.isEmpty());
    }

}
