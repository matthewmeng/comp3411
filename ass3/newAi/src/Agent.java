

/*********************************************
 *  Agent.java 
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411/9414/9814 Artificial Intelligence
 *  UNSW Session 1, 2018
*/

import java.util.*;
import java.io.*;
import java.net.*;


public class Agent {
  private int i = 0;
  private Strategy strategy = new Strategy();
  public char get_action( char view[][] ) {

	    
	  // REPLACE THIS CODE WITH AI TO CHOOSE ACTION
	  
	  i++;
	  System.out.println("i= "+i);
	 
		
	  int ch=0;
	  char action = 0;

	    System.out.println("Enter Action(s): ");
	    try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	  print_view(view);
	  this.strategy.updateView(view);
	  action = this.strategy.getAction();
	  return action;
	    
//	        while ( ch != -1 ) {
//
//	          switch( action ) { // if character is a valid action, return it
//	           case 'F': case 'L': case 'R': case 'C': case 'U':
//	           case 'f': case 'l': case 'r': case 'c': case 'u':
//	             return((char) ch );
//	          }
//	        }
//
//
//	    return 0;
}

//  public static VisitedView getVisitedview() {
//	return Explore.visitedview;
//}
  

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
      	//if(agent.strategy.explore.getGoback() == 0) {
      	agent.perform(action);
      	//}
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
 
  
  

void perform(char action) {
	// TODO Auto-generated method stub
	this.strategy.visitedview.perform(action);
}
}




