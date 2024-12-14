
/*
 * Method creates a cell that stores the state of
 * a single position on the grid
 */

// import the label
import javax.swing.JLabel;

// this class extends Jlabel
public class Cell extends JLabel {
	
    // store the Item from the file into the 'Cell' Class
    private char Item;

    // constructor method
    public Cell(char item){
        // keyword 'this' isnt neccessary since it isnt
        // ambiguous, however just for clarity its referencing a field
        this.Item = item;
        setCodeIcon();
    }

    // basic getter for the item (character)
    public char getItem(){
        return this.Item;
    }

    // setter for the item (character)
    public void setItem(char item){
        this.Item = item;
    }

    // set the icon to the appropriate image
    public void setCodeIcon() {
        // if the player is on this cell
        if (this.Item == 'P')
        // set the iceon to pacman
        setIcon(Icons.PACMAN[0]);
        // otherwise, we check all the other options
        // and set the icon accordingly
        else if (this.Item == '0')setIcon(Icons.GHOST[0]);
        else if (this.Item == '1')setIcon(Icons.GHOST[1]);
        else if (this.Item == '2')setIcon(Icons.GHOST[2]);
        else if (this.Item == 'W')setIcon(Icons.WALL);
        else if (this.Item == 'F')setIcon(Icons.FOOD) ;
        else if (this.Item == 'D')setIcon(Icons.DOOR);
        else if (this.Item == 'C')setIcon(Icons.CHERRY);
        else if (this.Item == 'B')setIcon(Icons.BLUECHERRY);
        else if (this.Item == 'W')setIcon(Icons.BLUEGHOST);
        else if (this.Item == 'T')setIcon(Icons.TRACER);
        else if (this.Item == 'G')setIcon(Icons.GATE);
        else if (this.Item == '+')setIcon(Icons.POWERPELLET);
    }
}