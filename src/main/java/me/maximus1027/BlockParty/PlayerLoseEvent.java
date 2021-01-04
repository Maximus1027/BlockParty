package me.maximus1027.BlockParty;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.w3c.dom.events.Event;

import java.io.File;
import java.io.IOException;

public class PlayerLoseEvent implements Listener {

   

    @EventHandler
    public void onDeath(PlayerDeathEvent e){

            if (BPHandler.bpPlayers.contains(e.getEntity())) {
                BPHandler.bpPlayers.remove(e.getEntity());
                if (!BPStick.wandEnabled) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7"+e.getEntity().getName()+ " is now out! &e&l"+ BPHandler.bpPlayers.size()+" &eplayers remain!"));
                    e.getEntity().setLevel(0);
                    e.getEntity().getInventory().clear();
                    if (BPHandler.bpPlayers.size() == 1) {
                        Player winner = BPHandler.bpPlayers.get(0);
                       winner.setLevel(0);
                       winner.getInventory().clear();
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY &e» &6"+winner.getName()+" &7has &a&lWON &7the Blockparty!"));
                        Bukkit.getScheduler().cancelTask(BPHandler.BlockPartyRunnable);
                        for(Player player : Bukkit.getOnlinePlayers()){
                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lBLOCKPARTY") , ChatColor.translateAlternateColorCodes('&', "&c"+winner.getName()+"&6 Has won!"));
                        }
                        BPHandler.bpPlayers.clear();
                        BPHandler.Blocks.clear();
                        BPStick.wandEnabled = true;




                        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                        File file = new File("plugins/BlockPartyWAX/Floors/jbmcfloor.schematic");
                        Location location = BPStick.firstLocation;
                        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 1000);
                        CuboidClipboard clipboard = null;
                        try {
                            clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                            //clipboard.rotate2D(90);
                            clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
                        }catch (MaxChangedBlocksException | DataException | IOException o){
                            o.printStackTrace();
                        }


                    }
                }
            }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

            if (BPHandler.bpPlayers.contains(e.getPlayer())) {
                BPHandler.bpPlayers.remove(e.getPlayer());
                if (!BPStick.wandEnabled) {
                    e.getPlayer().setLevel(0);
                    e.getPlayer().getInventory().clear();
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7"+e.getPlayer().getName()+ " is now out! &e&l"+ BPHandler.bpPlayers.size()+" &eplayers remain!"));
                    if (BPHandler.bpPlayers.size() == 1) {
                        Player winner = BPHandler.bpPlayers.get(0);
                        winner.setLevel(0);
                        winner.getInventory().clear();
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY &e» &6"+winner.getName()+" &7has &a&lWON &7the Blockparty!"));
                        Bukkit.getScheduler().cancelTask(BPHandler.BlockPartyRunnable);
                        for(Player player : Bukkit.getOnlinePlayers()){
                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lBLOCKPARTY") , ChatColor.translateAlternateColorCodes('&', "&c"+winner.getName()+"&6 Has won!"));
                        }
                        BPHandler.bpPlayers.clear();
                        BPHandler.Blocks.clear();
                        BPStick.wandEnabled = true;


                        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                        File file = new File("plugins/BlockPartyWAX/Floors/jbmcfloor.schematic");
                        Location location = BPStick.firstLocation;
                        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 1000);
                        CuboidClipboard clipboard = null;
                        try {
                            clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                            //clipboard.rotate2D(90);
                            clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
                        }catch (MaxChangedBlocksException | DataException | IOException o){
                            o.printStackTrace();
                        }

                    }
                }
            }
    }
}
