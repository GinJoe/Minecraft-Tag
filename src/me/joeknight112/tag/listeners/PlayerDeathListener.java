package me.joeknight112.tag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.joeknight112.tag.Main;

public class PlayerDeathListener implements Listener
{
	private Main plugin;
	
	public PlayerDeathListener(Main plugin)
	{
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e)
	{
		//Check if the player was the hunter or hunted
		if (plugin.gameInProgress)
		{
			Player p =  (Player) e.getEntity();
			if (p.getName().equals(plugin.hunted.getName()))
			{
				plugin.endTheGame("hunter");
			} else if (p.getName().equals(plugin.hunter.getName()))
			{
				plugin.endTheGame("hunted");
			}
		}
	}
	
}
