//package ttt3d;

import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     * the current state of the board
     * @param deadline
     * time before which we must have returned
     * @return the next state the board is in after our move
     */
    public static final int[][] testLine = new int[][]{{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11},
            {12, 13, 14, 15}};

    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);            // find all possible move gamestate
        //System.out.println(Arrays.toString(nextStates));

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        int player = Constants.CELL_O;
        int maxDepth = 2;                               // try different value

        Node<Integer> alpha = new Node<Integer>(Integer.MIN_VALUE);
        Node<Integer> beta = new Node<Integer>(Integer.MAX_VALUE);

        Node<Integer> parent = new Node<Integer>(gameState);      // initilize the parent if nessasary
        Node<Integer> best_child = minimax_pruning(player, maxDepth, parent, alpha, beta);
        GameState game = gameState;
        while (best_child.getParent() != null) {
            game = best_child.getState();
            best_child = best_child.getParent();
        }
        Random random = new Random();
        //return nextStates.elementAt(random.nextInt(nextStates.size()));
        return game;


      

      

        
        //return nextStates.elementAt(2);

    }

    // Player A, me and player B, opponent
    // utility function: given a player and a state says how "useful" the state is for the player.
    // U, micro, given a player and current state, it produce all possible moves(subset) for the player.

    //Constants.CELL_X || Constants.CELL_O
    // the node is the parent

    /***
     *
     * @param state
     * @param player
     * @param maxDepth
     * @param nod
     * @return return best childnode with highest value
     */

    public Node<Integer> minimax_pruning(int player, int maxDepth, Node<Integer> node, Node<Integer> alpha, Node<Integer> beta) {
        // state : the current state we are analyzing
        // player : the current player
        // returns a heuristic value that approximates a utility function of the state
        //if legal_moves(state, player) == null

        int player_A = Constants.CELL_X;
        int player_B = Constants.CELL_O;
        int maxdepth = maxDepth - 1;


        Vector<GameState> nextStates = new Vector<GameState>();
        GameState state = node.getState();
        state.findPossibleMoves(nextStates);
        Node<Integer> currentNode = node;


        if (nextStates.size() == 0 || maxdepth == 0) {
            int leaf = heuristic(state, player);        // kan lägga player som parameter i myMark funktion
            currentNode.setData(leaf);          // lägg in värde i sista noden
            return currentNode;                 // return the node with value
        } else {
            if (player == player_A) {

                Node<Integer> v = new Node<Integer>(Integer.MIN_VALUE);
                int index = 0;

                for (GameState child : nextStates) {
                    currentNode.addChild(child);                            // add the child and it add the parent to the child in the same time
                    Node<Integer> childNode = currentNode.getChildren().get(index);
                    index++;
                    Node<Integer> alphabeta = minimax_pruning(player_B, maxdepth, childNode, alpha, beta);
                    // v = max(v, alphabeta())
                    if (v.getData() > alphabeta.getData()) {              // TODO: fix the equal
                        v = v;
                    } else {
                        v = alphabeta;
                    }
                    // alpha = max(alpha, v)
                    if (alpha.getData() > v.getData()) {
                        alpha = alpha;
                    } else {
                        alpha = v;
                    }
                    if (beta.getData() <= alpha.getData()) {
                        break;
                    }   // beta prune
                }
                return v;
            } else {

                Node<Integer> v = new Node<Integer>(Integer.MAX_VALUE);
                int index = 0;
                for (GameState child : nextStates) {
                    currentNode.addChild(child);                          // add the child and it add the parent to the child in the same time
                    Node<Integer> childNode = currentNode.getChildren().get(index);
                    index++;
                    Node<Integer> alphabeta = minimax_pruning(player_A, maxdepth, childNode, alpha, beta);
                    // v = min(v, alphabeta())
                    if (v.getData() < alphabeta.getData()) {              // TODO: fix the equal
                        v = v;
                    } else {
                        v = alphabeta;
                    }
                    // alpha = min(alpha, v)
                    if (beta.getData() < v.getData()) {
                        beta = beta;
                    } else {
                        beta = v;
                    }
                    if (beta.getData() <= alpha.getData()) {
                        break;
                    } // alpha pruning
                }
                return v;
            }
        }
    }


    public int heuristic(GameState state, int player) { //OBS: DENNA HAR JAG BÖRJAT MED TODO: 3dDiagonaler behövs fixas
        int player1_mark = Constants.CELL_X;
        int player2_mark = Constants.CELL_O;
        int empty_cell = Constants.CELL_EMPTY;

        int[][][] board = new int[4][4][4];

        int[] tempSpecial4_1 = new int[4];
        int[] tempSpecial4_2 = new int[4];
        int[] tempSpecial4_3 = new int[4];
        int[] tempSpecial4_4 = new int[4];
        int tempindex = 3;
        // row 0-3 är alla layers kanter, row 4-7 är alla columns kanter, row 8-11 är alla raders kanter
        int[][] kanter12_14 = new int[12][4];
        int[][] diagnal12_14 = new int[12][4];
        int[][] mitten12_23 = new int[12][4];
        int[][] diagnal12_23 = new int[12][4];
        int[][] leastWins24 = new int[24][4];

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                for (int layer = 0; layer < board[0][0].length; layer++) {
                    // fill in states of the board
                    if (state.at(row, col, layer) == player1_mark) {
                        board[row][col][layer] = player1_mark;
                    } else if (state.at(row, col, layer) == player2_mark) {
                        board[row][col][layer] = player2_mark;
                    } else {
                        board[row][col][layer] = empty_cell;
                    }

                    kanter12_14[0][layer] = state.at(0, 0, layer);
                    kanter12_14[1][layer] = state.at(3, 0, layer);
                    kanter12_14[2][layer] = state.at(0, 3, layer);
                    kanter12_14[3][layer] = state.at(3, 3, layer);

                    diagnal12_14[0][layer] = state.at(layer, layer, 0);
                    diagnal12_14[1][layer] = state.at(3 - layer, layer, 0);
                    diagnal12_14[2][layer] = state.at(layer, layer, 3);
                    diagnal12_14[3][layer] = state.at(3 - layer, layer, 3);

                    mitten12_23[0][layer] = state.at(1, 1, layer);
                    mitten12_23[1][layer] = state.at(1, 2, layer);
                    mitten12_23[2][layer] = state.at(2, 1, layer);
                    mitten12_23[3][layer] = state.at(2, 2, layer);

                    diagnal12_23[0][layer] = state.at(layer, layer, 1);
                    diagnal12_23[1][layer] = state.at(layer, layer, 2);
                    diagnal12_23[2][layer] = state.at(3 - layer, layer, 1);
                    diagnal12_23[3][layer] = state.at(3 - layer, layer, 1);
                }
                kanter12_14[4][col] = state.at(0, col, 0);
                kanter12_14[5][col] = state.at(3, col, 0);
                kanter12_14[6][col] = state.at(0, col, 3);
                kanter12_14[7][col] = state.at(3, col, 3);

                diagnal12_14[4][col] = state.at(col, 0, col);
                diagnal12_14[5][col] = state.at(col, 0, 3 - col);
                diagnal12_14[6][col] = state.at(col, 3, col);
                diagnal12_14[7][col] = state.at(col, 3, 3 - col);

                mitten12_23[4][col] = state.at(1, col, 1);
                mitten12_23[5][col] = state.at(1, col, 2);
                mitten12_23[6][col] = state.at(2, col, 1);
                mitten12_23[7][col] = state.at(2, col, 2);

                diagnal12_23[4][col] = state.at(col, 2, col);
                diagnal12_23[5][col] = state.at(col, 3, col);
                diagnal12_23[6][col] = state.at(col, 2, 3 - col);
                diagnal12_23[7][col] = state.at(col, 3, 3 - col);

            }
            kanter12_14[8][row] = state.at(row, 0, 0);
            kanter12_14[9][row] = state.at(row, 0, 3);
            kanter12_14[10][row] = state.at(row, 3, 0);
            kanter12_14[11][row] = state.at(row, 3, 3);

            diagnal12_14[8][row] = state.at(0, row, 3 - row);
            diagnal12_14[9][row] = state.at(0, row, row);
            diagnal12_14[10][row] = state.at(3, row, 3 - row);
            diagnal12_14[11][row] = state.at(3, row, row);

            mitten12_23[8][row] = state.at(row, 1, 1);
            mitten12_23[9][row] = state.at(row, 1, 2);
            mitten12_23[10][row] = state.at(row, 2, 1);
            mitten12_23[11][row] = state.at(row, 2, 2);

            diagnal12_23[8][row] = state.at(2, row, row);
            diagnal12_23[9][row] = state.at(3, row, row);
            diagnal12_23[10][row] = state.at(2, row, 3 - row);
            diagnal12_23[11][row] = state.at(3, row, 3 - row);
            // the states of the special lines
            tempSpecial4_1[row] = board[row][row][row];
            tempSpecial4_2[row] = board[row][row][tempindex - row];
            tempSpecial4_3[row] = state.at(tempindex - row, row, row);
            tempSpecial4_4[row] = state.at(tempindex - row, row, tempindex - row);
        }


        for (int j = 0; j < board.length; j++) {
            leastWins24[0][j] = state.at(0, 2, j);
            leastWins24[1][j] = state.at(0, 3, j);
            leastWins24[2][j] = state.at(3, 2, j);
            leastWins24[3][j] = state.at(3, 3, j);
            leastWins24[4][j] = state.at(j, 2, 0);
            leastWins24[5][j] = state.at(j, 3, 0);
            leastWins24[6][j] = state.at(j, 2, 3);
            leastWins24[7][j] = state.at(j, 3, 3);

            leastWins24[8][j] = state.at(2, j, 0);
            leastWins24[9][j] = state.at(3, j, 0);
            leastWins24[10][j] = state.at(2, j, 3);
            leastWins24[11][j] = state.at(3, j, 3);
            leastWins24[12][j] = state.at(2, 0, j);
            leastWins24[13][j] = state.at(3, 0, j);
            leastWins24[14][j] = state.at(2, 3, j);
            leastWins24[15][j] = state.at(3, 3, j);

            leastWins24[16][j] = state.at(0, j, 2);
            leastWins24[17][j] = state.at(0, j, 3);
            leastWins24[18][j] = state.at(3, j, 2);
            leastWins24[19][j] = state.at(3, j, 3);
            leastWins24[20][j] = state.at(j, 0, 2);
            leastWins24[21][j] = state.at(j, 0, 3);
            leastWins24[22][j] = state.at(j, 3, 2);
            leastWins24[23][j] = state.at(j, 3, 3);
        }


        int totalScore = 0;
        totalScore += pointSpecial4(tempSpecial4_1);
        totalScore += pointSpecial4(tempSpecial4_2);
        totalScore += pointSpecial4(tempSpecial4_3);
        totalScore += pointSpecial4(tempSpecial4_4);

        for (int i = 0; i < 12; i++) {
            totalScore += point14(kanter12_14[i]);
            totalScore += point14(diagnal12_14[i]);
            totalScore += point23(mitten12_23[i]);
            totalScore += point23(diagnal12_23[i]);
        }

        for (int j = 0; j < 24; j++) {
            totalScore += pointPlainLine(leastWins24[j]);
        }

        return totalScore;
    }

    public int point14(int[] line) {
        int player1_mark = Constants.CELL_X;
        int player2_mark = Constants.CELL_O;
        int empty_cell = Constants.CELL_EMPTY;
        int winPoint = 0;
        int losePoint = 0;
        int empty = 0;
        int score = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == player1_mark) {
                winPoint += 1;
            } else if (line[i] == player2_mark) {
                losePoint += 1;
            } else if (line[i] == empty_cell) {
                empty += 1;
            }
        }
        if (winPoint == 4) {
            score += 1400;
        } else if (winPoint == 3 && empty == 1 && line[0] == player1_mark && line[3] == player1_mark) {
            score += 200;
        } else if (winPoint == 3 && empty == 1 && line[0] == player1_mark && line[3] != player1_mark) {
            score += 100;
        } else if (winPoint == 3 && empty == 1 && line[3] == player1_mark && line[0] != player1_mark) {
            score += 100;
        } else if (winPoint == 2 && empty == 2 && line[0] == player1_mark && line[3] == player1_mark) {
            score += 50;
        } else if (winPoint == 2 && empty == 2 && line[0] != player1_mark && line[3] == player1_mark) {
            score += 25;
        } else if (winPoint == 2 && empty == 2 && line[0] == player1_mark && line[3] != player1_mark) {
            score += 25;
        } else if (winPoint == 2 && empty == 2 && line[0] != player1_mark && line[3] != player1_mark) {
            score += 20;
        } else if (winPoint == 1 && empty == 3 && ((line[0] == player1_mark) || (line[3] == player1_mark))) {
            score += 10;
        } else if (winPoint == 1 && empty == 3 && !((line[0] == player1_mark) || (line[3] == player1_mark))) {
            score += 5;
        }

        if (losePoint == 4) {
            score -= 1400;
        } else if (losePoint == 3 && empty == 1 && line[0] == player2_mark && line[3] == player2_mark) {
            score -= 200;
        } else if (losePoint == 3 && empty == 1 && line[0] == player2_mark && line[3] != player2_mark) {
            score -= 100;
        } else if (losePoint == 3 && empty == 1 && line[3] == player2_mark && line[0] != player2_mark) {
            score -= 100;
        } else if (losePoint == 2 && empty == 2 && line[0] == player2_mark && line[3] == player2_mark) {
            score -= 50;
        } else if (losePoint == 2 && empty == 2 && line[0] != player2_mark && line[3] == player2_mark) {
            score -= 25;
        } else if (losePoint == 2 && empty == 2 && line[0] == player2_mark && line[3] != player2_mark) {
            score -= 25;
        } else if (losePoint == 2 && empty == 2 && line[0] != player2_mark && line[3] != player2_mark) {
            score -= 20;
        } else if (losePoint == 1 && empty == 3 && ((line[0] == player2_mark) || (line[3] == player2_mark))) {
            score -= 10;
        } else if (losePoint == 1 && empty == 3 && !((line[0] == player2_mark) || (line[3] == player2_mark))) {
            score -= 5;
        }
        return score;
    }

    public int point23(int[] line) {
        int player1_mark = Constants.CELL_X;
        int player2_mark = Constants.CELL_O;
        int empty_cell = Constants.CELL_EMPTY;
        int winPoint = 0;
        int losePoint = 0;
        int empty = 0;
        int score = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == player1_mark) {
                winPoint += 1;
            } else if (line[i] == player2_mark) {
                losePoint += 1;
            } else if (line[i] == empty_cell) {
                empty += 1;
            }
        }
        if (winPoint == 4) {
            score += 1400;
        } else if (winPoint == 3 && empty == 1 && line[1] == player1_mark && line[2] == player1_mark) {
            score += 200;
        } else if (winPoint == 3 && empty == 1 && line[1] == player1_mark && line[2] != player1_mark) {
            score += 100;
        } else if (winPoint == 3 && empty == 1 && line[2] == player1_mark && line[1] != player1_mark) {
            score += 100;
        } else if (winPoint == 2 && empty == 2 && line[1] == player1_mark && line[2] == player1_mark) {
            score += 50;
        } else if (winPoint == 2 && empty == 2 && line[1] != player1_mark && line[2] == player1_mark) {
            score += 25;
        } else if (winPoint == 2 && empty == 2 && line[1] == player1_mark && line[2] != player1_mark) {
            score += 25;
        } else if (winPoint == 2 && empty == 2 && line[1] != player1_mark && line[2] != player1_mark) {
            score += 20;
        } else if (winPoint == 1 && empty == 3 && ((line[1] == player1_mark) || (line[2] == player1_mark))) {
            score += 10;
        } else if (winPoint == 1 && empty == 3 && !((line[1] == player1_mark) || (line[2] == player1_mark))) {
            score += 5;
        }

        if (losePoint == 4) {
            score -= 1400;
        } else if (losePoint == 3 && empty == 1 && line[1] == player2_mark && line[2] == player2_mark) {
            score -= 200;
        } else if (losePoint == 3 && empty == 1 && line[1] == player2_mark && line[2] != player2_mark) {
            score -= 100;
        } else if (losePoint == 3 && empty == 1 && line[2] == player2_mark && line[1] != player2_mark) {
            score -= 100;
        } else if (losePoint == 2 && empty == 2 && line[1] == player2_mark && line[2] == player2_mark) {
            score -= 50;
        } else if (losePoint == 2 && empty == 2 && line[1] != player2_mark && line[2] == player2_mark) {
            score -= 25;
        } else if (losePoint == 2 && empty == 2 && line[1] == player2_mark && line[2] != player2_mark) {
            score -= 25;
        } else if (losePoint == 2 && empty == 2 && line[1] != player2_mark && line[2] != player2_mark) {
            score -= 20;
        } else if (losePoint == 1 && empty == 3 && ((line[1] == player2_mark) || (line[2] == player2_mark))) {
            score -= 10;
        } else if (losePoint == 1 && empty == 3 && !((line[1] == player2_mark) || (line[2] == player2_mark))) {
            score -= 5;
        }
        return score;
    }

    public int pointPlainLine(int[] line) {
        int player1_mark = Constants.CELL_X;
        int player2_mark = Constants.CELL_O;
        int empty_cell = Constants.CELL_EMPTY;
        int winPoint = 0;
        int losePoint = 0;
        int empty = 0;
        int score = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == player1_mark) {
                winPoint += 1;
            } else if (line[i] == player2_mark) {
                losePoint += 1;
            } else if (line[i] == empty_cell) {
                empty += 1;
            }
        }

        if (winPoint == 4) {
            score += 1000;
        } else if (winPoint == 3 && empty == 1) {
            score += 75;
        } else if (winPoint == 2 && empty == 2) {
            score += 15;
        } else if (winPoint == 1 && empty == 3) {
            score += 4;
        }

        if (losePoint == 4) {
            score -= 1000;
        } else if (losePoint == 3 && empty == 1) {
            score -= 75;
        } else if (losePoint == 2 && empty == 2) {
            score -= 15;
        } else if (losePoint == 1 && empty == 3) {
            score -= 4;
        }

        return score;

    }

    public int pointSpecial4(int[] line) {
        int player1_mark = Constants.CELL_X;
        int player2_mark = Constants.CELL_O;
        int empty_cell = Constants.CELL_EMPTY;
        int winPoint = 0;
        int losePoint = 0;
        int empty = 0;
        int score = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == player1_mark) {
                winPoint += 1;
            } else if (line[i] == player2_mark) {
                losePoint += 1;
            } else if (line[i] == empty_cell) {
                empty += 1;
            }
        }

        if (winPoint == 4) {
            score += 2000;
        } else if (winPoint == 3 && empty == 1) {
            score += 800;
        } else if (winPoint == 2 && empty == 2) {
            score += 90;
        } else if (winPoint == 1 && empty == 3) {
            score += 19;
        }

        if (losePoint == 4) {
            score -= 2000;
        } else if (losePoint == 3 && empty == 1) {
            score -= 800;
        } else if (losePoint == 2 && empty == 2) {
            score -= 90;
        } else if (losePoint == 1 && empty == 3) {
            score -= 19;
        }

        return score;
    }

}


