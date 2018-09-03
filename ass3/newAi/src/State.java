import java.util.ArrayList;
import java.util.LinkedList;

import javax.management.Query;

import java.io.*;
public class State {
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	
	private String info = " ";
	private char[][] Theview = new char[160][160];
	VisitedView visitedview = new VisitedView();
	Explore explore = new Explore(visitedview);
	State prestate;
	LinkedList<Character>  possibleList = new LinkedList<Character> ();
	char action;
	public char getAction() {
		return action;
	}
	public void setAction(char action) {
		this.action = action;
	}
	State getPrestate() {
		return prestate;
	}
	public void setPrestate(State prestate) {
		this.prestate = prestate;
	}
	public void setPossibleList(LinkedList<Character>  possibleList) {
		this.possibleList = possibleList;
	}
	
	public LinkedList<Character> getPossibleList() {
		return this.possibleList;
	}
	public State() {
		
	}
	public State(VisitedView visitedview,State prestate,Explore explore) {
		this.explore = explore;
		this.prestate = prestate;
		this.visitedview = visitedview;
		StringBuilder sb = new StringBuilder(String.valueOf(this.info));
		this.Theview = visitedview.getview();
		for(int i = 0 ; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
//				if(this.Theview[i][j] != '&') {
					sb.append(Theview[i][j]);
//				}
			}
		}
		this.info = sb.toString();
	}
	
	
	public String getInfo() {
		return info;
	}
	public char[][] getTheview() {
		return Theview;
	}
	
//	public LinkedList<Character> allpossibleMoves(){
//		LinkedList<Character>  possibleList = new LinkedList<Character> ();
//		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
//		int pos_x = this.visitedview.getPos_x();
//		int pos_y = this.visitedview.getPos_y();
//		adjacency = this.visitedview.adjacency(pos_x, pos_y);
//		
//		System.out.println(this.visitedview.getPos_x()+"---current-----"+this.visitedview.getPos_y());
//		for(Integer[] test: adjacency) {
//			System.out.println("the test is "+test[0]+"->"+test[1]);
//		}
//				
//		int i = 0;
//		while(!adjacency.isEmpty()) {
//			
//			Integer[] pos = adjacency.remove(i);
//			//i++;
//			int newx = pos[0];
//			int newy = pos[1];
//			int choose = 0;
//			choose = checkaction(newx,newy);
//			Nextaction next = new Nextaction(choose);
//			possibleList.add(next.getAction());
//	}
//		
//		return possibleList;
//		
//		
//	}
	public LinkedList<Character> get_possiblelist(){
	  	ArrayList<Integer[]> adjacency = this.getAdjacency();
		int size = adjacency.size();
		for(int i = 0; i < size; i++) {
			Integer[] pos = adjacency.get(i);
			int sum = pos[0]*100 + pos[1];
			if(this.explore.postionList.containsKey(sum)) {
				continue;
			}
			int newx = pos[0];
			int newy = pos[1];
			int choose = 0;
			choose = this.checkaction(newx, newy);
			Nextaction next = new Nextaction(choose);
			//System.out.println(next.getAction());
			this.possibleList.add(next.getAction());
		}
		return this.possibleList;
  }
	
	public ArrayList<Integer[]> getAdjacency(){
		LinkedList<Character>  possibleList = new LinkedList<Character> ();
		ArrayList<Integer[]> adjacency = new ArrayList<Integer[]>();
		int pos_x = this.visitedview.getPos_x();
		int pos_y = this.visitedview.getPos_y();
		adjacency = this.visitedview.adjacency(pos_x, pos_y);
		return adjacency;	
	}
	
	
	
	
	
	/**
	 * @param newx
	 * @param newy
	 * @return
	 * 1 -> f
	 * 2 -> l
	 * 3 -> r
	 * 4 -> l
	 */
	public int checkaction(int newx, int newy) {
		if(this.visitedview.getCurrDirection() == EAST) {
			if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()+1)) {
				return 1;
			}else if(newx == (this.visitedview.getPos_x()+1) && newy == this.visitedview.getPos_y()) {
				return 3;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()-1)) {
				return 4;
			}else if(newx == (this.visitedview.getPos_x()-1) && newy == this.visitedview.getPos_y()){
				return 2;
			}
		}else if(this.visitedview.getCurrDirection() == NORTH) {
			
			if(newx == (this.visitedview.getPos_x()-1) && newy == this.visitedview.getPos_y()){
				return 1;
			}else if(newx == (this.visitedview.getPos_x()+1) && newy == this.visitedview.getPos_y()) {
				return 4;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()+1)) {
				return 3;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()-1)) {
				return 2;
			}
		}else if(this.visitedview.getCurrDirection() == SOUTH) {
			
			if(newx == (this.visitedview.getPos_x()+1) && newy == this.visitedview.getPos_y()) {
				return 1;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()+1)) {
				return 2;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()-1)) {
				return 3;
			
			}else if(newx == (this.visitedview.getPos_x()-1) && newy == this.visitedview.getPos_y()){
				return 4;
			}
		}else if(this.visitedview.getCurrDirection() == WEST) {
			if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()-1)) {
				return 1;
			
			}else if(newx == (this.visitedview.getPos_x()+1) && newy == this.visitedview.getPos_y()) {
				return 2;
			}else if(newx == this.visitedview.getPos_x() && newy == (this.visitedview.getPos_y()+1)) {
				return 4;
			}else if(newx == (this.visitedview.getPos_x()-1) && newy == this.visitedview.getPos_y()){
				return 3;
			}
		}
		return 0;
	
	}
}

