package Game;

import Commands.CommandEvent;
import Commands.StartEvent;
import QueueSystem.Queue;
import ScoreBoardWork.PrestartScoreBoard;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Ending {
    public static void EndGame() {
        StartEvent.isGameTimerStarted = false;
        Commands.StartEvent.isGameStarted = false;
        StartEvent.secPreStart = 60;
        GameCycle.battle = 1;

        int max = 0;
        String winner = "null";

        for (String name : Queue.redQueueList)
            if (GameCycle.gameStats.get(name) > max) {
                max = GameCycle.gameStats.get(name);
                winner = name;
            }

        Player player = Bukkit.getPlayer(winner);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(ChatColor.AQUA + winner, ChatColor.GREEN + "победитель", 20, 40, 20);
            onlinePlayer.sendMessage(ChatColor.GREEN + "Игрок " + ChatColor.AQUA + winner + ChatColor.GREEN + " победитель!");
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            onlinePlayer.spawnParticle(Particle.DRAGON_BREATH, onlinePlayer.getLocation(), 80);
        }

        for (String playerName : Queue.redQueueList) {
            Player redPlayer = Bukkit.getPlayer(playerName);
            CommandEvent.teleportToLobby(redPlayer);

            PrestartScoreBoard.red.removeEntry(playerName);
        }

        Queue.redQueueList.clear();

        for (String playerName : Queue.yellowQueueList) {
            Queue.redQueueList.add(playerName);

            Player yellowPlayer = Bukkit.getPlayer(playerName);
            PrestartScoreBoard.red.addEntry(playerName);
            yellowPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]", ChatColor.LIGHT_PURPLE + "Приготовьтесь к игре!", 20, 20, 20);
            yellowPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]");
            yellowPlayer.playSound(yellowPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }

        Queue.yellowQueueList.clear();

        for (int i = 0; i < 10; i++) {
            String playerName = Queue.greenQueueList.get(i);


            if (playerName == null)
                break;

            Queue.greenQueueList.remove(i);
            Queue.yellowQueueList.add(playerName);

            Player greenPlayer = Bukkit.getPlayer(playerName);
            PrestartScoreBoard.yellow.addEntry(playerName);
            greenPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]", ChatColor.LIGHT_PURPLE + "Ожидайте!", 20, 20, 20);
            greenPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]");
            greenPlayer.playSound(greenPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }
    }
}
