import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A Path has start position at the head and goal position at the tail
 */
public class Path {
	public static final Path EMPTY_PATH = new Path();
	private Deque<Position> positions;
	private int cost;
	
	/**
	 * Creates a new path
	 */
	public Path() {
		positions = new ArrayDeque<>();
	}
	
	/**
	 * Creates a copy of a path for the given path
	 * @param path
	 */
	public Path(Path path) {
		positions = new ArrayDeque<>();
		positions.addAll(path.positions);
	}
	
	
	/**
	 * Adds a position to the head of the path
	 */
	public void push(Position position) {
		positions.push(position);
	}
	
	/**
	 * Adds a position at the tail of the path
	 * @param position
	 */
	public void addLast(Position position) {
		positions.addLast(position);
	}
	
	/**
	 * Peeks the next position (head). Returns the position but not remove it
	 */
	public Position peek() {
		return positions.peek();
	}
	
	/**
	 * @return the second Position in the path but not remove it
	 */
	public Position peekNextHead () {
		Position head = positions.pop();
		Position nextHead = positions.peek();
		positions.push(head);
		return nextHead;
			
	}
	
	/**
	 * Pops the next position (head)
	 */
	public Position pop() {
		return positions.pop();
	}
	
	/**
	 * Adds another path to this path
	 * @param otherPath to add
	 * @return the combined path
	 */
	public Path addPath(Path otherPath) {
		Iterator<Position> otherPathIterator = otherPath.iterator();
		while (otherPathIterator.hasNext()) {
			positions.push(otherPathIterator.next());
		}
		return this;
	}
	
	/**
	 * @return iterator to iterate through the map
	 */
	public Iterator<Position> iterator() {
		return positions.iterator();
	}
	
	/**
	 * @param position
	 * @return true if the path contains the position, false otherwise
	 */
	public boolean contains (Position position) {
		return positions.contains(position);
	}
	

	/**
	 * Creates a new path with the given path and the position at the heaad
	 * @param path
	 * @return
	 */
	public static Path createPath (Path path, Position position) {
		Path newPath = new Path(path);
		newPath.push(position);
		return newPath;
	}
	
	@Override
	/**
	 * Returns a string that contains all the positions in the path. Used to print
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("Path ");
		for (Position position : positions) {
			buffer.append(position);	
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
	/**
	 * @return cost of the path
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * Sets the cost for this path
	 * @param cost
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * @return the path is empty
	 */
	public boolean isEmpty() {
		return positions.isEmpty();
	}
}
