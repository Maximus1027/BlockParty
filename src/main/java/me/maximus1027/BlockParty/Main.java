package me.maximus1027.BlockParty;


import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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
        File file = new File(getDataFolder(), "Floors/");

        file.mkdirs();


        new BPHandler(this);
        new BPStick(this);
        getServer().getPluginManager().registerEvents(new AntiCommands(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoseEvent(), this);
        getServer().getPluginManager().registerEvents(new BPStick(this), this);
    }
}
