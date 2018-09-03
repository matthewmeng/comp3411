/**
 * State is the states of the movement. Each state know the info of current situation
 * 
 *
 */
public class State {
	
	State prevState;
	Integer[] pos;
	Integer numStone = 0;
	public boolean have_raft    = false;
	public boolean have_stone   = false;
	public boolean on_raft      = false;
	public boolean have_axe     = false;
	public boolean have_key     = false;
	public boolean hava_treasure = false;
	public VisitedView visitedview;
	
	public State() {
		@SuppressWarnings("unused")
		State prevState = null;	
	}
	
	// Follows are operation of the attributes
	
	public void setHava_treasure(boolean hava_treasure) {
		this.hava_treasure = hava_treasure;
	}
	
	public boolean isHava_treasure() {
		return hava_treasure;
	}
	
	public void setPos(int pos_x, int pos_y) {
		Integer[] temp = {pos_x, pos_y};
		this.pos = temp;
	}
	
	public void setPrevState(State prevState) {
		this.prevState = prevState;
	}
	
	public void setHaveRaft(boolean have_raft) {
		this.have_raft = have_raft;
	}
	
	public void setOnRaft(boolean on_raft) {
		this.on_raft = on_raft;
	}
	
	public void setHaveStone(boolean have_stone) {
		this.have_stone = have_stone;
	}
	
	public void setHaveAxe(boolean have_axe) {
		this.have_axe = have_axe;
	}
	
	public void setHaveKey(boolean have_key) {
		this.have_key = have_key;
	}

	public boolean getHaveAxe() {
		return this.have_axe;
	}
	
	public boolean getHaveKey() {
		return this.have_key;
	}
	
	public boolean getHaveRaft() {
		return this.have_raft;
	}
	
	public boolean getOnRaft() {
		return this.on_raft;
	}
	
	public boolean getHaveStone() {
		return this.have_stone;
	}
	
	public void setNumStone(Integer numStone) {
		this.numStone = numStone;
	}
	
	public int getNumStone() {
		return this.numStone;
	}
	
	public State getPrevState() {
		return this.prevState;
	}
	
	public Integer[] getPos() {
		return this.pos;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Overrides equals for priority queue
	 */
	@Override
	public boolean equals(Object a) {
		if (a == null) {
			return false;
		}
		
		if(this == a) {
			return true;
		}
		
		State otherPos = (State)a;
		Integer[] pos = this.getPos();
		Integer[] opos = otherPos.getPos();
		
		if (pos[0] == opos[0] && pos[1] == opos[1]) {
			return true;
		}
		return false;
	}
	

	public void setVisitedView(VisitedView visitedview) {
		this.visitedview = visitedview;
		
	}
	
	
	
}


