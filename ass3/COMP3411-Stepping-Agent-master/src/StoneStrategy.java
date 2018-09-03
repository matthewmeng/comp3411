import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategies to find the optimal path to place a stone that lead us to unreachable tools
 *
 */
public class StoneStrategy {
	private ToolKnowledge toolKnowledge;
	private PathFinderThroughWater stoneCounter;
	
	/**
	 * Creates a StoneStrategy
	 * @param toolKnowledge
	 * @param waterPathFinder
	 */
	public StoneStrategy(Environment environment, PathFinderThroughWater waterPathFinder) {
		this.toolKnowledge = environment.getToolKnowledge();
		this.stoneCounter = waterPathFinder;

	}
	
	/**
	 * Finds a path to place a stone for unreachable tools
	 * @return the least cost path for an unreachable tool
	 */
	public Path findPathToPlaceStone() {
		ArrayList<Position>unreachableTools = toolKnowledge.getUnreachableTools();
		ArrayList<Path> leastCostPaths = new ArrayList<>();
		HashMap<Position, ArrayList<Path>> toolPathCosts = new HashMap<>();
		
		// get all the costs for unreachable tools
		for (Position tool : unreachableTools) {
			ArrayList<Path>  paths = stoneCounter.findPath(tool);
			
			if(!paths.isEmpty()) {
				toolPathCosts.put(tool, paths);
				// remember the shortest paths
				if(leastCostPaths.isEmpty()
					|| !leastCostPaths.isEmpty()
					&& paths.get(0).getCost() < leastCostPaths.get(0).getCost()) {
						leastCostPaths = paths;
				}
			}	
		}
		
		//printPathsCosts(toolPathCosts);		
		
		if(!leastCostPaths.isEmpty()) {
			// found optimal path to place stone
			return findOptimalPath(leastCostPaths, toolPathCosts);	
		}
		
		// we don't see any unreachable tools
		return Path.EMPTY_PATH;
	}
	
	/**
	 * Finds the optimal paths among paths with the same costs by comparing the costMinization if we place a stone in the water.
	 * Whether that position that we want to place a stone also minimizes the costs to other unreachable tools
	 * @param paths
	 * @param toolsPathCosts
	 * @return optimal path
	 */
	private Path findOptimalPath (ArrayList<Path> paths, HashMap<Position, ArrayList<Path>> toolsPathCosts) {
		//System.out.println("Stone strategy: findOptimalPath amongst these");
		//printPaths(paths);
		
		if (paths.size() == 1) {
			return paths.get(0);
		}
		
		int maxMinization = 0;
		Path optimalPath = new Path();
		
		// compare the costMinization of all paths
		for (Path path : paths) {
			 // calculate the cost of the water position
			// head of the path is the position where the agent can reach, water position is the one after the head
			Position placeStonePos = path.peekNextHead();
			int costMinizations = getCostMinizations(toolsPathCosts, placeStonePos);
			if (costMinizations > maxMinization) {
				maxMinization = costMinizations;
				optimalPath = path;
			}
		}
		
		//System.out.print("Stone strategy: optimal path: " + optimalPath);
		//toolKnowledge.printTools();
		return optimalPath;
	}
	
	/**
	 * Counts how many times the position where we would place the stones occurs in the paths to the other tools
	 * (meaning that, if we place a stone there, we minimize the cost to go to other tools as well)
	 * @param pathForAllTools
	 * @param placeStonePos
	 * @return
	 */
	private int getCostMinizations(HashMap<Position, ArrayList<Path>> toolsPaths, Position placeStonePos) {
		//System.out.println("Stone strategy: getCostMinizations. PlaceStonePos " + placeStonePos);
		
		
		int costMinization = 0;
		
		for (Map.Entry<Position, ArrayList<Path>> tool : toolsPaths.entrySet()) {
			ArrayList<Path> paths = tool.getValue();
			//System.out.println("Path to tool " + tool.getKey() + " Contains? " + placeStonePos);
			//printPaths(paths);
			if (containPosition(paths, placeStonePos)) {
				costMinization++;
				//System.out.println("YES! costMinization " + costMinization);
			}
		}
		return costMinization;
	}
	
	private boolean containPosition(ArrayList<Path> paths, Position position) {
		for (Path path : paths) {
			if(path.contains(position)) {
				return true;
			}
		}
		return false;
	}
	
	private void printPathsCosts(HashMap<Position, ArrayList<Path>> allPaths) {
		System.out.println("++++ Paths ++++");
		for (Map.Entry<Position, ArrayList<Path>> pathsToPos : allPaths.entrySet()) {
			System.out.println(pathsToPos.getKey() + "  -->  ");
			printPaths(pathsToPos.getValue());
			
		}
	}
	
	private void printPaths (ArrayList<Path> paths) {
		for (Path path : paths) {
			System.out.println("Cost = " + path.getCost() + "  -> ");	
			System.out.println(path);	
		}
	}
}
