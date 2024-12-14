Pacman Made in Java  


This project was made for my computer science course,  
where we were given a base pacman game and our objective was  
to improve the game as much as possible  


FEATURES: 
    
		This game runs at 20 FPS, or 1 tick / 50 ms. The player and the ghosts move    
	 	much slower at 4 TPS, or 250 ms per move. This is to allow for speed powerups, simply to increasing or    
	 	decreading the frequency of these 'Move Ticks'    
    
	======= Variable game speed and powerups with speed changes    
    
			This feature essentially allows the ghost and the user to move at two different speeds    
			by allowing the pacman to move every Nth tick and the ghosts to move every Mth tick. N and M    
			are variables, and as long as a powerup is active (in my case a counter is larger than 0) these    
			numbers can be changed and the speed will correspondingly    
			This powerup wasn't too difficult to implement, as all I had to do was to decrease the value of    
			N or M to increase the speed of their respective characters    
    
	======= Move queuing (for better user experience)    
    
			This feature is pretty helpful for the user experience, and for someone who's tried both with / without    
			it makes a (in my opinion) pretty big difference in terms of user experience (UX). The way that this works    
			is that if the player makes a move that is curerntly illegal, then we store that move in a variable    
			so that if in the next tick we can move in that direction, then we will, so that there is a longer window    
			that you need to hit in order to turn    
    
	======= Search algorithm (flood fill) calculates best move    
    
			The search algorithm is essentially the core of the Ghosts AI, the bread and butter of the game. Every time    
			the ghosts make a move, the program runs the flood fill to determine the best move to make. I chose to write    
			BFS as opposed to DFS or Dijksra's/A* because it is simpler to implement. For each ghost tick, it runs a BFS    
			search starting at the pacman. Each new 'layer' of cells it meets will be assigned an increasing large number,    
			indicating the distance from the cell to Pacman. Because it fills the entire grid, we only need to run this search    
			once, and we can use the values that it returns for all the ghosts. By just simply moving in a direction that lowers    
			the number, the ghost automatically pathfinds its way around obstacles and other ghosts towards the pacman.    
			In fact, adding pathfinding away from pacman during the POWERUP is very similar, by moving towards the direction with    
			the largest number, the ghost will now move away from pacman    
    
			On the hardest difficulty (Level 3) there are many heuristics that I used to make the ghosts 'Intelligent'.    
    
			Here are some heuristics that I've added to the pathfinding that makes    
			it seem intelligent.    
    
			- Ghosts pathfind around each other, allowing for better coordination    
				This one was pretty crucial for both the difficulty and the coordination of the ghosts. The reason is due to the    
				fact that because the ghosts start out together, their most efficient route to pacman is most likely going to be the    
				exact same path, or subpath, as each other. It definitely isn't the best    
				strategy for ghosts because it won't matter if there is 1 ghost or 3 ghosts blocking a path, the result is the same.    
    
				Instead, what you want is each ghost to block a different path, cornering the pacman. This can be done in multiple ways    
				but the way that I implemented was to treat the ghosts as walls during the pathfinding (BFS phase). This means the ghosts    
				will pathfind around each other, as the path that passes through another ghost is no longer available. This also fixes    
				another issue which was ghosts would overlap on top of each other. However,    
				now that they are treated like walls, the no longer do so    
    
			- Ghost pathfinding through doors, allows for ghost shortcuts    
				This one is simple but powerful. The code for this is pretty much identical to the one used for pacman, but implemented    
				in the search.    
    
			- Added Pathfinding away from the player when the ghosts are blue    
				As mentioned above, a couple lines of code that reverses the condition when the power up is active. Makes the     
				just slightly harder and a lot more frustrating    
    
			- Added ghost revival after being eaten    
				Respawning them back to their house but they are no longer blue    
				meaning you can be chasing a ghost while having another one chase you    
    
			- Added Color change when power up is active    
				The ghosts turn blue when the powerup is active and they    
				turn back when the powerup is over    
    
			- Added a One-Way Gate for the ghost 'house'    
				Added a check to ensure the only thing passing    
				through the gate comes from below    
    
	======= and some smaller features    
		- Cherries add 50 points and act as a speed powerup    
		- Added a Menu screen with a mouselistener that detects mouse clicks to start the game    
		- Added Timers, score, and lives at the bottom of the screen to help the player visualize how much time / lives they have left    
		- Added Difficulty levels, easy medium, hard for the AI    
		- Level Advancing, total of 3 levels    
		- Leaderboard, uses file reading and writing to display the highest scores    
		- Storing scores in a file for use later (leaderboards/highscores)    
		- Some custom images, like the new door, gate, larger sprites, power pellets, blue cherry etc.    
