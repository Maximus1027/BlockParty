package me.maximus1027.BlockParty;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FloorListing {

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    public static void listFloors(Player plr) {
    String message = "";

        Path path = Paths.get("plugins//BlockPartyWAX//Floors");
    File[] floors = new File(String.valueOf(path)).listFiles();

    for(File floor : floors){

        message = message + floor.getName().replaceAll(".schematic", "") + ", ";
    }
    message = replaceLast(message, ",", ".");
    plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Floors: &e"+message));

}

}
