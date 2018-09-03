
import java.util.ArrayList;
import java.util.Collection;

/**
 * The ToolKnowledge keeps tracks of
 * - all the tools positions that are discovered and the agent has to acquired
 * - tools that the agent already acquired
 * - tools that are unreachable at the moment
 */
public class ToolKnowledge {
	public static final int INDEX_GOLD = 0;
	public static final int INDEX_AXE = 1;
	public static final int INDEX_KEY = 2;
	public static final int INDEX_STONE = 3;

	public static final char NO_TOOL = 0;
	public static final char AXE = 'a';
	public static final char KEY = 'k';
	public static final char GOLD = '$';
	public static final char STONE = 'o';
	
	// tools we need to acquire
	private ArrayList<Position> stonesToGet;
	private Position axeToGet;
	private Position keyToGet;
	private Position goldToGet;
	
	// tools we have
	private int[] toolsHeld;
	private boolean numOfToolsChanged;
	
	// tools that are not reachable as path is blocked
	private ArrayList<Position> unreachableTools;

	/**
	 * Creates a ToolKnowledge
	 */
	public ToolKnowledge() {
		stonesToGet = new ArrayList<>();
		unreachableTools = new ArrayList<>();
		axeToGet = new Position(Position.INVALID, Position.INVALID);
		keyToGet = new Position(Position.INVALID, Position.INVALID);
		goldToGet = new Position(Position.INVALID, Position.INVALID);
		toolsHeld = new int[4];
	}
	
	/**
	 * Sets the position of an axe to get
	 * @param pos
	 */
	public void setAxePos(Position pos) {
		axeToGet = pos;
	}
	
	/**
	 * Sets the position of a key to get
	 * @param pos
	 */
	public void setKeyPos(Position pos) {
		keyToGet = pos;
	}
	
	/**
	 * Sets the position of the gold to get
	 * @param pos
	 */
	public void setGoldPos(Position pos) {
		goldToGet = pos;
	}
	
	/**
	 * Adds a position of a stone to get
	 * @param pos
	 */
	public void addStonePos(Position pos) {
		// first stone discovered
		if (stonesToGet.isEmpty()) {
			stonesToGet.add(pos);
			return;
		}
		
		// only add a stone to the list if it hasn't been discovered
		for (Position stonePos : stonesToGet) {
			if (pos.row == stonePos.row
				&& pos.col == stonePos.col) {
				// stone is already in the list, don't add it 
				return;
			}
				
		}
		stonesToGet.add(pos);
	}
	
	/**
	 * Acquires the tool if the given row and column match with any of the tools
	 * @param row
	 * @param col
	 */
	public void acquireTool(Position tool) {
		numOfToolsChanged = false;
		if (tool.equals(axeToGet)) {
			updateAcquiredTools(axeToGet, INDEX_AXE);
		} else if (tool.equals(keyToGet)) {
			updateAcquiredTools(keyToGet, INDEX_KEY);
		} else if (stonesToGet.contains(tool)){
			stonesToGet.remove(tool);
			updateAcquiredTools(tool, INDEX_STONE);
		} else if (tool.equals(goldToGet)) {
			updateAcquiredTools(goldToGet, INDEX_GOLD);
		}
	}
	
	/**
	 * Updates the tools we have acquired
	 * @param pos
	 */
	private void updateAcquiredTools(Position toolToGetPosition, int toolIndex) {
		numOfToolsChanged = true;
		toolsHeld[toolIndex]++;
		toolToGetPosition.row = Position.INVALID;
		toolToGetPosition.col = Position.INVALID;
		unreachableTools.clear(); // we found new tool, maybe we'll able to reach the other unreachable tools now with the new tool
		//printTools();
	}
	
	/**
	 * @return a list of tools the agent still needs to acquired
	 */
	public ArrayList<Position> getToolsToAcquire() {
		ArrayList<Position> toolsToGet = new ArrayList<>();
		if (isReachable(goldToGet)) {
			toolsToGet.add(goldToGet);
		}
		
		if (isReachable(axeToGet)) {
			toolsToGet.add(axeToGet);
		}
		
		if (isReachable(keyToGet)) {
			toolsToGet.add(keyToGet);
		}
		
		for (Position stone : stonesToGet) {
			if(isReachable(stone))
				toolsToGet.add(stone);
		}
		//Position.printList("Tools", toolsToGet);
		return toolsToGet;
	}
	
	/**
	 * @param tool
	 * @return true if tool is reachable, false otherwise
	 */
	private boolean isReachable(Position tool) {
		return tool.isValid() && !unreachableTools.contains(tool);
	}
	
	/**
	 * Adds an unreachable tool into the list of unreachable tools
	 * @param tool
	 */
	public void addUnreachable(Position tool) {
		if(!unreachableTools.contains(tool)){
			unreachableTools.add(tool);
		}
	}
	
	/**
	 * @return true if the agent has found gold
	 */
	public boolean hasGold() {
		return toolsHeld[INDEX_GOLD] > 0;
	}
	
	/**
	 * @return true if the agent owns an axe
	 */
	public boolean hasAxe() {
		return getToolsHeld()[ToolKnowledge.INDEX_AXE] > 0;
	}
	
	/**
	 * @return true if the agent owns a key
	 */
	public boolean hasKey() {
		return getToolsHeld()[ToolKnowledge.INDEX_KEY] > 0;
	}
	
	/**
	 * Uses given INDEXES defined at the beginning of this file to get the tool item
	 * @return array of tool the agent has acquired
	 */
	public int[] getToolsHeld() {
		return toolsHeld;
	}
	
	/**
	 * @return number of stones the agent has acquired
	 */
	public int getNumOfStonesHeld() {
		return toolsHeld[INDEX_STONE];
	}
	
	
	/**
	 * @return true if the number of tool the agent owns has changed since the last action
	 */
	public boolean hasToolSetChanged() {
		return numOfToolsChanged;
	}
	
	
	/**
	 * Removes the stone at the given position
	 */
	public void reduceStone(Position stonePosition) {
		toolsHeld[INDEX_STONE]--;
		stonesToGet.remove(stonePosition);
		numOfToolsChanged = true;
	}
	
	/**
	 * 
	 * @return list of unreachable tools
	 */
	public ArrayList<Position> getUnreachableTools() {
		return unreachableTools;
	}
	
	/**
	 * Prints all the tools to get and tools the agent owns
	 */
	public void printTools() {
		System.out.println("Gold = " + goldToGet) ;
		System.out.println("Axe = " + axeToGet);
		System.out.println("Key = " + keyToGet);
		Position.printList("Stones = ", stonesToGet);
		System.out.println();	
		System.out.println("NrOfGold = " + toolsHeld[INDEX_GOLD]) ;
		System.out.println("NrOfAxe = " + toolsHeld[INDEX_AXE]) ;
		System.out.println("NrOfKey = " + toolsHeld[INDEX_KEY]);
		System.out.println("NrOfStones = " + toolsHeld[INDEX_STONE]);
	}
}
