
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * PathFinder implementation using AStar algorithm to find the shortest path
 *
 */
public class PathFinderAStar implements PathFinder {
	private static final int QUEUE_CAPACITY = 30;
	private HeuristicDistance heuristic;
	private Environment environment;
	
	/**
	 * Creates a PathFinderAstar
	 * @param environment
	 * @param heuristic to estimate distance to goal
	 */
	public PathFinderAStar(Environment environment, HeuristicDistance heuristic) {
		this.environment = environment;
		this.heuristic = heuristic;		
	}

	/**
	 * Finds a path for the given start position and to end position
	 * @param start
	 * @param end
	 * @return path
	 */
	@Override
	public Path findPath(Position start, Position goal) {
		//System.out.println("findPath from " + start.toString() + " to " + goal.toString());
		
		// goal is blocked
		if (environment.isBlocked(goal)) {
			return Path.EMPTY_PATH;
		}

		HashMap<Position, Position> path = doFindPath(start, goal);
		
		//System.out.println("Finishing finding path. Now build path ");
		
		if(path.containsKey(goal)){
			System.out.println("YYYYYYYYYYYEEEEEESSSSSSSS");
			return buildPath(path, start, goal);
		}
		// No path found
		return new Path();
	}

	/**
	 * Find the shortest path for given start and goal
	 * @param start
	 * @param goal
	 * @return a HashMap of positions that connect start and goal (e.g. similar to a link list)
	 */
	private HashMap<Position, Position> doFindPath(Position start, Position goal) {
		HashMap<Position, Integer> costs = new HashMap<>();
		HashMap<Position, Position> parents = new HashMap<>();
		CostComparator comparator = new CostComparator(costs, goal);
		PriorityQueue<Position> open = new PriorityQueue<>(QUEUE_CAPACITY, comparator);
		ArrayList<Position> closed = new ArrayList<>();

		open.add(start);
		costs.put(start, 0);
		int i = 0;
		while (open.size() != 0) {
			System.out.println(open.size());
			Position current = open.remove();
			closed.add(current);
			i++;
			System.out.println(i);
			if (current.row == goal.row && current.col == goal.col) {
				// found path to goal
				//System.out.println("FOUND GOAL!!!!!!");
				break;
			}
			
			ArrayList<Position> neighbors = environment.getAccessibleNeighbors(current);
			System.out.println("Neighbors of :"+current.row+"->"+current.col);
			for (Position neighbor : neighbors) {
				System.out.println(neighbor.row+"--------"+neighbor.col);
				//System.out.println("Explore neighbor " + neighbor);

				int newCost = costs.get(current) + heuristic.getCost(current, neighbor);
				//System.out.println("neighbor: {"+neighbor.row+"}-{"+neighbor.col+"} ="+newCost);
				if (costs.get(neighbor) == null) {
					costs.put(neighbor, newCost);
				}
				
				int oldCost = costs.get(neighbor);
				// found a cheaper path, update neighbor's cost
				if (newCost < oldCost
					&& (closed.contains(neighbor)||open.contains(neighbor))){
					//System.out.println("Reset cost: new = " + newCost + ", old = " + oldCost);
					//System.out.println("Reset parent: Parent of " + neighbor +  " = " + current);
					if(current == goal){
						System.out.println("SAME???????????????????");
					}
					costs.put(neighbor, newCost);
					System.out.println("Added from:"+current.row+" "+current.col+"  to:"+neighbor.row+" "+neighbor.col);
					parents.put(neighbor, current);
					//System.out.println("Input ONE");
				}
				
				if (!open.contains(neighbor) && !closed.contains(neighbor)) {
						if(current == goal){
						System.out.println("SAME???????????????????");
					}
					open.add(neighbor);
					System.out.println("Opne Added from:"+current.row+" "+current.col+"  to:"+neighbor.row+" "+neighbor.col);
					parents.put(neighbor, current);

					//System.out.println("Input Two");
					//System.out.println("Add parent: Parent of " + neighbor +  " = " + current);
					//Position.printList("open ", open);
					//Position.printList("closed ", closed);	
				}
			}
		}
		return parents;
	}
	
	/**
	 * Builds a path for the given start and goal function by looking for connected positions in the given HashMap
	 * @param parents contains position that connected to each other
	 * @param start
	 * @param goal
	 * @return path
	 */
	private Path buildPath (HashMap<Position, Position> parents, Position start, Position goal) {
		//System.out.println("buildPath from " + start.toString() + " to " + goal.toString());
		
		Path path = new Path();
		Position target = goal;
		
		// backward, push goal first and back to start
		while (target != null && !target.equals(start)) {
			path.push(target);
			target = parents.get(target);
		}
		//System.out.println("buildPath from " + start + " to " + goal + " =" + path);
		return path;
	}
	
	/**
	 * Compares 2 paths according to their costs. Heuristic is used to estimate cost to goal
	 *
	 */
	private class CostComparator implements Comparator<Position> {
		private HashMap<Position, Integer> costs;
		private Position goal;
		
		public CostComparator(HashMap<Position, Integer> costs, Position goal) {
			this.costs = costs;
			this.goal = goal;
		}
		
		@Override
		public int compare(Position pos1, Position pos2) {
			int costPos1 = costs.get(pos1);
			int costPos2 = costs.get(pos2);
			int fPos1 = costPos1 + heuristic.getCost(pos1, goal);
			int fPos2 = costPos2 + heuristic.getCost(pos2, goal);
			return fPos1 - fPos2;
		}	
	}
}
