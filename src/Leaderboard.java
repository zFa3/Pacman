// importing everything needed for the leaderboard frame
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.io.*;

// this class extends (uses) JFrame to show all the elements, and uses
// the mouse listener to exit the frame when needed
public class Leaderboard extends JFrame implements MouseListener{

    // this is just some really random set of characters used as a delimeter
    // as long as the user doesn't enter these characters it wont break
    // a way to get around this is to just use any delimeter and use the last occurance
    // of the delimeter to seperate the score
    public static final String delimeter = "🫚™️";
    // create a new Scanner that reads the leaderboard text file
    private Scanner fileInput;
    // store all the scores in this array of String[]
    private ArrayList<String[]> allScores;

    // constructor method. This method creates the class and assigns
    // some parameters to the window and its elements
    public Leaderboard(){
        // set some parameters
        setSize(Menu.dimensions, Menu.dimensions);
        // set the title and the layout manager
        setTitle("Leaderboard - Jayden's Pacman game");
        setLayout(null);
        // code source: szelong told me about getcontentpane
        // since this is a JFrame and not a JPanel
        getContentPane().setBackground(Color.BLACK);

        // close the game when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // empty the list before we add anything to it
        allScores = new ArrayList<>();
        // use a try catch to enture that there are no errors
        try {
            // use the scanner to open the file and read the contents
            fileInput = new Scanner(new File("Assets/LB.txt"));
            // while the file has a next line (or "\n" character)
            while (fileInput.hasNextLine()){
                // use the regex (delimeter) to split the line
                String[] highscore = (fileInput.nextLine()).split(delimeter);
                // add the score to the Arraylist
                allScores.add(new String[] {highscore[0], highscore[1], highscore[2]});
            }
        // if there is an error
        } catch (Exception error){}
        
        // soritng an arraylist
        // https://www.javatpoint.com/how-to-sort-arraylist-in-java
        // Collections.sort(allScores, (name, time, score) ->);
        allScores.sort(Comparator.comparingInt(arr -> Integer.valueOf(arr[2])));
        // reverse the list to get largest scores first, since larger is better
        // for this application
        Collections.reverse(allScores);

        // set the height of the label
        int labelHeight = 75; // in pixels
        // for the top 10 people, or less if there are less than 10 people who've played the game
        for (int rank = 0; rank < Math.min(10, allScores.size()); rank++){
            // make two new strings
            String[] spacers = new String[] {"", ""};
            // subtract the number of characters of their name from the max characters allowd
            // this line of code lines up all of the names neatly
            for (int i = 0; i < Menu.MAXNAMELEN + 2 - String.valueOf(allScores.get(rank)[0]).length(); i++) spacers[0] += " ";
            // this line does the same but for the score
            for (int i = 0; i < Menu.MAXNAMELEN - String.valueOf(allScores.get(rank)[1]).length(); i++) spacers[1] += " ";
            // create a new JLabel that shows the name, time, score etc.
            JLabel Score = new JLabel();
            // we will set the text of the label to:
            Score.setText(
                // the player's rank
                String.valueOf(rank+1)
                // a constant number of spaces
                + "       "
                // plus one more space if the rank isn't 10
                // since 10 has one more character than the rest of the numbers
                + (rank + 1 == 10 ? "" : " ")
                // add the name
                + allScores.get(rank)[0]
                // add the correct number of spaces
                + spacers[0]
                // add the time taken (ticks)
                + allScores.get(rank)[1] 
                // add the correct number of spaces
                + spacers[1]
                // and finally we add the score that they achieved
                + Integer.valueOf(allScores.get(rank)[2]) * 10
            );
            // set the font to something more... pacman-like
            Score.setFont(new Font("Courier New", Font.PLAIN, (int)(labelHeight / 3)));
            // set the color to the corresponding rank
            // if they are rank #1, we change the color to gold
            if (rank == 0) Score.setForeground(new Color(255, 215, 0, 255));
            // if they are rank #2, we change the color to silver
            else if (rank == 1) Score.setForeground(new Color(192, 192, 192, 255));
            // if they are rank #3, we change the color to bronze
            else if (rank == 2) Score.setForeground(new Color(205, 127, 50, 255));
            // otherwise we can set teh score to white
            else Score.setForeground(Color.WHITE);

            // set the bounds and the placement of these labels
            // and we increment the placement every rank by the height of the label
            Score.setBounds((int)(Menu.dimensions - 700) / 2, 275, 1000, 100 + rank * labelHeight);
            // finally we will add the label to the screen
            add(Score);
        }
        
        // create the leaderboard image that goes behind the labels
        JLabel titleLabel = new JLabel(Menu.resize("images/Leaderboard.bmp", Menu.dimensions, Menu.dimensions));
        titleLabel.setBounds(0, 0, Menu.dimensions, Menu.dimensions);
        // add the label last, which places it at the back
        add(titleLabel);
        
        // add the mouselistener
        // this mouselistener returns us to the main menu when we are ready
        addMouseListener(this);
        
        // set the visible to true
        setVisible(true);
    }

    // this method is a built in java mouse listener method
    // that gets called whenever the mouse is pressed
    public void mousePressed(MouseEvent e) {
        // we will destroy this window
        setVisible(false);
        dispose();
        // and reopen the menu when the mouse is clicked
        new Menu();
    }

    // the rest of the methods required for MouseListener
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}    
}