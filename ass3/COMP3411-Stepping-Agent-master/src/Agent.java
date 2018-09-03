

/*********************************************
 *  Agent.java 
 *  COMP3411/9414 Artificial Intelligence
 *  UNSW Session 1, 2016
 *  
 *  Thanh Nguyen Mueller (z5056262) , Xuefeng Li (z5085453)
 *  Group 6
 *  
 *	Our main strategy for the game:
 *  On every step update, 
 *	1.	If there are any tools in our copy map, let the agent check if we can get these tools and then find a path to get the nearest one.
 *	2.	If there is no accessible tools, explore  unvisited positions to expand the map and update all the information about tools.
 *	3.	If the map is not expandable anymore and the agent has an axe or key, the agent removes trees and doors to see if it leads to a new path
 *	4.	If there is no new path found, the agent will find the optimal way to use the stones to get to unreachable tools.
 *		This works as follows:
 *		find all the shortest paths (number of stones needed as cost) to all the unreachable tools (blocked by water) from any position
 *		that the agent can access 
 *		If there are multiple paths with equal costs, find the path which have the most cost optimization (path that also reduce costs to
 *		get to other tools). Then we put the stone at the position with max costMinization.
 *	
 *	Algorithms used in our program:
 *
 *	1.	Backtracking algorithm: exploring the map, make sure the agent will visit all the available positions.
 *	2.	A* search: finding the shortest path to the tools.
 *	3.	Dijkstra Algorithm: calculating the least cost (stones needed) for each position in the map
 *  4.  BFS to find the shortest path from an unreachable tool to an accessible position.
 *
 *	Data Structures used in our program:
 *
 *	1.	Two dimensional array: storing the map and cost data to each position.
 *	2.	Priority Queue: used in A* search
 *	3.	Hash map: used in path finding algorithms to store the path and costs
 *	4.	Some other basic data structures.
 *	
 *  
*/

import java.io.*;
import java.net.*;

public class Agent {
	private KnowledgeBase knowledgeBase = new KnowledgeBase();
	   
	public char getAction(char view[][]) {

/*
		 try {
		Thread.sleep(250);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
*/
		knowledgeBase.tell(view);
		Action action = knowledgeBase.ask();
		if (action != Action.NONE) {
			return Action.toChar(action);
		}
		
		
		int actionChar = 0;

		
		 System.out.print("Enter Action(s): ");
		  
		  try {
			  while ( actionChar != -1 ) { // read character from keyboard 
				  actionChar = System.in.read();
			  
				  switch( actionChar ) { // if character is a valid action, return it 
				  case 'F': case 'L': case 'R': case 'C': case 'U':
				  case 'f': case 'l': case 'r':  case 'c': case 'u':
					  return((char) actionChar );
				  }
			  }
		  } catch (IOException e) {
		  System.out.println ("IO error:" + e ); }
		 
		return 0;
	}
	
	private void performAction(char actionChar) {
		knowledgeBase.performAction(Action.toAction(actionChar));
	}
	
	
	void print_view(char view[][]) {
		int i, j;
		System.out.println("\n+-----+");
		for (i = 0; i < 5; i++) {
			System.out.print("|");
			for (j = 0; j < 5; j++) {
				if ((i == 2) && (j == 2)) {
					System.out.print('^');
				} else {
					System.out.print(view[i][j]);
				}
			}
			System.out.println("|");
		}
		System.out.println("+-----+");
	}
	
	public static void main(String[] args) {
		InputStream in = null;
		OutputStream out = null;
		Socket socket = null;
		Agent agent = new Agent();
		char view[][] = new char[5][5];
		char action = 'F';
		int port;
		int ch;
		int i, j;
		
		if (args.length < 2) {
			System.out.println("Usage: java Agent -p <port>\n");
			System.exit(-1);
		}

		port = Integer.parseInt(args[1]);

		try { // open socket to Game Engine
			socket = new Socket("localhost", port);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			System.out.println("Could not bind to port: " + port);
			System.exit(-1);
		}

		try { // scan 5-by-5 wintow around current location
			while (true) {
				for (i = 0; i < 5; i++) {
					for (j = 0; j < 5; j++) {
						if (!((i == 2) && (j == 2))) {
							ch = in.read();
							if (ch == -1) {
								System.exit(-1);
							}
							view[i][j] = (char) ch;
						}
					}
				}
				//agent.print_view(view); // COMMENT THIS OUT BEFORE SUBMISSION
				action = agent.getAction(view);
				agent.performAction(action);
			
				
				
				
				out.write(action);
			}
		} catch (IOException e) {
			System.out.println("Lost connection to port: " + port);
			System.exit(-1);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
