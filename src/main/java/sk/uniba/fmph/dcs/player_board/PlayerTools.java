/**
 * The {@code PlayerTools} class represents a player's collection of tools in a game,
 * allowing for the management, addition, and usage of tools. Tools have different
 * characteristics, including single-use and reusable tools, and their usage is tracked
 * per game round.
 *
 * <p>This class implements the {@link InterfaceGetState} interface and provides a
 * JSON representation of its internal state for serialization.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * PlayerTools playerTools = new PlayerTools();
 * playerTools.addTool();
 * Optional<Integer> toolUsed = playerTools.useTool(0);
 * System.out.println("Tool Used: " + toolUsed.orElse(0));
 * }</pre>
 *
 * @see InterfaceGetState
 */
package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class PlayerTools implements InterfaceGetState {
    /**
     * Array representing the player's tools. Tools in positions 0–2 are reusable,
     * while positions 3–5 can hold single-use tools.
     */
    private final int[] tools;

    /**
     * Boolean array tracking whether reusable tools have been used in the current round.
     */
    private final boolean[] usedTools;

    /**
     * Maximum number of reusable tools allowed.
     */
    private final static int maxMultiplyUseTools = 3;

    /**
     * Maximum total number of tools (both reusable and single-use).
     */
    private final static int maxToolsCount = 6;

    /**
     * Total number of tools available to the player.
     */
    private int totalToolsCount;

    /**
     * Number of tools available for use in the current round.
     */
    private int roundToolsCount;

    /**
     * Initializes a new {@code PlayerTools} instance with empty tool slots and no used tools.
     */
    public PlayerTools() {
        this.tools = new int[6];
        this.usedTools = new boolean[3];
        Arrays.fill(tools, -1);
        Arrays.fill(usedTools, false);
    }

    /**
     * Resets the state of tools for a new game round. Reusable tools are marked as unused,
     * and the round tool count is reset to the total tool count.
     */
    public void newTurn(){
        Arrays.fill(usedTools, false);
        roundToolsCount = totalToolsCount;
    }

    /**
     * Adds a reusable tool to the player's collection, if there is available space.
     * Tools are added in sequence, increasing in strength.
     */
    public void addTool(){
        if(totalToolsCount < 12) {
            int position = totalToolsCount % 3;
            int value = 1 + totalToolsCount / 3;
            tools[position] = value;
            totalToolsCount++;
            roundToolsCount++;
        }
    }

    /**
     * Adds a single-use tool with a specified strength, if there is available space.
     *
     * @param strength the strength of the single-use tool to add.
     * @return {@code true} if the tool was added successfully, {@code false} otherwise.
     */
    public boolean addSingleUseTool(int strength) {
        for (int i = maxMultiplyUseTools; i < tools.length; i++) {
            if (tools[i] == -1) {
                tools[i] = strength;
                totalToolsCount += strength;
                roundToolsCount += strength;
                return true;
            }
        }
        return false;
    }


    /**
     * Uses a tool at the specified index. For reusable tools, usage is tracked for the round.
     * For single-use tools, the tool is removed after use.
     *
     * @param index the index of the tool to use.
     * @return an {@link Optional} containing the tool's strength if it was successfully used,
     * or an empty {@code Optional} if the tool could not be used.
     */
    public Optional<Integer> useTool(int index) {
        Optional<Integer> toReturn = Optional.empty();
        if(index >= maxToolsCount){
            return toReturn;
        }
        if (index > 2){
            if(tools[index] != -1){
                toReturn = Optional.of(tools[index]);
                totalToolsCount -= tools[index];
                roundToolsCount -= tools[index];
                tools[index] = -1;
            }
        } else {
            if(tools[index] != -1 && !usedTools[index]) {
                roundToolsCount = roundToolsCount - tools[index];
                usedTools[index] = true;
                toReturn = Optional.of(tools[index]);
            }

        }
        return toReturn;
    }

    /**
     * Checks if the player has enough tools available to meet a specified goal.
     *
     * @param goal the required tool strength.
     * @return {@code true} if the player has sufficient tools, {@code false} otherwise.
     */
    public boolean hasSufficientTools(int goal){
        return goal <= roundToolsCount;
    }


    /**
     * Returns a JSON string representation of the current state of the player's tools.
     *
     * @return a JSON string representing the tools' state.
     */
    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "tools", tools,
                "usedTools", usedTools,
                "totalToolsCount", totalToolsCount,
                "roundToolsCount", roundToolsCount
        );
        return new JSONObject(state).toString();
    }

    /**
     * Retrieves the total number of tools available to the player.
     *
     * @return the total tool count.
     */
    public int getTotalTools() {
        return this.totalToolsCount;
    }

    /**
     * Retrieves the total tool count.
     *
     * @return the total tool count.
     */
    public int getTotalToolsCount() {
        return totalToolsCount;
    }

    /**
     * Retrieves the array indicating whether each reusable tool has been used.
     *
     * @return a boolean array representing tool usage.
     */
    public boolean[] getUsedTools() {
        return usedTools;
    }

    /**
     * Retrieves the array of tools and their respective strengths.
     *
     * @return an array of tool strengths.
     */
    public int[] getTools() {
        return tools;
    }

    /**
     * Retrieves the number of tools available for use in the current round.
     *
     * @return the round tool count.
     */
    public int getRoundToolsCount() {
        return roundToolsCount;
    }
}
