// import all the items we need
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

// this class creates the graphical user interface for the game
// it allows us to accces the gmae and the leaderboards
// implements mouse listener to start the game when the screen is clicked
// and some other conditions are met
public class Menu extends JFrame implements MouseListener{
    // screen dimensions
    // set public so other programs can use them
    public static int dimensions = 1000;
    // max length for names
    public static final int MAXNAMELEN = 16;
    // set the starting level to level 1
    public static int startLevel = 1;
    // the total score keeps track of score across levels to use
    // for the leaderboards
    public static int totalScore = 0;
    public static PacManGUI LV;
    private static JTextField Name;
	
	// constructor method that takes in the difficulty and the level number
	public Menu() {
		
		// set some parameters
		setSize(dimensions, dimensions);
        // set the title of the screen
		setTitle("Jayden's PacMan Game");
        setLayout(null);
        // source: szelong told me about getcontentpane
        // since this is a JFrame and not a JPanel
        // setting the frame black doesn't work
        getContentPane().setBackground(Color.BLACK);

		// close the game when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // load the menu of the screen
        loadMenu();
        
        // adds a mouseListener to this frame
        addMouseListener(this);
        
        // set the visibility of the screen
        setVisible(true);
        
    } // end of method

    private void loadMenu() {
        // Create a new title label that holds the title image (found in images folder)
        JLabel titleLabel = new JLabel(resize("images/TITLE2.bmp", 842*2, 388*2));
        // set the bounds and the position
        titleLabel.setBounds(-390, 0, 842*2, 388*2);

        // make a new text field, for the player to enter their username
        // this will eventually be used for the highscores and the leaderboard
        Name = new JTextField("Enter your name...");
        // this line of code centers the text in the middle of the text field
        // https://stackoverflow.com/questions/15507639/how-do-i-center-a-jtextfield
        Name.setHorizontalAlignment(JTextField.CENTER);
        // set soem aesthetic parameters
        // set the font, color, and the bounds
        Name.setFont(new Font("Monospaced", Font.ITALIC, 26));
        // set the text color to blue
        Name.setForeground(new Color(0, 0, 150, 255));
        Name.setBounds(250, 450, 500, 85);
        
        // make a new button that redirects the user to the leaderboard page
        JButton leaderboards = new JButton();
        // set the text for the leaderboards button
        leaderboards.setText("LEADERBOARD");
        // set the font for the leaderboard button
        leaderboards.setFont(new Font("Monospaced", Font.ITALIC, 32));
        // set the text color to white
        leaderboards.setForeground(new Color(0, 0, 0, 255));
        // set the boundaries for this Java swing element
        leaderboards.setBounds(350, 825, 300, 75);
        // add the action listener that checks if the button has been pressed
        leaderboards.addActionListener(new ActionListener() {
            // must have the name 'ActionEvent'
            public void actionPerformed(ActionEvent e){
                // hide the window
                setVisible(false);
                // close the window
                dispose();
                // open the leaderboard window
                new Leaderboard();
            }
        });

        // add these elements to the board
        add(leaderboards);
        add(Name);
        add(titleLabel);
    }

    // A method that is used to resize images, to shorten the code a little
    // making it public means other parts of the game can also access this method,
    // allowing them to use it as well
	public static ImageIcon resize(String pathToImage, int new_x_Dimension, int new_y_Dimension) {
        // create a new temporary image icon that we use to store the image
    	ImageIcon tempImage = new ImageIcon(pathToImage);
        // then we resize the image, and set it to an image
    	Image resized = tempImage.getImage().getScaledInstance(new_x_Dimension, new_y_Dimension, Image.SCALE_DEFAULT);
        // return the new image object
    	return new ImageIcon(resized);
    }

    // this method is just a getter for the player's name
    // used to store leaderboard results
    public static String getPlayerName(){
        // get the text from the name text field
        return Name.getText();
    }

    // this method checks if the mouse has been pressed
    @Override
    public void mousePressed(MouseEvent e) {
        // if the mouse has been pressed, and the player has entered a name that is acceptable
        // then we start the game by creating a new PacManGUI object which creates the frame
        // also the name cannot contain (🫚™️) since that is the delimeter. Although no one will
        // realistically use 🫚™️ as their name, its just a precaution
        if (!(Name.getText().equals("Enter your name...") || Name.getText().length() < 2 || Name.getText().length() > MAXNAMELEN) && !Name.getText().contains("🫚™️")){
            // create a new PacMan GUI
            // pass in ( difficulty (1 -> 3) and level #)
            LV = new PacManGUI(startLevel, startLevel);
            // these two lines destroy the old menu
            setVisible(false);
            dispose();
        }
    }

    // the rest of the methods required for MouseListener
    // these are unused
    @Override
    public void mouseClicked (MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

} // end of class