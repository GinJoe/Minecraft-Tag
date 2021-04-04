package me.joeknight112.tag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.joeknight112.tag.Main;

public class PlayerMoveListener implements Listener
{
	private Main plugin;
	
	public PlayerMoveListener(Main plugin)
	{
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) 
	{
		Player player = e.getPlayer();
		Location loc = player.getLocation();
		//Check if the player is being tracked
		if (plugin.trackList.containsKey(player.getName()))
		{
			Player plyr =  plugin.trackList.get(player.getName());
			
			if (plyr.isOnline()) 
			{
				plyr.setCompassTarget(loc);
			}
			
		}
	}
}
