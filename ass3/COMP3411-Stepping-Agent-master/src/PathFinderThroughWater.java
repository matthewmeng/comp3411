import java.util.ArrayList;

/**
 * This class finds all possible shortest paths (cost == numStones) to a given position.
 * 
 * This approach builds a distance map. The distance map stores the  least cost needed
 * at each position on the map (of number stones needed to get) to any position
 * the agent can access. The space where agent can access has no cost.
 * The distance map is created upon the invocation of findPath() if there is no map exists
 * or the number of tools the agent is holding has changed.
 *  
 * After building the map, to find the shortest path from the given position to any accessible positions
 * it use BFS to travel all possible paths (in decreasing distance) 
 * to find all variants of paths with the same minimal distance
 *
 **/
public class PathFinderThroughWater {
	private Environment env;
	
	private static final int INIT_COST= -1;			
	
	private boolean needsToUpdate = true;
	
	private static final int BOUNDS_ROW_MIN = 0;
	private static final int BOUNDS_ROW_MAX = 1;
	private static final int BOUNDS_COL_MIN = 2;
	private static final int BOUNDS_COL_MAX = 3;
	private int[] bounds;
	private int[][] distMap; // see comment above
	
	/**
	 * Creates a new path finder
	 * @param environment
	 */
	public PathFinderThroughWater(Environment environment) {
		this.env = environment;
		initBounds();
		initDistMap();
	}
	
	private void initBounds() {
		bounds = new int[4];
		for (int index = 0; index < bounds.length; index++)
			bounds[index] = -1;
	}
	
	private void initDistMap() {
		if (distMap == null)
			distMap = new int[Environment.MAP_MAX_ROWS][Environment.MAP_MAX_COLS];
		for (int indexRow = 0; indexRow < Environment.MAP_MAX_ROWS; indexRow++)
			for (int indexCol = 0; indexCol < Environment.MAP_MAX_COLS; indexCol++)
				distMap[indexRow][indexCol] = INIT_COST;
	}
	
//	public void updateMapBoundaries() {
//		int row = env.getCurrentPosition().row;
//		int col = env.getCurrentPosition().col;
//		
//		if (bounds[BOUNDS_ROW_MIN] == -1) {
//			bounds[BOUNDS_ROW_MIN] = row - 2;
//			bounds[BOUNDS_ROW_MAX] = row + 3;
//			bounds[BOUNDS_COL_MIN] = col - 2;
//			bounds[BOUNDS_COL_MAX] = col + 3;
//		
//		} else {
//			if (row - 2 < bounds[BOUNDS_ROW_MIN])
//				bounds[BOUNDS_ROW_MIN] = row - 2;
//			if (row + 3 > bounds[BOUNDS_ROW_MAX])
//				bounds[BOUNDS_ROW_MAX] = row + 3;
//			if (col - 2 < bounds[BOUNDS_COL_MIN])
//				bounds[BOUNDS_COL_MIN] = col - 2;
//			if (col + 3 > bounds[BOUNDS_COL_MAX])
//				bounds[BOUNDS_COL_MAX] = col + 3;
//		}
//		
//		System.out.println("[SC: updateMapBoundaries] "+"\trowMin: "+bounds[BOUNDS_ROW_MIN]+"\trowMax: "+bounds[BOUNDS_ROW_MAX]+"\tcolMin: "+bounds[BOUNDS_COL_MIN]+"\tcolMax: "+bounds[BOUNDS_COL_MAX]);
//		needsToUpdate = true;
//	}
	
	/**
	 * Finds all possible paths that would need the least amount of number of stones to the given positions
	 * @param position 
	 * @return paths with least costt
	 */
	public ArrayList<Path> findPath(Position position) {
		//System.out.println("[SC: countStones] position: "+position.toString());
		 
		if (needsToUpdate)
			buildDistanceMap();	
		
		// get the minimum cost to get to the position
		int cost = distMap[position.row][position.col];

		ArrayList<Path> toVisitPaths = new ArrayList<>();
		ArrayList<Path> shortestPaths = new ArrayList<>();
		
		// create a new path
		Path curPath = new Path();
		curPath.push(position);
		toVisitPaths.add(curPath);
		
		while (toVisitPaths.size() > 0 && cost >= 0) {
			ArrayList<Path> haveVisitedPaths = new ArrayList<>();
			
			//Find all variants of paths that can be reached without any cost
			while (!toVisitPaths.isEmpty()) {
				curPath = toVisitPaths.get(0);
				toVisitPaths.remove(0);
				haveVisitedPaths.add(curPath);
				
				// get last position of the first path (curpath)
				Position curPos = curPath.peek();
				
				// find adjacent neighbors that is connectable (without cost)
				ArrayList<Position> neighbors = env.getAllNeighbors(curPos);
				for (int i = 0; i < neighbors.size(); i++) {
					Position pos = neighbors.get(i);
					
					// reach the empty space,
					// found a path starting from the given position that needed the same cost stored in the distance map
					// to get to accessible space
					if (cost == 0 && distMap[pos.row][pos.col] == cost
						&& distMap[curPos.row][curPos.col] == cost + 1) {
						shortestPaths.add(Path.createPath(curPath, pos));
					
					// found a neighbor that is not a neighbor and that is not explored
					} else if (distMap[pos.row][pos.col] == cost
							&& !env.hasWater(pos.row, pos.col)
							&& !curPath.contains(pos)) {
						// save these connectable neighbors (as a new path) into toVisitPaths 
						toVisitPaths.add(Path.createPath(curPath, pos));
					}
				}
			}
			
			toVisitPaths = haveVisitedPaths;
			haveVisitedPaths = new ArrayList<>();
			
			// Find neighbors that are 1 cost away (water node with the same cost value)
			while (toVisitPaths.size() > 0) {
				curPath = toVisitPaths.get(0);
				toVisitPaths.remove(0);
				
				Position curPos = curPath.peek();
				ArrayList<Position> neighbors = env.getAllNeighbors(curPos);
				
				for (int indexNeighbour = 0; indexNeighbour < neighbors.size(); indexNeighbour++) {
					Position pos = neighbors.get(indexNeighbour);
					
					if (distMap[pos.row][pos.col] == cost
						&& env.hasWater(pos.row, pos.col)
						&& !curPath.contains(pos))
						haveVisitedPaths.add(Path.createPath(curPath, pos));
				}
			}
			toVisitPaths = haveVisitedPaths;		
			cost--;
		}
		
//		System.out.println("[SC: countStones] shortestPaths.size(): "+shortestPaths.size());
//		for (int indexShortestPath = 0; indexShortestPath < shortestPaths.size(); indexShortestPath++)
//			System.out.print(shortestPaths.get(indexShortestPath));

		setCost(shortestPaths, position);
		//printDistMapLarge();
		
		return shortestPaths;
	}
	
	/**
	 * Sets the cost to get to the given position for the  given paths
	 * @param paths
	 * @param position
	 */
	private void setCost(ArrayList<Path> paths, Position position) {
		int cost = distMap[position.row][position.col];
		for (Path path : paths)
			path.setCost(cost);
	}
	
	
	/**
	 * Builds the distance map
	 */
	private void buildDistanceMap() {
		Position curPos = env.getCurrentPosition();
		initDistMap();
		
		ArrayList<Position> haveVisited = new ArrayList<>();
		ArrayList<Position> toVisit = new ArrayList<>();
		ArrayList<Position> edgePositions = new ArrayList<>();	// holds the "boundary" positions, which will lead to the next "distance" group		

		// Starts from where the agent stands (accessible = no cost)
		toVisit.add(curPos);
		int currentCost = 0;
		
		while (!toVisit.isEmpty()) {
			// Find adjacent connectable neighbors (having the same cost to get to as current position
			while (!toVisit.isEmpty()) {
				curPos = toVisit.remove(0);;
				
				if (!haveVisited.contains(curPos)) {
					haveVisited.add(curPos);
					
					ArrayList<Position> curNeighbours = env.getAllNeighbors(curPos);	
					for (int i = 0; i < curNeighbours.size(); i++) {
						Position pos = curNeighbours.get(i);
						
						// if neighbor can be reached with the available tools, add neighbors to the toVisit list
						if (canReach(pos)) {
							toVisit.add(pos);
							// set cost to access neighbor as current cost
							distMap[pos.row][pos.col] = currentCost;						
						}
					}
					
					// if any edge neighbor is not connectable (has different cost),
					// the current position (not the neighbor) is an edge case
					if (isEdgePosition(curPos.row, curPos.col))
						edgePositions.add(curPos);
				}
			}
			
			// 	For each Position in edgePosition array
			// 		- Find adjacent neighbors that have the value INIT_VALUE
			//		- Set these neighbors to currentCost + 1
			//		- Add the position (the neighbors, not current position) into the toVisit array
			for  (int i = 0; i < edgePositions.size(); i++) {
				ArrayList<Position> curNeighbours = env.getAllNeighbors(edgePositions.get(i));
				for (int indexNeighbour = 0; indexNeighbour < curNeighbours.size(); indexNeighbour++) {
					Position pos = curNeighbours.get(indexNeighbour);
					if (distMap[pos.row][pos.col] == INIT_COST) {
						// neighbor is one cost away 
						distMap[pos.row][pos.col] = currentCost + 1;
						
						if (!toVisit.contains(pos))
							toVisit.add(pos);
					}
				}
			}
	
			edgePositions.clear();
			haveVisited.clear();
			currentCost++;
		}
		needsToUpdate = false;
	}
	
	/**
	 * @param pos
	 * @return true if the given pos can be reached by the agent, false otherwise
	 */
	private boolean canReach(Position pos) {
		// Already populated - This will ALWAYS be equal or lesser value.
		if (distMap[pos.row][pos.col] != INIT_COST)
			return false;
		
		// Empty Space
		if (env.hasEmptySpace(pos.row, pos.col))
			return true;
		
		// Not water && Not obstacle ((door && hasKey) || (tree && hasAxe))
		if (!env.hasWater(pos.row, pos.col) && !env.hasObstacle(pos.row, pos.col))
			return true;
		
		return false;
	}
	
	private boolean isEdgePosition(int row, int col) {		
		if ((distMap[row-1][col] == INIT_COST) || (distMap[row+1][col] == INIT_COST) || 
			(distMap[row][col-1] == INIT_COST) || (distMap[row][col+1] == INIT_COST)) 
			return true;
		return false;
	}

	
	private void printDistMapLarge() {
		System.out.print("[SC: printDistMap]\n");
		for (int i = 0; i < distMap.length; i ++) {
			for (int j = 0; j < distMap[i].length; j++) {
				if(distMap[i][j] == INIT_COST) {
					System.out.print("-");
				} else {
					System.out.print("" + distMap[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Notifies the PathFinder that the number of tools has changed 
	 */
	public void onNumberToolsChanged() {
		// toggles a flag which tells the StoneCounter that its distMap is out of date 
		needsToUpdate = true;
	}
}
