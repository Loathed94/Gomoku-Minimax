

import java.util.Scanner;

/**
 * The Main class runs the game, making sure that each player has their turn and handles wins.
 *
 * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
 */
public class Main {
	private final int BOARD_SIZE = 8;
	private final Gomoku game = new Gomoku (BOARD_SIZE);
	boolean isRunning = true;


	public static void main(String[] args) {
		Main main = new Main ();
		main.run ();
	}


	private void run() {
		final String COMMA_DELIMITER = ",";
		Scanner scanner = new Scanner (System.in);


		addComputersFirstStone();
		game.printBoard ();
		while (isRunning) {
			System.out.println ("Insert Coordinates: x,y");

			String[] coordinateStrings = scanner.nextLine ().split (COMMA_DELIMITER);
			int x = Integer.parseInt (coordinateStrings[0]);
			int y = Integer.parseInt (coordinateStrings[1]);

			
			if (game.addStone (x, y, false)) {
				if(game.checkHumanWin(x, y)) {
					game.printBoard ();
					System.out.println("Human wins!");
					break;
				}
				long startTime = System.currentTimeMillis (); // meassuring the execution time
				Move computerMove = game.addComputerStone (x,y);
				long endTime = System.currentTimeMillis ();
				long duration = (endTime - startTime);
				System.out.println ("Move took " + duration + "ms");
				if(game.checkComputerWin(computerMove.getX(), computerMove.getY())) {
					game.printBoard ();
					System.out.println("Computer wins!");
					break;
				}
				game.printBoard ();

			} else {
				System.out.println ("Error: coordinate is either out of range or occupied. try again:");
			}

		}//while

		scanner.close ();
	}//run

	private void addComputersFirstStone() {
		game.addStone (BOARD_SIZE/2,BOARD_SIZE/2,true);

	}





}// main
