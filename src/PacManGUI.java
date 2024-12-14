// import java swing and color and font
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;


// this class creates the graphical user interface for the game
// it holds the board and the heads up display, and is called
// whenever the game starts
public class PacManGUI extends JFrame{
	
	// create the game board
	private static String pacmanUnicode = "ᗧ";
	private static String pacmanLives = "";
	private static Board gameBoard;
	private static JLabel scoreboard = new JLabel();
	// private static fileWriter Leaderboard = new fileWriter();

	
	// constructor method that takes in the difficulty and the level number
	public PacManGUI(int difficulty, int level) {
		// set layout manager to null
		setLayout(null);
        getContentPane().setBackground(Color.BLACK);
		
		// create the new gameboard JPanel
		gameBoard = new Board(difficulty, level);
		// set the bounds of the game boards
		gameBoard.setBounds(0, 0, 1000, 1000);
		scoreboard.setBounds(50, 980, 1000, 100);
		// set the number of lives accordingly
		String pacmanLives = "";
		// append the pacman character to the string
		for (int i = 0; i < gameBoard.getLives(); i++) pacmanLives += pacmanUnicode;
		// set the text of the number of lives + the instructions
		scoreboard.setText("					 " + pacmanLives + "     PRESS ANY KEY TO START!");
		// https://stackoverflow.com/questions/59763059/change-the-font-style-size-in-a-java-swing-appliation
		scoreboard.setFont(new Font("Courier New", Font.BOLD, 24));
		scoreboard.setForeground(Color.WHITE);

		// screen dimensions
		setSize(1000, 1100);
		setTitle("Jayden's PacMan Game");

		// close the game when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add the board to the screen
		add(gameBoard);

		// add the scoreboard
		add(scoreboard);
		
		// add the key listener, which listens 
		// to keys pressed on the keyboard
		addKeyListener(gameBoard);
		
		// set the visibility of the screen
		setVisible(true);

	} // end of method

	// this method is used to update the label at the bottom of the 
	// game screen
	static public void updateLabel(){
		pacmanLives = "";
		for (int i = 0; i < gameBoard.getLives(); i++) pacmanLives += pacmanUnicode;
		// set the text to number of lives + the timers
		scoreboard.setText(
			// for each timer, we check if it is active with the ternary operator
			// this means if they are not active, they are not visible.
			(gameBoard.getPowerUpTimer() > 0 ? "Powerup: " + String.valueOf(gameBoard.getPowerUpTimer()) + "s  " : "             ")
			+ (gameBoard.getSlowDownTimer() > 0 ? "Freezing: " + String.valueOf(gameBoard.getSlowDownTimer()) + "s  " : "              ")
			+ (gameBoard.getSpeedTimer() > 0 ? "Fast Speed: " + String.valueOf(gameBoard.getSpeedTimer()) + "s  " : "                ")
		 	// display the number of lives the player has remaining
			+ gameBoard.getScore() + "0 point" + (gameBoard.getScore() != 1 ? "s" : "") + "   " + pacmanLives	
		);
	}
	
} // end of class
