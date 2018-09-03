import java.util.ArrayList;

public class Strategy {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	public Explore explore;
	
	private ArrayList<Character> actionList;
	public VisitedView visitedview;
	public Strategy() {
		this.explore = new Explore(this.visitedview);
		this.actionList = new ArrayList<Character>();
		this.visitedview= new VisitedView();
		
	}
	
	public void updateView(char[][] view) {
		this.visitedview.updateMap(view);
	}
	
	public char getAction() {
		this.explore.visitedview = this.visitedview;
		char action = ' ';
		if(actionList.isEmpty()) {
			
			ArrayList<Character> temp = new ArrayList<Character>();
			temp = this.explore.getMoves();
			for(char move : temp) {
				
				this.actionList.add(move);
				
			}
			action = this.actionList.get(0);
			this.actionList.remove(0);
			
			
		}
		else {
			action = this.actionList.get(0);
			this.actionList.remove(0);
			
		}
		
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
		}
		return action;
	}
	
}
