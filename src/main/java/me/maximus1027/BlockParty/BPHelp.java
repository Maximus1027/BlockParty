package me.maximus1027.BlockParty;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BPHelp {
    private static String e(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static void blockpartyHelp(Player plr){
        plr.sendMessage(e("&7&m-+-&r &6&lBLOCK &e&lPARTY &f&lHELP &7&m-+-&r"));
        plr.sendMessage(e(""));
        plr.sendMessage(e("&e/bpwand &7&m-&r &fSelection tool to set the map of BP"));
        plr.sendMessage(e("&e/bp start &7&m-&r &fCommand to &astart &fthe game &7&o(Selected points are required)"));
        plr.sendMessage(e("&e/bp stop &7&m-&r &fCommand to &cstop &fthe game"));
        plr.sendMessage(e("&e/bp join &7&m-&r &fCommand to join the game &7&o(No permissions required)"));
        plr.sendMessage(e("&e/bp jointoggle &7&m-&r &fCommand to toggle the &6/bp join &fcommand"));
        plr.sendMessage(e("&e/bp setspawn &7&m-&r &fCommand to set the BlockParty's game spawn."));
        plr.sendMessage(e("&e/bp loadfloor <floorname> &7&m-&r &fLoads a specific Block Party floor"));
        plr.sendMessage(e("&e/bp listfloors &7&m-&r &fLists the current Block Party floors loaded"));
    }
}
