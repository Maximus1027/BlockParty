package me.maximus1027.BlockParty;


import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends JavaPlugin {
    public static Main plugin;

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Path path  = Paths.get("plugins/BlockPartyWAX/Floors");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }






        new BPHandler(this);
        new BPStick(this);
        getServer().getPluginManager().registerEvents(new PlayerLoseEvent(), this);
        getServer().getPluginManager().registerEvents(new BPStick(this), this);
    }
}
