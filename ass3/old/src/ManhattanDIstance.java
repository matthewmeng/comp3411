
public class ManhattanDIstance {
	public ManhattanDIstance(){
		
	}
	public int getCost(int startx, int starty, int goalx, int goaly) {
		return Math.abs(goalx-startx) + Math.abs(goaly - starty);
	}
}
