package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

/**
 * The GameBoard class represents the central game board for a Stone Age board game.
 * It manages all locations, buildings, cards, and resources for gameplay.
 * This class initializes and organizes various components like resource sources,
 * tool-making huts, fields, hunting grounds, and civilization card locations.
 */
public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocation> allLocations = new HashMap<>();

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
            // Victory POINTs (3 cards)
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
            // One-use.tool (3 cards)
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
    private final Building[] buildings = new Building[]{
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.WOOD, Effect.CLAY))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.STONE, Effect.GOLD))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.STONE, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.STONE, Effect.CLAY, Effect.CLAY))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.STONE, Effect.STONE, Effect.WOOD))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.CLAY, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.STONE, Effect.STONE, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.WOOD, Effect.CLAY, Effect.GOLD))),
            new SimpleBuilding(new ArrayList<>(List.of(Effect.STONE, Effect.CLAY, Effect.GOLD))),
            //Variable buildings
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            new VariableBuilding(4, 2),
            //Arbitrary buildings
            new ArbitraryBuilding(1),
            new ArbitraryBuilding(2),
            new ArbitraryBuilding(3),
            new ArbitraryBuilding(4),
            new ArbitraryBuilding(5),
            new ArbitraryBuilding(6),
            new ArbitraryBuilding(6),
            new ArbitraryBuilding(7),
            new ArbitraryBuilding(7)
    };

    private final ArrayList<Building> allBuildings;

    /**
     * Constructs a GameBoard instance and initializes all locations, cards, and buildings
     * based on the given players.
     *
     * @param players A list of players participating in the game.
     */
    public GameBoard(final List<Player> players) {
        allBuildings = new ArrayList<>(List.of(buildings));

        ToolMakerHutFields fields = new ToolMakerHutFields(players.size());

        //Initialization of: toolMaker, fields and hut
        allLocations.put(Location.FIELD, new FigureLocationAdaptor(new PlaceOnFieldsAdaptor(fields), players));
        allLocations.put(Location.TOOL_MAKER, new FigureLocationAdaptor(new PlaceOnToolMakerAdaptor(fields), players));
        allLocations.put(Location.HUT, new FigureLocationAdaptor(new PlaceOnHutAdaptor(fields), players));

        //Initialization of: Hunting grounds, forest, clay mound, quarry and river
        allLocations.put(Location.HUNTING_GROUNDS, new FigureLocationAdaptor(new ResourceSource(Effect.FOOD,  players.size()), players));
        allLocations.put(Location.FOREST, new FigureLocationAdaptor(new ResourceSource(Effect.WOOD, players.size()), players));
        allLocations.put(Location.CLAY_MOUND, new FigureLocationAdaptor(new ResourceSource(Effect.CLAY, players.size()), players));
        allLocations.put(Location.QUARRY, new FigureLocationAdaptor(new ResourceSource(Effect.STONE, players.size()), players));
        allLocations.put(Location.RIVER, new FigureLocationAdaptor(new ResourceSource(Effect.GOLD, players.size()), players));

        //Initialization of buildings tiles
        ArrayList<Location> buildingTiles = new ArrayList<>();
        buildingTiles.add(Location.BUILDING_TILE1);
        buildingTiles.add(Location.BUILDING_TILE2);
        buildingTiles.add(Location.BUILDING_TILE3);
        buildingTiles.add(Location.BUILDING_TILE4);
        for(int i = 0; i < players.size(); i++){
            allLocations.put(buildingTiles.get(i), new FigureLocationAdaptor(new BuildingTile(generateBuildings()), players));
        }

        //Initialization of CardDeck
        CivilizationCardDeck civilizationCardDeck = new CivilizationCardDeck(generateCards());
        //Initialization of Card places
        CivilizationCardPlace cardPlace4 = new CivilizationCardPlace(null, civilizationCardDeck, 4);
        CivilizationCardPlace cardPlace3 = new CivilizationCardPlace(cardPlace4, civilizationCardDeck,3);
        CivilizationCardPlace cardPlace2 = new CivilizationCardPlace(cardPlace3, civilizationCardDeck, 2);
        CivilizationCardPlace cardPlace1 = new CivilizationCardPlace(cardPlace2, civilizationCardDeck, 1);
        allLocations.put(Location.CIVILISATION_CARD1, new FigureLocationAdaptor(cardPlace1, players));
        allLocations.put(Location.CIVILISATION_CARD2, new FigureLocationAdaptor(cardPlace2, players));
        allLocations.put(Location.CIVILISATION_CARD3, new FigureLocationAdaptor(cardPlace3, players));
        allLocations.put(Location.CIVILISATION_CARD4, new FigureLocationAdaptor(cardPlace4, players));
    }

    /**
     * Generates a shuffled list of buildings to be used on the game board.
     *
     * @return A list of buildings for the current game.
     */
    private List<Building> generateBuildings(){
        Collections.shuffle(allBuildings);
        List<Building> toReturn = new ArrayList<>();
        int oldIdx = allBuildings.size() - 1;
        for(int i = oldIdx; i > oldIdx - 7 ; i--){
            toReturn.add(allBuildings.get(i));
            allBuildings.remove(i);
        }
        return toReturn;
    }

    /**
     * Generates a shuffled list of civilization cards to be used during the game.
     *
     * @return A list of civilization cards for the current game.
     */
    private List<CivilizationCard> generateCards(){
        List<CivilizationCard> toShuffle = new ArrayList<>(List.of(allCard));
        Collections.shuffle(toShuffle);
        return toShuffle;
    }

    /**
     * Retrieves all game locations with their associated figure locations.
     *
     * @return A map where keys are locations and values are figure locations.
     */
    public final Map<Location, InterfaceFigureLocation> getAllLocations() {
        return allLocations;
    }

    /**
     * Provides the current state of the game board in JSON format.
     *
     * @return A string representation of the game board's state in JSON format.
     */
    @Override
    public String state() {
        Map<Location, String> states = new HashMap<>();

        for(Location location: allLocations.keySet()){
            states.put(location, allLocations.get(location).toString());
        }
        return new JSONObject(states).toString();
    }
}