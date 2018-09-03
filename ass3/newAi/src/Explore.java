import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Explore{
	

	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	public VisitedView visitedview;
	private static StateHashMap statehashmap = new StateHashMap();
	HashMap<Integer,Integer> postionList = new HashMap<Integer,Integer>();
	LinkedList<Character> possibleList = new LinkedList<Character>();
	char action = 'e';
	public ArrayList<Integer[]> prevStates = new ArrayList<Integer[]>();
	public Explore(VisitedView visitedview){
		int sum = 79*100+79;
		this.postionList.put(sum, 1);
		this.visitedview = visitedview;
	}
  
	public ArrayList<Character> getMoves() {
	
		  	  
		ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
		ArrayList<Character> moves = new ArrayList<Character>();
		
		possible = this.possibleAdjacency();
		
		if(!possible.isEmpty()) {
			Integer[] pos = possible.get(0);
			moves = this.toAdjacency(pos);
			
			return moves;
		}
		else {
			int size = this.prevStates.size();
			if(size >= 1) {
				Integer[] pos = this.prevStates.get(size-1);
				this.prevStates.remove(size - 1);
			
				moves = this.toAdjacency(pos);
			}
			
			
			
		}
		
		return moves;
		
		  
	
	

	  	
	  
		  
	}
  
private ArrayList<Character> toAdjacency(Integer[] pos) {
	ArrayList<Character> moves = new ArrayList<Character>();
	
	Integer pos_x = this.visitedview.getPos_x();
	Integer pos_y = this.visitedview.getPos_y();
	Integer currDirection = this.visitedview.getCurrDirection();
	if(pos_x == pos[0] && pos_y + 1== pos[1]) {
		if(currDirection == this.NORTH) {
			moves.add('R');
		}
		else if(currDirection == this.WEST) {
			moves.add('R');
			moves.add('R');
		}
		else if(currDirection == this.SOUTH) {
			moves.add('L');
		}
		else if(currDirection == this.EAST) {
		}
	}
	else if(pos_x + 1 == pos[0] && pos_y == pos[1]) {
		if(currDirection == this.NORTH) {
			moves.add('R');
			moves.add('R');
		}
		else if(currDirection == this.WEST) {
			moves.add('L');
		}
		else if(currDirection == this.SOUTH) {
			
		}
		else if(currDirection == this.EAST) {
			moves.add('R');
		}
	}
	else if(pos_x == pos[0] && pos_y - 1 == pos[1]) {
		if(currDirection == this.NORTH) {
			moves.add('L');
		}
		else if(currDirection == this.WEST) {
			
		}
		else if(currDirection == this.SOUTH) {
			moves.add('R');
		}
		else if(currDirection == this.EAST) {
			moves.add('R');
			moves.add('R');
		}	
	}
	else if(pos_x - 1 == pos[0] && pos_y == pos[1]) {
		if(currDirection == this.NORTH) {
			
		}
		else if(currDirection == this.WEST) {
			moves.add('R');
		}
		else if(currDirection == this.SOUTH) {
			moves.add('R');
			moves.add('R');
		}
		else if(currDirection == this.EAST) {
			moves.add('L');
		}	
	}
	
	if(this.visitedview.have_axe && this.visitedview.visitedview[pos[0]][pos[1]] == 'T') {
		moves.add('C');
		
	}
	if(this.visitedview.have_key && this.visitedview.visitedview[pos[0]][pos[1]] == '-') {
		moves.add('U');
		
	}
	moves.add('F');
	return moves;
}

public ArrayList<Integer[]> possibleAdjacency(){
  	int pos_x = this.visitedview.getPos_x();
	int pos_y = this.visitedview.getPos_y();
	
	ArrayList<Integer[]> adjacency = this.visitedview.adjacency(pos_x, pos_y);
	ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
	int size = adjacency.size();
	
	for(int i = 0; i < size; i++) {
		Integer[] pos = adjacency.get(i);
		int sum = pos[0]*100 + pos[1];
		if(this.postionList.containsKey(sum)) {
			continue;
		}
		int newx = pos[0];
		int newy = pos[1];
		Integer[] newpos = {newx, newy};
		possible.add(newpos);
	}
	return possible;
}


  public char get_return_value(State s) {
	  
	  if(!s.possibleList.isEmpty()) {
			for(char c : this.possibleList) {
				System.out.println(c);
			}
			System.out.println(s.possibleList.size());
		  	this.action = s.possibleList.removeFirst();
		  	s.setAction(this.action);
		  	return this.action;
		  }
	  return this.action;
  }
  
 
  
  
  
}