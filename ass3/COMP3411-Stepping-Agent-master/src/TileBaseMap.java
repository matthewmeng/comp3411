
public interface TileBaseMap {
	/**
	 * @return width of the map
	 */
	public int getWidth();
	
	/**
	 * @return height of the map
	 */
	public int getHeight();
	
	/**
	 * @param position
	 * @return true if the position is not accessible
	 */
	public boolean isBlocked(Position position);
}
