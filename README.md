I built a Pac-Man game in Java using Swing and the MVC design pattern. The game has a welcome screen, a difficulty selection screen, and a complete maze where you eat dots, dodge ghosts, and try to survive as long as possible by collecting white dots.The biggest thing I learned is that AI is just tool and that it doesn't always get things right the first time. Several times the AI gave me code that looked correct but had bugs I had to find myself/ asking what we're the logic that broke, for exmaple ghosts spawning on top of player charcater (Pac-Man) causing an instant game over, or resetGame() being called inside a loop over ghosts which caused a crash. I had to actually understand what the code was doing to catch those issues and describe them clearly enough for the AI to fix them.
I also learned that being specific in your prompts makes a huge difference. Because some vague prompts like "fix the ghosts" would alway giive me partial fixes. Specific prompts like "the ghost spawns on the same spot as Pac-Man which causes instant game over so could you please move the spawn points away" got me exactly what I needed. Another important note the MVC pattern made the project much easier to manage, because it was spilt across three classes. First being gameModel which handles all game logic and data with no Swing imports. GameView which reads from the model and draws everything the player sees. GameController handles keyboard input and runs the game loop using a Swing Timer. So in other words I could fix bugs in one file without worrying about breaking the others.


What the game is--
Pac-Man is a maze game where the player moves a character through a maze eating dots while avoiding ghosts. You lose a life every time a ghost touches you. The goal is to eat every dot in the maze before running out of lives.

What the Model stores (GameModel.java)--
Pac-Man's position and movement direction, a maze with walls, dots, and eaten tiles, a list of Ghost objects with their own positions as well as thier own movements, the player's score and high score, lives remaining, game state such as (game over/reset, paused), difficulty level, and a timer that tracks play time and freezes correctly when paused.

What the View draws (GameView.java)--
A welcome screen showing the game title and my name, a difficulty selection screen with three clickable buttons, the maze with blue walls and white dots small and or large, Pac-Man as a yellow that faces the direction he is moving, ghosts, lives, time, and difficulty, and when the player runs out of lives it shows an overlay.

What the Controller handles (GameController.java)--
Arrow key input to move Pac-Man, P key to pause and unpause, R key to reset back to the main menu,

