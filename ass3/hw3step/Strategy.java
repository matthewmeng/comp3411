import java.util.ArrayList;

/**
 * Strategy is the core algorithm of the program, contains the strategy we use
 * to find the treasure. The general thought is we first check if we already
 * have the gold, if yes we try to go back to the origional point. If not, then 
 * if we knoe the location of the treasure, we try to find a path to get it. 
 * Check the tools or available actions in the following order on the visited. 
 * And if we can't do the above operation try to explore all places of the 
 * current land. If we have explore every place and has a raft or stone. Go 
 * to explore the water. Also we need to deal with the case of raft and stone.
 * 
 * @author jingcheng li
 *
 */
public class Strategy {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	public boolean goToWater = false;
	public Explore explore;
	
	private ArrayList<Character> actionList;
	public ArrayList<Character> totalactionList;
	public VisitedView visitedview;
	public PathFinder pathfinder;
	public int literator = 0;
	public ArrayList<Integer[]> pointPosition = new ArrayList<Integer[]>();
	public Integer[] StartPoint = {79,79};

	/**
	 * Constructor of Strategy Object
	 */
	public Strategy() {
		this.explore = new Explore(this.visitedview);
		this.actionList = new ArrayList<Character>();
		this.visitedview= new VisitedView();
		this.totalactionList = new ArrayList<Character>();
		this.pathfinder = new PathFinder(this.explore.visitedview);
	}
	
	/**
	 * update the visitedview map by the new view
	 * @param view, the current view
	 */
	public void updateView(char[][] view) {
		this.visitedview.updateMap(view);
	}
	

	/**
	 * update the state and the statue of explore after we do some actions
	 * 
	 * @param action, the action to be taken
	 */
	public void addState(char action) {
		// if action is F
		if(action == 'F') {
			
			int currDirection = this.visitedview.getCurrDirection();
			int pos_x = this.visitedview.getPos_x();
			int pos_y = this.visitedview.getPos_y();
			Integer[] pos = {pos_x, pos_y};
			
			//update the positon and state
			this.explore.prevStates.add(pos);
			if(currDirection == NORTH) {
				pos_x --;
				int sum = pos_x*100 + pos_y;
				this.explore.postionList.put(sum, 1);
			}
			else if(currDirection == SOUTH) {
				pos_x ++;
				int sum = pos_x*100 + pos_y;
				this.explore.postionList.put(sum, 1);
			}
			else if(currDirection == EAST) {
				pos_y ++;
				int sum = pos_x*100 + pos_y;
				this.explore.postionList.put(sum, 1);
			}
			else if(currDirection == WEST) {
				pos_y --;
				int sum = pos_x*100 + pos_y;
				this.explore.postionList.put(sum, 1);
			}
			if(this.explore.goBack == false) {
				State state = new State();
				state.setPrevState(this.explore.state);
				state.setPos(pos_x, pos_y);
				this.explore.state = state;
			}
			else {
				State state = this.explore.state.getPrevState();
				this.explore.state = state;
				this.explore.goBack = false;
			}
			
		}
		//if we cut a tree, update the explore statue
		else if(action == 'C') {
			this.explore.tempExlpored = false;
		}
		//if we unlock a tree, update the explore statue
		else if(action == 'U') {
			this.explore.tempExlpored = false;
		}
	}
	
	/**
	 * get the next action to be taken
	 * 
	 * @return action, the action to be taken
	 */
	public char getAction() {
		
		this.explore.visitedview = this.visitedview;
		char action = ' ';
		ArrayList<Character> temp = new ArrayList<Character>();
		//if we are on a raft, update the time
		if(this.visitedview.visitedview[this.explore.visitedview.getPos_x()][this.explore.visitedview.getPos_y()] == '~') {
			this.explore.on_raft_time++;
		}
		//as long as we leave the water, we reset it
		else {
			this.explore.on_raft_time = 0;
		}
		//if there are no other actions to be done
		if(actionList.isEmpty()) {
			//If we already find the treasure , try to find a path go back to the start position
			if(this.explore.visitedview.have_treasure) {
				Integer[] start2 = {this.explore.visitedview.getPos_x(), this.explore.visitedview.getPos_y()};
				State currentstate = this.explore.state;				
				currentstate.setHava_treasure(true);
				this.pointPosition.add(start2);				
				
				Integer[] goal2 = {79,79};
				pathfinder = new PathFinder(this.explore.visitedview);
				State from = new State();
				from.setPos(start2[0], start2[1]);
				State to = new State();
				to.setPos(goal2[0], goal2[1]);
				ArrayList<Integer[]> pathlist2= pathfinder.Path(from,to);
				//add the actions into the list
				if(pathlist2 != null && pathlist2.size() > 0) {
					
					temp = pathfinder.changetocharList(pathlist2);
					if(temp.size() >= 1) {
						for(char move : temp) {
							this.actionList.add(move);
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						addState(action);
						return action;
					}

				}
				
			}
			//Check if there is a treasure in the visitedview, if there is try to find a path go to the treasure
			if(this.explore.visitedview.getGoldLocation()[0]!= 0 && this.explore.visitedview.have_treasure == false) {
				
				Integer[] goalPos = this.explore.visitedview.getGoldLocation();
				Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startPos[0], startPos[1]);
				State goal = new State();
				goal.setPos(goalPos[0], goalPos[1]);
				ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
				//add the actions into the list
				if(pathlist != null) {
					Integer[] pos = pathlist.get((pathlist.size()-1));
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
					if(getmoves.size() >= 1) {
						for(char move : getmoves) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						addState(action);
						return action;
					}
					
				}
			}
			//Check the tools or available actions in the following order on the visited
			
			//try to get the key if we haven't had it
			if(this.explore.visitedview.getKeyLocation()[0]!= 0 && this.explore.visitedview.have_key == false) {
				
				Integer[] keypos = this.explore.visitedview.getKeyLocation();
				Integer[] startposKey = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startposKey[0], startposKey[1]);

				State goal = new State();
				goal.setPos(keypos[0], keypos[1]);
				ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
				//add the actiosn into the list
				if(pathlist != null) {
					Integer[] pos = pathlist.get((pathlist.size()-1));
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);

					
					if(getmoves.size() >= 1) {
						for(char move : getmoves) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
						return action;
					}
				}

			}
			//try to get the axe if we haven't had it
			if(this.explore.visitedview.getAxeLocation()[0]!= 0 && this.explore.visitedview.have_axe == false) {
				
				Integer[] goalPos = this.explore.visitedview.getAxeLocation();
				Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startPos[0], startPos[1]);

				State goal = new State();
				goal.setPos(goalPos[0], goalPos[1]);
				ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
				//add the actions into the list
				if(pathlist != null) {
					Integer[] pos = pathlist.get((pathlist.size()-1));
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
					if(getmoves.size() >= 1) {
						for(char move : getmoves) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
						return action;
					}
				}
			}

			//try to cut a tree if we do not have a raft and have a axe
			if(this.visitedview.have_axe && (this.visitedview.getTreeLocations().size() > 0) && (!this.visitedview.have_raft)) {
				
				Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startpos[0], startpos[1]);
				ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();
				//get the first accessible tree
				for(int i = 0; i < this.visitedview.getTreeLocations().size(); i++) {
					
					Integer[] treepos = this.visitedview.getTreeLocations().get(i);
					State goal = new State();
					goal.setPos(treepos[0], treepos[1]);
					pathlist = pathfinder.Path(start, goal);
					
					if(pathlist != null && pathlist.size() > 0) {
						
						break;
					}
					
				}
				
				//add the actions into the list
				if(pathlist != null && pathlist.size() > 0) {
		
					Integer[] pos = pathlist.get((pathlist.size()-1));
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
					
					if(getmoves.size() >= 1) {
						
						for(char move : getmoves) {
						
							this.actionList.add(move);
						
						}
						
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
													
						return action;
					}
				}

				
				
			}
			//try to open a door
			if(this.visitedview.have_key && (this.visitedview.getDoorLocations().size() > 0)) {
				
				Integer[] doorpos = this.explore.visitedview.getDoorLocations().get(0);
				Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startpos[0], startpos[1]);
				State goal = new State();
				goal.setPos(doorpos[0], doorpos[1]);
				ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
				//add the actions into the list
				if(pathlist != null) {
					
					Integer[] pos = pathlist.get((pathlist.size()-1));
					ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
					
					
					if(getmoves.size() >= 1) {
						
						for(char move : getmoves) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
												
						return action;					
					}
	
				}

				
				
			}
			//try to pick a stone
			if(this.explore.visitedview.getStoneLocations().size() > 0 && this.visitedview.getGoldLocation()[0]!=0) {
				State goal = new State();
				Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
				pathfinder = new PathFinder(this.explore.visitedview);
				State start = new State();
				start.setPos(startPos[0], startPos[1]);
				ArrayList<Integer[]>pathlist = new ArrayList<Integer[]>();
				
				for(int i = 0; i < this.visitedview.getStoneLocations().size(); i++) {
				
					Integer[] stonepos = this.visitedview.getStoneLocations().get(i);
					goal = new State();
					goal.setPos(stonepos[0], stonepos[1]);
					pathlist = pathfinder.Path(start, goal);
					
					if((pathlist != null) && (pathlist.size() > 0)) {
						
						break;
					}
					
				}
				//add the actions into the list
				if((pathlist != null) && (pathlist.size() > 0)) {
					
					ArrayList<Character> stonepath = pathfinder.changetocharList(pathlist);
					
					if(stonepath.size() >= 1) {
						for(char move : stonepath) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
						
						return action;
					}
					
				}
			}
			//put a stone, first check if the adjacent places are water, if yes, then
			//check the next adjacent place is available, if yes, we put a stone and 
			//go across it
			if(this.visitedview.have_stone &&!(this.explore.visitedview.getGoldLocation()[0]!= 0 && this.explore.visitedview.have_treasure == false)) {
				
				for(Integer[] nextstep:this.visitedview.waterAdjacency(this.visitedview.getPos_x(), this.visitedview.getPos_y())) {
					//if the adjacent place is water
					if(this.visitedview.visitedview[nextstep[0]][nextstep[1]]== '~') {
						
						for(Integer[] next2step : this.visitedview.waterAdjacency(nextstep[0], nextstep[1])) {
						
							if((this.visitedview.visitedview[next2step[0]][next2step[1]] != '~')) {
							
								pathfinder = new PathFinder(this.explore.visitedview);
								State start = new State();
								start.setPos(this.visitedview.getPos_x(), this.visitedview.getPos_y());
								State goal = new State();
								goal.setPos(next2step[0],next2step[1]);
								ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
								
								if(pathlist != null) {
									
									Integer[] pos = pathlist.get((pathlist.size()-1));
									ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
									
									//add the actions into the list
									if(getmoves.size() >= 1) {
										
										for(char move : getmoves) {
										
											this.actionList.add(move);
										
										}
										
										action = this.actionList.get(0);
										this.actionList.remove(0);
										this.totalactionList.add(action);
										addState(action);
										this.explore.tempExlpored = false;										
										return action;
									
									}
								}
							}
						}
					}
				}
			}
			
			//if we haven't explore all the current accesible places, explore it
			if(!this.explore.tempExlpored) {
				
				temp = this.explore.getMoves();
				//add the actions into the list
				if(temp.size() >= 1) {
					
					for(char move : temp) {
					
						this.actionList.add(move);
					
					}
					action = this.actionList.get(0);
					this.actionList.remove(0);
					this.totalactionList.add(action);
					addState(action);
					
					return action;
				}
				else {
					
					this.explore.tempExlpored = true;
				}
			}
			//if we have explored all the current accesible places
			if(this.explore.tempExlpored ) {
	
				//try to cut a tree ito explore new world
				if(this.visitedview.have_axe && (this.visitedview.getTreeLocations().size() > 0) && (!this.visitedview.have_raft)) {
					
					Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startpos[0], startpos[1]);
					ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();
					//get the first accessible tree
					for(int i = 0; i < this.visitedview.getTreeLocations().size(); i++) {
						
						Integer[] treepos = this.visitedview.getTreeLocations().get(i);
						State goal = new State();
						goal.setPos(treepos[0], treepos[1]);
						pathlist = pathfinder.Path(start, goal);
						
						if(pathlist != null && pathlist.size() > 0) {
							
							break;
						}
						
					}
					
					//add the actions into the list
					if(pathlist != null && pathlist.size() > 0) {
			
						Integer[] pos = pathlist.get((pathlist.size()-1));
						ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
						
						if(getmoves.size() >= 1) {
							
							for(char move : getmoves) {
							
								this.actionList.add(move);
							
							}
							
							action = this.actionList.get(0);
							this.actionList.remove(0);
							this.totalactionList.add(action);
							addState(action);
														
							return action;
						}
					}

					
					
				}
				//try to open a door to explore new world
				if(this.visitedview.have_key && (this.visitedview.getDoorLocations().size() > 0)) {
					
					Integer[] doorpos = this.explore.visitedview.getDoorLocations().get(0);
					Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startpos[0], startpos[1]);
					State goal = new State();
					goal.setPos(doorpos[0], doorpos[1]);
					ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
					//add the actions into the list
					if(pathlist != null) {
						
						Integer[] pos = pathlist.get((pathlist.size()-1));
						ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
						
						
						if(getmoves.size() >= 1) {
							
							for(char move : getmoves) {
							
								this.actionList.add(move);
							
							}
							action = this.actionList.get(0);
							this.actionList.remove(0);
							this.totalactionList.add(action);
							addState(action);
													
							return action;					
						}
		
					}

					
					
				}
				//try to pick a new stone to explore new world
				if(this.explore.visitedview.getStoneLocations().size() > 0) {
					Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					State start = new State();
					start.setPos(startPos[0], startPos[1]);
					State goal = new State();
					pathfinder = new PathFinder(this.explore.visitedview);
					ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();
					pathlist = new ArrayList<Integer[]>();
					for(int i = 0; i < this.visitedview.getStoneLocations().size(); i++) {
						
						Integer[] stonepos = this.visitedview.getStoneLocations().get(i);
						goal = new State();
						goal.setPos(stonepos[0], stonepos[1]);
						pathlist = pathfinder.Path(start, goal);
						
						if(pathlist != null && pathlist.size() > 0) {
							break;
						}
						
					}
					//add the actions into the list
					if(pathlist != null && pathlist.size() >0) {
						Integer[] pos = pathlist.get((pathlist.size()-1));
						ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
						
						
						if(getmoves.size() >= 1) {
							
							for(char move : getmoves) {
							
								this.actionList.add(move);
							
							}
							
							action = this.actionList.get(0);
							this.actionList.remove(0);
							this.totalactionList.add(action);
							addState(action);
							
							return action;
						}
					}
				}
				
				//if we explored the place, we need to go to zhe water to explore new place
				if((this.goToWater == false) && (this.visitedview.have_stone || this.visitedview.have_raft)) {
					
					if(this.visitedview.getWaterLocations().size() > 0) {
						
						this.goToWater = true;
						Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
						pathfinder = new PathFinder(this.explore.visitedview);
						State start = new State();
						start.setPos(startPos[0], startPos[1]);
						State goal = new State();
						ArrayList<Integer[]> nearestlist = new ArrayList<Integer[]>();
						ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();
						int maxcount = 0;
						//find the nearest water
						for(int i = 0; i < this.visitedview.getWaterLocations().size(); i++) {
							
							Integer[] goalPos = this.visitedview.getWaterLocations().get(i);
							goal.setPos(goalPos[0], goalPos[1]);
							pathlist= pathfinder.Path(start, goal);
							
							if(pathlist != null && pathlist.size()>0) {

								int count = 0;
								if(this.visitedview.visitedview[goalPos[0]+1][goalPos[1]] == ' ') {
									
									count++;
								}
								if(this.visitedview.visitedview[goalPos[0]-1][goalPos[1]] == ' ') {
									
									count++;
								}
								if(this.visitedview.visitedview[goalPos[0]][goalPos[1]+1] == ' ') {
									
									count++;
								}
								if(this.visitedview.visitedview[goalPos[0]][goalPos[1]-1] == ' ') {
									
									count++;
								}

								if(maxcount < count) {
									
									maxcount = count;
									nearestlist = pathlist;
								}
							}

						}
						//add the actions into the list
						pathlist = nearestlist;
						if(pathlist != null && pathlist.size()>0) {
							
							temp = pathfinder.changetocharList(pathlist);
							
							if(temp.size() >= 1) {
								
								for(char move : temp) {
								
									this.actionList.add(move);
								
								}
								
								action = this.actionList.get(0);
								this.actionList.remove(0);
								this.totalactionList.add(action);
								addState(action);
								
								return action;
							}

						}
						
					}
				}
			
				//if we are on a raft, we use water moves to get actions
				if(this.visitedview.on_raft) {
					temp = this.explore.getWaterMoves();
					//add the actions into the list
					if(temp.size() >= 1) {
						
						for(char move : temp) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
						
						return action;
					}
					
				}
				//or we use normal ways to get actions
				temp = this.explore.getMoves();
				if(temp.size() >= 1) {
					
					for(char move : temp) {
					
						this.actionList.add(move);
					
					}
					action = this.actionList.get(0);
					this.actionList.remove(0);
					this.totalactionList.add(action);
					addState(action);
					
					return action;
				}
			}
			
		}
		//if there are still actions to be taken, do that
		else {
			
			action = this.actionList.get(0);
			this.actionList.remove(0);
			
			this.totalactionList.add(action);
			addState(action);
			return action;
			
		}
			
		this.totalactionList.add(action);
		addState(action);
		return action;
	}
	


}
