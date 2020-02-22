package RoundSystem;

import PluginUtils.Chat;
import PluginUtils.DiscordWebHooks;
import PluginUtils.LocationUtils;
import QueueSystem.MainScoreBoard;
import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class GameCycle {

    public static HashMap<Player, Integer> gameStats = new HashMap<Player, Integer>();

    public static GameState gameState = GameState.WAITING;

    public static void mainCycle() {
        // START

        if (gameState == GameState.STARTING)
            MainScoreBoard.countdown();

        else if (gameState == GameState.IN_GAME && !RoundSystem.isRoundStarted && !RoundSystem.isRoundTimerEnabled)
            RoundSystem.startRound();

        else if (gameState == GameState.IN_GAME)
            RoundSystem.roundTimer();
    }

    public static void StartGame() {
        for (Player player : Queue.redQueueList) {
            player.setExp(0);
            RoundSystem.playerReset(player);
            LocationUtils.teleportToSpawn(player);
            RoundSystem.roundStats.put(player, 0);
            gameStats.put(player, 0);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            NBS.NoteBlockPlayer.playMusic(player, "MadWorld.nbs");
            player.sendTitle(ChatColor.BLUE + "Игра началась!", ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
            player.sendMessage(ChatColor.AQUA + Chat.divThick16 + ChatColor.GOLD + "[EVENT] " + ChatColor.BLUE + "Игра началась!\n" + ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]\n" + ChatColor.AQUA + Chat.divThick16);
        }

        gameState = GameState.IN_GAME;
        DiscordWebHooks.gameStarted();
    }

    public static void endGame() {
        DiscordWebHooks.gameEnded();
        MainScoreBoard.bossbar.setTitle(ChatColor.GOLD + "Ожидание...");
        MainScoreBoard.bossbar.setColor(BarColor.YELLOW);
        MainScoreBoard.bossbar.setProgress(1);

        gameState = GameState.WAITING;
        MainScoreBoard.mainSecPreStart = 60;
        RoundSystem.round = 1;

        Player winner = getWinner();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            onlinePlayer.spawnParticle(Particle.DRAGON_BREATH, onlinePlayer.getLocation(), 80);

            onlinePlayer.sendTitle(ChatColor.GOLD + winner.getName(), ChatColor.BLUE + "победитель", 20, 40, 20);
            onlinePlayer.sendMessage(ChatColor.BLUE + "Игрок " + ChatColor.GOLD + winner.getName() + ChatColor.BLUE + " победитель!");
        }

        for (Player redPlayer : Queue.redQueueList) {
            LocationUtils.teleportToLobby(redPlayer);
            MainScoreBoard.red.removeEntry(redPlayer.getName());
        }

        Queue.redQueueList.clear();

        for (Player yellowPlayer : Queue.yellowQueueList) {
            Queue.redQueueList.add(yellowPlayer);

            MainScoreBoard.red.addEntry(yellowPlayer.getName());
            yellowPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]", ChatColor.LIGHT_PURPLE + "Приготовьтесь к игре!", 20, 20, 20);
            yellowPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]");
            yellowPlayer.playSound(yellowPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }

        Queue.yellowQueueList.clear();

        for (int i = 0; i < 10; i++) {
            if (Queue.greenQueueList.get(i) == null) break;
            Player greenPlayer = Queue.greenQueueList.get(i);
            Queue.greenQueueList.remove(i);
            Queue.yellowQueueList.add(greenPlayer);

            MainScoreBoard.yellow.addEntry(greenPlayer.getName());
            greenPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]", ChatColor.LIGHT_PURPLE + "Ожидайте!", 20, 20, 20);
            greenPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]");
            greenPlayer.playSound(greenPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }
    }

    public static Player getWinner() {
        int max = -1;
        Player winner = null;

        for (Player player : Queue.redQueueList)
            if (RoundSystem.roundStats.get(player) > max) {
                max = RoundSystem.roundStats.get(player);
                winner = player;
            }

        return winner;
    }
}
