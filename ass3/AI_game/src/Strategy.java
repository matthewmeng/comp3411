import java.util.ArrayList;
import java.util.LinkedList;

public class Strategy {
	
	final static int EAST   = 0;
	final static int NORTH  = 1;
	final static int WEST   = 2;
	final static int SOUTH  = 3;
	public boolean flag = false;
	public boolean goToWater = false;
	public Explore explore;
	
	private ManhattanDistance heuristic ;
	private ArrayList<Character> actionList;
	public ArrayList<Character> totalactionList;
	public VisitedView visitedview;
	public PathFinder pathfinder;
	private ArrayList<Character> gobacklist = new ArrayList<Character>();
	private boolean gobackflag = false;
	public int literator = 0;
	public ArrayList<Integer[]> pointPosition = new ArrayList<Integer[]>();
	public Integer[] StartPoint = {79,79};

	public Strategy() {
		this.explore = new Explore(this.visitedview);
		this.actionList = new ArrayList<Character>();
		this.visitedview= new VisitedView();
		this.totalactionList = new ArrayList<Character>();
		this.heuristic = new ManhattanDistance();
		this.pathfinder = new PathFinder(this.explore.visitedview);
	}
	
	public void updateView(char[][] view) {
		this.visitedview.updateMap(view);
	}
	
	public void addState(char action) {
		if(action == 'F') {
			
			int currDirection = this.visitedview.getCurrDirection();
			int pos_x = this.visitedview.getPos_x();
			int pos_y = this.visitedview.getPos_y();
			Integer[] pos = {pos_x, pos_y};
			
			
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
		else if(action == 'C') {
			this.explore.tempExlpored = false;
		}
		else if(action == 'U') {
			this.explore.tempExlpored = false;
		}
	}
	
	public char getAction() {
		this.explore.visitedview = this.visitedview;
		if(this.visitedview.visitedview[this.explore.visitedview.getPos_x()][this.explore.visitedview.getPos_y()] == '~') {
			this.explore.on_raft_time++;
		}else {
			this.explore.on_raft_time = 0;
		}
		char action = ' ';
		ArrayList<Character> temp = new ArrayList<Character>();

		System.out.println("have raft : " + this.visitedview.have_raft);
		System.out.println("on raft : " + this.visitedview.on_raft);
		System.out.println("explored : " + this.explore.tempExlpored);
		System.out.println("stone num: " + this.explore.visitedview.getStoneLocations().size());
		System.out.println("stone : " + this.explore.visitedview.numStone);
		System.out.println("have stone : " + this.explore.visitedview.have_stone);
		//System.out.println("GO BACK FLAG!!!:" + this.explore.goBack);
			if(actionList.isEmpty()) {
				
				if(this.explore.visitedview.have_treasure) {
					System.out.println("GO Back");
					int end_x = this.explore.visitedview.getPos_x();
					int end_y = this.explore.visitedview.getPos_y();
					Integer[] start2 = {end_x,end_y};
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
					if(pathlist2 != null && pathlist2.size() > 0) {
						this.gobackflag = true;
						
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
				if(this.explore.visitedview.getGoldLocation()[0]!= 0 && this.explore.visitedview.have_treasure == false) {
					System.out.println("FIND PATH TO GOLD");
					Integer[] goalPos = this.explore.visitedview.getGoldLocation();
					Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startPos[0], startPos[1]);
					State goal = new State();
					goal.setPos(goalPos[0], goalPos[1]);
					ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
					if(pathlist != null) {
						Integer[] pos = pathlist.get((pathlist.size()-1));
						System.out.println(pos[0]+" "+ pos[1]);
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
				if(this.explore.visitedview.getKeyLocation()[0]!= 0 && this.explore.visitedview.have_key == false) {
					System.out.println("FIND KEY");
					Integer[] keypos = this.explore.visitedview.getKeyLocation();
					Integer[] startposKey = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					//System.out.println("From:"+this.visitedview.getPos_x()+" "+this.visitedview.getPos_y());
					//System.out.println(this.visitedview.getGoldLocation()[0]+" "+this.visitedview.getGoldLocation()[1]);
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startposKey[0], startposKey[1]);

					State goal = new State();
					goal.setPos(keypos[0], keypos[1]);
					ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
					if(pathlist != null) {
						Integer[] pos = pathlist.get((pathlist.size()-1));
						ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
						//this.totalactionList.add(getmoves.get(0));
						//addState(getmoves.get(0));
						//return getmoves.get(0);
						
						
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
				if(this.explore.visitedview.getStoneLocations().size() > 0 && this.visitedview.getGoldLocation()[0]!=0) {
					System.out.println("FIND stone");
					State goal = new State();
					Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					//System.out.println("From:"+this.visitedview.getPos_x()+" "+this.visitedview.getPos_y());
					//System.out.println(this.visitedview.getGoldLocation()[0]+" "+this.visitedview.getGoldLocation()[1]);
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startPos[0], startPos[1]);
					ArrayList<Integer[]>pathlist = new ArrayList<Integer[]>();
					for(int i = 0; i < this.visitedview.getStoneLocations().size(); i++) {
					
						Integer[] stonepos = this.visitedview.getStoneLocations().get(i);
						System.out.println("pos:" + stonepos[0] + " " + stonepos[1]);
						goal = new State();
						goal.setPos(stonepos[0], stonepos[1]);
						pathlist = pathfinder.Path(start, goal);
						
						if((pathlist != null) && (pathlist.size() > 0)) {
							System.out.println("pos:" + stonepos[0] + " " + stonepos[1]);
							break;
						}
						
					}
					
					if((pathlist != null) && (pathlist.size() > 0)) {
						
						ArrayList<Character> stonepath = pathfinder.changetocharList(pathlist);
						
						if(stonepath.size() >= 1) {
							//this.actionList.clear();
							for(char move : stonepath) {
							
								this.actionList.add(move);
							
							}
							action = this.actionList.get(0);
							this.actionList.remove(0);
							this.totalactionList.add(action);
							addState(action);
							//this.explore.tempExlpored = false;
							return action;
						}
					}
				}
				if(this.explore.visitedview.getAxeLocation()[0]!= 0 && this.explore.visitedview.have_axe == false) {
					System.out.println("FIND AXE");
					System.out.println("See the Axe");
					Integer[] goalPos = this.explore.visitedview.getAxeLocation();
					Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					//System.out.println("From:"+this.visitedview.getPos_x()+" "+this.visitedview.getPos_y());
					//System.out.println(this.visitedview.getGoldLocation()[0]+" "+this.visitedview.getGoldLocation()[1]);
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startPos[0], startPos[1]);

					State goal = new State();
					goal.setPos(goalPos[0], goalPos[1]);
					ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
					if(pathlist != null) {
						Integer[] pos = pathlist.get((pathlist.size()-1));
						ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
						//addState(getmoves.get(0));
						//this.totalactionList.add(getmoves.get(0));
						//return getmoves.get(0);
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
				if(this.visitedview.have_axe && (this.visitedview.getTreeLocations().size() > 0) && (!this.visitedview.have_raft)) {
					System.out.println("EXPLORED CUT TREE");
					Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					System.out.println("trrrrrrrrrrrrrree" + this.visitedview.getTreeLocations().size());
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startpos[0], startpos[1]);
					ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();
					for(int i = 0; i < this.visitedview.getTreeLocations().size(); i++) {
						
						Integer[] treepos = this.visitedview.getTreeLocations().get(i);
						State goal = new State();
						goal.setPos(treepos[0], treepos[1]);
						pathlist = pathfinder.Path(start, goal);
						
						if(pathlist != null && pathlist.size() > 0) {
							System.out.println("trrrrrrrrrrrrrree");
							break;
						}
						
					}
					

					if(pathlist != null && pathlist.size() > 0) {
//						temp = pathfinder.changetocharList(pathlist);
//						
//						if(temp.size() >= 1) {
//							//this.actionList.clear();
//							for(char move : temp) {
//							
//								this.actionList.add(move);
//							
//							}
//							action = this.actionList.get(0);
//							this.actionList.remove(0);
//							this.totalactionList.add(action);
//							addState(action);
//							//this.explore.tempExlpored = false;
//							return action;
//						}
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
				if(this.visitedview.have_key && (this.visitedview.getDoorLocations().size() > 0)) {
					Integer[] doorpos = this.explore.visitedview.getDoorLocations().get(0);
					Integer[] startpos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
					pathfinder = new PathFinder(this.explore.visitedview);
					State start = new State();
					start.setPos(startpos[0], startpos[1]);
					State goal = new State();
					goal.setPos(doorpos[0], doorpos[1]);
					ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
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
				if(this.visitedview.have_stone &&!(this.explore.visitedview.getGoldLocation()[0]!= 0 && this.explore.visitedview.have_treasure == false)) {
					System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
					for(Integer[] nextstep:this.visitedview.waterAdjacency(this.visitedview.getPos_x(), this.visitedview.getPos_y())) {
						if(this.visitedview.visitedview[nextstep[0]][nextstep[1]]== '~') {
							System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
							for(Integer[] next2step : this.visitedview.waterAdjacency(nextstep[0], nextstep[1])) {
								System.out.println(this.visitedview.visitedview[next2step[0]][next2step[1]]);
								if((this.visitedview.visitedview[next2step[0]][next2step[1]] != '~')) {
									System.out.println("SHOULD PASS??????????????????????????????????????????");
									pathfinder = new PathFinder(this.explore.visitedview);
									State start = new State();
									start.setPos(this.visitedview.getPos_x(), this.visitedview.getPos_y());
									State goal = new State();
									goal.setPos(next2step[0],next2step[1]);
									ArrayList<Integer[]> pathlist= pathfinder.Path(start, goal);
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
						}
					}
				}
				}
				
				
			
				if(!this.explore.tempExlpored) {
					System.out.println("explore");
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
					else {
						//System.out.println("All land explored");
						
						this.explore.tempExlpored = true;
					}
				}
				


				if(this.explore.tempExlpored ) {
					if(this.visitedview.have_stone && (this.visitedview.getStoneLocations().size() > 0)) {
						
						Integer[] startPos = {this.explore.visitedview.getPos_x(),this.explore.visitedview.getPos_y()};
						State start = new State();
						start.setPos(startPos[0], startPos[1]);
						State goal = new State();
						pathfinder = new PathFinder(this.explore.visitedview);
						ArrayList<Integer[]> pathlist = new ArrayList<Integer[]>();

						
						if(this.explore.visitedview.getKeyLocation()[0]!= 0 && this.explore.visitedview.have_key == false) {
							System.out.println("EXPLORED FIND key");
							Integer[] keypos = this.explore.visitedview.getKeyLocation();
							goal.setPos(keypos[0], keypos[1]);
							pathlist= pathfinder.Path(start, goal);
							if(pathlist != null) {
								Integer[] pos = pathlist.get((pathlist.size()-1));
								ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
								//this.totalactionList.add(getmoves.get(0));
								//addState(getmoves.get(0));
								//return getmoves.get(0);
								
								
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
						
						if(this.explore.visitedview.getAxeLocation()[0]!= 0 && this.explore.visitedview.have_axe == false) {
							System.out.println("EXPLORED FIND AXE");
							Integer[] goalPos = this.explore.visitedview.getAxeLocation();
							goal.setPos(goalPos[0], goalPos[1]);
							pathlist= pathfinder.Path(start, goal);
							if(pathlist != null) {
								Integer[] pos = pathlist.get((pathlist.size()-1));
								ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
								//this.totalactionList.add(getmoves.get(0));
								//addState(getmoves.get(0));
								//return getmoves.get(0);
								
								
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
						
						if(this.explore.visitedview.getStoneLocations().size() > 0) {
							System.out.println("EXPLORED FIND STONE");
							Integer[] goalPos = this.explore.visitedview.getAxeLocation();
							pathlist = new ArrayList<Integer[]>();
							for(int i = 0; i < this.visitedview.getStoneLocations().size(); i++) {
								
								Integer[] stonepos = this.visitedview.getStoneLocations().get(i);
								goal = new State();
								goal.setPos(stonepos[0], stonepos[1]);
								pathlist = pathfinder.Path(start, goal);
								
								if(pathlist != null && pathlist.size() > 0) {
									System.out.println("trrrrrrrrrrrrrree");
									break;
								}
								
							}
							
							if(pathlist != null && pathlist.size() >0) {
								Integer[] pos = pathlist.get((pathlist.size()-1));
								ArrayList<Character> getmoves = this.explore.toAdjacency(pos);
								//this.totalactionList.add(getmoves.get(0));
								//addState(getmoves.get(0));
								//return getmoves.get(0);
								
								
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
						
						

						
						
					}
				
					if((this.goToWater == false) && (this.visitedview.have_stone || this.visitedview.have_raft)) {
						System.out.println("GO TO WATER");
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
							
							
							System.out.println("FOUND curr " + startPos[0] + " " + startPos[1]);
							
							for(int i = 0; i < this.visitedview.getWaterLocations().size(); i++) {
								Integer[] goalPos = this.visitedview.getWaterLocations().get(i);
								System.out.println("FOUND " + goalPos[0] + " " + goalPos[1]);
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
									System.out.println("FOUND ggggggggg " + goalPos[0] + " " + goalPos[1]);
								}
								

								
							}
							
							
							
							pathlist = nearestlist;
							if(pathlist != null && pathlist.size()>0) {
								temp = pathfinder.changetocharList(pathlist);
								
								if(temp.size() >= 1) {
									//this.actionList.clear();
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
				
					
					if(this.visitedview.on_raft) {
						temp = this.explore.getWaterMoves();
						if(temp.size() >= 1) {
							for(char move : temp) {
							
								this.actionList.add(move);
							
							}
							action = this.actionList.get(0);
							this.actionList.remove(0);
							this.totalactionList.add(action);
							addState(action);
							int pos_x = this.visitedview.getPos_x();
							int pos_y = this.visitedview.getPos_y();
							System.out.println("vvvvvv " + this.explore.postionList.get(pos_x*100+pos_y));
							return action;
						}
						
					}
					
					temp = this.explore.getMoves();
					if(temp.size() >= 1) {
						for(char move : temp) {
						
							this.actionList.add(move);
						
						}
						action = this.actionList.get(0);
						this.actionList.remove(0);
						this.totalactionList.add(action);
						addState(action);
						int pos_x = this.visitedview.getPos_x();
						int pos_y = this.visitedview.getPos_y();
						System.out.println("vvvvvv " + this.explore.postionList.get(pos_x*100+pos_y));
						return action;
					}
				}
				
				System.out.println("has raft: " + this.visitedview.have_raft);
				System.out.println("on raft: " + this.visitedview.on_raft);
			}
			else {
				action = this.actionList.get(0);
				this.actionList.remove(0);
				
				this.totalactionList.add(action);
				addState(action);
				int pos_x = this.visitedview.getPos_x();
				int pos_y = this.visitedview.getPos_y();
				System.out.println("vvvvvv " + this.explore.postionList.get(pos_x*100+pos_y));
				return action;
				
			}
			
			System.out.println("temp explored: " + explore.tempExlpored);
	
			this.totalactionList.add(action);
			addState(action);
			return action;

	}
	
	public Integer[] getNearestWater(VisitedView visitedview) {
		
		int pos_x = visitedview.getPos_x();
		int pos_y = visitedview.getPos_y();
		Integer[] pos = {pos_x, pos_y};
		int min_distance = 100000;
		ArrayList<Integer[]> waterLocations = visitedview.getWaterLocations();
		
		for(int i = 0; i < waterLocations.size() - 1; i++) {
			Integer[] temp = waterLocations.get(i);
			int distance = (temp[0] - pos_x)*(temp[0] - pos_x) + (temp[1] - pos_y)*(temp[1] - pos_y);
			
			if(distance < min_distance) {
				min_distance = distance;
				pos = temp;
			}
		}
		
		
		return pos;
	}


}
