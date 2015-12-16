README

How to Build/Launch:
	
	For the game to build, the kryonet 2.21 all.jar, and the slick.jar needs to be in the build path. A modified version of the jig project needs to be 
	in the build path as well. The new version of the jig project has a modified shape.java and convexpolygon.java class files.
	
	To run server side: Launch mainServer.java from the server package and then launch UCGame.java from the uc package.
	
	To run client side: Just launch UCGame.java 
	
	To play the game: The server side chooses a name, player limit, time limit, and score limit, as well as the character, then clicks "begin" to host the game.
					  The client side needs to choose a player name, and input the correct IP address/hostname of the server to join the game. 
					  
		Important: the server side player needs to host/launch the game before the client can join. If the client joins before the server hosts, the game crashes.

Controls for the Game:

	Mouse: to aim, click to shoot
	W:	   Jump
	A:	   Move Left
	D:	   Move Right
	S:	   Crouch
	1:	   Switch to primary weapon
	2:	   Switch to secondary weapon
	3: 	   Switch to melee weapon
	TAB:   View game statistics
	ESC:   Disconnect/Go to main menu

	Navigate through menus by using mouse
	
Cheat Codes:
	
	Server side only: For no cooldown time: press 'I', 'O', and 'P' together.
	
Originally Proposed Low-Bar Goals:
	
	Avatar Movement:   Completed
	Avatar Shooting:   Completed
	Environment:	   Completed
	Statistics:		   Completed
	Network:		   Completed
	Power-ups:		   Completed (but to lesser extend than originally planned)
	Different Classes: Completed
	
List of Other Goals:
	
	Menus
	Animations for all characters
	Sounds
	Animated explosions
	Round restart
	Weapon cool-down
	Rocket-jumping
	
Licensing Terms:
	
	Creative Commons
	
	
Known bugs/ unfinished things:

	GameOver Menu exit to menu breaks the game.
	ESC to exit from game isn't hooked up.
	Sometimes the arms are offset strangely on the server side.
	If client joins before server hosts, game crashes.

Sources:

// pictures used:
	https://pixabay.com/static/uploads/photo/2014/03/04/15/32/wall-279519_960_720.jpg
	http://hasgraphics.com/wp-content/uploads/2010/08/spritesheet1.png
	
// sounds used:
	http://opengameart.org/content/cyborg-hurt - Vinrax
	http://opengameart.org/content/11-male-human-paindeath-sounds - Michel Baradari
	http://opengameart.org/content/3-melee-sounds - remaxim
	http://opengameart.org/content/bow-arrow-shot - dorkster
	http://opengameart.org/content/beep-sound - Test User
	http://opengameart.org/content/platformer-jumping-sounds - dklon
	http://opengameart.org/content/chaingun-pistol-rifle-shotgun-shots - Michel Baradari
	http://opengameart.org/content/boom-pack-1 - dklon
	http://opengameart.org/content/stereotypical-90s-space-shooter-music - Jan125
	