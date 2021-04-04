package me.joeknight112.tag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.joeknight112.tag.Main;

public class PlayerHitListener implements Listener
{
	private Main plugin;
	
	public PlayerHitListener(Main plugin) 
	{
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e)
	{
		if (plugin.gameInProgress)
		{
			if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) 
			{
				//If player is the hunted then end game and announce winner
				Player beenHit = (Player) e.getEntity();
				Player hitter = (Player) e.getDamager();	
				
				if (beenHit.getName().equals(plugin.hunted.getName()) && hitter.getName().equals(plugin.hunter.getName()))
				{
					beenHit.sendMessage("You got smacked! Bad Luck :/");
					hitter.sendMessage("You sure showed him! Congratulations!");
					plugin.gameInProgress = false;
					plugin.endTheGame("hunter");
				}
			}
		}
	}

}
