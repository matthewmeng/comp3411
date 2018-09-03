import java.util.LinkedList;
import java.util.Stack;

public class DFS {
	private Stack stack = new Stack<State>();
	StateHashMap statehashmap = new StateHashMap();
	private State s = new State();
	private int cost = 0;
	public DFS() {
		
	}
	public DFS(State s) {
		stack.push(s);
		statehashmap.add(s);
		cost++;
		//findgoal();
	}
	/*
	private void findgoal() {
		while(!stack.isEmpty()) {
			State current = (State) stack.peek();
			if(isgoal(current)) {
				System.out.println("Find!!!");
				break;
			}
//			System.out.println("1");
//			current.visitedview.print_visited_view();
		
			LinkedList<State> toMove = current.allpossibleMoves();
			for(State newS : toMove) {
				System.out.println("2");
				if(statehashmap.add(newS) == true) {
					stack.add(newS);
					newS.visitedview.print_visited_view();
				}
			}
		}
	}
	
	private boolean isgoal(State s) {
		return s.visitedview.checkTreasure(s.visitedview.getPos_x(), s.visitedview.getPos_y());
	}
	*/
}
