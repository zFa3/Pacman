/*



UNUSED CLASS


*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

// class that creates the maze builder
public class MazeBuilder extends JFrame implements MouseListener {

    // 25 rows, 25 columns
    private Cell[][] board = new Cell[25][25];

    // contructor method
    public MazeBuilder(){

        for (int row = 0; row < 25; row++){
            for (int col = 0; col < 25; col++){
                board[row][col] = new Cell('X');
            }
        }

        // set some parameters
		setLayout(new GridLayout(25, 25));
		setBackground(Color.BLACK);
		// set some parameters
		setSize(625, 625);

        // set the title of the window
		setUndecorated(true);
		// close the game when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set the visibility of the screen
        
        // center the window on the screen
        // Foudn on this site - https://stackoverflow.com/questions/9543320/how-to-position-the-form-in-the-center-screen
        // Get the size of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        // by dividing the (screen width - window width) / 2
        int wid = getSize().width;
        int hgt = getSize().height;
        int x = (dimension.width-wid)/2;
        int y = (dimension.height-hgt)/2;

        // Move the window
        setLocation(x, y);

        // adds a mouseListener to this frame
        addMouseListener(this);
        
        // load the board
        loadBoard();

        //set visible to true
		setVisible(true);

	} // end of method
	
    // this method loads the board
    public void loadBoard(){
        for (int row = 0; row < 25; row++){
            for (int col = 0; col < 25; col++){
                board[row][col].setCodeIcon();
                add(new Cell(board[row][col].getItem()));
            }
        }
    }

    public void consoleDebug(){
        for (int row = 0; row < 25; row++){
            for (int col = 0; col < 25; col++){
                System.out.print(board[row][col].getItem());
            }
            System.out.println();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int row = (e.getY()/25);
        int col = (e.getX()/25);
        if (board[row][col].getItem() == 'W'){
            board[row][col].setItem('X');
        } else {
            board[row][col].setItem('W');
        }
        
        revalidate();
        repaint();
        
        loadBoard();
        
        revalidate();
        repaint();
        consoleDebug();
    }

    // the rest of the methods required for MouseListener
    @Override
    public void mouseClicked (MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}