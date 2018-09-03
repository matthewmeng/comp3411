import java.util.ArrayList;
import java.util.LinkedList;

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
	
	
	int MAX = 80;
	private int pos_x = 0;
	public int getPos_x() {
		return pos_x;
	}

	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	public int getPos_y() {
		return pos_y;
	}

	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	private int pos_y = 0;
	boolean startflag = false;
	private int currDirection;
	public int getCurrDirection() {
		return currDirection;
	}

	public void setCurrDirection(int currDirection) {
		this.currDirection = currDirection;
	}
	private char[][] view;
	
	public boolean have_axe     = false;
	public boolean have_key     = false;
	public boolean have_treasure= false;
	public boolean have_raft    = false;
	public boolean have_stone   = false;
	public boolean on_raft      = false;
	public boolean off_map      = false;
	public int numStone = 0;
	 
	private ArrayList<Integer[]> stoneLocations = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> doorLocations = new ArrayList<Integer[]>();
	private Integer[] axeLocation = {0, 0};
	private Integer[] keyLocation = {0, 0};
	private Integer[] goldLocation = {0, 0};

	
	
	
	
	public char[][] getview() {
		return view;
	}

	public void setview(char[][] view) {
		this.view = view;
	}

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
	
	public void initial_visitedview(char[][] view) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				this.visitedview[(this.pos_x-2+i)][(this.pos_y-2+j)] = view[i][j];
			}
		}
		this.startflag = true;
	}
	
	
	
	public void updateMap(char[][] view) {
		
		char[][] rotatedview = this.rotate(view);
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				char ch = rotatedview[i + 2][j + 2];
				int row = pos_x + i;
				int col = pos_y + j;
				this.visitedview[row][col] = ch;
				uptateItem(row, col, ch);
			}
		}
		System.out.println("___________");
		
		this.print_visited_view();
		
		
		ArrayList<Integer[]> adjacency = this.adjacency(pos_x, pos_y);
		
//		for(int i = 0; i < adjacency.size(); i++) {
//			Integer[] pos = adjacency.get(i);
//			System.out.println("A: " + this.visitedview[pos[0]][pos[1]]);
//		}
		/*
		System.out.println("key: " + this.keyLocation[0] + " " + this.keyLocation[1]);
		System.out.println("axe: " + this.axeLocation[0] + " " + this.axeLocation[1]);
		
		for(Integer i = 0; i < this.stoneLocations.size(); i++) {
    		
			System.out.println("stone: ");
			Integer[] pos = this.stoneLocations.get(i);
			System.out.println(pos[0] + "| |" + pos[1]);
    		
    	}
		
		System.out.println(this.stoneLocations.size());
		System.out.println("___________");
		*/
		
		
	}


	private void uptateItem(int row, int col, char ch) {
		  if(ch == 'a') {
			  	this.axeLocation[0] = row;
			  	this.axeLocation[1] = col;
		  }
		  else if(ch == 'k') {
			  	this.keyLocation[0] = row;
				this.keyLocation[1] = col;
		  }
		  else if(ch == '$') {
			  	this.goldLocation[0] = row;
			  	this.goldLocation[1] = col;
		  }
		  else if(ch == 'o') {
			  System.out.println("stoneeefefwefee: ");
				if(this.stoneLocations.isEmpty()) {
					Integer[] newStone = {row, col};
					System.out.println("stoneeee: " + newStone[0] + " " + newStone[1]);
					this.stoneLocations.add(newStone);
				}
				else {
					Integer size = this.stoneLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						Integer[] pos = this.stoneLocations.get(i);
						System.out.println("stoneeeee: " + pos[0] + " " + pos[1]);
						if((pos[0] == row) && (pos[1] == col)) {
							found = true;
						}
						
						
					}
					
					if(!found) {
						Integer[] newStone = {row, col};
						this.stoneLocations.add(newStone);
						System.out.println("stone: " + newStone[0] + " " + newStone[1]);
					}
				}
		  }
		  else if(ch == '-') {
				if(this.doorLocations.isEmpty()) {
					Integer[] newDoor = {row, col};
					System.out.println("stoneeee: " + newDoor[0] + " " + newDoor[1]);
					this.doorLocations.add(newDoor);
				}
				else {
					Integer size = this.doorLocations.size();
					boolean found = false;
					for(Integer i = 0; i < size; i++) {
						Integer[] pos = this.doorLocations.get(i);
						System.out.println("stoneeeee: " + pos[0] + " " + pos[1]);
						if((pos[0] == row) && (pos[1] == col)) {
							found = true;
						}
						
						
					}
					
					if(!found) {
						Integer[] newDoor = {row, col};
						this.doorLocations.add(newDoor);
						System.out.println("stone: " + newDoor[0] + " " + newDoor[1]);
					}
				}
		  }
          
		
	}

	public void setGoldLocation(Integer[] goldLocation) {
		this.goldLocation = goldLocation;
	}
	
	public Integer[] getGoldLocation() {
		return goldLocation;
	}
	public ArrayList<Integer[]> adjacency(Integer row, Integer col){
		boolean checkflag = true;
		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
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
			System.out.println(this.visitedview[row][col-1]);
			Integer[] pos = {row, col-1};

				adjacency.add(pos);

		}
		return adjacency;
		
		
	}
	
	public ArrayList<Integer[]> waterAdjacency(Integer row, Integer col){
		boolean checkflag = true;
		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
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
			System.out.println(this.visitedview[row][col-1]);
			Integer[] pos = {row, col-1};

				adjacency.add(pos);

		}
		return adjacency;
		
		
	}
	
	
	
	
	public boolean checkTreasure(Integer row, Integer col) {
		if(this.visitedview[row-1][col] == '$') {
			return true;
		}else if(this.visitedview[row][col-1] == '$') {
			return true;
		}else if(this.visitedview[row+1][col] == '$') {
			return true;
		}else if(this.visitedview[row][col+1] == '$') {
			return true;
		}else{
			return false;
		}
	}
	
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
public boolean checkWaterObstacle(Integer row, Integer col) {
		System.out.println("3");
		if(this.visitedview[row][col] == 'T' && have_axe) {
			return true;
		}
		else if(this.visitedview[row][col] == '-' && have_key) {
			return true;
		}
		else if(this.visitedview[row][col] == 'o') {
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
	
	public void print_visited_view() {
		//boolean flag = false;
		for(int i = 65; i < 100; i++) {
			for(int j = 60;j < 110; j++) {
				
				if(i == this.pos_x && j == this.pos_y) {
					System.out.print(this.getDirectionChar(this.currDirection));
				
				}
			
				else{
					System.out.print(this.visitedview[i][j]);
					//flag = true;
				}
				
			}
	
			System.out.println();
		}
	}

	public char[][] rotate(char[][] view) {
		char[][] newView = new char[5][5];
		int row = 0,column = 0;
		
		for( int i = -2; i <= 2; i++ ) {
	         for( int j = -2; j <= 2; j++ ) {
	            switch(this.currDirection) {
	            case NORTH:
	            	row = 2+i;
	            	column = 2+j;
	            	break;
	            case SOUTH:
	            	row = 2-i;
	            	column = 2-j;
	            	break;
	            case WEST: 
	            	row = 2+j;
	            	column = 2-i;
	            	break;
	            case EAST: 
	            	row = 2-j;
	            	column = 2+i;
	            	break;
	            }
	            newView[2+i][2+j] = view[row][column];
	         }
	      }
		//printView(newView);
		return newView;
	}

	public void perform(char action) {
		//if(this.canmove == true) {
		if(( action == 'L' )||( action == 'l' )) {	
		    this.currDirection = ( this.currDirection + 1 ) % 4;
		    this.visitedview[this.pos_x][this.pos_y] = this.getDirectionChar(currDirection);
		    
		    System.out.println("x:"+this.pos_x+"->y:"+this.pos_y);
	    }
	    else if(( action == 'R' )||( action == 'r' )) {
	    	this.currDirection = ( this.currDirection + 3 ) % 4;
	    	this.visitedview[this.pos_x][this.pos_y] = this.getDirectionChar(currDirection);
	    }
	    else if(( action == 'F' )||( action == 'f' )){
	  
	    	int row = this.pos_x;
			int col = this.pos_y;
			if(this.canMoveForward()) {
				switch (this.currDirection) {
				case NORTH:
					row--;
					break;
				case EAST:
					col++;
					break;
				case SOUTH:
					row++;
					break;
				case WEST:
					col--;
					break;
				}
				if(this.visitedview[row][col] == 'o') {
					this.have_stone = true;
					this.numStone++;	
				}
				else if(this.visitedview[row][col] == 'a') {
					this.have_axe = true;
				}
				else if(this.visitedview[row][col] == 'k') {
					this.have_key = true;
				}
				else if(this.visitedview[row][col] == '$') {
					this.have_treasure = true;
				}
				else if(this.visitedview[row][col] == '~' && this.have_raft) {
					this.on_raft = true;
					this.have_raft = false;
				}
				else if(this.visitedview[row][col] == ' ' && this.on_raft) {
					this.on_raft = false;
				}
				System.out.println("pppppppppppppp");
				System.out.println(this.getDirectionChar(currDirection));
				//this.visitedview[pos_x][pos_y] = ' ';
			}
			
			this.pos_x = row;
			this.pos_y = col;
			this.visitedview[pos_x][pos_y] = this.getDirectionChar(currDirection);
			
	    }
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
	    	
	    }
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


