package me.maximus1027.BlockParty;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCommands implements Listener {
    @EventHandler
    public void PlayerCommand(PlayerCommandPreprocessEvent e){
        if(e.getPlayer().isOp() || e.getPlayer().hasPermission("bp.perms")){
            return;
        }
        if(BPHandler.bpPlayers.contains(e.getPlayer())){
            if(BPStick.wandEnabled == false){
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot run commands while the blockparty is in progress!");
                e.setCancelled(true);
                return;
            }
        }
    }
}
