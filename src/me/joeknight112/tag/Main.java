package me.joeknight112.tag;

import java.util.Hashtable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.joeknight112.tag.commands.StartCommand;
import me.joeknight112.tag.commands.StopCommand;
import me.joeknight112.tag.listeners.PlayerDeathListener;
import me.joeknight112.tag.listeners.PlayerHitListener;
import me.joeknight112.tag.listeners.PlayerInteractListener;
import me.joeknight112.tag.listeners.PlayerMoveListener;

public class Main extends JavaPlugin
{	
	//Spawning issues (serverside?)
	public Hashtable<String,Player> trackList = new Hashtable<String, Player>();
	
	public Boolean gameInProgress = false;
	public Player hunted = null;
	public Player hunter = null;
	
	@Override
	public void onEnable()
	{
		new StartCommand(this); //Initiates game
		new StopCommand(this); //Stops a game in progress
		new PlayerHitListener(this); //Checks if someone has been tagged
		new PlayerMoveListener(this); //Updates the compass
		new PlayerDeathListener(this); //If someone dies ends game
		new PlayerInteractListener(this); //Gives you the y coord of the player
	}
	
	// When The game has ended this function is called and resets the variables and gives a message to the winner
	public void endTheGame(String winner)
	{
		//Conditional reset
		if (winner.equals("hunter"))
		{
			Bukkit.broadcastMessage("§a Congratulations! " + hunter.getName() + "has hunted them down!");
		} else if (winner.equals("hunted"))
		{
			Bukkit.broadcastMessage("§a Congratulations! " + hunted.getName() + "has successfully escaped!");
		} else if (winner.equals("none"))
		{
			Bukkit.broadcastMessage("§8 Match abandoned");
		}
		
		//General Reset
		this.gameInProgress = false;
		WorldBorder border = hunter.getWorld().getWorldBorder();
		border.setSize(59999968);
		this.hunted.getInventory().clear();
		this.hunter.getInventory().clear();
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if (!(player.getName().equals(hunter.getName())) && !(player.getName().equals(hunted.getName())) )
			{
				player.teleport(hunter.getLocation()); //Prevents them from dying to no clipping around.
				player.setGameMode(GameMode.SURVIVAL);
				player.setAllowFlight(false);
			}
		}
		this.hunter.setCompassTarget(new Location(hunter.getWorld(), 0.0, 0.0, 0.0));
		this.hunted = null;
		this.hunter = null;
		this.trackList.clear();		
	}
}
