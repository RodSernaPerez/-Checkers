import java.util.*;
import java.lang.Math;



public class Player4 {

    int pointsfinal=1000;
    int infinite=10000;
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    //This plays minimax,alpha-beta and a variable depth depending on the number of pieces 
    public GameState play(final GameState pState, final Deadline pDue) {

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(pState, new Move());
        }

        int number_pieces=0;
        // Counts pieces and queens
        for (int i = 0; i < 32; i++) {
            if (0 != (pState.get(i) & Constants.CELL_RED))
            {
                number_pieces++;
            }
            else if (0 != (pState.get(i) & Constants.CELL_WHITE))
            {
                number_pieces++;
            } 
        }
        
        int depth;
        if (number_pieces<4)
            depth=12;
        else
            depth=8;


        GameState gs=minimaxab(pState,depth);
        
        return gs;
    
    }

    
    int heuristic(GameState pState, int player)
    {        
        int red_pieces = 0;
        int white_pieces = 0;
        int red_queens = 0;
        int white_queens = 0;

        //int froww=0,frowr=0; //Pieces that defend the first/last line
        int borderpw=0,borderpr=0; //Pieces in the borders

        // Counts pieces and queens
        for (int i = 0; i < 32; i++) {
            if (0 != (pState.get(i) & Constants.CELL_RED))
            {
                if(i==11||i==19||i==27||i==4||i==12||i==20)
                    borderpr++;
                // if(i>=0 && i<=3)
                //     frowr++;
                if(0 != (pState.get(i) & Constants.CELL_KING))
                    red_queens++;
                else
                    red_pieces++;
            }
            else if (0 != (pState.get(i) & Constants.CELL_WHITE))
            {
                if(i==11||i==19||i==27||i==4||i==12||i==20)
                    borderpw++;
                // if(i>=28 && i<=31)
                //     froww++;
                if(0 != (pState.get(i) & Constants.CELL_KING))
                    white_queens++;
                else
                    white_pieces++;
            }
          
        }
        
              


        int distw=0,distr=0;
        for(int i=0;i<7;i++)
        {
            for(int j=0;j<7;j++)
            {
                if(pState.get(i,j)=='r')
                    distr=distr+7-i;
                else if (pState.get(i,j)=='w')
                    distw=distw+i;
            }
        }   

        int a=20;
        int b=10;
        int c=1;
        int d=5;
        int e=5;
        
        //int valor= a*(red_queens-white_queens)+b*(red_pieces-white_pieces)+c*(distr-distw)+d*(frowr-froww)+e*(borderpr-borderpw);
        int valor= a*(red_queens-white_queens)+b*(red_pieces-white_pieces)+c*(distr-distw)+e*(borderpr-borderpw);

        if(player==1)//red player
        {
            if(red_pieces+red_queens==0)
                return -pointsfinal;
            if(white_pieces+white_queens==0)
                return pointsfinal;
            return valor;
        }
        else//white player
        {
            if(red_pieces+red_queens==0)
                return pointsfinal;
            if(white_pieces+white_queens==0)
                return -pointsfinal;
            
           return -valor;
        }
    }
    
    int minimaxab(GameState pState, int depth,int it,int player,int prevalpha,int prevbeta)
    {
        int alpha=new Integer(prevalpha);
        int beta=new Integer(prevbeta);

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        

        if(pState.getMove().isRedWin())
        {
            if(player==1)
                return pointsfinal;
            else
                return -pointsfinal;
        }else if(pState.getMove().isWhiteWin())
        {
            if(player==2)
                return pointsfinal;
            else
                return -pointsfinal;
        }

        
        //If it is no possible to make more moves
        if(lNextStates.isEmpty())
        {
            if(it%2==1)//Min
                return pointsfinal;
            else //Max
                return -pointsfinal;
        }
            
            

        //If it is a draw
        if(pState.getMovesUntilDraw()==0)
            return 0;


        if(it!=depth)
        {

            
            if(it%2==1)//MIN
            {
                
                for (GameState g : lNextStates) 
                {
                    int value=minimaxab(g,depth,it+1,player,alpha,beta);
                    beta=Math.min(beta,value);
                    if(beta<=alpha)
                        break; 
                }
                return beta;
            }
            else //MAX
            {
               
                for (GameState g : lNextStates ) 
                {
                    int value=minimaxab(g,depth,it+1,player,alpha,beta);
                    alpha=Math.max(alpha,value);
                    if(beta<=alpha)
                        break;   
                }

                return alpha;
            }  
        
        }
        else
        {
            int value= heuristic(pState,player);
           
            return value;
        }
    }

    GameState minimaxab(GameState pState,int depth)
    {
        //Gets the possible moves
        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
        
        //Gets the colour of the agent
        int player=pState.getNextPlayer();



        //Starts the minimax algorithm
        GameState nextState=new GameState();
        int value=-infinite;//- infinite

        int alpha=-infinite;
        int beta=infinite;
        for (GameState gs : lNextStates ) 
        {
            int newvalue=minimaxab(gs,depth,1,player,alpha,beta);
            alpha=Math.max(alpha,newvalue);

            if(newvalue>value) 
            {
                value=newvalue;
                nextState=gs;
            }
        }

        //Returns the new state
        return nextState;
    }
}
