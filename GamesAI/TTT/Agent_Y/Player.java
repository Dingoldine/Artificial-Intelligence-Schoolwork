import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    private int initial_depth = 3;
    private int player = Constants.CELL_O;
    private int opponent = player = Constants.CELL_X;







    public GameState play(final GameState gameState, final Deadline deadline) {

        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);


        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

      Random random = new Random();

      return nextStates.elementAt(random.nextInt(nextStates.size()));
       //return bestMove(gameState, nextStates);

    }


private GameState bestMove(GameState state, Vector<GameState> moves){


    GameState best_state = moves.get(0);


    int x = minimax(moves.get(0), initial_depth, Integer.MIN_VALUE, Integer.MAX_VALUE,  false);
    
    for (int i = 0; i < moves.size(); i++) {

        int y = minimax(moves.get(i), initial_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        

        if (y > x){
            best_state = moves.get(i);
        }
        

        
    }


    return best_state;

}


    int scoreMultiplier(int x, int player){
        int[] multiplier = {0, 10, 20, 50, 1000};
        int score = 0;
        if (player == Constants.CELL_X) score = multiplier[x];
        if (player == Constants.CELL_O) score = score - multiplier[x];
        return score;
    }



    int countDiag2(GameState state, int player) {
        int counter=0;
        for ( int i = 0 ; i < state.BOARD_SIZE ; i++ ) {
            if (state.at(i, 3-i) == player) counter++;
            
        }
        return scoreMultiplier(counter, player);
    }

    int countDiag1(GameState state, int player){
        int counter=0;
        for ( int i = 0 ; i < state.BOARD_SIZE ; i++ ) {
            if ( state.at(i,i) == player ) counter++;
            
        }
        return scoreMultiplier(counter, player);
    }

    int countRow(GameState state, int row, int player){
        int counter = 0;
        for (int col = 0; col < state.BOARD_SIZE; col++){
            if (state.at(row ,col) == player) counter++;
        }
        return scoreMultiplier(counter, player);
    }

    int countColoumn(GameState state, int column, int player) {
        int counter=0;
        for (int row = 0; row < state.BOARD_SIZE ; row++ ) {
            if (state.at(row, column) == player) counter++;
    
        }
        return scoreMultiplier(counter, player);
    }


int heuristicFunction(GameState state){
       
/*    winningCominations
    possibleWins = 10;
    {0, 1, 2, 3}, 
    {4, 5, 6, 7},
    {8, 9, 10, 11},
    {12, 13, 14, 15},
    {0, 4, 8, 12},
    {1, 5, 9, 13}, 
    {2, 6, 10, 14}, 
    {3, 7, 11, 15},
    {0, 5, 10, 15}, 
    {3, 6, 9, 12}
    };*/

    int score = 0;
    int [] weight = {5, 2, 2, 5, 2, 10 ,10 ,2, 2, 10 ,10 ,2, 5, 2, 2, 5};

    if (state.isXWin()) return 2000;
    if (state.isOWin()) return 0 -2000;



    for (int i = 0; i <16 ;i++ ) {
        if (state.at(i) == Constants.CELL_X) score += weight[i]; 
        if (state.at(i) == Constants.CELL_O) score -= weight[i]; 
    }

    

    for (int i = 0; i < 4;i++) {
        score += countRow(state, i, Constants.CELL_X);
        score += countRow(state, i, Constants.CELL_O);
        score += countColoumn(state, i, Constants.CELL_O);
        score += countColoumn(state, i, Constants.CELL_X);    
    }
    score += countDiag1(state,  Constants.CELL_X);
    score += countDiag2(state, Constants.CELL_X);
    score += countDiag1(state, Constants.CELL_O);
    score += countDiag2(state, Constants.CELL_O);





    return score;


    //Scoreing of non terminal states
}




int minimax(GameState state, int depth, int alpha, int beta, boolean maximizingPlayer){

    Vector<GameState> nextStates = new Vector<GameState>();
    state.findPossibleMoves(nextStates);    
    
    //Termination
    if (depth == 0 || nextStates.size() == 0){
        return heuristicFunction(state); //the heuristic value of state 
        }


    if (maximizingPlayer){  
         int bestValue = Integer.MIN_VALUE;
         for (int i = 0; i < nextStates.size(); i++){
            GameState child_state = nextStates.get(i);
            int val = minimax(child_state, depth - 1, alpha, beta, false);
            bestValue = Math.max(bestValue, val);
            alpha = Math.max(alpha, bestValue);
            if (alpha >= beta) return alpha;
            }

        return bestValue;
    }

     else   
        {
         int bestValue = Integer.MAX_VALUE;
         for (int i = 0; i < nextStates.size(); i++){
            GameState child_state = nextStates.get(i);
            int val = minimax(child_state, depth - 1, alpha, beta,  true);
            bestValue = Math.min(bestValue, val);
            beta = Math.min(bestValue, beta);
            if(beta <= alpha) return beta;
            }

         return bestValue;
         }
}





}

