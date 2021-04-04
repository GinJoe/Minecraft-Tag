# Minecraft-Tag
Minecraft Mod that enables you to play tag using Spigot

## Dependencies
This Tag game was made using Spigot-1.16. You can get spigot for your server here: https://getbukkit.org/download/spigot
The code is also heavily reliant on the Bukkit library which you can find in the same place.

## The game

The game is made for 2 players. You start the game using the "/fight player1 player2" command and you are then teleported to a random bounded area where player1 is the hunter and player2 is the hunted. The aim is for the hunted to avoid getting hit by the hunter in the bounded area before the time runs out. 

The hunter is also given a compass which gives you Player2's position at all times. Right clicking with the compass will give you the x,y,z values in your chat feed. The compass needle should also point at the player at all times.

## The Commands

### /fight 

/fight takes 2 arguments the name of the first player and the name of the second player. They are then teleported into a random area within a 5 million X 5 million block area. The playing area is 100x100 blocks with no limit on the y axis. players are spawned on the first block that isn't air. If it is water they are given an obsidian block to stand on.

details about /fight can be found in the StartCommand.java code
### /breakitup

/breakitup ends any current game with no winner. 
