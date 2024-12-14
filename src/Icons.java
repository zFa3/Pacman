// this method loads all of the elements, retrieving all of the imageicons
// import the swing imageicon package
import javax.swing.ImageIcon;

public class Icons {
    // create all the images from the images folder
    public static final ImageIcon POWERPELLET = new ImageIcon("images/POWER.bmp");
    public static final ImageIcon GATE = new ImageIcon("images/GATE.bmp");
    public static final ImageIcon FOOD = new ImageIcon("images/FOOD.bmp");
    public static final ImageIcon BLANK = new ImageIcon("images/BLACK.bmp");
    public static final ImageIcon TRACER = new ImageIcon("images/BLACK2.bmp");
    public static final ImageIcon DOOR = new ImageIcon("images/DOOR.gif");
    public static final ImageIcon SKULL = new ImageIcon("images/SKULL.bmp");
    public static final ImageIcon CHERRY = new ImageIcon("images/CHERRY.bmp");
    public static final ImageIcon BLUEGHOST = new ImageIcon("images/BLUEGHOST.bmp");
    public static final ImageIcon BLUECHERRY = new ImageIcon("images/BLUECHERRY.bmp");
    public static ImageIcon WALL = new ImageIcon("images/WALL.bmp");

    // pacman's left sprite is zero, moving clockwise as the numbers increase
    public static final ImageIcon[] PACMAN = {
        new ImageIcon("images/PacMan0.gif"),
        new ImageIcon("images/PacMan1.gif"),
        new ImageIcon("images/PacMan2.gif"),
        new ImageIcon("images/PacMan3.gif"),
    };
    
    // the different ghost variations, (ex. different colors, designs)
    public static final ImageIcon[] GHOST = {
        new ImageIcon("images/GHOST0.bmp"),
        new ImageIcon("images/GHOST1.bmp"),
        new ImageIcon("images/GHOST2.bmp"),
    };
}
