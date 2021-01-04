package me.maximus1027.BlockParty;

import com.avaje.ebean.validation.NotNull;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.registry.WorldData;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BPHandler implements CommandExecutor {
    public static HashMap<Block, ItemStack> Blocks = new HashMap<Block, ItemStack>();
    public boolean test2 = false;

    //Returns ItemStack version of a random block from Floor.
    public static ItemStack getRandom(HashMap<Block, ItemStack> blocks){
        ItemStack randomItem = blocks.get(blocks.keySet().toArray()[new Random().nextInt(blocks.keySet().toArray().length)]);
        return randomItem;
    }

    //Gets a randomly chosen floor from the "floors" folder.
    public File getRandomFloor(){
        Path path = Paths.get("plugins//BlockPartyWAX//Floors");
        File[] floors = new File(String.valueOf(path)).listFiles();


        int rnd = new Random().nextInt(floors.length);
        return floors[rnd];

    }

    ItemStack chosenBlock = null;
    int bPMT = 0;
    int bPTS = 5;
    int official = 0;
    int init = 0;
    int sub = 0;
    public static int BlockPartyRunnable;
    public static int InitialPlayerAmount = 0;
    public static List<Player> bpPlayers = new ArrayList<>();
    public static Main plugin;

    public BPHandler(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("blockparty").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender, Command command, final String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player plr = (Player) sender;
        if (args.length < 1) {
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBlockparty by Maximus1027, xWafless.\n /bp help"));
            return false;
        }

        //Command to join the game.

        if(args[0].equalsIgnoreCase("join")){

            //If Player is already in ArrayList and if game is not going.
            if(bpPlayers.contains(plr) && BPStick.wandEnabled){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have already joined the blockparty!"));
                return false;
            }

            //Joining the block party when game is not already going
            if(!bpPlayers.contains(plr) && BPStick.wandEnabled){
                bpPlayers.add(plr);
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have joined the blockparty!"));
                int random_int = (int)(Math.random() * (3 - 1 + 1) + 1);
                if(random_int == 1){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6"+plr.getName()+"&e is ready to boogie!"));
                }else if(random_int == 2){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6"+plr.getName()+"&e is ready to breakdance!"));
                }else if(random_int == 3){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6"+plr.getName()+"&e is ready to disco!"));
                }


                return false;
            }

            //If game is going
            if(!BPStick.wandEnabled){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't join the blockparty while it's in progress"));
            }
            return false;



        }

        //No permissions to the command below.
        if (!(plr.hasPermission("bp.perms"))) {
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficient permissions."));
            return false;
        }
        sub = 0;

        //Start the game command.
        if (args[0].equalsIgnoreCase("start")) {
            //If more than 1 players are in the game.
            if(bpPlayers.size() <= 1){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must have more than one player to start blockparty! Join with /bp join"));
                return false;
            }

            //Make sures the game spawnLocation has been set.
            if ((Double) plugin.getConfig().getDouble("gameSpawn.x") == 0) {
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGame spawn location must be set! /bp setspawn"));
                return false;
            }


            //Selection A and B are not null.
            if (BPStick.firstLocation != null && BPStick.secondLocation != null) {

                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully started BlockParty! &eGame will begin in 5 seconds."));
                BPStick.wandEnabled = false;
                bPMT = 0;
                official = 0;
                bPTS = 5;
                InitialPlayerAmount = bpPlayers.size();
                //New Bukkit runnable (Actual game)
                BlockPartyRunnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

                    public void run() {

                          //Beginning Broadcasts | Announces to the sub server.
                        if (bPTS <= 5 && bPTS >= 0) {
                            if(bPTS == 5 || bPTS == 4){
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY STARTING IN&e: &c&l"+bPTS));
                            }else if(bPTS == 3 || bPTS == 2){
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY STARTING IN&e: &6&l"+bPTS));
                            }else if(bPTS == 1){
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY STARTING IN&e: &a&l"+bPTS));
                            }

                            if (bPTS == 0) {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lBLOCKPARTY HAS STARTED."));


                                //Loads the basic JBMCFLOOR as the first floor.
                                // This is to make sure there is nothing empty as the beginning floor
                                WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                                File file = new File("plugins/BlockPartyWAX/Floors/jbmcfloor.schematic");
                                Location location = plr.getLocation();
                                EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 1000);
                                CuboidClipboard clipboard = null;
                                try {
                                    clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                                    //clipboard.rotate2D(90);
                                    clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
                                }catch (MaxChangedBlocksException | DataException | IOException e){
                                    e.printStackTrace();
                                }

                                //Title screen showing that the game has began.
                                //
                                //BLOCKPARTY
                                //Has Began!
                                for(Player plr2 : Bukkit.getOnlinePlayers()){
                                    plr2.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lBLOCKPARTY"), ChatColor.translateAlternateColorCodes('&', "&6Has Began!"));
                                }

                                //Sets player's in the game xp level and sets them to survival mode.
                                for (Player activePlayer : bpPlayers) {
                                    activePlayer.setGameMode(GameMode.SURVIVAL);
                                    activePlayer.setExp(1.0F);
                                }
                                //Teleports players in the game
                                TeleportPlayers.teleportPlayers(bpPlayers);
                            }
                        }

                        //One time Blocks arraylist loading.
                        if(bPTS == 1){
                            for(int x = (int) Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); x <= Math.max(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); ++x ){
                                for(int z = (int) Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); z <= Math.max(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); ++z){
                                    int y = (int) BPStick.firstLocation.getY();
                                    World world = BPStick.firstLocation.getWorld();
                                    if(!Blocks.containsValue(new ItemStack(world.getBlockAt(x, y, z).getType(), 1, (short) world.getBlockAt(x, y, z).getData()))){
                                        Blocks.put(world.getBlockAt(x, y, z), new ItemStack(world.getBlockAt(x, y, z).getType(), 1, (short) world.getBlockAt(x, y, z).getData()));
                                    }

                                }
                            }

                            //Sets the players alive's inventory to the chosen block.
                            if(sub == 0){
                                chosenBlock = getRandom(Blocks);
                                for (Player activePlayer : bpPlayers) {
                                    for (int i = 0; i < 9; i++) {
                                        activePlayer.getInventory().setItem(i, new ItemStack(chosenBlock));
                                    }
                                }
                            }

                        }


                        //Time increments
                        if(bPTS <= 0) {
                            bPMT++;
                        }
                        official++;
                        bPTS--;
                        init++;


                        //Sets the xp level to the amount of time till floor is erased.
                        if(bPMT >= 1 && bPMT < 10-sub){
                            for(Player plr : bpPlayers){
                                plr.setLevel(Math.abs((10 - sub) - bPMT)-1);
                            }
                        }

                        //CLEAR FLOOR
                        if(bPMT == 10-sub){
                            for(int x = (int) Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); x <= Math.max(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); ++x ){
                                for(int z = (int) Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); z <= Math.max(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); ++z){
                                    int y = (int) BPStick.firstLocation.getY();
                                    World world = BPStick.firstLocation.getWorld();
                                    if(world != null && (world.getBlockAt(x, y, z).getData() != chosenBlock.getData().getData() || world.getBlockAt(x, y, z).getType() != chosenBlock.getData().getItemType())){
                                        Location location = new Location(world, x, y, z);
                                        world.getBlockAt(x, y, z).setType(Material.AIR);
                                    }
                                }
                            }

                        }

                        //GENERATE
                        if(bPMT == 13-sub){
                            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                            File file = getRandomFloor();
                            Location location = plr.getLocation();
                            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 1000);
                            CuboidClipboard clipboard = null;
                            try {
                                clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                                //clipboard.rotate2D(90);
                                clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
                            }catch (MaxChangedBlocksException | DataException | IOException e){
                                e.printStackTrace();
                            }

                            if (sub < 7) {
                                sub = sub + 1;
                            }
                            bPMT = 0;


                        //Clears the current blocks arraylist and resets it as the new floor's
                            Blocks.clear();
                            for(int x = (int) Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); x <= Math.max(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); ++x ) {
                                for (int z = (int) Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); z <= Math.max(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); ++z) {
                                    int y = (int) BPStick.firstLocation.getY();
                                    World world = BPStick.firstLocation.getWorld();

                                    if(!Blocks.containsValue(new ItemStack(world.getBlockAt(x, y, z).getType(), 1, (short) world.getBlockAt(x, y, z).getData()))){
                                        Blocks.put(world.getBlockAt(x, y, z), new ItemStack(world.getBlockAt(x, y, z).getType(), 1, (short) world.getBlockAt(x, y, z).getData()));
                                    }

                                }
                            }

                            //Chooses the new block.
                            chosenBlock = getRandom(Blocks);
                            for (Player activePlayer : bpPlayers) {
                                for (int i = 0; i < 9; i++) {
                                    activePlayer.getInventory().setItem(i, new ItemStack(chosenBlock));
                                }
                            }
                        }
                    }
                }, 0L, 20L);


            } else {
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBoth First and Second location must be defined to start a blockparty!"));
            }


        }

        //Shows the list of floors loaded. (BlockPartyWax/Floors)
        if(args[0].equalsIgnoreCase("listfloors")){
            FloorListing.listFloors(plr);
        }

        //Sets game spawn, one time thing and does not require it to be set every server reload.
        //Will overwrite past spawn.
        if(args[0].equalsIgnoreCase("setspawn")){

            plugin.getConfig().set("gameSpawn.world", plr.getWorld().getName());
            plugin.getConfig().set("gameSpawn.x", plr.getLocation().getX());
            plugin.getConfig().set("gameSpawn.y", plr.getLocation().getY());
            plugin.getConfig().set("gameSpawn.z", plr.getLocation().getZ());
            plugin.saveConfig();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully set game spawn!"));
        }

        //Sends player the help page.
        if(args[0].equalsIgnoreCase("help")){
            BPHelp.blockpartyHelp(plr);
        }

        //Chooses a random floor to load.
        //(DEBUG COMMAND)
        if(args[0].equalsIgnoreCase("random")){
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            File file = getRandomFloor();
            Location location = plr.getLocation();
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 1000);
            CuboidClipboard clipboard = null;
            try {
                clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                //clipboard.rotate2D(90);
                clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
            }catch (MaxChangedBlocksException | DataException | IOException e){
                e.printStackTrace();
            }
        }


        //Loads a specific floor.
        if(args[0].equalsIgnoreCase("loadfloor")){
            if(args.length < 2) {
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /bp loadfloor <floorname>"));
                return false;
            }
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            File file = null;
            try {
                file = new File("plugins/BlockPartyWAX/Floors/" + args[1] + ".schematic");
            } catch (NullPointerException e){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe floor &6"+args[1]+"&c does not exist!"));
                e.printStackTrace();
            }
            Location location = plr.getLocation();
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1);
            try {
                CuboidClipboard clipboard = null;

                clipboard = MCEditSchematicFormat.getFormat(file).load(file);

                //clipboard.rotate2D(90);

                clipboard.paste(session, new com.sk89q.worldedit.Vector(Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()), BPStick.firstLocation.getY()+1, Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ())), false);
            }catch (MaxChangedBlocksException | DataException | IOException e){
                e.printStackTrace();
            } catch (NullPointerException t){
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe floor &6"+args[1]+"&c does not exist!"));
                t.printStackTrace();
            }
        }



        //Clears the floor to complete air.
        //Early stages of blockparty debug command.
        if(args[0].equalsIgnoreCase("debug")){
            for(int x = (int) Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); x <= Math.max(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); ++x ){
                for(int z = (int) Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); z <= Math.max(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); ++z){
                    int y = (int) BPStick.firstLocation.getY();
                    World world = BPStick.firstLocation.getWorld();
                    Blocks.put(world.getBlockAt(x, y, z), new ItemStack(world.getBlockAt(x, y, z).getType(), 1, (short) world.getBlockAt(x, y, z).getData()));

                    Location location = new Location(world, x, y, z);
                    world.getBlockAt(x, y, z).setType(Material.AIR);

                }
            }

        }

        //Not in use but might be in the future.
        //Make a randomly generated floor.
        if(args[0].equalsIgnoreCase("generate")){
            for(int x = (int) Math.min(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); x <= Math.max(BPStick.firstLocation.getX(), BPStick.secondLocation.getX()); ++x ){
                for(int z = (int) Math.min(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); z <= Math.max(BPStick.firstLocation.getZ(), BPStick.secondLocation.getZ()); ++z){
                    int y = (int) BPStick.firstLocation.getY();
                    World world = BPStick.firstLocation.getWorld();
                    Location location = new Location(world, x, y, z);
                    chosenBlock = getRandom(Blocks);
                    Block b = world.getBlockAt(x, y, z);
                    b.setType(chosenBlock.getType());
                    b.setData((byte) chosenBlock.getDurability());

                }
            }
        }

        //Completely stops the blockparty game.
        if(args[0].equalsIgnoreCase("stop")){
            Bukkit.getScheduler().cancelTask(BlockPartyRunnable);
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully stopped the blockparty game."));
            BPStick.wandEnabled = true;
            Blocks.clear();
            for(Player bpPLR : bpPlayers){
                bpPLR.setExp(0F);
                bpPLR.setLevel(0);
                bpPLR.getInventory().clear();
            }
            bpPlayers.clear();


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





        return false;
    }
}

