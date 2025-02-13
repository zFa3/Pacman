// import statements
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.Timer;

// public class creates the JPanel board
// this class is a panel that gets layered onto the PacManGui JFrame
// this class handles most of the board mechanics, such as pathfinding
// for ghosts and the player's movement
public class Board extends JPanel implements KeyListener, ActionListener{
	
	// define some search parameters
	// 1e9 -> 10 ^ 9, a number large enough such that it never
	// occurs in normal pathfinding, so it is used to deter
	// ghosts from passing through walls
	public static final int inf = (int)1e9;
	// is the game on easy mode (default is easiest mode)
	private static int difficulty = 1;
	private static int level;

	// total number of rows/columns
	private static final int ROWS = 25;
	private static final int COLS = 27;

	// number of lives the player has
	private static int lives = 3;
	private static boolean load = false;

	// the delay between ticks in ms
	// called (Ticks per second) or TPS
	private final int TPS = 20;
	// the milliseconds per tick
	private final int msPerTick = 1000/TPS;
	// the directions used in the pathfinding algorithm
	// these are the four cardinal directions (delta x delta y)
	private static final int[][] direction = new int[][] {new int[]{0, -1}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{1, 0}};

	// the gameCounter allows for different characters to have different speeds
	private static int gameCounter = 0;
	// speedUpTimer indicates whether or not pacman is fast
	private static int speedUpTimer = 0;
	// whether the ghosts are slowed down
	private static int slowDownTimer = 0;
	// whether pacman can eat the ghost
	private static int powerUpTimer = 0;

	// create the timer
	private Timer gameTimer = new Timer(msPerTick, this);
	
	// create the array that holds the cells
	private static Cell[][] mazeArray = new Cell[ROWS][COLS];
	
	// create the pacman
	private static Mover pacman;

	// create the ghost characters
	private static Mover[] ghostArray = new Mover[3];
	
	// create a variable that counts the number of pellets
	private static int pellets = 0;

	// the lower the number, the faster the movements
	// these numbers represent on which tick these movers will move
	// For example, 5 means the Pacman will move every 5th tick, since
	// the ticks are much faster now.
	private static int pacmanSpeed = 6;
	private static int pacmanFastSpeed = 3;
	// the ghost and the pacman speed should be factors / multiples
	// of each other otherwise their movements will be out of sync
	private static int ghostSpeed = 6;
	// or the second powerupTimerpowerUpTimer is to slow down the ghosts
	private static int ghostSlowSpeed = 12;
	
	// otherwise, we cannot move in that direction.
	// however, some poeple might have pressed that
	// one move too early, which is a bit unfair
	
	// To deal with this, we add the move that the player pressed
	// to a variable 'queued move' and later we check if we can make
	// that move, and if we can, we will do so. However, there is
	// a limit, called the movesUntilDecay, which means the move
	// pressed will only work if it is within 1 step of the actual
	// turn

	// initialize the queued direction
	private int[] queuedDirection = {-1, -1};

	// there is a 1 tick 'generous gap' where if you press a button
	// 1 tick too early, it will still turn if the opportunity is
	// available, otherwise, it "expires"
	private int movesUntilDecay = 1;

	// an integer that holds the score currently achieved
	private int score = 0;
	
	// constructor method for the game board
	public Board(int difficultyParameter, int levelParameter) {
		
		// set some parameters
		setLayout(new GridLayout(ROWS, COLS));
		setBackground(Color.BLACK);
		
		// set the difficulty accordingly
		difficulty = difficultyParameter;
		level = levelParameter;

		// load the board by reading the file
		loadBoard(load);
	}
	
	// method that reads the file and loads to Cell[] mazeArray
	// this is used later ot show the board
	private void loadBoard(boolean load) {
		// starting point, were at the zeroth row
		int row = 0;
		if (!load) pellets = 0;

		// create a scanner object
		Scanner inputFile;
		
		// try catch block to catch any errors
		// in case the file doesnt exist, there are bugs etc.
		try {
			
			// scan the file instead of the console
			inputFile = new Scanner(new File("Assets/maze" + level + ".txt"));
			
			// while the file has more rows
			while (inputFile.hasNext()) {
				
				// for each line, push it to an array to characters 
				char[] charArray = inputFile.nextLine().toCharArray();
				
				// for each column (character) in the row, we add it to the array
				for(int column = 0; column < charArray.length; column++) {
					
					// add the characters to the array
					if (load){
						// if we just died, we dont want to respawn the pellets again, just
						// reuse the icons from the previous mazeArray
						mazeArray[row][column] = new Cell(mazeArray[row][column].getItem());
					} else {
						// else we create new Cells
						mazeArray[row][column] = new Cell(charArray[column]);
					}

					// if the item in the file is a pellet, then we increment the gameCounter
					// and we store it in the variable
					
					// if we arent restarting the game, (because we lost) then we dont recount 
					// the score since its already done
					if (!load){
						// pellet increment for cherries/other powerupTimerpowerUpTimers
						// you can customize how much score they give
						if (charArray[column] == 'F') {
							// each pellet is worth one point
							pellets++;
						} else if(charArray[column] == 'C' || charArray[column] == 'B') {
							// both cherry is worth 50 points
							pellets += 50;
						}
					}
					// if the charcter is pacman
					if(charArray[column] == 'P') {
						// the third arguement indicates whether or not the mover is pacman.
						pacman = new Mover(row, column, true);
						// set the icons, directions, etc
						pacman.setIcon(Icons.PACMAN[0]);
						pacman.setDirection(0);
					// otherwise if it is a ghost...
					} else if(charArray[column] == '0' || charArray[column] == '1' || charArray[column] == '2'){
						// is it a ghost, and if so, we add it to the array
						int gNum = Character.getNumericValue(mazeArray[row][column].getItem());
						ghostArray[gNum] = new Mover(row, column, false);
						ghostArray[gNum].setIcon(Icons.GHOST[gNum]);
					}
					// add the array to the board
					add(mazeArray[row][column]);
					
				} // end of for loop
				row++; // increment the row value
				
			} // end of while loop
			inputFile.close(); // close the input
			 
		} catch(FileNotFoundException event) {
			// if there is an error with the text file
			// output the error + stack trace
			System.out.println("File Error:" + event);
		}
	}

	// this method handles the keypresses
	@Override
	public void keyPressed(KeyEvent key) {
		// if the game is still running but the timer isn't
		if(gameTimer.isRunning() == false && pacman.isDead() == false){
			// we start the timer
			gameTimer.start();
		}
		// for sake of clarity, not pacman.isDead() is not used
		else if(pacman.isDead() == false && score != pellets && key.getKeyCode() > 36 && key.getKeyCode() < 41) {
			// get the direction that the player want to go
			// since the ascii code for the arrow keys is 37-40,
			// subtracting 37 normalized these numbers to 0-3
			int direction = key.getKeyCode() - 37;
			
			// delta col/delta row is the change in the position
			int deltaColumn = 0;
			int deltaRow = 0;
			
			// which direction was pressed?
			if(direction == 0) deltaColumn = -1; // left
			if(direction == 1) deltaRow = -1; // up
			if(direction == 2) deltaColumn = 1; // right
			if(direction == 3) deltaRow = 1; // down

			// change pacman's movement, and change its sprite baesd off the movement
			if (mazeArray[pacman.getRow() + deltaRow][pacman.getColumn() + deltaColumn].getIcon() != Icons.WALL) {
				// set the icon and the direction accordingly
				pacman.setIcon(Icons.PACMAN[direction]); 
				pacman.setDirection(direction);
			} else {
				// here, we add the invalid move to use later if we can
				queuedDirection = new int[] {direction, movesUntilDecay};
			}
		}
	}
	
	// for each tick from the timer
	public void actionPerformed(ActionEvent event) {
		// if the source was the timer
		if(event.getSource() == gameTimer) {
			// these two if statements mean the ghosts
			// and pacman can have different speeds
			if (gameCounter % (speedUpTimer > 0 ? pacmanFastSpeed : pacmanSpeed) == 0){
				// move both pacman and the ghosts
				performMove(pacman);
				// because if we want the speed to increase, the delay should increase too
				queuedDirection[1]--;
			}
			// if the tick is the ghosts tick (for example the 5th tick)
			// it will perform a move
			if (gameCounter % ghostSpeed == 0){
				// if the ghost is on slowdown, then we move the ghost every other ghost tick
				if (gameCounter % (slowDownTimer > 0 ? ghostSlowSpeed : ghostSpeed) == 0){
					// move both pacman and the ghosts
					moveGhosts();
				}
			}
			// game gameCounter - Theoretically can integer overflow but
			// no one will keep the game running for 2 years to get int overflow
			gameCounter++;
			speedUpTimer--;
			slowDownTimer--;
			powerUpTimer--;
			// if the powerup timer is no longer active, then
			// we can set all the ghosts back to normal, and they will return
			// to chasing the player
			if (powerUpTimer < 0){
				for (Mover ghost : ghostArray){
					// set dead to false
					ghost.setDead(false);
				}
			}
			// update the label (timers, score, lives) every tick
			PacManGUI.updateLabel();
		}
	}

	// perform the move (or queue the move if we are the player)
	private void performMove(Mover mover) {
		// get the queued move deltas
		int dc = 0; int dr = 0;
		// get the queued direction deltas
		if(queuedDirection[0] == 0) dc = -1;
		else if(queuedDirection[0] == 1) dr = -1;
		else if(queuedDirection[0] == 2) dc = +1; // plus sign for clarity
		else if(queuedDirection[0] == 3) dr = +1; // positive means you move towards bottom / right

		// get the cell that its currently in
		Cell currentCell = mazeArray[mover.getRow()][mover.getColumn()];
		// get the next row and next column
		Cell nextCell = mazeArray[mover.getNextRow()][mover.getNextColumn()];
		// get the next QUEUED row and next column
		Cell nextQueuedMove = mazeArray[mover.getRow() + dr][mover.getColumn() + dc];

		// this if statement allows for the door on the edge of the map,
		// which is only possible through the gap that is the door, all
		// the other cells have walls
		if (mover.getColumn() == 1) {
			// set the column to the other side
			mover.setColumn(ROWS);
			mover.setDirection(0);
			mazeArray[12][0].setIcon(Icons.DOOR);
			// this ensures the pacman is facing the right way when he passes through a door
			// This fixes a bug where you can quickly go backwards just as you go through the door
			// allowing you to move in a different direction than where you are headed
			if (mover.isPacman) pacman.setIcon(Icons.PACMAN[pacman.getDirection()]);
		} else if(mover.getColumn() == ROWS) {
			// set the column to the other side
			mover.setColumn(1);
			mover.setDirection(2);
			mazeArray[12][26].setIcon(Icons.DOOR);
			if (mover.isPacman) pacman.setIcon(Icons.PACMAN[pacman.getDirection()]);
		}

		// move the game won check outside the movement checks
		if (score == pellets && !pacman.isDead()) {
			// if there are no more pellets, then we win the game!
			JOptionPane.showMessageDialog(this, "YOU WON!");
			if (level == 3){
				// if we won the game, then add the score 
				FileWriter.writeToFile(Menu.getPlayerName() + Leaderboard.delimeter + gameCounter + Leaderboard.delimeter +(Menu.totalScore + (int)score));
				resetter();
				new Menu();
				// prevents a duplicate Icon from appearing on the
				// next level (took a while to debug)
				return;
			} else {
				// a bunch of variables to reset after winning the game
				// stop the timer
				gameTimer.stop();
				load = false;
				// reset the number of lives
				lives = 3;
				// close this current window
				Menu.LV.setVisible(false);
				Menu.LV.dispose();
				this.removeAll();
				// icnrement the total score by score
				Menu.totalScore += score;
				// then we reset the score
				score = 0;
				// reset the position of the pacman
				pacman.setRow(16);
				pacman.setColumn(14);
				// and rseet all the timers to prevent powerups from leaking into other levels
				powerUpTimer = -1;
				speedUpTimer = -1;
				slowDownTimer = -1;
				// finally we create the new GUI
				Menu.LV = new PacManGUI(level + 1, level + 1);
				// return statement prevents a duplicate Icon from appearing on the
				// next level (took a while to debug)
				return;
			}
		}

		// try to used to queued move
		if (queuedDirection[1] > -1 && nextQueuedMove.getItem() != 'W' && nextQueuedMove.getItem() != 'G' && mover.isPacman) {
			// if it is a valid move then we will use it
			// and we have to change the direction
			mover.setDirection(queuedDirection[0]);
			if (mover.isPacman){
				// if the mover is pacman, then we also have to change the icon
				mover.setIcon(Icons.PACMAN[mover.getDirection()]);
			}
			moveValidation(mover, currentCell, nextQueuedMove);
		// if the item isnt a wall, and we dont have a queued move  AND  It is NOT (element is gate and mover coming from top)
		} else if (nextCell.getItem() != 'W' && !(nextCell.getItem() == 'G' && (mover.getNextRow() - mover.getRow() == 1))) {
			// move normally
			moveValidation(mover, currentCell, nextCell);
		}
	}

	// this method moves the Mover object to a different cell
	// also includes some validation code such as doors and walls
	private void moveValidation(Mover mover, Cell currentCell, Cell nextCell){
		// if it isnt a piece of food, then we have to replace it after the ghost leaves
		if (mover != pacman && currentCell.getItem() == 'F'){
			currentCell.setIcon(Icons.FOOD);
		// same goes for the cherries and the powerups
		} else if (mover != pacman && currentCell.getItem() == 'C'){ currentCell.setIcon(Icons.CHERRY);
		} else if (mover != pacman && currentCell.getItem() == 'G'){ currentCell.setIcon(Icons.GATE);
		} else if (mover != pacman && currentCell.getItem() == '+'){ currentCell.setIcon(Icons.POWERPELLET);
		} else if (mover != pacman && currentCell.getItem() == 'B'){ currentCell.setIcon(Icons.BLUECHERRY);
		} else { currentCell.setIcon(Icons.BLANK); // otherwise there isnt anything on the cell
		}
		// same code as above
		mover.move();
		// set the current cell to what it used to be
		currentCell = mazeArray[mover.getRow()][mover.getColumn()];
		// if we collided with a ghost and the powerup timer isn't active
		// we lost a life
		if (collided() && powerUpTimer < 1) {
			// run the 'gameOver' method
			gameOver();
		// otherwise, we collided but the powerUp timer is active. This means
		// that we ate a ghost while the powerup was active, so we dont lost and reset
		// the ghost back to its 'house'
		} else if (collided()){
			boolean respawned = true;
			for (Mover ghost : ghostArray){
				// for each ghost, if it was the one that we ate, then we return it to its
				// house in the center, and we set dead to false. this means as soon as the ghost
				// respawns, it is able to start eating us again
				if (ghost.getRow() == pacman.getRow() && ghost.getColumn() == pacman.getColumn() && ghost.isDead()){
					mazeArray[ghost.getRow()][ghost.getColumn()].setIcon(Icons.PACMAN[pacman.getDirection()]);
					// set dead to false
					ghost.setDead(false); respawned = false;
					// we set the row and the column
					ghost.setRow(13);
					ghost.setColumn(15);
				}
			}
			// theoretically shouldn't happen
			if (respawned){ gameOver(); }
			// and otherwise we increase our score for eating a ghost
			else {
				// and update the label to reflect that
				Menu.totalScore += 25;
				PacManGUI.updateLabel();
			}
		} else {
			// set the next cell to the player / ghost icon
			currentCell.setIcon(mover.getIcon());
		}
	
		// if we just ate some food
		if (mover == pacman && nextCell.getItem() == 'F') {
			// we increment the food
			score++;
			// and we mark the food as eaten
			currentCell.setItem('E');
		} else if (mover == pacman && nextCell.getItem() == 'C') {
			// we increment the food by 50 since its a cherry
			score += 50;
			speedUpTimer = 200;
			// and we mark the food as eaten
			currentCell.setItem('E');
		} else if (mover == pacman && nextCell.getItem() == 'B') {
			// we increment the food by 50 since its a cherry
			score += 50;
			// we activate the slow down timer
			slowDownTimer = 200;
			// and we mark the food as eaten
			currentCell.setItem('E');
		} else if (mover == pacman && nextCell.getItem() == '+') {
			powerUpTimer = 200;
			for (Mover g : ghostArray) g.setDead(true);
			// and we mark the food as eaten
			currentCell.setItem('E');
		}
	}


	// This method is the one that is responsible for the decisions made in pathfinding, from
	// the difficulty randomness to the flee mode when the player eats a power pellet
	// All the decisions are made in this method
	// 
	// In fact, this pathfinding also pathfinds around other ghosts, by trating them as walls
	// This is because the ghosts will automatically pathfind differently to the player, not only
	// making the movements more unique, but also much more challenging as the player can now easily get trapped
	// by the ghosts.
	private int pathfinding(Mover mover, int targetRow, int targetCol, int difficulty, boolean isGhostDead){
		// the minimum and the maximum are the ones that keep track of the best and worst moves
		// the best move is used when the AI is on it's hardest difficulty, and the worst move is 
		// used when the pacman is fleeing from a player that ate a power pellet
		int minimum = inf;
		int maximum = -inf;

		// and these are the two variables that store their respective moves
		int bestDirection = -1;
		int worstDirection = -1;

		// the int[] list all directions is used to slightly randomize some of the moves that the
		// ghosts make
		int[] allDirections = new int[4];
		// This is the magic function that generates all the values - essentially the distance
		// from each cell to pacman. This lets us only do one BFS/Flood fill search for all three ghosts
		// nat that it matters for this game
		int[][] values = bfs(targetRow, targetCol);
		// for each direction, find the score (distance from pacman)
		for (int i = 0; i < 4; i++){
			// get the value from the int[][] values
			int score = values[mover.getRow() + direction[i][0]][mover.getColumn() + direction[i][1]];
			// if its the best move, store it
			if (score < minimum){
				// set the new minimum to the value of the move
				minimum = score;
				bestDirection = i;
			}
			
			// if its the worst move, store it
			if (score > maximum && score != inf){
				// set the new maximum to the value of score
				maximum = score;
				worstDirection = i;
			}

			// if it is a legal move then also store it
			if (score < inf){
				allDirections[i] = score;
			}
		}
		// if the ghost is dead (if the player ate a power pellet) then we move away by
		// choosing / returning the worst move
		if (isGhostDead) return worstDirection;
		// otherwise wehave a random chance to return a subpar move with the Math.random() function
		// however if the difficulty (or level) is 3, difficulty - 3 is zero, there fore there
		// is no randomness
		if ((Math.abs(difficulty-3) * Math.random()) < 0.2 + (difficulty == 1 ? 0.5 : 0)){
			return bestDirection;
		}
		// if we didn't got lucky and the ghosts didn't choose the
		// best direction, they have a chance to choose from any
		// legal direction
		for (int i = 0; i < 4; i++){
			// just choose the first direction that is legal, and preferrably not backwards 
			if (allDirections[i] != 0 && (mover.getDirection() - i) != 2 && allDirections[i] != inf){
				// return the move that satisfies our conditions
				return i;
			}
		}

		// if we must, then we will choose a move that goes opposite to where we are going
		// this line is just the same for loop as above, just without the commens on the inside
		for (int i = 0; i < 4; i++){if (allDirections[i] != 0 && allDirections[i] != inf){return i;}}

		// should't come to this but if there are no legal moves then we just return
		// zero by default. If there is a wall in that direction the ghost wont move at all
		// this is to prevent breaking holes in the walls, ruining the map / game
		return 0;
	}

	// this method finds the optimal path for the ghosts
	// A feature of this pathfinding is that I made the
	// ghosts unable to pathfind through other ghosts,
	// which is a feature, meaning they will have to find other paths
	// towards the player, often cutting them off or trapping them in corridors
	// as an example, level 2 its quite effective

	// I've also added pathfinding through doors, as its much more difficult to use
	// the doors to evade the ghosts, and they will now come for you through the doors from the other
	// side of the map. I've been snuck up on many times myself when I dont pay attention
	private static int[][] bfs(int targetRow, int targetCol){
		// knowledge from previous learning (graph theory)
		ArrayList<int[]> newmoves = new ArrayList<>();
		ArrayList<int[]> moves = new ArrayList<>();
		// Set<String> visited = new HashSet<String>();
		boolean[] visited = new boolean[ROWS * COLS];

		int[][] values = new int[ROWS][COLS];
		// fill the entire array with infinite, so that if the pacman cannot pathfind to some parts
		// of the map (example paths that are blocked by ghosts) the ghosts dont pathfind further from the pacman
		// since the default for int arrays zero
		for (int i = 0; i < ROWS; i++) { for (int j = 0 ; j < COLS; j++) values[i][j] = inf; }
		int distance = 0;
		
		// add pacman's position
		newmoves.add(new int[] {targetRow, targetCol});
		visited[targetRow * ROWS + targetCol] = true;
		values[targetRow][targetCol] = distance;
		
		// this double move list I made up. not sure if i've seen this somewhere else
		while (newmoves.size() != 0) {
			// while the number of legal moves isn't zero
			// increment the distance by one
			distance++;
			// copy the legal moves to a new list
			moves = new ArrayList<>(newmoves);
			// clear the other list
			newmoves.clear();
			
			// while this list isn't zero
			while (moves.size() != 0) {
				// get the first move from the list
				int[] current = moves.get(0);
				// remove the move to prevent duplicates
				moves.remove(0);
				
				// all 4 possible directions)
				for (int i = 0; i < 4; i++) {
					// get the new row and the new column
					int newRow = current[0] + direction[i][0];
					int newCol = current[1] + direction[i][1];
					// if the item is a door, then we look through to see if there is a path
					if (mazeArray[newRow][newCol].getItem() == 'D'){
						// if it is on the left half of the board
						if (newCol < 12) newCol = ROWS;
						// otherwise its on the other side of the board
						else newCol = 1;
					}
					// not 'else if' since we still want to pathfind when we go through a door
					if (isGhostSafe(newRow, newCol) && !visited[newRow * ROWS + newCol] && !isGhost(newRow, newCol)) {
						// VERY IMPORTANT LINE
						// This line of code prevents the search from researching this square.
						// without it, it will try to search hundreds of thoudsands of paths each tick
						visited[newRow * ROWS + newCol] = true;
						values[newRow][newCol] = distance;
						// add any legal moves to the list to check next iteration
						newmoves.add(new int[] {newRow, newCol});
					}
				}
			}
		}
		
		// set all the walls to infinite (1e9) so the ghosts dont
		// pathfind through them
		for (int i = 0; i < ROWS; i++){
			for (int j = 0 ; j < COLS; j++){
				// if it is a wall or another ghost
				if (!isGhostSafe(i, j) || isGhost(i, j)){
					// set value to inf
					values[i][j] = inf;
				}
			}
		}
		// return the table of values
		return values;
	}
	
	// this method was just to make the main pathfinding method a little less cluttered
	private static boolean isGhostSafe(int newRow, int newCol){
		char item = mazeArray[newRow][newCol].getItem();
		return (
			// cannot be a wall
			item != 'W'
			// however, we can pathfind through the gate since the
			// player is also unable to pass it, the only time we
			// need to pathfind through is in the beginning
		);
	}
	
	// this method checks if the current square is a ghost or not
	private static boolean isGhost(int newRow, int newCol){
		return (
			// cannot be another ghost, this allows the ghosts to pathfind different routes,
			// cutting off pacman
			(newRow == ghostArray[0].getRow() && newCol == ghostArray[0].getColumn()) ||
			(newRow == ghostArray[1].getRow() && newCol == ghostArray[1].getColumn()) ||
			(newRow == ghostArray[2].getRow() && newCol == ghostArray[2].getColumn())
		);
	}

	// check if we lost the game
	private boolean collided(){
		// for each ghost in the array
		for(Mover ghost : ghostArray){
			// if the row and the column is the same
			if(ghost.getRow() == pacman.getRow() && ghost.getColumn() == pacman.getColumn())
				return true;
		}
		// otherwise we are fine
		return false;
	}
	
	// this is the method that handles game over. when we lose to a ghost
	// there are two scenarios. first, we lost the entire game, and we will reset
	// aff of the variables and open a new menu. The second case is when we just lose a life
	// and we still have some more left. This means we dont reset the scores, but we do reset
	// the position.
	private void gameOver(){
		// we decrement the number of lives by one
		lives--;
		// and we set load to true. This means we will now load from the mazeArray,
		// to retain the food that we've already ate. This is needed since in the beginning,
		// the mazeArray is null, leading to errors if we try to access them
		load = true;
		// if we have no lives after the decrement, we are at zero, and we have lost the game
		// this means we have to reset and reopen the menu
		if (lives == 0){
			// set the dead to true
			pacman.setDead(true);	
			
			// set the icon to the skull
			mazeArray[pacman.getRow()][pacman.getColumn()].setIcon(Icons.SKULL);
			// stop the timer and show the option pane
			JOptionPane.showMessageDialog(this, "GAME OVER");
			// write the score to the file if we have lost
			FileWriter.writeToFile(Menu.getPlayerName() + Leaderboard.delimeter + gameCounter + Leaderboard.delimeter + ((int)Menu.totalScore + score));
			// reset the variables
			resetter();
			// create a new menu
			new Menu();
		} else {
			// otherwise, we lost only a life, meaning we dont reset
			restart();
		}
	}

	// this method resets the variables, in all the ways you lose
	// so I dont have to rewrite this every time
	private void resetter(){
		// stop the timer
		gameTimer.stop();
		// reset all the variables
		queuedDirection = new int[]{-1, -1};
		load = false;
		lives = 3;
		Menu.totalScore = 0;
		score = 0;
		// remove all of the frames
		Menu.LV.setVisible(false);
		Menu.LV.dispose();
		this.removeAll();
		pacman.setDead(true);
		// set all the timers back to zero
		gameCounter = 0;
		powerUpTimer = 0;
		slowDownTimer = 0;
		speedUpTimer = 0;
	}

	// this method restarts the game, but doesn't reset the game. This happens when
	// you lose a life but not the entire game
	private void restart(){
		// https://stackoverflow.com/questions/1097366/java-swing-revalidate-vs-repaint
		this.removeAll();
        this.loadBoard(load);
        this.revalidate();
        this.repaint();
		// this pauses the game so you can
		// collect yourself. Just press any key
		// to start the game
		gameTimer.stop();
	}

	// method to move the ghost
	private void moveGhosts() {
		// for each ghost in the array
		for (int i = 0; i < 3; i++) {
			// fetch from the ghost array
			Mover ghost = ghostArray[i];
			int dir = pathfinding(ghost, pacman.getRow(), pacman.getColumn(), difficulty, ghost.isDead());
			// set the direction to the dir
			ghost.setDirection(dir);
			// if the POWER PELLET was eaten, then we set the ghosts
			// to blue, indicating the player can eat the ghosts
			if (ghost.isDead() && powerUpTimer > 0) ghost.setIcon(Icons.BLUEGHOST);
			// otherwise we return the color to normal
			else ghost.setIcon(Icons.GHOST[i]);
			if(!pacman.isDead())
			// stop the ghosts when the game is over
				performMove(ghost);
		}
	}

	//  Some getters for local variables to use later
	public int getScore(){ return score; }
	public int getLives(){ return lives; }
	// these are the getters that are used to show the information on the title screen
	// power ups, speed, flow and overall timer
	public int getPowerUpTimer(){ return (int)(powerUpTimer / (1000 / msPerTick)); }
	public int getSlowDownTimer(){ return (int)(slowDownTimer / (1000 / msPerTick)); }
	public int getSpeedTimer(){ return (int)(speedUpTimer / (1000 / msPerTick)); }
	public int getTimer(){ return (int)(gameCounter / (1000 / msPerTick)); }

	@Override // unused implemented methods
	public void keyTyped(KeyEvent event) {}
	@Override // unused implemented methods
	public void keyReleased(KeyEvent event) {}
}
