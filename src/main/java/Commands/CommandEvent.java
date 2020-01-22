package Commands;

import PluginUtilities.LocationUtulities;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import ScoreBoardWork.PrestartScoreBoard;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandEvent implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[EVENT] Only players can use this command!");
        } else {
            Player player = (Player) sender;

            if (args.length < 1)
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.RED + "Помощь:\n" + ChatColor.GREEN + "/event start" + ChatColor.YELLOW + " - начать эвент\n" + ChatColor.GREEN + "/event setspawn" + ChatColor.YELLOW + " - установить спавн\n" + ChatColor.GREEN + "/event setlobby" + ChatColor.YELLOW + " - установить лобби\n" + ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            else if (args[0].equals("start")) {
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GREEN + "Эвент запущен!" + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                Commands.StartEvent.startEvent();
            } else if (args[0].equals("setspawn")) {
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GREEN + "Спавн установлен!\n" + LocationUtulities.getPlayerLocation(player.getLocation()) + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                saveSpawnLoc(player.getLocation());
            } else if (args[0].equals("setlobby")) {
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GREEN + "Лобби установлено!\n" + LocationUtulities.getPlayerLocation(player.getLocation()) + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                saveLobbyLoc(player.getLocation());
            } else if (args[0].equals("clear")) {
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GREEN + "Очереди очищены!" + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                clearQueues();
            } else {
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.RED + "Помощь:\n" + ChatColor.GREEN + "/event start" + ChatColor.YELLOW + " - начать эвент\n" + ChatColor.GREEN + "/event setspawn" + ChatColor.YELLOW + " - установить спавн\n" + ChatColor.GREEN + "/event setlobby" + ChatColor.YELLOW + " - установить лобби\n" + ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            }
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
            PrestartScoreBoard.scoreboard.getTeam("RED").removeEntry(name);
        Queue.redQueueList.clear();

        for (String name : Queue.yellowQueueList)
            PrestartScoreBoard.scoreboard.getTeam("YELLOW").removeEntry(name);
        Queue.yellowQueueList.clear();

        for (String name : Queue.greenQueueList)
            PrestartScoreBoard.scoreboard.getTeam("GREEN").removeEntry(name);
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
}
