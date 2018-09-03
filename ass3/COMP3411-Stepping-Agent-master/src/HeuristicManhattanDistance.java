/**
 * Estimates a cost using Manhattan distance
 */
public class HeuristicManhattanDistance implements HeuristicDistance {

	@Override
	public int getCost(Position start, Position goal) {
		return Math.abs(goal.row - start.row) + Math.abs(goal.col - start.col);
	}
}
