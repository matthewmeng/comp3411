
import java.util.HashMap;
import java.util.Map;

/**
 * Direction enum of the agent
 */
public enum Direction {
	NORTH(0), EAST(1), SOUTH(2), WEST(3);
    public final int value;
    private static Map<Integer, Direction> directions = new HashMap<Integer, Direction>();

    static {
        for (Direction direction : Direction.values()) {
            directions.put(direction.value, direction);
        }
    }
    
    private Direction (int value) {
    	this.value = value;
    } 
    
    /**
     * Gets the direction for the given value
     * @param value
     * @return
     */
    public static Direction getDirection (int value) {
    	//System.out.println("value " + value);
    	return directions.get(value);
    }    
    
    /**
     * Returns the symbol representing the agent for the given direction
     * @param direction
     * @return
     */
    public static String getAgentSymbol (Direction direction) {
    	switch (direction) {
    	case NORTH:
    		return "^";
    	case EAST:
    		return ">";
    	case SOUTH:
    		return "v";
    	case WEST:
    		return "<";
    	default:
    		return " ";
    	}
    }
}
	