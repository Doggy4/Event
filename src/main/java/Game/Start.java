package Game;

import Commands.CommandEvent;
import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Start {

    public static void StartGame (){
        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);

            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.setExp(0);

            CommandEvent.teleportToSpawn(player);
            GameCycle.gameStats.put(playerName, 0);
        }
        Commands.StartEvent.isGameStarted = true;
    }
}
