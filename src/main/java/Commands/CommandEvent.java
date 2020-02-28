package Commands;

import Constructors.ArmorStandConstructor;
import ImageMaps.ImageMapCommands;
import NBS.NoteBlockPlayer;
import PluginUtils.Chat;
import QueueSystem.MainScoreBoard;
import QueueSystem.Queue;
import RoundList.RoundMineAnOre;
import RoundSystem.GameCycle;
import RoundSystem.GameState;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEvent implements TabExecutor {

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            return Arrays.asList("help", "start", "setlobby", "setspawn", "broadcast", "test", "clear", "music", "build", "image");
        }

        if (args.length == 2 && args[0].equals("music")) {
            File[] files = new File(Main.main.getDataFolder().getAbsolutePath() + "/songs/").listFiles();
            List<String> fileNames = new ArrayList<String>();
            for (File file : files) {
                fileNames.add(file.getName());
            }
            return fileNames;
        }

        if (args.length == 2 && args[0].equals("build")) {
            File[] files = new File(Main.main.getDataFolder().getAbsolutePath() + "/schematics/").listFiles();
            List<String> fileNames = new ArrayList<String>();
            for (File file : files) {
                fileNames.add(file.getName());
            }
            return fileNames;
        }

        if (args.length == 2 && args[0].equals("image")) {
            File[] files = new File(Main.main.getDataFolder().getAbsolutePath() + "/images/").listFiles();
            List<String> fileNames = new ArrayList<String>();
            for (File file : files) {
                fileNames.add(file.getName());
            }
            return fileNames;
        }

        return Collections.emptyList();
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

        String divider = Chat.div;

        if (args.length < 1 || args[0].equals("help")) {
            player.sendMessage(ChatColor.YELLOW + divider + ChatColor.RED + "Помощь:\n" + ChatColor.GREEN + "/event start" + ChatColor.YELLOW + " - начать эвент\n" + ChatColor.GREEN + "/event setspawn" + ChatColor.YELLOW + " - установить спавн\n" + ChatColor.GREEN + "/event setlobby" + ChatColor.YELLOW + " - установить лобби\n" + ChatColor.GREEN + "/event broadcast [сообщение]" + ChatColor.YELLOW + " - отправить оповещение\n" + ChatColor.GREEN + "/event clear" + ChatColor.YELLOW + " - очистить очереди\n" + ChatColor.GREEN + "/event music [музыка] " + ChatColor.YELLOW + "- проиграть музыку\n" + ChatColor.GREEN + "/event build [арена]" + ChatColor.YELLOW + " - построить арену\n" + ChatColor.GREEN + "/event image [файл.png] " + ChatColor.YELLOW + " - настройка карты изображений\n" + ChatColor.YELLOW + divider);
        } else if (args[0].equals("start")) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Эвент успешно запущен!");
            GameCycle.gameState = GameState.STARTING;
        } else if (args[0].equals("setspawn")) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Центр арены успешно установлен!");
            saveSpawnLoc(player.getLocation());
        } else if (args[0].equals("setlobby")) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Лобби успешно установлено!");
            saveLobbyLoc(player.getLocation());
        } else if (args[0].equals("clear")) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Очереди успешно очищены!");
            clearQueues();
        } else if (args[0].equals("broadcast")) {
            broadcast(Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equals("build")) {
            MapRebuild.loadSchematic(args[1]);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Арена успешно загружена!");
        } else if (args[0].equals("music")) {
            NoteBlockPlayer.playMusic(player, args[1]);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Мелодия успешно запущена!");
        } else if (args[0].equals("image")) {
            ImageMapCommands.imageMapCommand(player, args);
        } else if (args[0].equals("statue")) {
            ArmorStandConstructor.ArmorStandConstructor(player.getLocation(), args[1]);
        } else if (args[0].equals("test")) {
            player.sendMessage("Test");
            RoundMineAnOre.mineAnOre();
        } else
            return false;

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
        Chat.broadcastToEveryone("Встаньте в очередь! Приоритет очереди: " + ChatColor.RED + "[RED]");

        for (Player player : Queue.redQueueList)
            MainScoreBoard.red.removeEntry(player.getName());
        Queue.redQueueList.clear();

        for (Player player : Queue.yellowQueueList)
            MainScoreBoard.yellow.removeEntry(player.getName());
        Queue.yellowQueueList.clear();

        for (Player player : Queue.greenQueueList)
            MainScoreBoard.green.removeEntry(player.getName());
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
        Chat.broadcastToEveryone(String.join(" ", message));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "Внимание!", ChatColor.YELLOW + String.join(" ", message), 40, 60, 40);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
        }
    }
}
