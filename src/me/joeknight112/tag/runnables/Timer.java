package me.joeknight112.tag.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import me.joeknight112.tag.Main;

public class Timer extends BukkitRunnable
{
	public Main plugin;
	public int count = 0;
	public final int totalTime = 180;
	
	public Timer(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void run() 
	{
		if (count < totalTime && plugin.gameInProgress)
		{
			if (count % 5 == 0)
				plugin.getServer().broadcastMessage("§d"+ (totalTime-count) + " Seconds to go!!");
			count++;
		} else if (count >= totalTime)
		{ //Win by time out
			this.cancel();
			plugin.endTheGame("hunted");
		} else { //Win by smacky smack
			this.cancel();
		}
		
	}

}
