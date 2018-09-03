import java.util.Collection;

/**
 * All possible action that the agent can make
 *
 */
public enum Action {
	NONE, FORWARD, LEFT, RIGHT, CHOP_TREE, UNLOCK_DOOR;
	
	public static Action toAction(char actionChar) {
		switch (actionChar) {
		case 'L': case 'l':
			return Action.LEFT;
		case 'R': case 'r':
			return Action.RIGHT;
		case 'F': case 'f':
			return Action.FORWARD;
		case 'C': case 'c':
			return Action.CHOP_TREE;
		case 'U': case 'u':
			return Action.UNLOCK_DOOR;
		default:
			return Action.NONE;
		}
	}
	
	/**
	 * Translate an action to char
	 * @param action
	 * @return
	 */
	public static char toChar(Action action) {
		switch (action) {
		case LEFT:
			return 'L';
		case RIGHT:
			return 'R';
		case FORWARD:
			return 'F';
		case CHOP_TREE:
			return 'C';
		case UNLOCK_DOOR:
			return 'U';
		default:
			return 0;
		}	
	}
	
	/**
	 * Prints the given action list
	 * @param title
	 * @param actionList
	 */
	public static void printList(String title, Collection<Action> actionList) {
		System.out.print(title + " ");
		for (Action action : actionList) {
			System.out.print(action);	
		}
		System.out.println();
	}
}
