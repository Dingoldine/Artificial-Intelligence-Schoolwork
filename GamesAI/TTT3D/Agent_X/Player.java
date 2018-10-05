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

    private int initial_depth = 0;
    private int player = Constants.CELL_X;
    private int opponent = Constants.CELL_O;
    private int won = 10000;
    private int lost = 0 - won;



    public GameState play(final GameState gameState, final Deadline deadline) {

        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);



        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }




       return bestMove(gameState, nextStates);

    }


private GameState bestMove(GameState state, Vector<GameState> moves){


    GameState best_state = moves.get(0);
    


    int x = minimax(moves.get(0), initial_depth, Integer.MIN_VALUE, Integer.MAX_VALUE,  false);
    
    for (int i = 1; i < moves.size(); i++) {
      if (moves.get(i).isXWin()) return moves.get(i);
        int y = minimax(moves.get(i), initial_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        if (y > x){
            best_state = moves.get(i);
        }
        
    }


    return best_state;

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




int heuristicFunction(GameState state){
       
    

    if (state.isXWin()) return won;
    else if (state.isOWin()) return lost;

    else{
      int score = 0;

      //score = positions(state);

      //cross layer counting on rows
/*      for (int row = 0; row < state.BOARD_SIZE; row++) {
        for( int col = 0; col < state.BOARD_SIZE; col++){

        score += crossLayerRowCount(state, row, col, player);
        score += crossLayerRowCount(state, row, col, opponent);
        }
      }*/


      for (int i = 0; i < state.BOARD_SIZE;i++) {
  
        for (int j = 0; j < state.BOARD_SIZE;j++) {

          score += countRow(state, i,j, player);
          score += countRow(state,i,j, opponent);

          score += countColoumn(state,i,j, player);
          score += countColoumn(state,i,j, opponent);    
      }

/*      score += countDiag1(state, i,  player);
      score += countDiag2(state, i,  player);


      score += countDiag1(state, i,  opponent);
      score += countDiag2(state, i,  opponent);*/
    }

/*   //Main Diagonal 1
    score += cubeDiagonalOne(state, player);
    score += cubeDiagonalOne(state,  opponent);

    //Main Diagonal 2
    score += cubeDiagonalTwo(state, player);
    score += cubeDiagonalTwo(state,  opponent);


    //Main Diagonal 3
    score += cubeDiagonalThree(state, player);
    score += cubeDiagonalThree(state,  opponent);


    //Main Diagonal 4
    score += cubeDiagonalFour(state, player);
    score += cubeDiagonalFour(state,  opponent);*/



      return score;


    }







}

    int countRow(GameState state,int z, int x, int plr){
        int counter = 0;
        boolean rowBlocked = false;
        for (int y = 0; y < state.BOARD_SIZE; y++){
            if (state.at(x ,y, z) == plr)   counter++;
            else if (state.at(x ,y, z) == Constants.CELL_EMPTY) continue;
            else rowBlocked = true;
        }
         return scoreMultiplier(counter, plr, rowBlocked);
    }

    int countColoumn(GameState state, int x,int y, int plr) {
        int counter=0;
        boolean rowBlocked = false;
        for (int z = 0; z < state.BOARD_SIZE ; z++ ) {
            if (state.at(x,y,z) == plr) counter++;
            else if (state.at(x ,y, z) == Constants.CELL_EMPTY) continue;
            else rowBlocked = true;
        }

         return scoreMultiplier(counter, plr, rowBlocked);
    } 

  int cubeDiagonalOne(GameState state,  int plr){
    int counter=0;
    boolean rowBlocked = false;
    for (int i = 0; i< state.BOARD_SIZE; i++) {
       if ( state.at(i,i, i) == plr ) counter++;
       else if (state.at(i,i, i) == Constants.CELL_EMPTY) continue;
        else rowBlocked = true;
      
    }
    return scoreMultiplier(counter, plr, rowBlocked);


}
  int cubeDiagonalTwo(GameState state,  int plr){
     int counter=0;
     boolean rowBlocked = false;
    for (int i = 0; i< state.BOARD_SIZE; i++) {
      if (state.at(i, 3 - i , i) == plr) counter++;
      else if (state.at(i, 3 - i , i) == Constants.CELL_EMPTY) continue;
      else rowBlocked = true;
      
    }
    return scoreMultiplier(counter, plr, rowBlocked);
}

  int cubeDiagonalThree(GameState state,  int plr){
     int counter=0;
     boolean rowBlocked = false;
    for (int i = 0; i< state.BOARD_SIZE; i++) {
      if (state.at(3-i, i , i) == plr) counter++;
      else if (state.at(3-i, i , i) == Constants.CELL_EMPTY) continue;
      else rowBlocked = true;
      
    }
    return scoreMultiplier(counter, plr, rowBlocked);

}
  int cubeDiagonalFour(GameState state,  int plr){
     int counter=0;
     boolean rowBlocked = false;
    for (int i = 0; i< state.BOARD_SIZE; i++) {
      if (state.at(3-i, 3-i , i) == plr) counter++;
      else if (state.at(3-i, 3-i , i) == Constants.CELL_EMPTY) continue;
      else rowBlocked = true;
      
    }
    return scoreMultiplier(counter, plr, rowBlocked);

}

    int crossLayerRowCount(GameState state, int row, int col, int plr){
       int counter=0;
       for (int layer = 0; layer < state.BOARD_SIZE; layer++) {
        if (state.at(row, col, layer) == plr) counter++;
       }
       return scoreMultiplier(counter, plr);


    }



    int countDiag2(GameState state, int layer, int plr) {
        int counter=0;
        for ( int i = 0 ; i < state.BOARD_SIZE ; i++ ) {
            if (state.at(i, 3-i, layer) == plr) counter++;
            
        }
        return scoreMultiplier(counter, plr);
    }

    int countDiag1(GameState state, int layer,  int plr){
        int counter=0;
        for ( int i = 0 ; i < state.BOARD_SIZE ; i++ ) {
            if ( state.at(i,i,layer) == plr ) counter++;
            
        }
        return scoreMultiplier(counter, plr);
    }


    int[] multiplier = {0, 10, 20, 50, won};
    int scoreMultiplier(int x, int plr, boolean interference){
        int score = 0;
        if (plr == player) score = multiplier[x];
        if (plr == opponent) score = score - multiplier[x];
        if (x == 3 && interference && plr == player) return 10;
        if (x == 3 && interference && plr == opponent) return 0 - 10;
        return score;
    }



    int scoreMultiplier(int x, int plr){
        int[] multiplier = {0, 10, 20, 50, won};
        int score = 0;
        if (plr == player) score = multiplier[x];
        if (plr == opponent) score = score - multiplier[x];
        return score;
    }


    int [] weight  = {2, 0, 0, 2, 0, 0 ,0 ,0, 0, 0 ,0 ,0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2 ,2 ,0, 0, 2 ,2 ,0, 0, 0, 0, 0,
                      0, 0, 0, 0, 0, 2 ,2 ,0, 0, 2 ,2 ,0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0 ,0 ,0, 0, 0 ,0 ,0, 2, 0, 0, 2};
    int positions(GameState state){

      int eval = 0;

    //förstörs med denhär
      for (int i = 0; i < state.CELL_COUNT; i++) {
        if (state.at(i) == Constants.CELL_X) eval += weight[i]; 
        if (state.at(i) == Constants.CELL_O) eval -= weight[i];
  
    }

    return eval;

    }






}

