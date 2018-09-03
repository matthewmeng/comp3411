import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The Explorer of the agent.
 * This is the logic that decides which action the agent should makes.
 *
 */
public class Explorer {
	private Environment environment;
	private ToolKnowledge toolKnowledge;
	private StoneStrategy stoneStrategy;
	private ActionKnowledge actionKnowledge;
	private HeuristicDistance heuristic;
	private PathFinder pathFinder;
	private MapExpander mapExpander;
	
	private Deque<Action> actions;
	private Path pathToTake;
	

	/**
	 * Creates an Explorer
	 * @param environment
	 * @param actionKnowledge
	 * @param heuristic
	 * @param waterPathFinder
	 */
	public Explorer (Environment environment,
			ActionKnowledge actionKnowledge,
			HeuristicDistance heuristic,
			PathFinderThroughWater waterPathFinder) {
		this.environment = environment;
		this.actionKnowledge = actionKnowledge;
		this.heuristic = heuristic;
		this.toolKnowledge = environment.getToolKnowledge();
		
		this.pathFinder = new PathFinderAStar(environment, heuristic);
		this.actions = new ArrayDeque<>();
		this.pathToTake = new Path();
		this.mapExpander = new MapExpander(environment, actionKnowledge);
		this.stoneStrategy = new StoneStrategy(environment, waterPathFinder);
	}
	
	/**
	 * Gets the action for the agent
	 * @return action to take
	 */
	public Action getAction () {
		// there is still action in the queue to take
		Action nextAction = getNextAction();
		if(nextAction != Action.NONE) {
			mapExpander.recordStep(nextAction, environment.getForwardPosition());
		
		// got gold, move back to initial position
		} else if(toolKnowledge.hasGold()) {
			actions.clear();
			pathToTake = pathFinder.findPath(environment.getCurrentPosition(), environment.getInitPosition());
			// System.out.println("FOUND GOLD! PATH TO TAKE" + pathToTake);
			nextAction = getActionsForPathToTake();
		
		// gold not found, explore
		} else {
			nextAction = explore();
		}
		return nextAction;
	}
	
	/**
	 * Explores all possible options
	 * @return the next action to take or Action.NONE if the agent does not find any path
	 */
	public Action explore() {
		// first get paths to the tools
		Action action = getActionToNearestTool();
		if (action != Action.NONE) {
			// found tool			
			//System.out.println(" =============== GO TO NEAREST TOOL =========== ");
			return action;		
		}
		
		// no more tool or no path to tool found, expand map
		action = mapExpander.explore();
		if (action != Action.NONE) {
			//System.out.println(" =============== EXPAND MAP =========== ");
			return action;
		}
		
		// map is not expandable anymore
		action = moveToObstacle();
		if(action != Action.NONE) {
			//System.out.println(" =============== MOVE TO OBSTACLES =========== ");
			return action;
		}
		
		// map fully explored, find strategy to place stone to go to unreachable tools
		//System.out.println(" =============== MOVE TO WATER USING STONES =========== ");
		
		if(toolKnowledge.getNumOfStonesHeld() > 0 ) {
			action = moveToWaterToUseStone();
			if (action != Action.NONE) {
				return action;
			}
		}
		
		// there is no unreachable tool, see use stone if neighbor is water
		//System.out.println(" =============== MOVE TO WATER NEIGHBOR =========== ");
		return exploreWaterNeighbor();
	}
	
	/**
	 * 
	 * @return Action to go to the neighbor that has water
	 */
	private Action exploreWaterNeighbor() {
		ArrayList<Position> neighbors = environment.getAllNeighbors(environment.getCurrentPosition());
		
		for (Position neighbor : neighbors) {
			if (environment.hasWater(neighbor.row, neighbor.col)
				&& toolKnowledge.getNumOfStonesHeld() > 0){
				pathToTake = new Path();
				pathToTake.push(neighbor);
				return getActionsForPathToTake();
			}
		}
		return Action.NONE;
	}
	
	/**
	 * Goes to the remaining trees and doors and see if we find any path if remove trees and doors
	 * @return action to take
	 */
	private Action moveToObstacle() {
		//System.out.println("MOVE TO OBSTACLES!!!");
		pathToTake = findPathToTreeAndDoor();
		return getActionsForPathToTake();
	}
	
	private Action moveToWaterToUseStone() {
		pathToTake = findOptimalPathWithStones();
		//System.out.println("moveToWater " + pathToTake);
		return getActionsForPathToTake();
	}
	
	/**
	 * Finds shortest paths to remaining trees and doors
	 * @return shortest path
	 */
	private Path findPathToTreeAndDoor() {
		Set<Position> trees = environment.getUnchoppedTrees();
		Set<Position> doors = environment.getUnlockedDoors();
		
		if(trees.size() < doors.size() && toolKnowledge.hasAxe()) {
			Iterator<Position> treeInterator = trees.iterator();
			if (treeInterator.hasNext()) {
				return pathFinder.findPath(environment.getCurrentPosition(), treeInterator.next());
			}
		
		}
		
		if (!doors.isEmpty() && toolKnowledge.hasKey()) {
			Iterator<Position> doorsIterator = doors.iterator();
			if (doorsIterator.hasNext()) {
				return pathFinder.findPath(environment.getCurrentPosition(), doorsIterator.next());			
			}
		}
		return Path.EMPTY_PATH;
	}
	
	/**
	 * Finds optimal path to place a stone
	 * @return optimal path
	 */
	private Path findOptimalPathWithStones() {
		// find the optimal accessible from tool to an accessible position
		Path acessiblePathToTool = stoneStrategy.findPathToPlaceStone();
		if (acessiblePathToTool.isEmpty()) {
			return acessiblePathToTool;
		}
		
		// get the accessible position to tool from the path
		// to build path from agent's current position to that accessible position to get to tool
		Position accessiblePosition = acessiblePathToTool.pop();
		Path pathFromAgentToAccessiblePosition = pathFinder.findPath(environment.getCurrentPosition(), accessiblePosition);
		if (pathFromAgentToAccessiblePosition.isEmpty()) {
			return pathFromAgentToAccessiblePosition;
		}
		
		// get the position of the water in that path after the accessible position
		// adds the the water to path so that the agent can place stone
		Position waterPosition = acessiblePathToTool.pop();
		pathFromAgentToAccessiblePosition.addLast(waterPosition); 
		
		return pathFromAgentToAccessiblePosition;
	}
	
	/**
	 * Gets the next action in the queue. This is used when for one move, we need several actions
	 * @return next action in the queue to take
	 */
	private Action getNextAction () {
		if(!actions.isEmpty()) {
			return actions.pop();
		}
		return getActionsForPathToTake();
	}
	
	/**
	 * Gets the action for the path that we want to take
	 * @return
	 */
	private Action getActionsForPathToTake() {
		if(pathToTake.isEmpty()) {
			return Action.NONE;
		}
		
		//System.out.println("GET ACTION FOR PATH TO TAKE " + pathToTake);
		
		Position start = environment.getCurrentPosition();
		actions.addAll(actionKnowledge.getActions(start, pathToTake.pop()));
		Action action = actions.pop();
		
		if (action != Action.NONE) {
			// found tool			
			mapExpander.recordStep(action, environment.getForwardPosition()); // to step back if we can't find way out	
		} 
		return action;	
	}
	
	/**
	 * Find a path to the nearest tool and return an action to get that tool if there is any
	 * @return action
	 */
	private Action getActionToNearestTool() {
		ArrayList<Position> toolsToGet = toolKnowledge.getToolsToAcquire();
		Collections.sort(toolsToGet, new DistanceComparator(environment.getCurrentPosition()));
		
		Iterator<Position> toolIterator = toolsToGet.iterator();
		
		while (toolIterator.hasNext()) {
			Position tool = toolIterator.next();
			Path path = pathFinder.findPath(environment.getCurrentPosition(), tool);
			if (path.isEmpty()) {
				// we can't find a path to tool, mark at unreachable
				toolKnowledge.addUnreachable(tool);
				//System.out.println("No possible path to tool " + tool);
			} else {
				//buildActions(path);
				pathToTake = path;
				toolIterator.remove();
				return getActionsForPathToTake(); 
			}
		} 
		return Action.NONE;
	}
	
	/**
	 * Compares 2 positions according to the given heuristics
	 *
	 */
	private class DistanceComparator implements Comparator<Position> {
		private Position start;
		
		public DistanceComparator(Position start) {
			this.start = start;
		}

		@Override
		public int compare(Position pos1, Position pos2) {
			int costPos1 = heuristic.getCost(start, pos1);
			int costPos2 = heuristic.getCost(start, pos2);
			
			return costPos1 - costPos2;
		}
		
	}
}
