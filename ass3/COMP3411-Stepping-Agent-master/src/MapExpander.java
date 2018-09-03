
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * 
 * MapExpander 
 * Using backtracking algorithm to let the agent explore the unknown environment.
 * 
 */

public class MapExpander {
	private static final int VISITED = 1;
	private static final int NOT_VISITED = 0;
	
	private Environment environment;
	private ActionKnowledge actionKnowledge;
	private int[][] visitedMap;
	private ArrayDeque<Position> stepBackPath;
	private ArrayDeque<Action> actions;
	
	
	
	public MapExpander (Environment environment,
						ActionKnowledge actionKnowledge) {
		this.environment = environment;
		this.actionKnowledge = actionKnowledge;
		stepBackPath = new ArrayDeque<>();
		actions = new ArrayDeque<>();
		initVisitedMap();
	}
	
	private void initVisitedMap() {
		stepBackPath.add(environment.getCurrentPosition());
		visitedMap = new int[environment.getHeight()][environment.getWidth()];
		clearVisitedMap();
		visitedMap[environment.getCurrentPosition().row][environment.getCurrentPosition().col] = 1;
	}
	
	public void clearVisitedMap() {
		for(int i = 0;i < visitedMap.length;i++) {
			for(int j = 0;j < visitedMap[i].length;j++) {
				visitedMap[i][j] = NOT_VISITED;
			}
		}
	}
	
	
	/**
	 * explore
	 * the strategy is to check if there's any unvisited neighbors then the agent can 
	 * go to the neighbors or go back until find unvisted neighbors.
	 * @return
	 */
	public Action explore() {
		//System.out.println(" ----------------> EXPANDING MAP: CURRENT POS = " + environment.getCurrentPosition()
		//					+ " DIRECTION: " + environment.getCurrentDirection() );
		if(!actions.isEmpty()) {
			//System.out.print("Get next action from stack ");
			return applyNextAction();
		}
		
		ArrayList<Position> neighbors = findInvisitedNeighbor();
		if (neighbors.isEmpty()) {
			return stepBack();
		} else {
			Position neighbor = neighbors.get(0);
			Position start = environment.getCurrentPosition();
			actions.addAll(actionKnowledge.getActions(start, neighbor));
			return applyNextAction();
		}
	}
	
	/**
	 * Find all the neighbors of the agent which has not been visited.
	 * @return
	 */
	private ArrayList<Position> findInvisitedNeighbor() {
		Position currentPosition = environment.getCurrentPosition();
		ArrayList<Position> neighbors = environment.getAccessibleNeighbors(currentPosition);
		ListIterator<Position> iterator = neighbors.listIterator();
		while(iterator.hasNext()) {
			Position neighbor = iterator.next();
			if (visitedMap[neighbor.row][neighbor.col] == VISITED) {
				// remove neighbors that are already visited
				iterator.remove();
			}
		}
		//Position.printList("UNVISITED NEIGHBORS", neighbors);
		return neighbors;
	}
	
	/**
	 * get the next action and update the visited path.
	 * @return
	 */
	private Action applyNextAction() {
		Action action = actions.pop();
		if (action == null) {
			return Action.NONE;
		}
		//System.out.println(action);
		recordStep(action, environment.getForwardPosition());
		return action;
	}
	
	/**
	 * Record the step if the agent moves forward.
	 * @param action
	 * @param position
	 */
	public void recordStep(Action action, Position position) {
		if(action == Action.FORWARD) {
			//System.out.println(action);
			visitedMap[position.row][position.col] = VISITED;
			if(!position.equals(stepBackPath.peek())) {
				stepBackPath.push(position);
				//Position.printList("STEP BACK", stepBackPath);
			}
		}
	}
	
	
	/**
	 * after the agent has no neighbor available ,then pop the previous position from the 
	 * stack.
	 * @return
	 */
	private Action stepBack() {
		//Position.printList("STEP BACK", stepBackPath);
		Position prevPosion = getPreviousPosition();
		
		if (prevPosion != null) {
			//System.out.println("CURRENT POS " + environment.getCurrentPosition());
			//System.out.println("PREV POS " + prevPosion);
			return doStepBack(prevPosion);
		} else {
			// finish exploring
			//System.out.println("FINISH");
			//printUnvisitedMap();
			return Action.NONE;
		}
	}
	
	/**
	 * get the previous position from the visited path.
	 * @return
	 */
	private Position getPreviousPosition() {
		if(stepBackPath.isEmpty()) {
			return null;
		} else {
			Position position = stepBackPath.pop();
			if(position.equals(environment.getCurrentPosition())) { // get next one, head of path is current position
				if (stepBackPath.isEmpty())
					return null;
				else
					return stepBackPath.pop();
			}
			return position;
		}
	}
	
	/**
	 * apply the back step
	 * @param prevPosition
	 * @return
	 */
	private Action doStepBack(Position prevPosition) {
		Position currentPosition = environment.getCurrentPosition();
		//System.out.println("Do step back: current " + currentPosition + " to prev " + prevPosition);
		
		actions.addAll(actionKnowledge.getActions(currentPosition, prevPosition));
		
		//System.out.println("CURRENT POS "+ currentPosition);
		//System.out.println("PREV POS "+ prevPosition);
		//Action.printList("ACTIONS", actions);
		return applyNextAction();
	}
	
	/**
	 * helper function
	 */
	public void printUnvisitedMap () {
		for (int i = 0; i < visitedMap.length; i++) {
			for (int j = 0; j < visitedMap[i].length; j++) {
				System.out.print(visitedMap[i][j]);	
			}
			System.out.println();	
		}
	}
}
