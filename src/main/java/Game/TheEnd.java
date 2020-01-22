package Game;

import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TheEnd {
    public static void EndGame() {
        Bukkit.broadcastMessage("f");
        int max = 0;
        String winner = "null";

        for (String name : Queue.redQueueList)
            if (GameCycle.gameStats.get(name) > max) {
                max = GameCycle.gameStats.get(name);
                winner = name;
            }

        Player player = Bukkit.getPlayer(winner);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(ChatColor.AQUA + winner, ChatColor.GREEN + "победитель", 20, 20, 20);
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            onlinePlayer.spawnParticle(Particle.DRAGON_BREATH, onlinePlayer.getLocation(), 80);
        }
    }
}
