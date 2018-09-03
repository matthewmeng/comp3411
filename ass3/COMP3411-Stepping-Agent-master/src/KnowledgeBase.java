/**
 * The KnowledgeBase of the agent
 */
public class KnowledgeBase {
	private static final int AGENT_POS_ROW = 2;
	private static final int AGENT_POS_COL = 2;
	
	private Environment environment;
	private ActionKnowledge actionKnowledge;
	private Explorer explorer;
	private PathFinderThroughWater pathFinderThoughWater;
	
	/**
	 * Creates a KnowledgeBase for the agent
	 */
	public KnowledgeBase() {
		environment = new Environment();
		actionKnowledge = new ActionKnowledge(environment);
		pathFinderThoughWater = new PathFinderThroughWater(environment);
		explorer = new Explorer(environment, actionKnowledge, new HeuristicManhattanDistance(), pathFinderThoughWater);
	}
	
	/**
	 * Agents tells the KnowledgeBse about its new view
	 * @param view
	 */
	public void tell(char view[][]) {
		char[][] rotatedView = rotateView(view);
		
		// update map
		environment.updateMap(rotatedView);
		
		if (environment.getToolKnowledge().hasToolSetChanged()) {
			// new tool acquired, notify pathFinderThroughWater to update map
			pathFinderThoughWater.onNumberToolsChanged();
		}
	}
	
	/**
	 * Agent asks the KnowledgeBase what action it should take
	 * @return an action to take
	 */
	public Action ask() {
		Action nextAction = explorer.getAction(); 
		environment.updateEnvioronment(nextAction);
		
		if (environment.getToolKnowledge().hasToolSetChanged()) {
			// stone placed
			pathFinderThoughWater.onNumberToolsChanged();
		}
		return nextAction;
	}
	
	/**
	 * Performs the given action
	 * @param action to perform
	 */
	public void performAction(Action action) {
		environment.updateCurrentDirection(action);
		environment.updatePosition(action);
	}
	
	/**
	 * Returns a rotated view of the given view
	 */
	private char[][] rotateView(char view[][]) {
		char[][] newView = new char[5][5];
		int row = 0,column = 0;
		
		for( int i = -2; i <= 2; i++ ) {
	         for( int j = -2; j <= 2; j++ ) {
	            switch(environment.getCurrentDirection()) {
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
	
	/**
	 * Prints the given view
	 * @param view
	 */
	private void printView (char[][] view) {
		System.out.println("+-----+");
		for (int i = 0; i < view.length; i++) {
			System.out.print("|");
			for (int j = 0; j < view[i].length; j++) {
				if (i == AGENT_POS_ROW && j == AGENT_POS_COL) {
					System.out.print(Direction.getAgentSymbol(environment.getCurrentDirection()));	
				} else {
					System.out.print(view[i][j]);	
				}
			}
			System.out.println("|");
		}
		System.out.println("+-----+");
	}
}
