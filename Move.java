
/**
 * The class Move offers an easy way of storing both X and Y coordinates as well as a calculated value of the move from minimax (when needed).
 *
 * @author 	Christian Neij and Christoffer Öhman (or Oehman in the english alphabet)
 */
public class Move {
	private final  int x,y;
	private final int value;
	
	public Move(int x, int y) {
		this(x,y,0);
	}
	public Move(int x, int y, int value){
		this.x = x;
		this.y = y;
		this.value = value;
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int value() {
		return value;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Move)) {
			return false;
		}
		return this.x == ((Move) other).getX() && this.y == ((Move) other).getY() && this.value == ((Move) other).value();
	}
}
