package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class GameBoard implements InterfaceGetState {
    private String state;
    private final Map<Location, InterfaceFigureLocationInternal> allLocations = new HashMap<>();

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
    private final Building[] buildings = new Building[]{
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.CLAY))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.STONE, Effect.GOLD))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.STONE, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.CLAY, Effect.CLAY))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.STONE, Effect.WOOD))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.WOOD))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.STONE, Effect.STONE))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.GOLD))),
            new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.CLAY, Effect.GOLD))),
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

    private List<Building> allBuildings;

    public GameBoard(final Collection<Player> players) {
        allBuildings = Arrays.asList(buildings);

        ToolMakerHutFields fields = new ToolMakerHutFields(players.size());

        //Initialization of: toolMaker, fields and hut
        allLocations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        allLocations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        allLocations.put(Location.HUT, new PlaceOnHutAdaptor(fields));

        //Initialization of: Hunting grounds, forest, clay mound, quarry and river
        allLocations.put(Location.HUNTING_GROUNDS, new ResourceSource(Effect.FOOD,  players.size()));
        allLocations.put(Location.FOREST, new ResourceSource(Effect.WOOD, players.size()));
        allLocations.put(Location.CLAY_MOUND, new ResourceSource(Effect.CLAY, players.size()));
        allLocations.put(Location.QUARY, new ResourceSource(Effect.STONE, players.size()));
        allLocations.put(Location.RIVER, new ResourceSource(Effect.GOLD, players.size()));

        //Initialization of buildings tiles
        ArrayList<Location> buildingTiles = new ArrayList<>();
        buildingTiles.add(Location.BUILDING_TILE1);
        buildingTiles.add(Location.BUILDING_TILE2);
        buildingTiles.add(Location.BUILDING_TILE3);
        buildingTiles.add(Location.BUILDING_TILE4);
        for(int i = 0; i < players.size(); i++){
            allLocations.put(buildingTiles.get(i), new BuildingTile(generateBuildings()));
        }

        //Initialization of CardDeck
        CivilizationCardDeck civilizationCardDeck = new CivilizationCardDeck(generateCards());
        //Initialization of Card places
        CivilizationCardPlace cardPlace4 = new CivilizationCardPlace(null, civilizationCardDeck, 4);
        CivilizationCardPlace cardPlace3 = new CivilizationCardPlace(cardPlace4, civilizationCardDeck,3);
        CivilizationCardPlace cardPlace2 = new CivilizationCardPlace(cardPlace3, civilizationCardDeck, 2);
        CivilizationCardPlace cardPlace1 = new CivilizationCardPlace(cardPlace2, civilizationCardDeck, 1);
        allLocations.put(Location.CIVILISATION_CARD1, cardPlace1);
        allLocations.put(Location.CIVILISATION_CARD2, cardPlace2);
        allLocations.put(Location.CIVILISATION_CARD3, cardPlace3);
        allLocations.put(Location.CIVILISATION_CARD4, cardPlace4);
    }

    private List<Building> generateBuildings(){
        Collections.shuffle(allBuildings);
        List<Building> toReturn = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            toReturn.add(allBuildings.get(i));
            allBuildings.remove(allBuildings.get(i));
        }
        return toReturn;
    }

    private List<CivilizationCard> generateCards(){
        List<CivilizationCard> toShuffle = Arrays.asList(allCard);
        Collections.shuffle(toShuffle);
        return toShuffle;
    }

    public String state() {
        Map<Location, String> states = new HashMap<>();

        for(Location location: allLocations.keySet()){
            states.put(location, allLocations.get(location).state());
        }
        return new JSONObject(states).toString();
    }
}