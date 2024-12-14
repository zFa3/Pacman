// this class is the template class that pacman and
// ghosts inherit from

// we import the swng components
import javax.swing.JLabel;

// this class uses a JLabel as these can be added onto
// the board (JFrame) as image Icons
public class Mover extends JLabel{

    // the current location of the mover object
    private int row;
    private int column;

    // the delta movement of the ghost
    // default is 0
    private int deltaRow;
    private int deltaColumn;
    public boolean isPacman;

    // for pacman, is dead means you lost the game
    // for a ghost, this means pacman is able to turn them
    // blue, allowing him to eat them
    // default for boolean is false
    private boolean isDead;

    // the constructor method that we use to create the Mover
    // defines the position and the type of the mover
	public Mover(int row, int column, boolean isPacman) {
		super();
		this.row = row;
		this.column = column;
        this.isPacman = isPacman;
	}

    // getters and setters
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getDeltaRow() {
		return deltaRow;
	}

	public void setDeltaRow(int deltaRow) {
		this.deltaRow = deltaRow;
	}

	public int getDeltaColumn() {
		return deltaColumn;
	}

	public void setDeltaColumn(int deltaColumn) {
		this.deltaColumn = deltaColumn;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

    // this method moves the mover object according the 
    // delta row and the delta column
    public void move() {
        // apply the delta
        this.row += this.deltaRow;
        this.column += this.deltaColumn;
    }

    // set the deltas based off the direction
    // 0 - 3 zero being left one being up etc.
    public void setDirection(int direction) {
        // reset the deltas
        this.deltaColumn = 0;
        this.deltaRow = 0;

        // set the appropriate deltas
        if (direction == 0)
            this.deltaColumn = -1;
        if (direction == 1)
            this.deltaRow = -1;
        if (direction == 2)
            this.deltaColumn = 1;
        if (direction == 3)
            this.deltaRow = 1;
    }
   
    // get the direction based off the delta
    // 'reverse engineer' the logic
    public int getDirection() {
        if (this.deltaColumn == -1)
            return 0;
        if (this.deltaRow == -1)
            return 1;
        if (this.deltaColumn == 1)
            return 2;
        // no if statement needed, since
        // if all the others are false then 
        // we know it can only be down (3)
        return 3;
    }

    // get the next cell's row/column number
    public int getNextRow(){
        // return the delta plus current location
        return this.deltaRow + this.row;
    }

    // this method is just a getter for the next column
    // identical to the rows
    public int getNextColumn(){
        // return the requested value
        return this.deltaColumn + this.column;
    }
}
