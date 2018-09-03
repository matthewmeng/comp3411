
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The information about environment that the agent has explored.
 * 
 * The map of the agent is an 2D array of char which stores the map that the agent has explored every step.
 * As a map can be 80 x 80, a 2D array of 160x160 is used in order to make sure that the agent can start anywhere
 * and we have enough space to keep track of the map.
 * 
 * The init position of the agent in the map is at the center of the map.
 * 
 * The environment keeps track of the location and the facing direction of the agent.
 * 
 * The environment stoors the unchoppedTrees and unlockedDoors for steps optimization.
 */
public class Environment implements TileBaseMap {
	static final int MAP_MAX_ROWS = 159;
	static final int MAP_MAX_COLS = 159;
	
	private static final char BORDER = '|';
	private static final char TREE = 'T';
	private static final char DOOR = '-';
	private static final char WATER = '~';
	private static final char[] OBSTACLES = new char[] {WATER,TREE, DOOR, '*'};

	private char[][] map;
	private Direction currentDirection;
	private Position currentPosition;
	private Position initPosition;

	private ToolKnowledge toolKnowledge;
	private Set<Position> unchoppedTrees;
	private Set<Position> unlockedDoors;

	/**
	 * Creates a new environment for the agent
	 */
	public Environment() {
		initMap();
		currentDirection = Direction.NORTH;
		
		// init current row and column to be the center of the map
		initPosition = new Position((MAP_MAX_ROWS - 1)/2, (MAP_MAX_COLS - 1)/2);
		currentPosition = new Position(initPosition.row, initPosition.col);
		
		unchoppedTrees = new HashSet<>();
		unlockedDoors = new HashSet<>();
		
		toolKnowledge = new ToolKnowledge();
		//printEnvironment();
	}
	
	/**
	 * Initializes an empty map and the position and direction of the agent
	 */
	private void initMap() {
		map = new char[MAP_MAX_ROWS][MAP_MAX_COLS];
		
		for(int i = 0;i < MAP_MAX_ROWS;i++) {
			for(int j = 0;j < MAP_MAX_COLS;j++) {
				map[i][j] = '|';
			}
		}
	}
	
	/**
	 * Updates the map with the given view
	 * @param view
	 */
	public void updateMap(char[][] view) {
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				char value = view[i + 2][j + 2];
				int row = currentPosition.row + i;
				int col = currentPosition.col + j;
				map[row][col] = value;
				scanForObstacles(value, row, col);
				scanForTool(value, row, col);
			}
		}
		//printEnvironment();
	}
	
	/**
	 * Scans if there is any obstacle at that position. If it is a tree or a door, we save it.
	 * There is a special strategy to step through water, so we don't need to save the positions of water
	 * @param value of the position
	 * @param row of the position
	 * @param col of the position
	 */
	private void scanForObstacles(char value, int row, int col) {
		if (value == TREE) {
			unchoppedTrees.add(new Position(row, col));
		} else if (value == DOOR) {
			unlockedDoors.add(new Position(row, col));
		}
	}
	
	/**
	 * Scans if there is any tool at the given position.
	 * If there is tool, we save it in the toolKnowledge
	 * 
	 * @param value
	 * @param row
	 * @param col
	 */
	private void scanForTool(char value, int row, int col) {
		if (value == ToolKnowledge.AXE) {
			toolKnowledge.setAxePos(new Position(row, col));
		} else if (value == ToolKnowledge.KEY) {
			toolKnowledge.setKeyPos(new Position(row, col));
		} else if (value == ToolKnowledge.GOLD) {
			toolKnowledge.setGoldPos(new Position(row, col));
		} else if (value == ToolKnowledge.STONE) {
			toolKnowledge.addStonePos(new Position(row, col));
		} else if (value == 0) {
			// acquire tool
			toolKnowledge.acquireTool(new Position(row, col));
		}
	}
	
	/**
	 * Updates the environment (number of unchopped tree, number of unlocked doors, number of stones used)
	 * according to the given action
	 * @param action
	 */
	public void updateEnvioronment(Action action) {
		Position nextPosition = getForwardPosition();
		if (action == Action.CHOP_TREE) {
			unchoppedTrees.remove(getForwardPosition());
		} else if (action == Action.UNLOCK_DOOR) {
			unlockedDoors.remove(getForwardPosition());
		} else if (action == Action.FORWARD && hasWater(nextPosition.row, nextPosition.col)) {
			toolKnowledge.reduceStone(nextPosition);
		}
	}
	
	/**
	 * Updates the current direction according to the action.
	 * Changes the direction of the agent of the action is LEFT or RIGHT
	 * @param actionChar
	 */
	public void updateCurrentDirection(Action action) {
		if (action == Action.LEFT) {
			int value = floorMod(currentDirection.value - 1, 4);
			currentDirection = Direction.getDirection(value);
			
		} else if (action == Action.RIGHT) {
			int value = floorMod(currentDirection.value + 1, 4);
			currentDirection = Direction.getDirection(value);
		}
		//System.out.println("updateCurrentDirection: " + currentDirection);
	}
	
	/**
	 * Updates the current position of the agent if the action is FORWARD
	 * @param action
	 */
	public void updatePosition(Action action) {
		if(action == Action.FORWARD) {
			//System.out.println("Update position: Row: " + currentPosition.row + ". Col: " + currentPosition.col);
			currentPosition = getForwardPosition();
			//System.out.println("Update position: Row: " +  currentPosition.row + ". Col: " + currentPosition.col );
		}
	}
	
	/**
	 * Gets the next position if we're going forward starting from the current position
	 * @return new position
	 */
	public Position getForwardPosition() {
		int visitRow = currentPosition.row;
		int visitCol = currentPosition.col;
		
		switch (currentDirection) {
		case NORTH:
			visitRow--;
			break;
		case EAST:
			visitCol++;
			break;
		case SOUTH:
			visitRow++;
			break;
		case WEST:
			visitCol--;
			break;
		}
		return new Position(visitRow, visitCol);
	}
	
	/**
	 * Prints the environvment and the tools the agent has
	 */
	public void printEnvironment () {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i == currentPosition.row && j == currentPosition.col) {
					System.out.print(Direction.getAgentSymbol(currentDirection));	
				} else {
					System.out.print(map[i][j]);	
				}
			}
			System.out.println();	
		}
		toolKnowledge.printTools();
	}
	
	/**
	 * Returns all neighbors for the given position
	 * @param position
	 * @return
	 */
	public ArrayList<Position> getAllNeighbors (Position position) {
	ArrayList<Position> neighbors = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int row = position.row + i;
				int col = position.col + j;
				
				if (row == position.row && col == position.col // own position
					|| row != position.row && col != position.col // diagonal neighbors
					|| isBorder(row, col)
					|| isOutOfMap(row, col)) {
					continue;
				}
				neighbors.add(new Position(row, col));
			}
		}
		return neighbors;
	}
	
	/**
	 * Returns the neighbors that are not blocked.
	 * A neighbor is blocked when it has an obstacle and we don't have a tool for that.
	 * Note that neighbor with water is always blocked as we have special strategy to deal with water neighbors
	 * 
	 * @param position to get the neighbors
	 * @return neighbors that are accessible
	 */
	public ArrayList<Position> getAccessibleNeighbors (Position position) {
		ArrayList<Position> neighbors = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <=1; j++) {
				int row = position.row + i;
				int col = position.col + j;
				
				if (row == position.row && col == position.col // own position
					|| row != position.row && col != position.col // diagonal neighbors
					|| hasObstacle(row, col)
					|| isBorder(row, col)
					|| isOutOfMap(row, col)) {
					continue;
				}
				neighbors.add(new Position(row, col));
			}
		}
		//Position.printList("Neighbors of " + position.toString() + " = ", neighbors);
		return neighbors;
	}
	
	/**
	 * @return the current direction that the agent is facing
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	/**
	 * @return the current position of the agent
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * 
	 * @return ToolKnowledge of the agent
	 */
	public ToolKnowledge getToolKnowledge() {
		return toolKnowledge;
	}

	@Override
	public int getWidth() {
		return MAP_MAX_COLS;
	}

	@Override
	public int getHeight() {
		return MAP_MAX_ROWS;
	}
	
	/**
	 * Checks whether there is a obstacle at the given position
	 * @param row
	 * @param col
	 * @return true if there is a tree and we don't have an axe or door and we don't have a key
	 * or if there is water (as we will deal with water in the PathFinderThroughWaterand StoneStrategy.
	 * Returns false otherwise
	 */
	public boolean hasObstacle(int row, int col) {
		int[] tools = toolKnowledge.getToolsHeld();
		for (int i = 0; i < OBSTACLES.length; i++) {
			if (map[row][col] == OBSTACLES[i]) {
				if (hasTree(row, col) && tools[ToolKnowledge.INDEX_AXE] > 0
					|| hasDoor(row, col) && tools[ToolKnowledge.INDEX_KEY] > 0){
				continue; // we have the tool to remove obstacle
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is empty
	 */
	public boolean hasEmptySpace(int row, int col) {
		return map[row][col] == 0;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is door
	 */
	public boolean hasDoor(int row, int col) {
		return map[row][col] == DOOR;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is tree
	 */
	public boolean hasTree(int row, int col) {
		return map[row][col] == TREE;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is water
	 */
	public boolean hasWater(int row, int col) {
		return map[row][col] == WATER;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is border
	 */
	private boolean isBorder (int row, int col) {
		return map[row][col] == BORDER;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if the given position is out of the map
	 */
	private boolean isOutOfMap (int row, int col) {
		if(row == MAP_MAX_ROWS || row == -1
			|| col == MAP_MAX_ROWS || col == -1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isBlocked(Position position) {
		return hasObstacle(position.row, position.col);
	}
	
	/**
	 * @return init position of the agent
	 */
	public Position getInitPosition() {
		return initPosition;
	}
	
	/**
	 * @return list of unchopped trees
	 */
	public Set<Position> getUnchoppedTrees() {
		//Position.printList("UNCHOPPED TREES ", unchoppedTrees);
		return unchoppedTrees;
	}

	/**
	 * @return list of unlocked doors
	 */
	public Set<Position> getUnlockedDoors() {
		//Position.printList("UNCHOPPED DOORS ", unlockedDoors);
		return unlockedDoors;
	}
	
	int floorMod(int x, int y) {
		int floorMod = Math.abs(x) % Math.abs(y);
		floorMod *= Math.signum(x);
		floorMod = (floorMod + Math.abs(y)) % Math.abs(y);
	    return floorMod;
	}
}