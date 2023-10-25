# xo_game_android

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
4. Players can check the game history from see game details history choice and can watch the replay of the game directly on the screen.

### Used Algorithm
As we know, tic tac toe always has horizontal, vertical and diagonal winning forms. So I will bring you to figure out how my code check winning as I said
1. :arrow_right: Horizontal forms :arrow_right: : For this form I used 2 loops run through the board as row and column horizontally. For examples if we play on 3x3, the numbers of horizontal line will be 3 obviously. Then I've get the characters of player (X,O) along each line to check that if characters of player are the same all the line, so that player will win. ;)
