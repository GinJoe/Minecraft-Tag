package me.joeknight112.tag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.joeknight112.tag.Main;

public class PlayerInteractListener implements Listener
{
	public Main plugin;
	
	public PlayerInteractListener(Main plugin)
	{
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e)
	{
		if (plugin.gameInProgress)
		{
			Player player = (Player) e.getPlayer();
			if (player.equals(plugin.hunter))
			{
				if (e.getItem().equals(new ItemStack(Material.COMPASS)))
				{
					double yCoord = player.getCompassTarget().getY();
					player.sendMessage("§a Tracking " + plugin.hunted.getName() +". y=" + (int) yCoord );
				}
			}
		}
	}
}
