package me.joeknight112.tag.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.joeknight112.tag.Main;
import me.joeknight112.tag.runnables.Timer;


public class StartCommand implements CommandExecutor
{
	public Main plugin;
	
	public StartCommand(Main plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("fight").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label , String[] args) 
	{
		//we want /start name name
		if (plugin.gameInProgress)
		{
		plugin.getServer().broadcastMessage("A game is already in progress. Please wait until it has finished.");
			return true;
		}
		//Checking if args has 2 people.
		if (!(args.length >=2))
		{
			sender.sendMessage("Please give 2 players you want to play with.");
			return true;
		}
		//Getting all of the online players
		Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
		ArrayList<String> playerNames = new ArrayList<String>();
		for (Player player : playerList)
		{
			playerNames.add(player.getName());
		}
		//Checking if those people are online
		if (playerNames.contains(args[0]) && playerNames.contains(args[1]))
		{
			//Retrieving Player data.
			Player hunter = null;
			Player hunted = null;
			for(Player p : playerList)
			{
				if (p.getName().equals(args[0])) {hunter = p;}
				if (p.getName().equals(args[1])) {hunted = p;}
			}
			
			//Checking if the same username is used for both
			if (args[0].equals(args[1]))
			{
				sender.sendMessage("The two people can't be the same!");
				return true;
			}
			//Clear their inventory
			hunter.getInventory().clear();
			hunted.getInventory().clear();
			
			//Give them tools (Diamond axe, Shovel, Pick, Blocks)
			ItemStack[] itemSet = 
			{
			new ItemStack(Material.DIAMOND_PICKAXE),
			new ItemStack(Material.DIAMOND_AXE),
			new ItemStack(Material.DIAMOND_SHOVEL),
			new ItemStack(Material.COBBLESTONE,16)
			};
			
			hunter.getInventory().setContents(itemSet);
			hunted.getInventory().setContents(itemSet);
			
			//Set saturation of players so they don't get hungry
			hunter.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,2400,1));
			hunted.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,2400,1));
			
			//We need to give hunter a compass
			hunter.getInventory().addItem(new ItemStack(Material.COMPASS));
			
			//Set up tracked so it updates on move (See Player move listener)
			plugin.trackList.clear();
			plugin.trackList.put(hunted.getName(), hunter);
			
			//Set world borders (Random) (100 blocks)
		
			Location fightArena = areaGenerator(hunter);
			
			Biome startingBiome = fightArena.getBlock().getBiome();
			ArrayList<Biome> listOfBadBiomes = new ArrayList<Biome>();
			listOfBadBiomes.add(Biome.OCEAN);
			listOfBadBiomes.add(Biome.COLD_OCEAN);
			listOfBadBiomes.add(Biome.DEEP_COLD_OCEAN);
			listOfBadBiomes.add(Biome.DEEP_FROZEN_OCEAN);
			listOfBadBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
			listOfBadBiomes.add(Biome.DEEP_OCEAN);
			listOfBadBiomes.add(Biome.DEEP_WARM_OCEAN);
			listOfBadBiomes.add(Biome.FROZEN_OCEAN);
			listOfBadBiomes.add(Biome.LUKEWARM_OCEAN);
			listOfBadBiomes.add(Biome.WARM_OCEAN);
			
			while (listOfBadBiomes.contains(startingBiome))
			{
				fightArena = areaGenerator(hunter);
				startingBiome = fightArena.getBlock().getBiome();
			}
			
			double xCoord = fightArena.getX();
			double zCoord = fightArena.getY();
			
			
			//Debugging for initial load
			///This section is a concession to low chunk loading times.
			Location hunterSpecCoords = new Location(hunter.getWorld() ,xCoord + 49, 85, zCoord + 49);
			Location huntedSpecCoords = new Location(hunter.getWorld() ,xCoord - 49, 85, zCoord - 49);
			hunted.teleport(huntedSpecCoords);
			hunted.setGameMode(GameMode.ADVENTURE);
			hunted.setFlying(true);
			hunter.teleport(hunterSpecCoords);
			hunter.setGameMode(GameMode.ADVENTURE);
			hunter.setFlying(true);
			
			//Setting everyone else to spectators
			for (Player player : playerList)
			{
				if (!(player.getName().equals(hunter.getName())) && !(player.getName().equals(hunted.getName())) )
				{
					player.setGameMode(GameMode.SPECTATOR);
					player.setAllowFlight(true);
					player.teleport(hunterSpecCoords);
				}
			}
			
			//Setting borders
			WorldBorder border = hunter.getWorld().getWorldBorder();
			Location center = new Location(hunter.getWorld(),(double) xCoord, 0.0, (double) zCoord);
			border.setCenter(center);
			border.setSize(100.0);
			
			//Trying to load in the world
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Bukkit.broadcastMessage("Something went wrong with the 5 second timeout");
			}
			
			//Setting player locations (Safe spawning location) Start from 255 and keep going until not air
			Location hunterCoords = new Location(hunter.getWorld(),xCoord + 49, 255.0, zCoord + 49);
			Location huntedCoords = new Location(hunter.getWorld(),xCoord - 49, 255.0, zCoord - 49);
			
			while (hunterCoords.getBlock().getBlockData().getMaterial().isAir())
			{
				hunterCoords.setY(hunterCoords.getY()-1);
			}
			while (huntedCoords.getBlock().getBlockData().getMaterial().isAir())
			{
				huntedCoords.setY(huntedCoords.getY()-1);
			}
			
			//Check if spawn in water/lava
			if (huntedCoords.getBlock().isLiquid())
				huntedCoords.getBlock().setType(Material.OBSIDIAN,true);
			if (hunterCoords.getBlock().isLiquid())
				hunterCoords.getBlock().setType(Material.OBSIDIAN,true);
			
			//TP players 1 block above so they're not stuck in a block
			hunterCoords.setY(hunterCoords.getY()+1);
			huntedCoords.setY(hunterCoords.getY()+1);
			hunter.teleport(hunterCoords);
			hunted.teleport(huntedCoords);
			
			hunted.setGameMode(GameMode.SURVIVAL);
			hunter.setGameMode(GameMode.SURVIVAL);
			hunted.setFlying(false);
			hunter.setFlying(false);
			
			
			
			//Check on hits
			plugin.hunted = hunted;
			plugin.hunter = hunter;
			
			
			//Start Game
			plugin.gameInProgress = true;
			
			//Set Time limit to 2 minutes. And run timer here
			Timer timer = new Timer(plugin);
			timer.runTaskTimer(plugin, 0, 20*1);
			
			return true;
		} else {
			sender.sendMessage("Please choose people who are online to play tag.");
			return true;
		}
		
	}
	
	public static Location areaGenerator(Player hunter)
	{
		Random random = new Random();
		int upperBound = 5000000;
		double xCoord = (double) random.nextInt(upperBound);
		double zCoord = (double) random.nextInt(upperBound);
		
		return new Location(hunter.getWorld(),xCoord, 80.0, zCoord);
		
	}

}
