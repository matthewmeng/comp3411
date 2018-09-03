
/**
 * The Manhattan heuristic of A*
 *
 */
public class ManhattanDistance {
	public ManhattanDistance(){
		
	}
	
	/**
	 * get the the cost of Manhattan heuristic
	 * @param startx x-axis of start point
	 * @param starty y-axis of start point
	 * @param goalx  x-axis of end point
	 * @param goaly  y-axis of end point
	 * @return
	 */
	
	public int getCost(int startx, int starty, int goalx, int goaly) {
		return Math.abs(goalx-startx) + Math.abs(goaly - starty);
	}
}
