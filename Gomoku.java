



import java.util.ArrayList;

/**
 * The game Gomoku is a game similar to TicTacToe however usually involves a bigger board than 3x3. The board size of the game in this program is decided by the player when calling the constructor.
 * The game is played between a human player and a computer which will use minimax algorithm along with alpha-beta pruning to try beat the human. 
 *
 * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
 */
public class Gomoku {
    private static final int DEPTH = 5;
    private static final int NONE = 0;
    private static final int ZERO = 0;
    private static final int WINNING_NUMBER = 5;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    private static final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private final char[][] board; //
    private final Evaluate evaluate;
    private final int size; // length of sides.
    private int emptySquares;
    private ArrayList<Move> availableNeighbours = new ArrayList<> ();

    /**
     * A get-method that will return a char-matrix containing the board, or rather a matrix of characters in which the spaces contain either nothing, X or O where X and O are the characters for the two players.
     *
     * @return	char[][]	The board with the current markers placed on it.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public char[][] getBoard() {
        return board;
    }
    /**
     * A get-method that will return the width of the board (the board is square so the height is the same).
     *
     * @return	int		The width of the board.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public int getSize() {
        return size;
    }
    /**
     * The constructor of the class takes in the width of the board as an int and sets up the board in preparation of the game. It also creates an Evaluate-object that it will use for evaluations in the minimax-algorithm.
     *
     * @param	size	The width of the board.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public Gomoku(int size) {
        this.size = size;
        this.emptySquares = size * size;
        board = new char[size][size];
        evaluate = new Evaluate (size);
    }
    /**
     * This method receives x and y coordinates as int for the board where a player wishes to place its marker and also receives a boolean telling the method whether it's the player or the computer.
     * The method makes sure that the placement choice is acceptable, if it is the placement is made and the method returns true, otherwise false is returned and no placement is made.
     *
     * @param	x			The X-coordinate of the placement choice.
     * @param	y			The Y-coordinate of the placement choice.
     * @param	isComputer	Boolean telling method whether the player or the computer is making the placement.
     * @return	boolean		If placement can be made return true, otherwise return false.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public boolean addStone(int x, int y, boolean isComputer) {

        // check that stone is in bounds.
        if (!isIndexAcceptable (x, y)) {
            return false;
        }

        char square = board[x][y];
        final int EMPTY = 0;
        Move place = new Move (x, y);

        if (square != EMPTY) {
            return false;
        }
        availableNeighbours.remove (place);
        emptySquares--;
        board[x][y] = isComputer ? 'O' : 'X';
        produceNeighbours (place);
        return true;
    }
    /**
     * This method is called when a new marker is placed to add spaces from the board to a list of "neighbours". Neighbours are empty spaces that are next to spaces filled by the player or the computer. 
     * The purpose of neighbours is to limit where the computer can place its markers. The computer will only place its markers next to existing markers.
     *
     * @param	place	A Move-object representing the newly places marker.
     * @return	void
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private void produceNeighbours(Move place) {
        final int TOP_EDGE = 0, BOTTOM_EDGE = size - 1, LEFT_EDGE = 0, RIGHT_EDGE = size - 1;
        int x = place.getX ();
        int y = place.getY ();
        if (y > TOP_EDGE) { //Neighbours over the marker cannot exist if the marker is at the top.
            produceTopNeighbours (place);
        }
        if (y < BOTTOM_EDGE) { //Neighbours under the marker cannot exist if the marker is at the bottom.
            produceBottomNeighbours (place);
        }
        if (x > LEFT_EDGE) { //Neighbours left the marker cannot exist if the marker is at the left edge.
            Move leftNeighbour = new Move (place.getX () - 1, place.getY ());
            if (!availableNeighbours.contains (leftNeighbour) && board[x - 1][y] == 0) {
                availableNeighbours.add (leftNeighbour);
            }
        }
        if (x < RIGHT_EDGE) { //Neighbours right the marker cannot exist if the marker is at the right edge.
            Move rightNeighbour = new Move (place.getX () + 1, place.getY ());
            if (!availableNeighbours.contains (rightNeighbour) && board[x + 1][y] == 0) {
                availableNeighbours.add (rightNeighbour);
            }
        }
    }
    /**
     * This method will produce the neighbours on the row below the new marker. One underneath the marker (same X-coordinate) and two others diagonally from the marker (one left and one right) unless the marker is at an edge to the left or right, in those cases there will be two neighbours in total instead of three.
     * Spaces that are occupied will not be considered neighbours.
     *
     * @param	place	A Move-object representing the newly places marker.
     * @return	void
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private void produceBottomNeighbours(Move place) {
        final int LEFT_EDGE = 0, RIGHT_EDGE = size - 1, EMPTY = 0;
        int placeX = place.getX ();
        int placeY = place.getY ();
        int bottomIndexX, amountOfBottomNeighbours;
        int bottomIndexY = placeY + 1;

        if (placeX == LEFT_EDGE) { //At left edge there cannot be a neighbour diagonally down-left.
            bottomIndexX = placeX;
            amountOfBottomNeighbours = 2;
        } else if (placeX == RIGHT_EDGE) { //At right edge there cannot be a neighbour diagonally down-right.
            bottomIndexX = placeX - 1;
            amountOfBottomNeighbours = 2;
        } else {
            bottomIndexX = placeX - 1;
            amountOfBottomNeighbours = 3;
        }
        for (int j = 0; j < amountOfBottomNeighbours; j++) {
            Move newPlace = new Move (bottomIndexX + j, bottomIndexY);
            if (!availableNeighbours.contains (newPlace) && board[bottomIndexX + j][bottomIndexY] == EMPTY) {
                availableNeighbours.add (newPlace);
            }
        }
    }
    /**
     * This method will produce the neighbours on the row over the new marker. One on top of the marker (same X-coordinate) and two others diagonally from the marker (one left and one right) unless the marker is at an edge to the left or right, in those cases there will be two neighbours in total instead of three.
     * Spaces that are occupied will not be considered neighbours.
     *
     * @param	place	A Move-object representing the newly places marker.
     * @return	void
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private void produceTopNeighbours(Move place) {
        final int LEFT_EDGE = 0, RIGHT_EDGE = size - 1, EMPTY = 0;
        int placeX = place.getX ();
        int placeY = place.getY ();
        int topIndexX, amountOfTopNeighbours;
        int topIndexY = placeY - 1;

        if (placeX == LEFT_EDGE) { //At left edge there cannot be a neighbour diagonally up-left.
            topIndexX = placeX;
            amountOfTopNeighbours = 2;
        } else if (placeX == RIGHT_EDGE) { //At right edge there cannot be a neighbour diagonally up-right.
            topIndexX = placeX - 1;
            amountOfTopNeighbours = 2;
        } else {
            topIndexX = placeX - 1;
            amountOfTopNeighbours = 3;
        }
        for (int j = 0; j < amountOfTopNeighbours; j++) {
            Move newPlace = new Move (topIndexX + j, topIndexY);
            if (!availableNeighbours.contains (newPlace) && board[topIndexX + j][topIndexY] == EMPTY) {
                availableNeighbours.add (newPlace);
            }
        }
    }
    /**
     * The method takes x and y coordinates and returns whether or not the coordinates are within the limits of the game board's size. 
     *
     * @param	x			The X-coordinate for the placement choice.
     * @param	y			The Y-coordinate for the placement choice.
     * @return	boolean		Returns boolean telling whether index chosen is within the board.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private boolean isIndexAcceptable(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }
    /**
     * The method takes x and y coordinates and removes any marker on that space.
     *
     * @param	x			The X-coordinate for the marker.
     * @param	y			The Y-coordinate for the marker.
     * @return	void
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public void removeStone(int x, int y) {
        if (isIndexAcceptable (x, y)) {
            board[x][y] = 0;
            emptySquares++;
        }
    }
    /**
     * This method prints the board on the terminal allowing the player a graphical view of the board making it easier to play.
     *
     * @return	void
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public void printBoard() {

        // print column numbers.
        System.out.print ("    ");
        for (int i = 0; i < size; i++) {
            System.out.print (i + "   ");
        }
        System.out.println (); // creates a space between the board and the boardnumbers
        for (int y = 0; y < size; y++) {

            System.out.println (" ----------------------------------------");
            System.out.print (y + " "); // Prints row numbers
            for (int x = 0; x < size; x++) {
                char square = board[x][y];
                if (square != 0) {
                    System.out.print ("| " + board[x][y] + " ");
                } else {
                    System.out.print ("|   ");
                }
            }
            System.out.print ("| \n");
        }
        System.out.println ("-----------------------------------------");

    }//printBoard
    /**
     * The method takes x and y coordinates as well as an int telling the method how many markers in a row leads to a win and determines whether the placement leads to a win for the player who placed it. 
     *
     * @param	x				The X-coordinate for the placement choice.
     * @param	y				The Y-coordinate for the placement choice.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @return	int				An int is returned, if no win is found it will be zero, anything greater than zero is a win.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkWin(int x, int y, int targetValue) {
        char stone;
        if (board[x][y] == 'X') {
            stone = 'X';
        } else if (board[x][y] == 'O') {
            stone = 'O';
        } else {
            throw new IllegalArgumentException ("Invalid square, cannot evaluate win!");
        }
        int rowIndex;
        if (x >= targetValue) {
            rowIndex = x - targetValue;
        } else {
            rowIndex = 0;
        }
        int rowVal = checkRow (rowIndex, y, targetValue, stone);
        if (rowVal > 0) {
            return rowVal;
        }
        int columnIndex;
        if (y >= targetValue) {
            columnIndex = y - targetValue;
        } else {
            columnIndex = 0;
        }
        int columnVal = checkColumn (x, columnIndex, targetValue, stone);
        if (columnVal > 0) {
            return columnVal;
        }
        int diagVal = checkDiagonal (x, y, targetValue, stone);
        if (diagVal > 0) {
            return diagVal;
        }

        return ZERO;
    }
    /**
     * This method is called by checkWin to specifically check the row that the marker is placed on whether a win can be found horizontally. 
     *
     * @param	rowZero			The X-coordinate to start the check from.
     * @param	y				The Y-coordinate for the placement choice.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @param 	stone			Which marker type that is being looked at.
     * @return	int				An int is returned, if no win is found it will be zero, if win is found the value is set to targetValue.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkRow(int rowZero, int y, int targetValue, char stone) {
        int numbersInARow = ZERO;
        for (int i = rowZero; i < size; i++) {
            if (board[i][y] == stone) {
                numbersInARow++;
            } else {
                numbersInARow = ZERO;
            }
            if (numbersInARow == targetValue) {
                return targetValue;
            }
        }
        return ZERO;
    }
    /**
     * This method is called by checkWin to specifically check the column that the marker is placed on whether a win can be found vertically. 
     *
     * @param	x				The X-coordinate for the placement choice.
     * @param	columnTop		The Y-coordinate to start the check from.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @param 	stone			Which marker type that is being looked at.
     * @return	int				An int is returned, if no win is found it will be zero, if win is found the value is set to targetValue.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkColumn(int x, int columnTop, int targetValue, char stone) {
        int fiveInARow = ZERO;
        for (int i = columnTop; i < size; i++) {
            if (board[x][i] == stone) {
                fiveInARow++;
            } else {
                fiveInARow = ZERO;
            }
            if (fiveInARow == targetValue) {
                return targetValue;
            }
        }
        return ZERO;
    }
    /**
     * This method is called by checkWin to specifically check the diagonals that run through the marker to find a diagonal win.
     *
     * @param	x				The X-coordinate for the placement choice.
     * @param	y				The Y-coordinate for the placement choice.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @param 	stone			Which marker type that is being looked at.
     * @return	int				An int is returned, if no win is found it will be zero, if win is found the value is set to targetValue.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkDiagonal(int x, int y, int targetValue, char stone) {
        final int LEFT_EDGE = 0, TOP_EDGE = 0, RIGHT_EDGE = size - 1;
        int diagX, diagY;
        if (x == LEFT_EDGE || y == TOP_EDGE) {
            diagX = x;
            diagY = y;
        } else {
            diagX = x;
            diagY = y;
            do {
                diagX -= 1;
                diagY -= 1;
            } while (diagX > LEFT_EDGE && diagY > TOP_EDGE);
        }
        int diagLR = checkDiagLeftRight (diagX, diagY, targetValue, stone);
        if (diagLR > ZERO) {
            return diagLR;
        }
        if (x == RIGHT_EDGE || y == TOP_EDGE) {
            diagX = x;
            diagY = y;
        } else {
            diagX = x;
            diagY = y;
            do {
                diagX += 1;
                diagY -= 1;
            } while (diagX < RIGHT_EDGE && diagY > TOP_EDGE);
        }
        int diagRL = checkDiagRightLeft (diagX, diagY, targetValue, stone);
        if (diagRL > ZERO) {
            return diagRL;
        }
        return ZERO;
    }
    /**
     * This method is called by checkDiagonal to specifically check the diagonal going from top-left to bottom-right that runs through the marker to find a diagonal win.
     *
     * @param	diagX			The leftmost possible coordinate for the diagonal.
     * @param	diagY			The topmost possible coordinate for the diagonal.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @param 	stone			Which marker type that is being looked at.
     * @return	int				An int is returned, if no win is found it will be zero, if win is found the value is set to targetValue.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkDiagLeftRight(int diagX, int diagY, int targetValue, char stone) {
        final int RIGHT_EDGE = size - 1, BOTTOM_EDGE = size - 1;
        int fiveInARow = ZERO;
        while (diagX <= RIGHT_EDGE && diagY <= BOTTOM_EDGE) {
            if (board[diagX][diagY] == stone) {
                fiveInARow++;
            } else {
                fiveInARow = ZERO;
            }
            if (fiveInARow == targetValue) {
                return targetValue;
            }
            diagX++;
            diagY++;
        }
        return ZERO;
    }
    /**
     * This method is called by checkDiagonal to specifically check the diagonal going from top-right to bottom-left that runs through the marker to find a diagonal win.
     *
     * @param	diagX			The rightmost possible coordinate for the diagonal.
     * @param	diagY			The topmost possible coordinate for the diagonal.
     * @param	targetValue		An int telling the method how many markers of the same type in a row is needed for a win.
     * @param 	stone			Which marker type that is being looked at.
     * @return	int				An int is returned, if no win is found it will be zero, if win is found the value is set to targetValue.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    private int checkDiagRightLeft(int diagX, int diagY, int targetValue, char stone) {
        final int LEFT_EDGE = 0, BOTTOM_EDGE = size - 1;
        int fiveInARow = ZERO;
        while (diagX >= LEFT_EDGE && diagY <= BOTTOM_EDGE) {
            if (board[diagX][diagY] == stone) {
                fiveInARow++;
            } else {
                fiveInARow = ZERO;
            }
            if (fiveInARow == targetValue) {
                return targetValue;
            }
            diagX--;
            diagY++;
        }
        return ZERO;
    }
    /**
     * This method receives the previous move made by the human player and sends it on to the minimax-algorithm to determine the computer's next move and then returns the chosen move.
     *
     * @param	x		The X-coordinate that the human player played before computer turn.
     * @param	y		The Y-coordinate that the human player played before computer turn.
     * @return	Move	After the computer has made its choice a Move-object with the choice of placement is returned.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public Move addComputerStone(int x, int y) {

        Move computerMove = miniMax (x, y, DEPTH, true, NEGATIVE_INFINITY, POSITIVE_INFINITY);
        addStone (computerMove.getX (), computerMove.getY (), true);
        System.out.println ("Computers best move is " + computerMove.getX () + " " + computerMove.getY () + " value: " + computerMove.value ());
        return computerMove;
    }
    /**
     * Two following methods are just public re-wraps of the checkWin call. Perhaps only one of them would be necessary or checkWin could be made public. 
     * 	
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */
    public boolean checkHumanWin(int x, int y) {
        return checkWin (x, y, WINNING_NUMBER) == WINNING_NUMBER;
    }

    public boolean checkComputerWin(int x, int y) {
        return checkWin (x, y, WINNING_NUMBER) == WINNING_NUMBER;
    }


    /**
     * The method performs the minimax algorithm with alpha-beta pruning to calculate the best move for the computer. 
     * The method is called recursively. During its recursion the algorithm goes through the list of "neighbours" that the computer can choose from.
     * A maximum depth of recursion is determined to prevent the game from becoming too slow. 
     * Alpha-beta pruning is used to make the minimax simpler and faster. 
     *
     * @param 	x               X-coordinate of the move made before.
     * @param 	y               Y-coordinate of the move made before.
     * @param 	depth           The remaining depth that can be explored at this point in the recursion.
     * @param 	isComputersTurn A boolean telling the minimax whether the move being calculated is a computer move or a player move. 
     * @param 	alpha           The highest guaranteed value of the computer's move at this depth. The higher the better for the computer. Is initialized to -INFINITY.
     * @param 	beta            The highest guaranteed value of the player's move at this depth. The higher the better for the player (and inversely the worse for the computer, so this value is counted negatively instead) and is initialized to +INFINITY.
     * @return 	Move			Returns the best move determined at this depth along with the value of the move stored within the Move object.
     * @throws IllegalArgumentException om depth sÃ¤tts till ett negativt tal.
     * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
     */

    private Move miniMax(int x, int y, int depth, boolean isComputersTurn, int alpha, int beta) {
        if (depth < 0) {
            throw new IllegalArgumentException ();
        }
        final int movesBeforeWinIsPossible = 9;
        if (isComputersTurn && (size * size) - emptySquares >= movesBeforeWinIsPossible) {
            if (checkWin (x, y, WINNING_NUMBER) == WINNING_NUMBER) {
                return new Move (x, y, NEGATIVE_INFINITY + ((size * size) - emptySquares));
            }
        } else if ((size * size) - emptySquares >= movesBeforeWinIsPossible) {
            if (checkWin (x, y, WINNING_NUMBER) == WINNING_NUMBER) {
                return new Move (x, y, POSITIVE_INFINITY - ((size * size) - emptySquares));
            }
        }

        if (emptySquares == NONE) {
            return new Move (x, y, NONE);
        }

        if (depth == NONE) {
            return new Move (x, y, evaluate.getStaticEvaluation (board));
        }


        int bestVal = NEGATIVE_INFINITY;
        int bestX = -1;
        int bestY = -1;
        ArrayList<Move> placesToIterateOver = new ArrayList<Move> (availableNeighbours);
        // if computers turn: maximize
        if (isComputersTurn) {
            for (Move place : placesToIterateOver) {
                int x1, y1;
                x1 = place.getX ();
                y1 = place.getY ();
                if (board[x1][y1] == NONE) {
                    addStone (x1, y1, true);
                    int value = miniMax (x1, y1, depth - 1, false, alpha, beta).value ();
                    removeStone (x1, y1);
                    availableNeighbours = new ArrayList<Move> (placesToIterateOver);
                    // if the childNode value is higher than the current alpha whilst traversing the tree. the alpha is updated
                    // as well as the bestMove.
                    if (value > alpha) {
                        alpha = value;
                        bestX = x1;
                        bestY = y1;
                        bestVal = value;
                    }

                    if (alpha >= beta) {
                        break;
                    }
                }//if
            } // for
            return new Move (bestX, bestY, bestVal);


        } else {
            bestVal = POSITIVE_INFINITY;
            // if human players turn: minimize
            for (Move place : placesToIterateOver) {
                int x1, y1;
                x1 = place.getX ();
                y1 = place.getY ();
                if (board[x1][y1] == NONE) {
                    addStone (x1, y1, false);
                    int value = miniMax (x1, y1, depth - 1, true, alpha, beta).value ();
                    removeStone (x1, y1);
                    availableNeighbours = new ArrayList<Move> (placesToIterateOver);

                    if (value < beta) {
                        beta = value;
                        bestX = x1;
                        bestY = y1;
                        bestVal = value;
                    }//if


                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        return new Move (bestX, bestY, bestVal);
    } // minMax


}// Gomoku
