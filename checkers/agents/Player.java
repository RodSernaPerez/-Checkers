package agents;
import java.util.*;
import java.lang.Math;
import gameElements.*;
public interface Player {
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    public GameState play(final GameState pState, final Deadline pDue);

    
}
