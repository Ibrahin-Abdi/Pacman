Prompt 1 — MVC Skeleton

I'm building Pac-Man in Java using Swing and the MVC design pattern, split across three files. GameModel.java handles all game logic and data with no Swing imports. GameView.java reads from the model and draws everything the player sees. GameController.java handles keyboard input and runs the game loop using a Swing Timer. For now just create the three class shells with placeholder comments describing what each class will do. The program should compile and open a blank window.

Prompt 2 — Build the Model

Fill in GameModel.java. The model should track: Pac-Man's position, a grid-based maze, the dots Pac-Man can eat, the four ghosts and their positions, the player's score, and lives remaining starting at 3. Add logic to move Pac-Man in the direction the player is holding, check wall collisions so Pac-Man can't move through walls, eat dots when Pac-Man moves over them, move the ghosts, and detect when a ghost touches Pac-Man. No Swing imports.

Prompt 3 — Build the View

Fill in GameView.java. It should take a reference to the model and draw everything the player sees: the maze walls, the dots, Pac-Man as a yellow circle with a mouth, the four ghosts each as a different color, the score, and lives remaining. Show a centered game over message when the game ends. The view should only read from the model and never change game state.

Prompt 4 — Wire the Controller

Fill in GameController.java. Add keyboard controls so the player can move Pac-Man with the arrow keys. Add a game loop using a Swing Timer that updates the model each tick and redraws the view. Stop the loop when the game is over.

Prompt 5 — Fixing the "Tiny Maze, Big Pac-Man" Problem
"Hey, I ran the code and it's a bit of a mess visually. The maze is super small in the top corner, but Pac-Man is huge and floating way off to the side. I think it’s because the maze is using small numbers (like 1, 2, 3) and the window is using pixels. Can you update GameView.java to use a TILE_SIZE variable (maybe 30?) so the maze scales up to fill the screen? Also, make sure Pac-Man stays inside the lines of that maze and the window size matches the maze size."

Prompt 6 — Making Movement Feel "Real"
"Right now, Pac-Man only moves one 'hop' when I press a key, and it feels really clunky. In the real game, he keeps moving until he hits a wall. Can you update GameModel.java so that once I press a direction, he keeps going that way every time the timer ticks? Also, please fix it so he doesn't just teleport—I want him to slide from one spot to the next smoothly."

Prompt 7 — Adding the Ghosts and HUD
"I want to get the ghosts moving now. Can you add logic to GameModel.java so the four ghosts move around on their own? They don't need to be smart yet—maybe they just pick a random direction when they hit a wall. Also, my Score and Lives text are kind of hard to see or overlapping the maze. Can you move the text to the very bottom of the screen so it's out of the way?"

Prompt 8 — Expanding the Maze and Fixing "Off-Track" Movement
Error Observed: The maze is too small (only one lane) and it’s possible to accidentally move off the tracks because the collision checking is a bit loose.
Prompt:

"The maze is way too small—it’s just one big loop! Can you update the maze array in GameModel.java to be a 20x20 grid that looks more like the real Pac-Man level with intersections and boxes? Also, refine the movement logic so Pac-Man stays perfectly centered in the lanes. He shouldn't be able to turn unless he is exactly at an intersection, which should stop him from clipping into walls."

Prompt 9 — Mouth Direction and More Enemies
Error Observed: Pac-Man is always facing right, even when moving left or up. Also, the ghosts are overlapping and moving off the grid.
Prompt:

"Visual fix: Can you update GameView.java so Pac-Man’s mouth faces the direction he is actually moving? Also, let's add 4 ghosts total. Make sure each ghost starts in the center of the maze and has logic to stay inside the hallways just like Pac-Man, so they don't go flying off into the blue walls."

Prompt 10 — Pause, Reset, and High Score
Feature Request: Adding game controls and a way to track the best score.
Prompt:

"I need a way to stop the game. Can you add a 'Pause' feature using the P key and a 'Reset' feature using the R key? Update the GameController to listen for these. Also, add a highScore variable to GameModel.java. Every time the game ends, if the current score is higher than the high score, save it and display it at the bottom next to the current score."

---important---My game has two bugs I can't figure out. First, Pac-Man keeps glitching straight through the blue walls — he should only ever move on the black paths. Second, the ghosts randomly freeze and stop moving completely. Can you fix GameModel.java so that wall collision actually checks the correct next tile in the grid instead of just adding a few pixels, and rewrite the ghost AI so it always finds a valid open direction at every intersection so it can never get stuck? The ghosts should actively chase me."

Prompt 12 — Ghost Speed & Visual Upgrade

"Two things. First, the ghosts are moving too fast and I can't dodge them — can you slow them down slightly in GameModel.java so they move at about half of Pac-Man's speed? They should still feel like a threat but give me a chance to react. Second, the ghosts look like plain colored circles which is boring. Can you update GameView.java to draw them like real Pac-Man ghosts — rounded top, wavy bottom, and two small white eyes with colored pupils? Keep the four different colors."

Prompt 13 — Tunnel Wrap-Around

"In the real Pac-Man game, if you walk off the left edge of the map you come out on the right side, and vice versa. Can you add this tunnel effect to GameModel.java? When Pac-Man's x position goes past the right edge of the grid, wrap him to the left edge, and the same in reverse. Do the same for the ghosts too so they can also use the tunnel to chase me."

Prompt 14 — Completion Timer & Leaderboard

"When I eat every dot and beat the level I want a leaderboard to pop up. Add a timer to GameModel.java that starts when the game starts and stops when the last dot is eaten. Store the top 5 completion times. In GameView.java, when the game is won, show a leaderboard screen in the center of the maze listing those top 5 times in seconds, ranked 1st to 5th. Make it look clean — dark background panel, gold text for 1st place, white for the rest."

porblme
"I found a bug where the timer keeps counting up even when the game is paused. When I press P to pause the game, everything freezes but the timer in the HUD keeps going, so my finish time ends up being longer than it should be. Can you fix it so the timer completely freezes the moment I pause, and only continues counting from where it left off when I unpause?"
What I asked the AI to fix: I told the AI that my timer was not stopping when I paused the game and asked it to make the timer freeze when I press pause and continue from the same number when I unpause.

The tunnel wrapping works on the left side — if I walk off the left edge I appear on the right — but the right side doesn't work at all. I just get stuck at the right wall and nothing happens. Can you fix it so both sides work?"
What I asked the AI to fix: I told the AI that walking through the left side of the map worked fine but the right side was broken and I just hit a wall instead of teleporting. I asked it to make both sides work the same way so I can go through either edge and come out the other side.

![alt text](image.png)

Problem: In the Pac-Man game, the enemy ghosts do not properly chase the player and do not correctly wrap through tunnels — instead of reappearing on the opposite side, they disappear. Additionally, all three difficulty levels must function correctly with consistent enemy behavior.
Solution: Update the enemy movement logic to actively track and move toward the player's position, implement the same tunnel-wrap collision detection for enemies that already works for the player, and ensure all three difficulty levels apply the corrected chase and tunnel behavior with appropriately scaled enemy speed or aggression.
