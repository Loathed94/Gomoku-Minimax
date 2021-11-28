

/**
 * The Evaluate class is used to calculate a value for a board of Gomoku and the current placement of player and computer markers. The better the situation is for the computer the higher the score, the better the situation is for the human player the lower the score will be.
 *
 * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
 */
public class Evaluate {


    private char[][] board;
    private final int SIZE;
    private final int NONE = 0;
    private final char MAX_STONE = 'O';
    private final char MIN_STONE = 'X';
    
    /**
     * The Evaluate object is initialized with the width of the board of the game session that created the object.
     *
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public Evaluate(int size) {
        this.SIZE = size;
    }


    /**
     * This method is the method called to calculate the value of a board. It will evaluate the board by looking at it horizontally, vertically and diagonally. 
     *
     * @param	board	The board being evaluated.
     * @return	int		The value of the evaluation.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public int getStaticEvaluation(char[][] board) {

        this.board = board;
        int rowScore = evaluateRows ();
        int columnScore = evaluateColumns ();
        int diagonalScore = evaluateDiagonals ();

        return rowScore + columnScore + diagonalScore;
    }
    /**
     * This method evaluates each row one at a time and adds value for similar markers in a row, the more the better, for both computer and human player. The result is returned. 
     *
     * @return	int		The value of the evaluation for rows.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int evaluateRows() {
        int totalMaxPoints = 0;
        int totalMinPoints = 0;
        int inARowMaxPoints = 0;
        int inARowMinPoints = 0;
        char currentSquare;
        char previousSquare;

        for (int row = 0; row < SIZE; row++) {
            previousSquare = NONE;
            totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
            totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
            inARowMaxPoints = 0;
            inARowMinPoints = 0;

            for (int column = 0; column < SIZE; column++) {
                currentSquare = board[row][column];

                //  adds upp the number of chained stones as long as they are the same as previous.
                if (currentSquare == MAX_STONE) {
                    if (currentSquare == previousSquare) {
                        inARowMaxPoints++;
                    } else {
                        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
                        inARowMaxPoints = 0;
                    }
                }
                //  adds upp the number of chained stones as long as they are the same as previous.
                if (currentSquare == MIN_STONE) {
                    if (currentSquare == previousSquare) {
                        inARowMinPoints++;
                    } else { // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
                        inARowMinPoints = 0;
                    }
                }
                previousSquare = currentSquare;


            }// inner for loop
        } // outer for loop

        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
        return totalMaxPoints - totalMinPoints;
    }//evaluateRows()

    /**
     * This method evaluates each column one at a time and adds value for similar markers in a row, the more the better, for both computer and human player. The result is returned. 
     *
     * @return	int		The value of the evaluation for columns.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int evaluateColumns() {

        int totalMaxPoints = 0;
        int totalMinPoints = 0;
        char currentSquare;
        char previousSquare;
        int inARowMaxPoints = 0;
        int inARowMinPoints = 0;

        for (int column = 0; column < SIZE; column++) {
            totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
            totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
            previousSquare = NONE;
            inARowMaxPoints = NONE;
            inARowMinPoints = NONE;

            for (int row = 0; row < SIZE; row++) {
                currentSquare = board[row][column];

                //  adds upp the number of chained stones as long as they are the same as previous.
                if (currentSquare == MAX_STONE) {
                    if (currentSquare == previousSquare) {
                        inARowMaxPoints++;
                    } else {// if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
                        inARowMaxPoints = 0;
                    }
                }
                //  adds upp the number of chained stones as long as they are the same as previous.
                if (currentSquare == MIN_STONE) {
                    if (currentSquare == previousSquare) {
                        inARowMinPoints++;
                    } else { // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
                        inARowMinPoints = 0;
                    }
                }
                previousSquare = currentSquare;

            }// inner for loop
        } // outer for loop

        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
        return totalMaxPoints - totalMinPoints;
    }//evaluateColumns()

    /**
     * This method evaluates diagonals and adds value for similar markers in a row, the more the better, for both computer and human player. The result is returned. 
     *
     * @return	int		The value of the evaluation for diagonals.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int evaluateDiagonals() {
        int leftRightScore = 0;
        int rightLeftScore = 0;


        for (int x = 0; x < SIZE - 1; x++) {
            leftRightScore += evaluateLeftRightDiagonal (x, NONE);
        }
        for (int y = 1; y < SIZE - 1; y++) { //y=1 because 0 is already checked.
            leftRightScore += evaluateLeftRightDiagonal (NONE, y);
        }


        for (int x = SIZE - 1; x >= 0; x--) {
            rightLeftScore += evaluateRightLeftDiagonal (x, NONE);
        }
        for (int y = 1; y < SIZE - 1; y++) { // siz -2 because 1 is already searched
            rightLeftScore += evaluateRightLeftDiagonal (SIZE - 1, y);
        }
        return leftRightScore + rightLeftScore;

    }//evaluateDiagonals()
    /**
     * This method evaluates diagonals going from up-left to down-right and adds value for similar markers in a row, the more the better, for both computer and human player. The result is returned. 
     *
     * @return	int		The value of the evaluation for diagonals.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int evaluateLeftRightDiagonal(int x, int y) {
        int totalMaxPoints = 0;
        int totalMinPoints = 0;
        int inARowMaxPoints = 0;
        int inARowMinPoints = 0;
        char currentSquare;
        char previousSquare;

        previousSquare = NONE;
        do {
            currentSquare = board[x][y];
            //  adds upp the number of chained stones as long as they are the same as previous.
            if (currentSquare == MAX_STONE) {
                if (currentSquare == previousSquare) {
                    inARowMaxPoints++;
                    // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                } else {
                    totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
                    inARowMaxPoints = 0;
                }
            }   //  adds upp the number of chained stones as long as they are the same as previous.
            if (currentSquare == MIN_STONE) {
                if (currentSquare == previousSquare) {
                    inARowMinPoints++;
                } else {
                    // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                    totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
                    inARowMinPoints = 0;
                }
            }
            previousSquare = currentSquare;
            x++;
            y++;
        } while (x < SIZE && y < SIZE);
        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);

        return totalMaxPoints - totalMinPoints;
    }//evaluateLeftRightDiagonal()

    /**
     * This method evaluates diagonals going from up-right to down-left and adds value for similar markers in a row, the more the better, for both computer and human player. The result is returned. 
     *
     * @return	int		The value of the evaluation for diagonals.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int evaluateRightLeftDiagonal(int x, int y) {


        int totalMaxPoints = 0;
        int totalMinPoints = 0;
        int inARowMaxPoints = 0;
        int inARowMinPoints = 0;
        char currentSquare;
        char previousSquare;

        previousSquare = NONE;
        do {
            currentSquare = board[x][y];
            //  adds upp the number of chained stones as long as they are the same as previous.
            if (currentSquare == MAX_STONE) {
                if (currentSquare == previousSquare) {
                    inARowMaxPoints++;
                    // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                } else {
                    totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
                    inARowMaxPoints = 0;
                }
            }   //  adds upp the number of chained stones as long as they are the same as previous.
            if (currentSquare == MIN_STONE) {
                if (currentSquare == previousSquare) {
                    inARowMinPoints++;
                    // if the previous stone is of different type resets the rowcounter  and adds up anny result to the total.
                } else {
                    totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);
                    inARowMinPoints = 0;
                }
            }
            previousSquare = currentSquare;
            x--;
            y++;
        } while (x >= 0 && y < SIZE);
        totalMaxPoints += inARowMaxPoints == NONE ? NONE : Math.pow (inARowMaxPoints, 2);
        totalMinPoints += inARowMinPoints == NONE ? NONE : Math.pow (inARowMinPoints, 2);

        return totalMaxPoints - totalMinPoints;
    }//evaluateRightLeftDiagonal()
} // Evaluate() class



