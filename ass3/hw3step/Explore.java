import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * @author jingcheng li
 * Explore is a class that deals with the exploration of the map. When there are no other
 * actions needed to do, it will try to get to the places that hasn't been reached yet.
 * 
 * There are two cases -- can reach to water or not. It uses DFS and stores the state when
 * it reaches to a new place, also uses a hashmap to store the visited places. If there is 
 * no adjacent places to go, it will try to go back to the previous state.
 */

public class Explore{
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	public VisitedView visitedview;
	public int on_raft_time = 0;
	public boolean tempExlpored = false;
	public boolean goBack = false;
	public HashMap<Integer,Integer> postionList = new HashMap<Integer,Integer>();
	public LinkedList<Character> possibleList = new LinkedList<Character>();
	public ArrayList<Integer[]> prevStates = new ArrayList<Integer[]>();
	public State state = new State();
	
	
	/**
	 * Constructor of class, initialise an Explore object, the origional point is
	 * set to be visited.
	 * 
	 * @param visitedview, information of the current stage.
	 */
	public Explore(VisitedView visitedview){
		
		int sum = 79*100+79;
		this.postionList.put(sum, 1);
		this.visitedview = visitedview;
		this.state.setPrevState(null);
		this.state.setPos(79, 79);
	}
  
	/**
	 * Return the next few moves to do, does not take water into account. If 
	 * there is no available adjacent places to be reached, it will ask the 
	 * agent to go back to the previous state.
	 * 
	 * @return possible, a list of actions
	 */	
	public ArrayList<Character> getMoves() {
		
	  	  
		ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
		ArrayList<Character> moves = new ArrayList<Character>();
		
		possible = this.possibleAdjacency();
		
		//if there are accessible adjacent places

		if(!possible.isEmpty()) {
			//add the actions needed to an adjacent place

			Integer[] pos = possible.get(0);
			moves = this.toAdjacency(pos);
			
			return moves;
		}
		//if there are no accessible adjacent places
		else {
			//add the actions needed to to back to the previous state

			if((this.state.getPrevState() != null) && !(this.visitedview.on_raft == true && this.state.prevState.on_raft == false)) {
				
				Integer[] pos = this.state.getPrevState().getPos();
				if(this.visitedview.checkWaterObstacle(pos[0], pos[1])) {
					
					moves = this.toAdjacency(pos);
					this.goBack = true;
				}	
			}	
		}
		
		return moves;
	  
		  
	}
	/**
	 * Return the next few moves to do, takes water into account. If 
	 * there is no available adjacent places to be reached, it will ask the 
	 * agent to go back to the previous state.
	 * 
	 * @return possible, a list of actions
	 */
	public ArrayList<Character> getWaterMoves() {
		
	  	  
		ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
		ArrayList<Character> moves = new ArrayList<Character>();
		possible = this.possibleWaterAdjacency();
		//if there are accessible adjacent places
		if(!possible.isEmpty()) {
			
			//add the actions needed to an adjacent place
			Integer[] pos = possible.get(0);
			moves = this.toAdjacency(pos);
			
			return moves;
		}
		//if there are no accessible adjacent places
		else {
			
			//add the actions needed to to back to the previous state
			if((this.state.getPrevState() != null)&&!(this.visitedview.on_raft == true && this.on_raft_time == 1)) {
				
				Integer[] pos = this.state.getPrevState().getPos();
				moves = this.toAdjacency(pos);
				this.goBack = true;
			}
			
		}
		
		return moves;  
	}


	
	/**
	 * 
	 * This method calculates the moves needed to move to an adjacent place,
	 * and if there are doors or trees there, we will add relative actions.
	 * 
	 * @param pos, the place to be reached
	 * @return moves, a list of actions needed
	 */
	public ArrayList<Character> toAdjacency(Integer[] pos) {
		
		ArrayList<Character> moves = new ArrayList<Character>();
		Integer pos_x = this.visitedview.getPos_x();
		Integer pos_y = this.visitedview.getPos_y();
		Integer currDirection = this.visitedview.getCurrDirection();
		
		//if the adjacent is to the east of the current position
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
		//if the adjacent is to the south of the current position
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
		//if the adjacent is to the west of the current position
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
		//if the adjacent is to the north of the current position
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
		//if there is a tree, cut it
		if(this.visitedview.have_axe && this.visitedview.visitedview[pos[0]][pos[1]] == 'T') {
			
			moves.add('C');
		}
		//if there is a door, open it
		if(this.visitedview.have_key && this.visitedview.visitedview[pos[0]][pos[1]] == '-') {
			
			moves.add('U');
		}
		//move forward to the place
		moves.add('F');
		
		return moves;
	}

	/**
	 * Finds all the possible(accessible) adjacent places, and return them as a list, does not 
	 * take water into account.
	 * 
	 * @return possible, a list of all accessible adjacent places.
	 */
	public ArrayList<Integer[]> possibleAdjacency(){
		
	  	int pos_x = this.visitedview.getPos_x();
		int pos_y = this.visitedview.getPos_y();
		ArrayList<Integer[]> adjacency = this.visitedview.adjacency(pos_x, pos_y);
		ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
		int size = adjacency.size();
		
		for(int i = 0; i < size; i++) {
			
			Integer[] pos = adjacency.get(i);
			int sum = pos[0]*100 + pos[1];
			
			//if this place has been visited, ignore it
			if(this.postionList.containsKey(sum)) {
				
				continue;
			}
			//cutting trees is not an easy decision
			if(this.visitedview.visitedview[pos[0]][pos[1]] == 'T') {
				
				continue;
			}
			
			int newx = pos[0];
			int newy = pos[1];
			Integer[] newpos = {newx, newy};
			possible.add(newpos);
		}
		
		return possible;
	}


	/**
	 * Finds all the possible(accessible) adjacent places, and return them as a list, takes 
	 * water into account.
	 * 
	 * @return possible, a list of all accessible adjacent places.
	 */
	public ArrayList<Integer[]> possibleWaterAdjacency(){
		
	  	int pos_x = this.visitedview.getPos_x();
		int pos_y = this.visitedview.getPos_y();
		ArrayList<Integer[]> adjacency = this.visitedview.waterAdjacency(pos_x, pos_y);
		ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
		int size = adjacency.size();
		
		for(int i = 0; i < size; i++) {
			
			Integer[] pos = adjacency.get(i);
			int sum = pos[0]*100 + pos[1];
			
			//if this place has been visited, ignore it

			if(this.postionList.containsKey(sum)) {
				
				continue;
			}
			//cutting trees is not an easy decision

			if(this.visitedview.visitedview[pos[0]][pos[1]] == 'T') {
				
				continue;
			}
			//if we get to a new island, reset the flag
			if(this.visitedview.visitedview[pos[0]][pos[1]] == ' ') {
				
				if(this.tempExlpored) {
					
					this.tempExlpored = false;
				}
			}
	
			int newx = pos[0];
			int newy = pos[1];
			Integer[] newpos = {newx, newy};
			possible.add(newpos);
		}
		
		return possible;
	}
  
 
  
}