# Space Battle #

### Background ###

What you have before you is YAAC (yet another asteroids clone). It was inspired by CS 442 Programming Languages, a class taught at Pensacola Christian College. One of the languages examined in the class is Java, and that is the language used to develop Space Battle.

Java is a general-purpose object-oriented programming language designed to be used in conjunction with the Java Virtual Machine (JVM). "Java platform" is the name for a computing system that has installed tools for developing and running Java programs. Note that Java is not to be confused with JavaScript. [1]

### Description ###

Upon running the program, a window will be presented having the name in the title bar. The interior of the window will have a view of the world's background, dynamic objects, and heads-up display. Asteroids will be seen wandering around while attempting to crash into the player.

Several pieces of information are presented on the heads-up display:

* The currently selected weapon's name will be shown in the upper left corner of the window.
* The number of points earned by the player so far will be shown in the upper right corner.
* If the Rotary Cannon is not currently selected, the weapon's ready status will be visible.
* The number of lives remaining will be shown in the bottom right corner of the window.

### Objective ###

The program was designed to recreate part of the experience one might have received from Atari's Asteroids program released back in 1979. It was originally designed by Lyle Rains, Ed Logg, and Dominic Walsh. Asteroids was one of the first major hits of the golden age of arcade games and sold over 70,000 arcade cabinets.

The player controls a spaceship in an asteroid field but is not assaulted by flying saucers found in the original. The object of the game is to shoot and destroy asteroids while not colliding with them. The game becomes harder as the number of asteroids increases, but weapon hits count as one point with an extra life being awarded every hundred. [2]

### Controls ###

There are several buttons on the keyboard used to control the program:

* **Left** and **right** arrows will turn the player left and right respectively.
* The **space bar** will fire the player's currently selected weapon in the direction being faced.
* **Up** arrow will activate the player's main engine and cause forward acceleration.
* **Down** arrow will deploy a dynamic braking field designed to slow all player movement.

The previous controls check if are you holding a button, but the following only respond to key presses:

* **1 - 9** select any weapon that may be assigned to that button.
* **P** activates an emergency panic system that jumps the player to another location.
* **Z** toggles the type of explosion and hyperspace animations currently being used.
* **Esc** immediately exits the program and closes the window it was being displayed in.

### Weapons ###

Unlike the original Asteroids program, this one features a variety of weapons to destroy drifting rocks:

* **Rotary Cannon** - fires projectiles very quickly in short succession
* **Guided Missile** - locks onto the nearest asteroid and homes in on it
* **Space Mine** - sits in the place it was dropped and waits for an asteroid to collide into it
* **Cluster Crack** - shotgun style weapon capable of destroying large asteroids in a single burst
* **Tesla Strike** - sends out tendrils like lightning to find targets

The player may also become a weapon once a secret code has been entered into the program. Thereafter, colliding into asteroids is no longer a threat, but points may not be earned through this method of attack. The code is left as an exercise for readers to discover via experimentation.

### Credits ###

Mickey Stemen was the originator of Space Battle and introduced the program while teaching about Java in CS 442. He has degrees from both Pensacola Christian College and University of West Florida. He is one of several computer science teachers providing instruction at Pensacola Christian College.

Timothy Hansen inspired alternate explosion and hyperspace effects available in the program. He provided several programs demonstrating potential animations that were eventually supported in the special effects subsystem. He has multiple degrees from Pensacola Christian College and works in the advertising department.


  [1]: http://stackoverflow.com/tags/java/info
  [2]: https://en.wikipedia.org/wiki/Asteroids_(video_game)