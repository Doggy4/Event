package QueueSystem;

import ScoreBoardWork.PrestartScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    public static List<String> redQueueList = new ArrayList<String>();
    public static List<String> yellowQueueList = new ArrayList<String>();
    public static List<String> greenQueueList = new ArrayList<String>();

    private static Team redQueue = PrestartScoreBoard.scoreboard.getTeam("RED");
    private static Team yellowQueue = PrestartScoreBoard.scoreboard.getTeam("YELLOW");
    private static Team greenQueue = PrestartScoreBoard.scoreboard.getTeam("GREEN");

    public static void AddToRedQueue(Player player) {
        if (redQueueList.contains(player.getName())) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы уже находитесь в очереди " + ChatColor.RED + "[RED]" + ChatColor.WHITE + "!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.RED + "Ожидают: " + ChatColor.WHITE + "[" + redQueueList.size() + "/10]!");
            return;
        }

        if (redQueueList.size() >= 10) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Очередь " + ChatColor.RED + "[RED] " + ChatColor.WHITE + "заполнена!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.RED + "Ожидают: " + ChatColor.WHITE + "[" + redQueueList.size() + "/10]!");
            return;
        }

        LeaveQueue(player);
        redQueue.addEntry(player.getName());
        redQueueList.add(player.getName());

        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы успешно встали в очередь " + ChatColor.RED + "[RED]" + ChatColor.WHITE + "!");
        player.sendMessage(ChatColor.RED + "Ожидают: " + ChatColor.WHITE + "[" + redQueueList.size() + "/10]!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);

    }

    public static void AddToYellowQueue(Player player) {

        if (yellowQueueList.contains(player.getName())) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы уже находитесь в очереди " + ChatColor.YELLOW + "[YELLOW]" + ChatColor.WHITE + "!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.YELLOW + "Ожидают: " + ChatColor.WHITE + "[" + yellowQueueList.size() + "/10]!");
            return;
        }

        if (yellowQueueList.size() >= 10) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Очередь " + ChatColor.YELLOW + "[YELLOW] " + ChatColor.WHITE + "заполнена!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.YELLOW + "Ожидают: " + ChatColor.WHITE + "[" + yellowQueueList.size() + "/10]!");
            return;
        }

        LeaveQueue(player);
        yellowQueue.addEntry(player.getName());
        yellowQueueList.add(player.getName());

        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы успешно встали в очередь " + ChatColor.YELLOW + "[YELLOW]" + ChatColor.WHITE + "!");
        player.sendMessage(ChatColor.YELLOW + "Ожидают: " + ChatColor.WHITE + "[" + yellowQueueList.size() + "/10]!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);

    }

    public static void AddToGreenQueue(Player player) {

        if (greenQueueList.contains(player.getName())) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы уже находитесь в очереди " + ChatColor.GREEN + "[GREEN]" + ChatColor.WHITE + "!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.GREEN + "Ожидают: " + ChatColor.WHITE + "[" + greenQueueList.size() + "/18]!");
            return;
        }

        if (greenQueueList.size() >= 18) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Очередь " + ChatColor.GREEN + "[GREEN] " + ChatColor.WHITE + "заполнена!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            player.sendMessage(ChatColor.GREEN + "Ожидают: " + ChatColor.WHITE + "[" + greenQueueList.size() + "/18]!");
            return;
        }

        LeaveQueue(player);
        greenQueue.addEntry(player.getName());
        greenQueueList.add(player.getName());

        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы успешно встали в очередь " + ChatColor.GREEN + "[GREEN]" + ChatColor.WHITE + "!");
        player.sendMessage(ChatColor.GREEN + "Ожидают: " + ChatColor.WHITE + "[" + greenQueueList.size() + "/18]!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
    }

    public static void LeaveQueueBroadcaster(Player player) {
        boolean isInQueue = false;
        String lastQueue = null;

        if (redQueueList.contains(player.getName())) {
            lastQueue = ChatColor.RED + "[RED]";
            redQueueList.remove(player.getName());
            redQueue.removeEntry(player.getName());
        }


        if (yellowQueueList.contains(player.getName())) {
            lastQueue = ChatColor.YELLOW + "[YELLOW]";

            yellowQueueList.remove(player.getName());
            yellowQueue.removeEntry(player.getName());
        }


        if (greenQueueList.contains(player.getName())) {
            lastQueue = ChatColor.GREEN + "[GREEN]";

            greenQueueList.remove(player.getName());
            greenQueue.removeEntry(player.getName());
        }


        if (lastQueue != null) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы успешно вышли из очереди " + lastQueue
                    + ChatColor.WHITE + "!");
            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 10, 1);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы не находитесь в очереди!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }

    }

    public static void LeaveQueue(Player player) {
        boolean isInQueue = false;
        String lastQueue = null;

        if (redQueueList.contains(player.getName())) {
            lastQueue = ChatColor.RED + "[RED]";
            redQueueList.remove(player.getName());
            redQueue.removeEntry(player.getName());
        }


        if (yellowQueueList.contains(player.getName())) {
            lastQueue = ChatColor.YELLOW + "[YELLOW]";

            yellowQueueList.remove(player.getName());
            yellowQueue.removeEntry(player.getName());
        }


        if (greenQueueList.contains(player.getName())) {
            lastQueue = ChatColor.GREEN + "[GREEN]";

            greenQueueList.remove(player.getName());
            greenQueue.removeEntry(player.getName());
        }

    }
}
