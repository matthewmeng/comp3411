import java.util.ArrayList;
import java.util.LinkedList;

/**
 * VisitedView is the main structure of our program, it contains almost all the information 
 * at a single stage. visitedview is a 160 x 160 map, and the original point is (79, 79),
 * which ensures that we can put the map info we know into this map with out exceeding the
 * bound. It also stores the information about all the items, obstacles and special positions.
 * 
 * It also provides many methods to check the status of the things above, update the stored map,
 * check the available adjacent places, and update the status when the agent goes to the next
 * stage(make the next move).
 * 
 * @author jingcheng li
 *
 */

public class VisitedView {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	final static char CHAR_EAST   = '>';
	final static char CHAR_NORTH  = '^';
	final static char CHAR_WEST   = '<';
	final static char CHAR_SOUTH  = 'v';
	
	public char[][] visitedview = new char[160][160];
	public static LinkedList<Integer[]> positionList = new LinkedList<Integer[]>();
	private int pos_x = 0;
	private int pos_y = 0;
	boolean startflag = false;
	private int currDirection;
	private char[][] view;
	
	public boolean have_axe     = false;
	public boolean have_key     = false;
	public boolean have_treasure= false;
	public boolean have_raft    = false;
	public boolean have_stone   = false;
	public boolean on_raft      = false;
	public boolean off_map      = false;
	public int oneraftTime = 0;
	public int numStone = 0;
	 
	private ArrayList<Integer[]> stoneLocations = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> doorLocations = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> waterLocations = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> treeLocations = new ArrayList<Integer[]>();
	private Integer[] axeLocation = {0, 0};
	private Integer[] keyLocation = {0, 0};
	private Integer[] goldLocation = {0, 0};
	
	/**
	 * Constructor, create a VsitedView object, and initialise it
	 */
	public VisitedView() {
		for(int i = 0; i < 160; i++) {
			for(int j = 0; j < 160; j++) {
				this.visitedview[i][j] = '&';
			}
		}
		this.startflag = false;
		this.pos_x = 79;
		this.pos_y = 79;
		this.currDirection = SOUTH;
	}
	
	
	public int getPos_x() {
		
		return pos_x;
	}

	
	/**
	 * return the row of the current position
	 * 
	 * @param pos_x, row of current position
	 */
	public void setPos_x(int pos_x) {
		
		this.pos_x = pos_x;
	}

	/**
	 * return the column of the current position
	 * 
	 * @param pos_y, column of current position
	 */
	public int getPos_y() {
		
		return pos_y;
	}

	
	/**
	 * set the column of the current position
	 * 
	 * @param pos_y, column of current position
	 */
	public void setPos_y(int pos_y) {
		
		this.pos_y = pos_y;
	}
	
	/**
	 * return the current direction
	 * 
	 * @return currDirection, current direction of the agent
	 */
	public int getCurrDirection() {
		
		return currDirection;
	}

	/**
	 * set the current direction
	 * 
	 * @param currDirection, current direction of the agent
	 */
	public void setCurrDirection(int currDirection) {
		
		this.currDirection = currDirection;
	}
	
	/**
	 * return the location of the key
	 * 
	 * @return keyLocation, location of the key
	 */
	public Integer[] getKeyLocation() {
		
		return this.keyLocation;
	}
	


	/**
	 * return the current view
	 * 
	 * @return view, the view the agent has now
	 */
	public char[][] getview() {
		
		return this.view;
	}

	/**
	 * set the current view
	 * 
	 * @param view, the view the agent has now
	 */
	public void setview(char[][] view) {
		
		this.view = view;
	}


	
	/**
	 * initialise the map
	 * 
	 * @param view, the current view
	 */
	public void initial_visitedview(char[][] view) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				this.visitedview[(this.pos_x-2+i)][(this.pos_y-2+j)] = view[i][j];
			}
		}
		this.startflag = true;
	}
	
	
	
	/**
	 * update the visitedview map by the new view
	 * 
	 * @param view, the current view
	 */
	public void updateMap(char[][] view) {
		
		char[][] rotatedview = this.rotate(view);
		for (int i = 0; i <= 4; i++) {
			for (int j = 0; j <= 4; j++) {
				char ch = rotatedview[i][j];
				int row = pos_x + i - 2;
				int col = pos_y + j - 2;
				this.visitedview[row][col] = ch;
				uptateItem(row, col, ch);
			}
		}
		
		//this.print_visited_view();
		

	}

	/**
	 * get the locations of water
	 * 
	 * @return waterLocatiosn, locations of water
	 */
	public ArrayList<Integer[]> getWaterLocations() {
		
		return waterLocations;
	}
	
	/**
	 * get the locations of stone
	 * 
	 * @return stoneLocatiosn, locations of stone
	 */
	public ArrayList<Integer[]> getStoneLocations() {
		
		return stoneLocations;
	}
	
	
	/**
	 * get the locations of door
	 * 
	 * @return doorLocatiosn, locations of door
	 */
	public ArrayList<Integer[]> getDoorLocations() {
		
		return doorLocations;
	}
	
	/**
	 * get the locations of tree
	 * 
	 * @return treeLocatiosn, locations of tree
	 */
	public ArrayList<Integer[]> getTreeLocations() {
		
		return treeLocations;
	}
	
	/**
	 * get the locations of axe
	 * 
	 * @return axeLocatiosn, locations of axe
	 */
	public Integer[] getAxeLocation() {
		
		return axeLocation;
	}

	/**
	 * 
	 * update the information of items
	 * 
	 * @param row, the row of the position
	 * @param col, the column of the position
	 * @param ch, what's there at the position
	 */
	private void uptateItem(int row, int col, char ch) {
		
		//if it's an axe
		  if(ch == 'a') {
			  
			  	this.axeLocation[0] = row;
			  	this.axeLocation[1] = col;
		  }
		  //if it's a key
		  else if(ch == 'k') {
			  
			  	this.keyLocation[0] = row;
				this.keyLocation[1] = col;
		  }
		  //if it's a treasure
		  else if(ch == '$') {
			  
			  	this.goldLocation[0] = row;
			  	this.goldLocation[1] = col;
		  }
		  //if it's a stone
		  else if(ch == 'o') {
	
		  		if(this.stoneLocations.isEmpty()) {
		  			
					Integer[] newStone = {row, col};
					this.stoneLocations.add(newStone);
				}
				else {
					//ignore if we already add it
					Integer size = this.stoneLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						
						Integer[] pos = this.stoneLocations.get(i);
						if((pos[0] == row) && (pos[1] == col)) {
							
							found = true;
						}
					}
					
					if(!found) {
						
						Integer[] newStone = {row, col};
						this.stoneLocations.add(newStone);
					}
				}
		  }
		  //it's a door
		  else if(ch == '-') {
			  
				if(this.doorLocations.isEmpty()) {
					
					Integer[] newDoor = {row, col};
					this.doorLocations.add(newDoor);
				}
				else {
					//ignore if we already add it
					Integer size = this.doorLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						
						Integer[] pos = this.doorLocations.get(i);
						if((pos[0] == row) && (pos[1] == col)) {
							
							found = true;
						}
					}
					
					if(!found) {
						
						Integer[] newDoor = {row, col};
						this.doorLocations.add(newDoor);
					}
				}
		  }
		  //if it's water
		  else if(ch == '~') 
		  {
				if(this.waterLocations.isEmpty()) {
					
					Integer[] newWater = {row, col};
					this.waterLocations.add(newWater);
				}
				else {
					//ignore if we already add it
					Integer size = this.waterLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						
						Integer[] pos = this.waterLocations.get(i);
						if((pos[0] == row) && (pos[1] == col)) {
							found = true;
						}

					}
					
					if(!found) {
						
						Integer[] newWater = {row, col};
						this.waterLocations.add(newWater);
					}
				}
		  }
		  //if it's a tree
		  else if(ch == 'T') {
			  
				if(this.treeLocations.isEmpty()) {
					
					Integer[] newTree = {row, col};
					this.treeLocations.add(newTree);
				}
				else {
					//ignore if we already add it
					Integer size = this.treeLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						
						Integer[] pos = this.treeLocations.get(i);
						if((pos[0] == row) && (pos[1] == col)) {
							
							found = true;
						}
						
						
					}
					
					if(!found) {
						
						Integer[] newTree = {row, col};
						this.treeLocations.add(newTree);
					}
				}
		  }
          
		
	}

	/**
	 * set the location of treasure
	 * @param goldLocation, position of treasure
	 */
	public void setGoldLocation(Integer[] goldLocation) {
		
		this.goldLocation = goldLocation;
	}
	
	/**
	 * get the location of treasure
	 * @param goldLocation, position of treasure
	 */
	public Integer[] getGoldLocation() {
		
		return goldLocation;
	}
	
	/**
	 * Finds all the possible(accessible) adjacent places, and return them as a list, does not 
	 * take water into account.
	 * @param row
	 * @param col
	 * @return adjacency, a list of all accessible adjacent places.
	 */
	public ArrayList<Integer[]> adjacency(Integer row, Integer col){

		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
		//check the four directions
		if(this.checkObstacle(row, col+1)) {
			Integer[] pos = {row, col+1};

				adjacency.add(pos);

		}
		if(this.checkObstacle(row+1, col)) {
			Integer[] pos = {row+1, col};

				adjacency.add(pos);

		}
		if(this.checkObstacle(row-1, col)) {
			Integer[] pos = {row-1, col};

				adjacency.add(pos);

		}
		if(this.checkObstacle(row, col-1)) {

			Integer[] pos = {row, col-1};

				adjacency.add(pos);

		}
		return adjacency;
		
		
	}
	
	/**
	 * Finds all the possible(accessible) adjacent places, and return them as a list, 
	 * takes water into account.
	 * @param row
	 * @param col
	 * @return adjacency, a list of all accessible adjacent places.
	 */
	public ArrayList<Integer[]> waterAdjacency(Integer row, Integer col){
		
		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
		//check the four directions
		if(this.checkWaterObstacle(row, col+1)) {
			
			Integer[] pos = {row, col+1};
			adjacency.add(pos);

		}
		if(this.checkWaterObstacle(row+1, col)) {
			
			Integer[] pos = {row+1, col};
			adjacency.add(pos);

		}
		if(this.checkWaterObstacle(row-1, col)) {
			
			Integer[] pos = {row-1, col};
			adjacency.add(pos);

		}
		if(this.checkWaterObstacle(row, col-1)) {

			Integer[] pos = {row, col-1};
			adjacency.add(pos);

		}
		
		return adjacency;
	}
	
	
	
	
	/**
	 * check if treasure is around you
	 * @param row
	 * @param col
	 * @return if there is a treasure next to you
	 */
	public boolean checkTreasure(Integer row, Integer col) {
		
		if(this.visitedview[row-1][col] == '$') {
			
			return true;
		}
		else if(this.visitedview[row][col-1] == '$') {
			
			return true;
		}
		else if(this.visitedview[row+1][col] == '$') {
			
			return true;
		}
		else if(this.visitedview[row][col+1] == '$') {
			
			return true;
		}
		else{
			
			return false;
		}
	}
	
	/**
	 * check if there is an obstacle next to you and if you can reach that,
	 * does not take water into account
	 * 
	 * @param row
	 * @param col
	 * @return if you can reach that
	 */
	public boolean checkObstacle(Integer row, Integer col) {
		
		if(this.visitedview[row][col] == 'T' && have_axe) {
			
			return true;
		}
		else if(this.visitedview[row][col] == '-' && have_key) {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'o') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'O') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'k') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'a') {
			
			return true;
		}
		else if(this.visitedview[row][col] == '$') {
			
			return true;
		}
		else if(this.visitedview[row][col] == ' ') {
			
			return true;
		}
		else if(this.visitedview[row][col] == '*' || this.visitedview[row][col] == '&') {
			
			return false;
		}
		
		return false;
	}
	
	/**
	 * check if there is an obstacle next to you and if you can reach that,
	 * takes water into account
	 * 
	 * @param row
	 * @param col
	 * @return if you can reach that
	 */
	public boolean checkWaterObstacle(Integer row, Integer col) {

		if(this.visitedview[row][col] == 'T' && have_axe) {
			
			return true;
		}
		else if(this.visitedview[row][col] == '-' && have_key) {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'o') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'O') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'k') {
			
			return true;
		}
		else if(this.visitedview[row][col] == 'a') {
			
			return true;
		}
		else if(this.visitedview[row][col] == '$') {
			
			return true;
		}
		else if(this.visitedview[row][col] == ' ') {
			
			return true;
		}
		else if(this.visitedview[row][col] == '~' && this.have_stone) {
			
			return true;
		}
		else if(this.visitedview[row][col] == '~' && ((this.have_raft) || (this.on_raft))) {
			
			return true;
		}
		else if(this.visitedview[row][col] == '*' || this.visitedview[row][col] == '&') {
			
			return false;
		}
		
		return false;
	}
	
	/**
	 * print the visitedvew map
	 */
	public void print_visited_view() {

		for(int i = 65; i < 100; i++) {
			
			for(int j = 60;j < 110; j++) {
				
				if(i == this.pos_x && j == this.pos_y) {
					System.out.print(this.getDirectionChar(this.currDirection));
				
				}
			
				else{
					System.out.print(this.visitedview[i][j]);
				}
				
			}
	
			System.out.println();
		}
	}

	/**
	 * rotate the view, so we can adkust the information and update that in 
	 * our visitedview map
	 * @param view
	 * @return newView, rotated view
	 */
	public char[][] rotate(char[][] view) {
		
		char[][] newView = new char[5][5];
		int row = 0,column = 0;
		
		for( int i = 0; i <= 4; i++ ) {
	         
			for( int j = 0; j <= 4; j++ ) {
	           
				switch(this.currDirection) {
	            case NORTH:
	            	row = i;
	            	column = j;
	            	break;
	            case SOUTH:
	            	row = 4-i;
	            	column = 4-j;
	            	break;
	            case WEST: 
	            	row = j;
	            	column = 4-i;
	            	break;
	            case EAST: 
	            	row = 4-j;
	            	column = i;
	            	break;
	            }
	            newView[i][j] = view[row][column];
	         }
	      }

		return newView;
	}

	/**
	 * update the status of this object after taking a specific action
	 * @param action
	 */
	public void perform(char action) {

		//change the direction
		if(( action == 'L' )||( action == 'l' )) {	
		   
			this.currDirection = ( this.currDirection + 1 ) % 4;
		    this.visitedview[this.pos_x][this.pos_y] = this.getDirectionChar(currDirection);
		    
	    }
	    else if(( action == 'R' )||( action == 'r' )) {
	    	
	    	this.currDirection = ( this.currDirection + 3 ) % 4;
	    	this.visitedview[this.pos_x][this.pos_y] = this.getDirectionChar(currDirection);
	    }
		//update position
	    else if(( action == 'F' )||( action == 'f' )){
	  
	    	int row = this.pos_x;
			int col = this.pos_y;
			if(this.canMoveForward()) {
				
				switch (this.currDirection) {
				case NORTH:
					row--;
					break;
					
				case WEST:
					col--;
					break;
					
				case SOUTH:
					row++;
					break;
				case EAST:
					col++;
					break;
				
				
				}
				//update the stone information and the raft status
				if(this.visitedview[row][col] == 'o') {

					this.have_stone = true;
					this.numStone++;	
					int size = this.stoneLocations.size();
					for(int i = 0; i < size; i++) {
						
						Integer[] pos = this.stoneLocations.get(i);
						if((pos[0] == row) && (pos[1] == col)){
							
							this.stoneLocations.remove(i);
							size--;
							
						}
					}
					if(this.on_raft) {
						
						this.oneraftTime = 0;
						this.on_raft = false;
					}
				
				}
				//update the axe information and the raft status
				else if(this.visitedview[row][col] == 'a') {
					
					this.have_axe = true;
					if(this.on_raft) {
						
						this.oneraftTime = 0;
						this.on_raft = false;
					}
				}
				//update the key information and the raft status
				else if(this.visitedview[row][col] == 'k') {
					
					this.have_key = true;
					if(this.on_raft) {
						
						this.oneraftTime = 0;
						this.on_raft = false;
					}
				}
				//update the gold information and the raft status
				else if(this.visitedview[row][col] == '$') {
					
					this.have_treasure = true;
					if(this.on_raft) {
						
						this.oneraftTime = 0;
						this.on_raft = false;
					}
				}
				//update  the stone status
				else if(this.visitedview[row][col] == '~' && this.have_stone) {
					
					this.numStone--;
					if(this.numStone == 0) {
						
						this.have_stone = false;
					}

				}
				//update the raft status
				else if(this.visitedview[row][col] == '~' && this.have_raft) {
					
					this.on_raft = true;
					this.oneraftTime++;
					this.have_raft = false;
				}
				//update the raft status
				else if(this.visitedview[row][col] == ' ' && this.on_raft) {
					
					this.on_raft = false;
					this.oneraftTime = 0;
				}
				//update the raft status
				else if(this.visitedview[row][col] == 'O' && this.on_raft) {
					
					this.on_raft = false;
					this.oneraftTime = 0;
				}
			}
			
			this.pos_x = row;
			this.pos_y = col;
			this.visitedview[pos_x][pos_y] = this.getDirectionChar(currDirection);
			
	    }
		//if we cut a tree
	    else if((action == 'C') || (action == 'c')) {
	    	
	    	int row = this.pos_x;
	    	int col = this.pos_y;
	    	switch(this.currDirection) {
	    	case NORTH:
				row--;
				if(this.visitedview[row][col] == 'T' && have_axe) {
					
					this.visitedview[row][col] = ' ';
					have_raft = true;
				}
				break;
			case EAST:
				col++;
				if(this.visitedview[row][col] == 'T' && have_axe) {
					
					this.visitedview[row][col] = ' ';
					have_raft = true;
				}
				break;
			case SOUTH:
				row++;
				if(this.visitedview[row][col] == 'T' && have_axe) {
					
					this.visitedview[row][col] = ' ';
					have_raft = true;
				}
				break;
			case WEST:
				col--;
				if(this.visitedview[row][col] == 'T' && have_axe) {
					
					this.visitedview[row][col] = ' ';
					have_raft = true;
				}
				break;
	    	}
	    	
	    	
	    	int size = this.treeLocations.size();
			for(int i = 0; i < size; i++) {
				
				Integer[] pos = this.treeLocations.get(i);
				if((pos[0] == row) && (pos[1] == col)){
					
					this.treeLocations.remove(i);
					size--;
				}
			}
	    	
	    }
		//if we unlock a door
	    else if((action == 'U') || (action == 'u')) {
	    	
	    	int row = this.pos_x;
	    	int col = this.pos_y;
	    	switch(this.currDirection) {
	    	case NORTH:
				row--;
				if(this.visitedview[row][col] == '-' && have_key) {
					
					this.visitedview[row][col] = ' ';
				}
				break;
			case EAST:
				col++;
				if(this.visitedview[row][col] == '-' && have_key) {
					
					this.visitedview[row][col] = ' ';
				}
				break;
			case SOUTH:
				row++;
				if(this.visitedview[row][col] == '-' && have_key){
					
					this.visitedview[row][col] = ' ';
				}
				break;
			case WEST:
				col--;
				if(this.visitedview[row][col] == '-' && have_key) {
					
					this.visitedview[row][col] = ' ';
				}
				break;
	    	
	    	}
	    	int size = this.doorLocations.size();
	    	for (int i = 0; i < size; i++) {
	    		
	    		Integer[] pos = this.doorLocations.get(i);
	    		if((pos[0] == row) && (pos[1] == col)) {
	    			
	    			this.doorLocations.remove(i);
	    			size--;
	    		}
	    	}
	    }
	}
	
	/**
	 * check if we can move forward 
	 * @return if we can move forward 
	 */
	public boolean canMoveForward() {
		
		int row = this.pos_x;
		int col = this.pos_y;
		char ch = ' ';
		
		boolean canMove = true;
		switch (this.currDirection) {
			case NORTH:
				
				ch = this.visitedview[row-1][col];
				
				switch( ch ) { // can't move into an obstacle
			        case '*': case 'T': case '-':
			        canMove = false;
			        break;
		        }
				break;
			case EAST:
				
				ch = this.visitedview[row][col+1];
				
				switch( ch ) { // can't move into an obstacle
			        case '*': case 'T': case '-':
		        	canMove = false;
			        break;
		        }
				break;
			case SOUTH:
				ch = this.visitedview[row+1][col];
				
				switch( ch ) { // can't move into an obstacle
			        case '*': case 'T': case '-':
		        	canMove = false;
			        break;
		        }
				break;
			case WEST:
				
				ch = this.visitedview[row][col-1];
				
				switch( ch ) { // can't move into an obstacle
			        case '*': case 'T': case '-':
			        canMove = false;
			        break;
		        }
				break;
			default:
				canMove = false;
		        break;
		}
		return canMove;
		//return true;
		
		
	}
	/**
	 * get the character of the current direction
	 * @param currDirection
	 * @return
	 */
	public char getDirectionChar(int currDirection) {
		
	   	switch (currDirection) {
    	case NORTH:
    		return '^';
    	case EAST:
    		return '>';
    	case SOUTH:
    		return 'v';
    	case WEST:
    		return '<';
    	default:
    		return ' ';
    	}
		
	}
}







