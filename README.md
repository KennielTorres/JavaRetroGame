# JavaRetroGames

A game box made with Java 8 that contains lite recreations of the following famous retro games: Galaga, Pacman, and Zelda. No third-party libraries were used. 
    Part of my Advanced Programming course, as an undergraduate student in first year. The following contributions were done by me:

Galaga-> Cosmetics: Changed in-game appearance of blinking dots, background, and text color.
    Implemented bee entity: Spawn/Respawn of entity & player attacking algorithm. 
    Implemented enemy ship entity: Modifications from the bee entity that include player attack by shooting instead of flying to player & Spawn/Respawn Algorithm.
    
Pacman-> Cosmetics: Changed logo of pacman to one of the originals, and added blink to big dots.
    Added small algorithm to make small dots have a 1/30 chance of being a fruit.
    Implemented ghosts and ghost spawner: Ghost movement algorithm, spawn/respawn algorithm, and feature to spawn more than 4.
    Implemented bonus feature: when eating fruits, Pacman gains 2x speed for 2 seconds.
    
Zelda-> Side features(Press 'M' in game selection screen to see): Implemented debug keys to: select tile at halfway,
  randomize current tile, and randomize current tile set and tile together. Implemented 4 tiles with pre-dictated direction
  with recursive algorithm to move the player. Main game: Implemented display of current lives and debug key
  to add if below 3.
