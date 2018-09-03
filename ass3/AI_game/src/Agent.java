

/*********************************************
 *  Agent.java 
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411/9414/9814 Artificial Intelligence
 *  UNSW Session 1, 2018
 *  Jingcheng Li(5109027), Xiangzhuo Meng(5042679)
 *  
 *  Group 167
 *  
 *  1. Strategy
 *  	the main strategy for this game is in Strategy class
 *  	a. read the view from the agent, update the view to visitedview by explore
 *  	b. The strategy of our decision making.
 *  		1). If we already find the treasure , try to find a path go back to the start position
 *  		2). Check if there is a treasure in the visitedview, if there is try to find a path go to the treasure
 *  		3). Check the tools or available actions in the following order on the visited
 *  			(get key,get axe, cut Tree, open door, stone)
 *  		4). If we can't do the above operation try to explore all places of the current land
 *  		5). If we have explore every place and has a raft or stone. Go to explore the water
 *  		6). Use the stone before raft unless both next possible position are water
 *  
 *  2. Algorithm
 *  	We mainly use two search algorithms for this game(DFS and Manhattan A*){
 *  	1.Using the DFS(Uninformed search) to explore the map as far as we can 
 *  	2.If we find an specific position during the search and we can see it means we know the position. 
 *  	  Using the Manhattan A* Search Algorithm to find the optimal path.
 *  	  If we can not find the path, do the next step following the Strategy.
 *  
 *  3. Data Structures used:
 *  	1.priority queue: Store and sort the data in A* search
 *  	2.Arraylist : Store the data 
 *  	3.HashMap: Store and data
*/

import java.io.*;
import java.net.*;


/**
 * 
 *
 */
public class Agent {

  private Strategy strategy = new Strategy();
  
  /**
   * pass the view to the Strategy
   * get the action from the strategy
 * @param view
 * @return
 */
public char get_action( char view[][] ) {

	    
	  // REPLACE THIS CODE WITH AI TO CHOOSE ACTION
	  
	 
	  char action = 0;

	    //System.out.println("Enter Action(s): ");

	  this.strategy.updateView(view);
	  action = this.strategy.getAction();

	    
		while ( action != -1 ) {
		
		  switch( action ) { // if character is a valid action, return it
		   case 'F': case 'L': case 'R': case 'C': case 'U':
		   case 'f': case 'l': case 'r': case 'c': case 'u':
		     return(action);
		  }
		}


	    return 0;
}


  

void print_view( char view[][] )
  {
    int i,j;

    System.out.println("\n+-----+");
    for( i=0; i < 5; i++ ) {
      System.out.print("|");
      for( j=0; j < 5; j++ ) {
        if(( i == 2 )&&( j == 2 )) {
          System.out.print('^');
        }
        else {
          System.out.print( view[i][j] );
        }
      }
      System.out.println("|");
    }
    System.out.println("+-----+");
  }

  public static void main( String[] args )
  {
    InputStream in  = null;
    OutputStream out= null;
    Socket socket   = null;
    Agent  agent    = new Agent();
    char   view[][] = new char[5][5];
    char   action   = 'F';
    int port;
    int ch;
    int i,j;
 
   
    if( args.length < 2 ) {
      System.out.println("Usage: java Agent -p <port>\n");
      System.exit(-1);
    }

    port = Integer.parseInt( args[1] );

    try { // open socket to Game Engine
      socket = new Socket( "localhost", port );
      in  = socket.getInputStream();
      out = socket.getOutputStream();
    }
    catch( IOException e ) {
      System.out.println("Could not bind to port: "+port);
      System.exit(-1);
    }

    try { // scan 5-by-5 wintow around current location
      while( true ) {
        for( i=0; i < 5; i++ ) {
          for( j=0; j < 5; j++ ) {
            if( !(( i == 2 )&&( j == 2 ))) {
              ch = in.read();
              if( ch == -1 ) {
                System.exit(-1);
              }
              view[i][j] = (char) ch;
            }
          }
        }
      

     	//agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
        
      	action = agent.get_action( view );

      	agent.strategy.visitedview.perform(action);
   
        out.write(action);
      }

      
     }
    catch( IOException e ) {
      System.out.println("Lost connection to port: "+ port );
      System.exit(-1);
    }
    finally {
      try {
        socket.close();
      }
      catch( IOException e ) {}
    }
  }
 
  

}







