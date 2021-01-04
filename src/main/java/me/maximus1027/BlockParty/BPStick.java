package me.maximus1027.BlockParty;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class BPStick implements CommandExecutor, Listener {
    public static Location firstLocation = null;
    public static Location secondLocation = null;
    public static Boolean wandEnabled = true;


    private static String e(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }





    private Main plugin;
    public BPStick(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("bpwand").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player plr = (Player) sender;
        if(!(plr.hasPermission("bp.perms"))){
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "Insufficient permissions."));
            return false;
        }

        ItemStack bpWand = new ItemStack(Material.BLAZE_ROD);

        ItemMeta bpWandMeta = bpWand.getItemMeta();
        bpWandMeta.setDisplayName(e("&c&m:&r &b&lB&d&lP &f&lRegion Tool &c&m:&r"));
        bpWandMeta.setLore(Arrays.asList(
                e("&c&m=-+-=-+-=-+-=-+-=-+-=-+-="),
                e("&7Use this item to select one"),
                e("&7position to another position to"),
                e("&7set the map for &b&lBLOCK &d&lPARTY&7."),
                "",
                e("&c&lADMIN &cTOOL"),
                e("&8&oNote: Works similarly to World Edit axe"),
                e("&c&m=-+-=-+-=-+-=-+-=-+-=-+-=")));
        bpWand.setItemMeta(bpWandMeta);
        plr.getInventory().setItemInMainHand(bpWand);


        return false;
    }


@EventHandler
    public void stickSelections(PlayerInteractEvent event){

        Player plr = event.getPlayer();
        if(!(plr.hasPermission("bp.perms"))){
            return;
        }

        if(event.getAction() == Action.LEFT_CLICK_BLOCK && plr.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD){
            if(!wandEnabled){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSelection wand does not work while a blockparty is in progress!"));
                return;
            }
            event.setCancelled(true);
            firstLocation = event.getClickedBlock().getLocation();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFirst location set successfully."));

        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && plr.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD){
            if(!wandEnabled){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSelection wand does not work while a blockparty is in progress!"));
                return;
            }
            event.setCancelled(true);
            secondLocation = event.getClickedBlock().getLocation();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSecond location set successfully."));


        }
    }
}
