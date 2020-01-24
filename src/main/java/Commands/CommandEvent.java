package Commands;

import PluginUtilities.ArmorStandConstructor;
import PluginUtilities.ChatDividers;
import PluginUtilities.LocationUtulities;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import QueueSystem.PrestartScoreBoard;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEvent implements TabExecutor {

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        switch (args.length) {
            case 1:
                return Arrays.asList("help", "start", "setlobby", "setspawn", "broadcast", "test");
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[EVENT] Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "[EVENT] Only operators can use this command!");
            return true;
        }

        String divider = ChatDividers.div;

        if (args.length < 1 || args[0].equals("help"))
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.RED + "Помощь:\n" + ChatColor.GREEN + "/event start" + ChatColor.YELLOW + " - начать эвент\n" + ChatColor.GREEN + "/event setspawn" + ChatColor.YELLOW + " - установить спавн\n" + ChatColor.GREEN + "/event setlobby" + ChatColor.YELLOW + " - установить лобби\n" + ChatColor.GREEN + "/event broadcast [сообщение]" + ChatColor.YELLOW + " - отправить Title\n" + ChatColor.YELLOW + divider);
        else if (args[0].equals("start")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.GREEN + "Эвент запущен!\n" + ChatColor.YELLOW + divider);
            Commands.StartEvent.startEvent();
        } else if (args[0].equals("setspawn")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.GREEN + "Спавн установлен!\n" + LocationUtulities.getPlayerLocation(player.getLocation()) + ChatColor.YELLOW + "\n" + divider);
            saveSpawnLoc(player.getLocation());
        } else if (args[0].equals("setlobby")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.GREEN + "Лобби установлено!\n" + LocationUtulities.getPlayerLocation(player.getLocation()) + ChatColor.YELLOW + "\n" + divider);
            saveLobbyLoc(player.getLocation());
        } else if (args[0].equals("clear")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.GREEN + "Очереди очищены!\n" + ChatColor.YELLOW + divider);
            clearQueues();
        } else if (args[0].equals("broadcast")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.GREEN + "Сообщение опубликовано!\n" + ChatColor.YELLOW + divider);
            broadcast(Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equals("test")) {

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwningPlayer(player);
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + player.getName());
            skull.setItemMeta(meta);

            ArmorStandConstructor.ArmorStandConstructor(player.getLocation(), skull, ChatColor.GREEN + player.getName());

        } else {
            return false;
        }
        return true;
    }

    public void saveLobbyLoc(Location location) {
        FileConfiguration config = Main.main.getConfig();

        config.set("lobby.world", location.getWorld().getName());
        config.set("lobby.x", location.getX());
        config.set("lobby.y", location.getY());
        config.set("lobby.z", location.getZ());
        config.set("lobby.pitch", location.getPitch());
        config.set("lobby.yaw", location.getYaw());

        Main.main.saveConfig();
    }

    public void clearQueues() {

        for (String name : Queue.redQueueList)
            PrestartScoreBoard.red.removeEntry(name);
        Queue.redQueueList.clear();

        for (String name : Queue.yellowQueueList)
            PrestartScoreBoard.yellow.removeEntry(name);
        Queue.yellowQueueList.clear();

        for (String name : Queue.greenQueueList)
            PrestartScoreBoard.green.removeEntry(name);
        Queue.greenQueueList.clear();
    }

    public void saveSpawnLoc(Location location) {
        FileConfiguration config = Main.main.getConfig();

        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.pitch", location.getPitch());
        config.set("spawn.yaw", location.getYaw());

        Main.main.saveConfig();
    }

    public void broadcast(String[] message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "Внимание!", ChatColor.YELLOW + String.join(" ", message), 40, 60, 40);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
        }
    }

    public static void teleportToLobby(Player player) {
        FileConfiguration config = Main.main.getConfig();

        World world = Bukkit.getWorld(config.getString("lobby.world"));
        Location location = new Location(world, config.getDouble("lobby.x"), config.getDouble("lobby.y"), config.getDouble("lobby.z"));

        location.setPitch((float) config.getDouble("lobby.pitch"));
        location.setYaw((float) config.getDouble("lobby.yaw"));

        player.teleport(location);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы были телепортированы в Лобби!");
    }

    public static void teleportToSpawn(Player player) {
        FileConfiguration config = Main.main.getConfig();

        World world = Bukkit.getWorld(config.getString("spawn.world"));

        Double x = config.getDouble("spawn.x") + (Utilities.getRandom(0, 20) - 10);
        Double z = config.getDouble("spawn.z") + (Utilities.getRandom(0, 20) - 10);

        Location location = new Location(world, x, config.getDouble("spawn.y"), z);

        location.setPitch((float) config.getDouble("spawn.pitch"));
        location.setYaw((float) config.getDouble("spawn.yaw"));

        player.teleport(location);
    }

    public static Location randLocationSpawn() {
        FileConfiguration config = Main.main.getConfig();

        World world = Bukkit.getWorld(config.getString("spawn.world"));

        Double x = config.getDouble("spawn.x") + (Utilities.getRandom(0, 20) - 10);
        Double z = config.getDouble("spawn.z") + (Utilities.getRandom(0, 20) - 10);

        Location location = new Location(world, x, config.getDouble("spawn.y"), z);

        location.setPitch((float) config.getDouble("spawn.pitch"));
        location.setYaw((float) config.getDouble("spawn.yaw"));

        return location;
    }
}
