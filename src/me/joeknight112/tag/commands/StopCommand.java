package me.joeknight112.tag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.joeknight112.tag.Main;

public class StopCommand implements CommandExecutor
{
	public Main plugin;
	
	public StopCommand(Main plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("breakitup").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		plugin.endTheGame("none");
		Bukkit.broadcastMessage("§4 Force ending the game");
		return true;
	}
}
