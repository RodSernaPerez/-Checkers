package agents;
import java.util.*;
import java.lang.Math;
import gameElements.*;
import constants.Constants;
public class DummyPlayer implements Player 
{
    int pointsfinal=100;
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

    //This plays minimax without alphabeta and with a fixed depth
    public GameState play(final GameState pState, final Deadline pDue) {

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(pState, new Move());
        }

 
        GameState gs=minimax(pState,5);
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

    
    int minimax(GameState pState, int depth,int it,int player)
    {

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
            
            //System.out.println("Final de partida en nivel :"+it+ "de "+valorfin);
        }

        //If it is a draw
        if(pState.getMovesUntilDraw()==0)
            return 0;


        if(it!=depth)
        {

            int value;
             
            if(it%2==1)//MIN
            {
                value=infinite;//infinite
                for (GameState g : lNextStates) 
                {
                    int newvalue=minimax(g,depth,it+1,player);

                    if(newvalue<value)
                        value=newvalue;
                }
            }
            else //MAX
            {
                value=-infinite;//- inifinite
                for (GameState g : lNextStates ) 
                {
                    int newvalue=minimax(g,depth,it+1,player);

                    if(newvalue>value)
                        value=newvalue;
                }
            }  
            return value;         
        }
        else
        {
            int value= heuristic(pState,player);
           
            return value;
        }
    }


    //Level 0
    GameState minimax(GameState pState,int depth)
    {
        //Gets the possible moves
        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
        
        //Gets the colour of the agent
        int player=pState.getNextPlayer();

        //heuristic(pState,player);

        //Starts the minimax algorithm
        GameState nextState=new GameState();
        int value=-infinite;//- infinite

        for (GameState gs : lNextStates ) 
        {
           // ArrayList lista=new ArrayList();
            int newvalue=minimax(gs,depth,1,player);
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
