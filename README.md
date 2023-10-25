# xo_game_android

### Design Concepts
The design of this game based on the global Tic Tac Toe rules and from requirements of internship selection's exercise

### Details of the Game
1. This game (Tic Tac Toe) can play on 2 modes, included with multiplayer and AI.
2. On multiplayer mode, players can choose the size of the board as follow.
   - 3x3 (3 in row)
   - 4x4 (3 in row)
   - 5x5 (4 in row)
   - 6x6 (5 in row)
   - 7x7 (6 in row)
   - 8x8 (6 in row)
   - 9x9 (6 in row)
   - 10x10 (6 in row)
3. On AI mode, player will play with an AI (Artificial Intelligence) which only available in the 3x3 board size.
4. Players can check the game history from see game details history choice and can watch the replay of the game directly on the screen. This use case used SQLite database to collect all game histories.

### Win Checking Algorithm
As we know, tic tac toe always has horizontal, vertical and diagonal winning forms. So I will bring you to figure out how my code check winning as I said
1. :arrow_right: Horizontal Forms :arrow_left: : For this form I used 2 loops run through the board as row and column horizontally. For examples if we play on 3x3, the numbers of horizontal line will be 3 obviously. Then I get characters of player (X,O) along each line to check that if all characters of player are the same all the line, so that player will win. ;)
2. :arrow_down: Vertical Forms :arrow_up: : Same steps as horizontal form, just change it into vertical by swopping the index order of array.
3. :arrow_lower_right: Diagonal Forms :arrow_upper_left: : This form gets a little bit harder from the two first checking, because diagonal form is so different from two first checking. The trick to checking this form is you need to know the possibility of diagonal line's drawing on the board. For sample if I play on 3x3 the possibility numbers of diagonal line will be 2 and 5x5 will be 8. I've code to check this form by checking all diagonal possibilities.

### AI Algorithms
Finally, I've used minimax algorithm to find the most advantage of the game possibility for AI winning's the game. So, how minimax works?
Reference : https://www.javatpoint.com/mini-max-algorithm-in-ai

### Setup & Installation
1. Download file apk of this game from the follow link : https://drive.google.com/drive/folders/1Kw7fw7gvVXKVxpQMy278vKGwYCBe6sJN?usp=sharing
2. And then, drag the downloaded apk file into android emulators such as LDPlayer (recommended), NOX, BlueStacks or etc and wait for installation
3. Open the game and enjoy! 🤗
