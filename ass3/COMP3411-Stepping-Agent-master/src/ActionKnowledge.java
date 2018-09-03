import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import javax.print.attribute.standard.MediaSize.Engineering;

/**
 * The logic that translate a start position and goal position into actions
 */
public class ActionKnowledge {
	private Environment environment;
	
	/**
	 * Creates a ActionKnowledge
	 * @param environment
	 */
	public ActionKnowledge(Environment environment) {
		this.environment = environment;
	}
	
	/**
	 * Gets the actions to get from the given start to goal position
	 * @param start
	 * @param goal
	 * @return list of actions to take
	 */
	public Deque<Action> getActions(Position start, Position goal) {
		ArrayDeque<Action> actions = new ArrayDeque<>();
		
		if (start.equals(goal)) {
			// already at goal
			return actions;
		}
		
		// first rotate agent to the right direction
		// get actions to rotate the agent to face EAST
		if(start.row == goal.row
		   && start.col == goal.col - 1) {
			actions.addAll(getRotateAgentActions(Direction.EAST));
		
		// get actions to rotate the agent to face WEST
		} else if(start.row == goal.row
				  && start.col == goal.col + 1) {
			actions.addAll(getRotateAgentActions(Direction.WEST));
		
			// get actions to rotate the agent to face SOUTH	
		} else if(start.row == goal.row - 1
				  && start.col == goal.col) {
			actions.addAll(getRotateAgentActions(Direction.SOUTH));
			
		// get actions to rotate the agent to face NORTH
		} else if(start.row == goal.row + 1
				  && start.col == goal.col) {
			actions.addAll(getRotateAgentActions(Direction.NORTH));
			
		}
		
		// get actions to move agent
		actions.addAll(getMovementAction(goal));
		//Action.printList("ACTION for start " + start + " to goal " + goal, actions);
		return actions;
	}
	
	/**
	 * Gets actions to move the agent depending on the value of the goal
	 * @param goal
	 * @return
	 */
	private ArrayList<Action> getMovementAction (Position goal) {
		ArrayList<Action> actions = new ArrayList<>();
		int[] tools = environment.getToolKnowledge().getToolsHeld();
		
		// action to chop tree if goal has tree
		if (environment.hasTree(goal.row, goal.col)
			&& tools[ToolKnowledge.INDEX_AXE] > 0) {
			actions.add(Action.CHOP_TREE);
		
		// action to unlock door if goal has door
		} else if (environment.hasDoor(goal.row, goal.col)
			&& tools[ToolKnowledge.INDEX_KEY] > 0) {
			actions.add(Action.UNLOCK_DOOR);
			
		}
		
		// after removing obstacle, move to goal
		actions.add(Action.FORWARD);
		return actions;
	}
	
	/**
	 * Rotates the agent to the given direction
	 * @param goalDirection
	 * @return
	 */
	public Deque<Action> getRotateAgentActions(Direction goalDirection) {
		int rotations = goalDirection.value - environment.getCurrentDirection().value;
		if (rotations == 0) {
			// agent faces the right direction
			return new ArrayDeque<>();
		}
		
		if(rotations == 3)  {
			rotations = -1;
		} else if (rotations == -3) {
			rotations = 1;
		}
		
		ArrayDeque<Action> actions = new ArrayDeque<>();
		for (int i = 0; i < Math.abs(rotations); i++) {
			if (rotations < 0) {
				actions.add(Action.LEFT);
			} else {
				actions.add(Action.RIGHT);
			}
		}
		return actions;
	}
}
