import java.util.ArrayList;

public class Strategy {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	public boolean flag = false;
	
	public Explore explore;
	
	private ManhattanDIstance heuristic;
	private ArrayList<Character> actionList;
	public ArrayList<Character> totalactionList;
	public VisitedView visitedview;
	public PathFinder pathfinder;
	private ArrayList<Character> gobacklist = new ArrayList<Character>();
	private boolean gobackflag = false;
	public int literator = 0;
	public ArrayList<Integer[]> pointPosition = new ArrayList<Integer[]>();
	public Integer[] StartPoint = {79,79};
	public Strategy() {
		this.explore = new Explore(this.visitedview);
		this.actionList = new ArrayList<Character>();
		this.visitedview= new VisitedView();
		this.totalactionList = new ArrayList<Character>();
	}
	
	public void updateView(char[][] view) {
		this.visitedview.updateMap(view);
	}
	
	public ArrayList<Character> convertCharList(){
		ArrayList<Character> newCharList = new ArrayList<Character>();
		newCharList.add('r');
		newCharList.add('r');
		int i = (this.totalactionList.size()-1);
		while(i>=0) {
			if(this.totalactionList.get(i) == 'F') {
				newCharList.add('f');
			}else if(this.totalactionList.get(i) == 'R') {
				newCharList.add('l');
			}else if(this.totalactionList.get(i) == 'L') {
				newCharList.add('r');
			}
			i--;
		}
		return newCharList;
	}
	
	
	public char getAction() {
		this.explore.visitedview = this.visitedview;
		char action = ' ';
		if(this.gobackflag == true){
			System.out.println("HHHHERRERE");
			System.out.println(this.literator+"---->"+this.gobacklist.size());
			if(this.literator < this.gobacklist.size()) {
				this.literator++;
//				System.out.println(this.literator+"ooooooooooooooooooooooooooo");
				return this.gobacklist.get(literator);
			}
		}else {
			if(this.explore.visitedview.getGoldLocation()[0]!= 0 && this.explore.visitedview.have_treasure == false) {
				
				Integer[] goal = this.explore.visitedview.getGoldLocation();
				Integer[] start = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
				if(pathlist != null) {
		
					Integer[] pos = pathlist.get((pathlist.size()-1));
					int newx = pos[0];
					int newy = pos[1];
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);

					this.totalactionList.add(getmoves.get(0));
					return getmoves.get(0);
					
				}else {
				
					this.explore.visitedview = this.visitedview;
	
				}

			}else if(this.explore.visitedview.have_treasure) {
				System.out.println("GO Back");

					ArrayList<Character> getmoves = this.convertCharList();
					this.gobackflag = true;
					this.gobacklist = getmoves;

					for(Character c : getmoves) {
						System.out.println(c);
					}
				
				
					return getmoves.get(0);
				//}
			}
		
		
			
			if(actionList.isEmpty()) {
				
				ArrayList<Character> temp = new ArrayList<Character>();
				if(!this.explore.tempExlpored) {
					temp = this.explore.getMoves();
					if(temp.size() >= 1) {
						for(char move : temp) {
						
						this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
					}
					else {
						this.explore.tempExlpored = true;
					}
				}
				
				
				if(this.explore.tempExlpored) {
					if(this.flag == false) {
						this.actionList.add('F');
						this.actionList.add('F');
						//this.actionList.add('F');
						flag = true;
					}
					
					temp = this.explore.getWaterMoves();
					if(temp.size() >= 1) {
						for(char move : temp) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
					}
				}
				
				System.out.println("has raft: " + this.visitedview.have_raft);
				System.out.println("on raft: " + this.visitedview.on_raft);
			}
			else {
				action = this.actionList.get(0);
				this.actionList.remove(0);
				
			}
			System.out.println("temp explored: " + explore.tempExlpored);
			if(action == 'F') {
			
				int currDirection = this.visitedview.getCurrDirection();
				int pos_x = this.visitedview.getPos_x();
				int pos_y = this.visitedview.getPos_y();
				Integer[] pos = {pos_x, pos_y};
				
				
				this.explore.prevStates.add(pos);
				if(currDirection == NORTH) {
					pos_x --;
					int sum = pos_x*100 + pos_y;
					this.explore.postionList.put(sum, 1);
				}
				else if(currDirection == SOUTH) {
					pos_x ++;
					int sum = pos_x*100 + pos_y;
					this.explore.postionList.put(sum, 1);
				}
				else if(currDirection == EAST) {
					pos_y ++;
					int sum = pos_x*100 + pos_y;
					this.explore.postionList.put(sum, 1);
				}
				else if(currDirection == WEST) {
					pos_y --;
					int sum = pos_x*100 + pos_y;
					this.explore.postionList.put(sum, 1);
				}
				if(this.explore.goBack == false) {
					State state = new State();
					state.setPrevState(this.explore.state);
					state.setPos(pos_x, pos_y);
					this.explore.state = state;
				}
				else {
					State state = this.explore.state.getPrevState();
					this.explore.state = state;
					this.explore.goBack = false;
				}
				
			}
			this.totalactionList.add(action);
			return action;
		}
		return action;
	}
	
	
}
