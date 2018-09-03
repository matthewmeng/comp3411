/**
 * Interface for any distance heuristic
 *
 */
public interface HeuristicDistance {
	/**
	 * Gets the cost for the given start and goal position
	 * @param start
	 * @param goal
	 * @return cost
	 */
	public int getCost(Position start, Position goal);
}
