import java.util.Comparator;
import java.util.HashMap;

/**
 * Override the comparator to compares 2 paths according to their costs. 
 *
 */
class CostComparator implements Comparator<State>{
	private HashMap<State, Integer> costs;
	private State goal;
	private ManhattanDistance heuristic;
	
	/**
	 * pass the parameters to the Comparator
	 * @param costs A hash map which contains two adjacent position
	 * @param goal	The goal state
	 */
	public CostComparator(HashMap<State, Integer> costs, State goal) {
		this.costs = costs;
		this.goal = goal;
		this.heuristic = new ManhattanDistance();
	}
	
	@Override
	public int compare(State stat1, State stat2) {
		Integer[] goalpos = goal.getPos();
		Integer[] pos1 = stat1.getPos();
		Integer[] pos2 = stat2.getPos();
		int fPos1 = costs.get(stat1) + heuristic.getCost(pos1[0],pos1[1], goalpos[0],goalpos[1]);
		int fPos2 = costs.get(stat2) + heuristic.getCost(pos2[0],pos2[1], goalpos[0],goalpos[1]);
		return fPos1 - fPos2;
	}	
}
