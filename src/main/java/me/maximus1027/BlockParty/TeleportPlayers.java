package me.maximus1027.BlockParty;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TeleportPlayers {



    public static void teleportPlayers(List<Player> players){
        System.out.println(BPHandler.plugin.getConfig().getDouble("gameSpawn.x"));
        for(Player plr : players){
            plr.teleport(new Location(plr.getWorld(), BPHandler.plugin.getConfig().getDouble("gameSpawn.x"), BPHandler.plugin.getConfig().getDouble("gameSpawn.y"), BPHandler.plugin.getConfig().getDouble("gameSpawn.z")));

        }
    }
}
