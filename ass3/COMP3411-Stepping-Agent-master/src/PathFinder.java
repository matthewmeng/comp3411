/**
 * Interface for a path finder
 *
 */
public interface PathFinder {
	
	/**
	 * Finds a path for the given start position and to end position
	 * @param start
	 * @param end
	 * @return path
	 */
	public Path findPath(Position start, Position end);
}
