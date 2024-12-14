// import all the items we need
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.io.*;

// this class is the one that is responsible for 
// writing to the leaderboard file
public class FileWriter {
    // create a new method that handles the writing
    // we need a scanner because the formatter overwrites the text
    // in the file, we will first store the contents of the file
    // into an ArrayList, and then rewrite those scores along with our new score
    // when we write to the file
    public static void writeToFile(String text){
        // wrap the scanner and formatter in a try except
        try {
            // because if there are any issues, we can report them
            // create a new scanner
            Scanner lines = new Scanner(new File("Assets/LB.txt"));
            // make a new list of highscores
            ArrayList<String> highScores = new ArrayList<>();
            // while there is another score in the file
            while (lines.hasNextLine()){
                // add them to the list of scores
                highScores.add(lines.nextLine());
            }
            // create a new formatter to write to the file
            Formatter output = new Formatter("Assets/LB.txt");
            // for each score we have saved
            for (int i = 0; i < highScores.size(); i++){
                // rewrite them
                output.format(highScores.get(i));
                // add delimeter newline
                output.format("\n");
            }
            // then we add our text at the end
            // %s means we format as a string
            // https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
            output.format("%s", text);
            // close the scanner and the filewriter
            output.close();
            lines.close();
        // if there is an error, we notify the user
        } catch (Exception error) {
            System.err.println("ERROR : File not found");
        }
    }
}