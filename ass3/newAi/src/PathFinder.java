import java.util.Stack;

public class PathFinder {
	private VisitedView visitedview;
	private StateHashMap visitedList;
	private Stack actions;
	
	public PathFinder(VisitedView visitedview) {
		this.visitedview = visitedview;
		this.actions = new Stack();
	}
	/*
	public char getAction() {
		char nextAction = getNextAction();
		if(nextAction != null)
	}
	*/
}
