package Game;

import Commands.CommandEvent;
import Commands.StartEvent;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import ScoreBoardWork.PrestartScoreBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;

public class GameCycle {

    public static HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
    public static boolean isAnyBattleEnabled = false;
    public static int battle = 1;

    public static void mainCycle() {
        if (StartEvent.secPreStart < 0 && !Commands.StartEvent.isGameStarted) {
            for (String playerName : Queue.redQueueList){
                CommandEvent.teleportToSpawn(Bukkit.getPlayer(playerName));
                hashmap.put(playerName, 0);
            }
            Commands.StartEvent.isGameStarted = true;

        }

        if (!isAnyBattleEnabled && Commands.StartEvent.isGameStarted && battle < 10)
            randomBattle();


        for (String playerName : Queue.redQueueList) {
            Player redPlayer = Bukkit.getPlayer(playerName);
            redPlayer.getLocation().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, redPlayer.getLocation().getX(), redPlayer.getLocation().getY() + 2.5, redPlayer.getLocation().getZ(), 2);
        }

        if (StartEvent.secPreStart == 30 || StartEvent.secPreStart == 15 || StartEvent.secPreStart == 10 || StartEvent.secPreStart == 5)
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "" + StartEvent.secPreStart + " секунд", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        else if (StartEvent.secPreStart < 5 && StartEvent.secPreStart > 1)
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "" + StartEvent.secPreStart + " секунды", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }

        else if (StartEvent.secPreStart == 1)
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "1 секунду", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        else if (StartEvent.secPreStart == 0)
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Игра началась!", ChatColor.YELLOW + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Игра началась!\n" + ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
            }

    }

    private static void randomBattle() {
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Раунд: " + ChatColor.AQUA + battle + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
        battle++;

        isAnyBattleEnabled = true;

        int randomBattle = Utilities.getRandom(0, 1);
        switch (randomBattle) {
            case 0:
                PlaceBlock.placeBlock();
                break;
            case 1:
                DropItem.DropItem();
                break;
        }
    }

    public static void addScore(Player winner, int place) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + winner.getName() + " занял " + ChatColor.GREEN + place + ChatColor.WHITE + " место!");

        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "+" + (4 - place) + " очков(-а)!");

        hashmap.put(winner.getName(), 4 - place + hashmap.get(winner.getName()));
    }

}