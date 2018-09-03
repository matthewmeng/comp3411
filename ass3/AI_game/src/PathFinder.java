import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;



/**
 * PathFinder inplementation using A* algorithm to find the shortest path between to position
 *
 */
public class PathFinder {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	private ManhattanDistance heuristic;
	private VisitedView visitedview;
	CostComparator comparator;
	Explore explore;
	ArrayList<Integer[]> the_path = new ArrayList<Integer[]>();
	private boolean find = false;
	
	/**
	 * PathFinder need to know the current situation(visitedview) and the heuristic
	 * @param visitedview
	 */
	public PathFinder(VisitedView visitedview) {
		
		this.visitedview = visitedview;
		this.heuristic = new ManhattanDistance();
	}
	
	/**
	 * pass the State of start and end point to the method to find the optimal path 
	 * path is an Arraylist of ArrayList<State> each state Arraylist has two adjacent State
	 * we find the Arraylist which contains the goal and trace back through the path until find the start
	 * put all the position of the state we found though the tracing into an ArrayList<Integer[]>.
	 * @param start State
	 * @param goal State
	 * @return ArrayList<Integer[]>
	 * 
	 */
	public ArrayList<Integer[]> Path(State start, State goal) {
		ArrayList<ArrayList<State>> path = FindPath(start,goal);
		
		for(ArrayList<State> a: path) {
			
			Integer[] pos = a.get(0).getPos();
			Integer[] goalpos = goal.getPos();
			
			if(pos[0] == goalpos[0] && pos[1] == goalpos[1]) {
				
				this.find = true;
				break;
			}
		}
		
		if(this.find == true) {
			
			ArrayList<Integer[]> newpath = new ArrayList<Integer[]>();
			State target = goal;
			
			while((target != null) && (!target.equals(start))) {
				
				Integer[] targpos = target.getPos();
				
				newpath.add(targpos);
				
				for(ArrayList<State> b : path) {
					
					Integer[] pos = b.get(0).getPos();
					
					if(targpos[0] == pos[0] && targpos[1] == pos[1]) {
						
						target = b.get(1);
						break;
					}
				}
			}
			
			return newpath;
		}
		
		return null;
	}
		
	
	/**
	 * FindPath use the Manhattan A* algorithm to find the optimal path between the start and goal
	 * We use the HashMap to store the g-cost of each State
	 * path is an ArrayList contains the adjacent State
	 * each time we find the adjacent state of the current state calculate their cost and put the adjacent states which
	 * has the smallest cost into the ArrayList path
	 * @param start
	 * @param goal
	 * @return
	 */
	private ArrayList<ArrayList<State>> FindPath(State start, State goal){
		
		HashMap<State, Integer> costMap = new HashMap<>();
		ArrayList<State> adjacentState= new ArrayList<State>();
		ArrayList<ArrayList<State>> path = new ArrayList<ArrayList<State>>();
		ArrayList<State> closed = new ArrayList<>();
		ArrayList<Integer[]> neighbors = new ArrayList<Integer[]>();
		CostComparator comparator = new CostComparator(costMap,goal);
		PriorityQueue<State> open = new PriorityQueue<>(30,comparator);
		
		start.setNumStone(this.visitedview.numStone);
		start.setHaveRaft(this.visitedview.have_raft);
		start.setOnRaft(this.visitedview.on_raft);
		start.setHaveStone(this.visitedview.have_stone);
		start.setHaveAxe(this.visitedview.have_axe);
		start.setHaveKey(this.visitedview.have_key);
		start.setVisitedView(this.visitedview);
		open.add(start);
		costMap.put(start, 0);

		while(open.size() > 0) {
			
			State current = open.remove();
			closed.add(current);
			Integer currpos[] = current.getPos();
			Integer goalpos[] = goal.getPos();
			
			if((currpos[0] == goalpos[0]) && (currpos[1] == goalpos[1])) {
				
				break;
			}
			
//			i++;
//			if((i >= 2000) && (this.visitedview.have_treasure == false)) {
//				break;
//			}
			
			neighbors = this.waterAdjacency(current);
			
			for(Integer[] the_adjacent : neighbors) {
				
				State adjacent = new State();
				adjacent.setPos(the_adjacent[0], the_adjacent[1]);
				adjacent.setPrevState(current);
				boolean contains = false;
				boolean have_raft = current.have_raft;
				boolean on_raft = current.on_raft;
				boolean have_stone = current.have_stone;
				boolean have_axe = current.have_axe;
				boolean have_key = current.have_key;
				int numStone = current.numStone;
				VisitedView visitedview = current.visitedview;
				
				contains = closed.contains(adjacent) || open.contains(adjacent);
				adjacent.setNumStone(numStone);
				adjacent.setHaveRaft(have_raft);
				adjacent.setOnRaft(on_raft);
				adjacent.setHaveStone(have_stone);
				adjacent.setHaveAxe(have_axe);
				adjacent.setHaveKey(have_key);
				adjacent.setVisitedView(visitedview);
				adjacent = this.updateState(the_adjacent[0], the_adjacent[1], adjacent);
				
				int gCost = costMap.get(current);
				int hCost = this.heuristic.getCost(currpos[0], currpos[1], goalpos[0], goalpos[1]);
				int fCost = gCost + hCost;
				
				if(!costMap.containsKey(adjacent)) {
					
					costMap.put(adjacent, fCost);
				}
				
				int oriCost = costMap.get(adjacent);
				
				if((fCost < oriCost) && contains) {
					
						
						adjacentState.add(adjacent);
						adjacentState.add(current);
						path.add(adjacentState);
						costMap.put(adjacent, fCost);
						adjacentState = new ArrayList<State>();
				}
				
				if((!open.contains(adjacent)) && (!closed.contains(adjacent))) {
					
						open.add(adjacent);
						adjacentState.add(adjacent);
						adjacentState.add(current);
						path.add(adjacentState);
						adjacentState = new ArrayList<State>();
			}
			}
		}

		return path;	
	}
	

	/**
	 * Update the info of the State during the A* search
	 * @param row
	 * @param col
	 * @param adjacent
	 * @return
	 */
	private State updateState(Integer row, Integer col, State adjacent) {
		
		if(adjacent.visitedview.visitedview[row][col] == 'a') {
			
			adjacent.have_axe = true;
			
			if(adjacent.on_raft) {
				
				adjacent.on_raft = false;
			}
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == 'k') {
			
			adjacent.have_key = true;
			
			if(adjacent.on_raft) {
				
				adjacent.on_raft = false;
			}
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == 'O') {
			
			if(adjacent.on_raft) {
				
				adjacent.on_raft = false;
			}
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == 'T' && adjacent.have_axe) {
			
			adjacent.have_raft = true;
			
			if(adjacent.on_raft) {
				
				adjacent.on_raft = false;
			}
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == 'o') {
		  	
				adjacent.have_stone = true;
				adjacent.numStone++;	
				
				if(adjacent.on_raft) {
					
					adjacent.on_raft = false;
				}
			
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == '~' && adjacent.have_stone) {
			
			adjacent.numStone--;
			
			if(adjacent.numStone == 0) {
				adjacent.setHaveStone(false);
			}

		}
		
		else if(adjacent.visitedview.visitedview[row][col] == '~' && adjacent.have_raft) {
			
			adjacent.on_raft = true;
			adjacent.have_raft = false;
		}
		
		else if(adjacent.visitedview.visitedview[row][col] == ' ' && adjacent.on_raft) {
			
			adjacent.on_raft = false;
		}
	
		return adjacent;
	}

	/**
	 * find the adjacent state include water
	 * @param current
	 * @return
	 */
	private ArrayList<Integer[]> waterAdjacency(State current) {
		
		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
		int row = current.getPos()[0];
		int col = current.getPos()[1];
		
		if(this.checkWaterObstacle(row, col+1, current)) {
			
			Integer[] pos = {row, col+1};
			adjacency.add(pos);
		}
		
		if(this.checkWaterObstacle(row+1, col, current)) {
			
			Integer[] pos = {row+1, col};
			adjacency.add(pos);
		}
		
		if(this.checkWaterObstacle(row-1, col, current)) {
			
			Integer[] pos = {row-1, col};
			adjacency.add(pos);

		}
		if(this.checkWaterObstacle(row, col-1, current)) {
			
			Integer[] pos = {row, col-1};
			adjacency.add(pos);

		}
		
		return adjacency;
	}

	
	/**
	 * Check the Obstacle with water
	 * @param row
	 * @param col
	 * @param current
	 * @return
	 */
	public boolean checkWaterObstacle(Integer row, Integer col, State current) {
		
		char[][] visitedview = current.visitedview.visitedview;
	
		if(visitedview[row][col] == 'T' && current.have_axe) {
			
			return true;
		}
		
		else if(visitedview[row][col] == '-' && current.have_key) {
			
			return true;
		}
		
		else if(visitedview[row][col] == 'o') {
			
			return true;
		}
		
		else if(visitedview[row][col] == 'O') {
			
			return true;
		}
		
		else if(visitedview[row][col] == 'k') {
			
			return true;
		}
		
		else if(visitedview[row][col] == 'a') {
			
			return true;
		}
		
		else if(visitedview[row][col] == '$') {
			
			return true;
		}
		
		else if(visitedview[row][col] == ' ') {
			
			return true;
		}
		
		else if(visitedview[row][col] == '~' && current.have_stone) {
			
			return true;
		}
		
		else if(visitedview[row][col] == '~' && ((current.have_raft) || (current.on_raft))) {
			
			return true;
		}
		
		else if(visitedview[row][col] == '*' || visitedview[row][col] == '&') {
			
			return false;
		}
		
		return false;
	}
	
	
	
	/**
	 * Change the pathlist to an Arraylist of character ,directly pass to the Strategy as a list of actions
	 * @param pathlist
	 * @return
	 */
	public ArrayList<Character> changetocharList(ArrayList<Integer[]> pathlist){
		
		int currection = this.visitedview.getCurrDirection();
		ArrayList<Character> charlist = new ArrayList<Character>();
		int i = (pathlist.size()-1);
		Integer[] start = {this.visitedview.getPos_x(),this.visitedview.getPos_y()};
		Integer[] from = start;
		
		while(i >= 0) {			
		
			Integer[] to = pathlist.get(i);
			if(i >= 0) {
				
				i--;
			}

			if(currection == NORTH) {
				
				if((to[0] == (from[0]-1))&&((to[1] == from[1]))){
					
					currection = NORTH;
				}	
				else if((to[0] == (from[0]+1))&&((to[1] == from[1]))) {
					
					currection = SOUTH;
					charlist.add('R');
					charlist.add('R');
				}	
				else if((to[0] == from[0])&&((to[1] == (from[1]-1)))) {
					
					currection = WEST;
					charlist.add('L');
				}
				else if((to[0] == from[0])&&((to[1] == (from[1]+1)))) {
					
					currection = EAST;
					charlist.add('R');
				}
				
			}
			
			else if(currection == SOUTH) {
				
				if((to[0] == (from[0]+1))&&((to[1] == from[1]))){
					
					currection = SOUTH;
				}
				else if((to[0] == (from[0]-1))&&((to[1] == from[1]))) {
					
					currection = NORTH;
					charlist.add('R');
					charlist.add('R');
				}
				else if((to[0] == from[0])&&(to[1] == (from[1]+1))) {
					
					currection = EAST;
					charlist.add('L');
				}
				else if((to[0] == from[0])&&(to[1] == (from[1]-1))) {
					
					currection = WEST;
					charlist.add('R');
				}
			}
			
			else if(currection == WEST) {
				
				if((to[0] == (from[0]-1))&&(to[1] == from[1])){
					
					currection = NORTH;
					charlist.add('R');
					
				}
				else if((to[0] == (from[0]+1))&&(to[1] == from[1])) {
					
					currection = SOUTH;
					charlist.add('L');
				}
				else if((to[0] == from[0])&&(to[1] == (from[1]-1))) {
					
					currection = WEST;
				}
				else if((to[0] == from[0])&&(to[1] == (from[1]+1))) {
					
					currection = EAST;
					charlist.add('R');
					charlist.add('R');
				}
			}

			else if(currection == EAST) {
				
				if((to[0] == (from[0]-1))&&(to[1] == from[1])){
					
					currection = NORTH;
					charlist.add('L');
				}else if((to[0] == (from[0]+1))&&(to[1] == from[1])) {
					
					currection = SOUTH;
					charlist.add('R');
				}else if((to[0] == from[0])&&(to[1] == (from[1]-1))) {
					
					currection = WEST;
					charlist.add('R');
					charlist.add('R');
				}else if((to[0] == from[0])&&(to[1] == (from[1]+1))) {
					
					currection = EAST;
				}
			}
			
			if(this.visitedview.have_axe && this.visitedview.visitedview[to[0]][to[1]] == 'T') {
				
				charlist.add('C');
			}
			if(this.visitedview.have_key && this.visitedview.visitedview[to[0]][to[1]] == '-') {
				
				charlist.add('U');
			}
				
			charlist.add('F');
			from = to;
			
			if(i < 0) {
				
				break;
			}
		}

		return charlist;
	}
	
	

	
}
