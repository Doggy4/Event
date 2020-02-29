package RoundList;

import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class RoundSnowFight implements Listener {
    public static boolean isActivated = false;

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");
        RoundRules.PlayerDamageOff();

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            for (int j = 0; j < 36; j++)
                player.getInventory().setItem(j, new ItemStack(Material.SNOWBALL, 16));
        }
    }

    public static void endSnowFight() {
        isActivated = false;
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Кидайте снежки", "в своих соперников", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Кидайте снежки в своих соперников!");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Projectile projectile = (Projectile) event.getDamager();
        if (!(event.getDamager() instanceof Projectile)) return;
        if (!(projectile.getShooter() instanceof Player)) return;
        Player damager = (Player) projectile.getShooter();
        Player damaged = (Player) event.getEntity();
        if (!(Queue.redQueueList.contains(damaged))) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            RoundSystem.addScore(damager, 1);
            RoundSystem.addScore(damaged, -1);
        }
    }
}
