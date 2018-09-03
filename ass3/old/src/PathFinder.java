import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class PathFinder {
	
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	private ManhattanDIstance heuristic;
	private VisitedView visitedview;
	ArrayList<Integer[]> the_path = new ArrayList<Integer[]>();
	
	public PathFinder(VisitedView visitedview) {
		this.visitedview = visitedview;
		this.heuristic = new ManhattanDIstance();
	}
	
	public ArrayList<Integer[]> Path(Integer[] start, Integer[] goal) {
		HashMap<Integer[],Integer[]> path = FindPath(start, goal);
		//System.out.println(start[0]+"-------start---------"+start[1]+"       "+goal[0]+"-------------goal----------"+goal[1]);
		
		if(path.containsKey(goal)) {
			return buildPath(path,start,goal);
		}
		
		return null;
	}
		
	
	private HashMap<Integer[], Integer[]> FindPath(Integer[] start, Integer[] goal){
		HashMap<Integer[], Integer> the_cost = new HashMap<>();
		HashMap<Integer[], Integer[]> parents = new HashMap<>();
		CostComparator comparator = new CostComparator(the_cost,goal);
		PriorityQueue<Integer[]> open = new PriorityQueue<>(100,comparator);
		ArrayList<Integer[]> closed = new ArrayList<>();
		ArrayList<Integer[]> neighbors = new ArrayList<Integer[]>();
		
		//System.out.println(start[0]+"-------start---------"+start[1]+"       "+goal[0]+"-------------goal----------"+goal[1]);
		open.add(start);
		the_cost.put(start, 0);
		int i = 0;
		
		while(open.size() != 0) {
			Integer[] current = open.remove();
//			System.out.println(current[0]+"-------current--------"+current[1]);
			//System.out.println(goal[0]+"---------goal----------"+goal[1]);
			closed.add(current);
			
			if(current[0] == goal[0] && current[1] == goal[1]) {
				break;
			}
			i++;
			if(i >= 100) {
				break;
			}
			
			neighbors = this.visitedview.adjacency(current[0], current[1]);
			
			for(Integer[] adjacent : neighbors) {
				int newCost = the_cost.get(current) + heuristic.getCost(current[0], current[1], goal[0], goal[1]);
//				System.out.println("adjacent: {"+adjacent[0]+"}-{"+adjacent[1]+"} ="+newCost);
				if(the_cost.get(adjacent) == null) {
					the_cost.put(adjacent, newCost);
				}
				
				int oldCost = the_cost.get(adjacent);
				//System.out.println("The old cost : "+ oldCost);
				if(newCost < oldCost && (closed.contains(adjacent) || open.contains(adjacent))) {
					//System.out.println("Reset cost: new = " + newCost + ", old = " + oldCost);
					//System.out.println("Reset parent: Parent of " + adjacent[0]+"-"+adjacent[1] +  " = " + current[0]+"-"+current[1]);
					//System.out.println("CURRENT :"+current[0]+"----"+current[1]);
					the_cost.put(adjacent, newCost);
//					System.out.println("Current ::::"+current);
					if(adjacent[0] == goal[0]&&adjacent[1] == goal[1]) {
						//System.out.println("SAME");
						parents.put(goal, current);
					}else {
						parents.put(adjacent, current);
					}
						//System.out.println("Input ONE");
				}
				
				if(!open.contains(adjacent) && !closed.contains(adjacent)) {
					open.add(adjacent);
					//System.out.println("CURRENT :"+current[0]+"----"+current[1]);
					if(adjacent[0] == goal[0]&&adjacent[1] == goal[1]) {
						//System.out.println("SAME");
						parents.put(goal, current);
					}else {
						parents.put(adjacent, current);
					}
//					System.out.println("Current ::::"+current);
					//System.out.println("Input Two");
					//System.out.println("Add parent: Parent of " + adjacent[0]+"-"+adjacent[1] +  " = " + current[0]+"-"+current[1]);
				}
			}
		}
		//System.out.println("SIZE :"+parents.size());
		//System.out.println("CHECK"+parents.containsKey(goal)+"Goal is :"+goal);
		
		return parents;
		
		
	}
	
	private ArrayList<Integer[]> buildPath(HashMap<Integer[],Integer[]> parents, Integer[] start, Integer[] goal) {
		ArrayList<Integer[]> newpath = new ArrayList<Integer[]>();
		Integer[] target = goal;
		
		while(target != null && ! target.equals(start)) {
			newpath.add(target);
			//path.push(target);
			target = parents.get(target);
			//System.out.println("||||||||||||||||||||||||");
			
		}
		return newpath;
		
	}
	
	public ArrayList<Character> changetocharList(ArrayList<Integer[]> pathlist){
		int currection = this.visitedview.getCurrDirection();
		
		ArrayList<Character> charlist = new ArrayList<Character>();
		int i = (pathlist.size()-1);
		Integer[] start = {this.visitedview.getPos_x(),this.visitedview.getPos_y()};
		Integer[] from = start;
		//i--;
		while(i >= 0) {
			System.out.println(i);
		
			Integer[] to = pathlist.get(i);
			if(i > 0) {
				i--;
			}
			if(currection == NORTH) {
				if(to[0] == (from[0]-1)&&(to[1] == from[1])){
					currection = NORTH;
					charlist.add('f');
				}else if(to[0] == (from[0]+1)&&(to[1] == from[1])) {
					currection = SOUTH;
					charlist.add('r');
					charlist.add('r');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]-1))) {
					currection = WEST;
					charlist.add('l');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]+1))) {
					currection = EAST;
					charlist.add('r');
					charlist.add('f');
				}
				
			}else if(this.visitedview.getCurrDirection() == SOUTH) {
				if(to[0] == (from[0]+1)&&(to[1] == from[1])){
					currection = SOUTH;
					charlist.add('f');
				}else if(to[0] == (from[0]-1)&&(to[1] == from[1])) {
					currection = NORTH;
					charlist.add('r');
					charlist.add('r');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]+1))) {
					currection = EAST;
					charlist.add('l');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]-1))) {
					currection = WEST;
					charlist.add('r');
					charlist.add('f');
				}
			}else if(this.visitedview.getCurrDirection() == WEST) {
				if(to[0] == (from[0]-1)&&(to[1] == from[1])){
					currection = NORTH;
					charlist.add('r');
					charlist.add('f');
				}else if(to[0] == (from[0]+1)&&(to[1] == from[1])) {
					currection = SOUTH;
					charlist.add('l');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]-1))) {
					currection = WEST;
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]+1))) {
					currection = EAST;
					charlist.add('r');
					charlist.add('r');
					charlist.add('f');
				}
			}else if(this.visitedview.getCurrDirection() == EAST) {
				if(to[0] == (from[0]-1)&&(to[1] == from[1])){
					currection = NORTH;
					charlist.add('l');
					charlist.add('f');
				}else if(to[0] == (from[0]+1)&&(to[1] == from[1])) {
					currection = SOUTH;
					charlist.add('r');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]-1))) {
					currection = WEST;
					charlist.add('r');
					charlist.add('r');
					charlist.add('f');
				}else if(to[0] == from[0]&&(to[1] == (from[1]+1))) {
					currection = EAST;
					charlist.add('f');
				}
			}
			from = to;
			if(i == 0) {
				break;
			}
		}
		for(Character c : charlist) {
			System.out.println(c);
		}
		
		return charlist;
	}
	private class CostComparator implements Comparator<Integer[]>{
		private HashMap<Integer[],Integer> costs;
		private Integer[] goal;
		
		public CostComparator(HashMap<Integer[], Integer> costs, Integer[] goal) {
			this.costs = costs;
			this.goal = goal;
		}
		@Override
		public int compare(Integer[] pos1, Integer[] pos2) {
			int costPos1 = costs.get(pos1);
			int costPos2 = costs.get(pos2);
			int fPos1 = costPos1 + heuristic.getCost(pos1[0],pos1[1],goal[0],goal[1]);
			int fPos2 = costPos2 + heuristic.getCost(pos2[0],pos2[1], goal[0],goal[1]);
			return fPos1 - fPos2;
		}	
	}
	
	
}
