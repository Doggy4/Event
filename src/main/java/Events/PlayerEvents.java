package Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Vector;

public class PlayerEvents implements Listener {
    // Слушатель убийств игрока
    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        String killed = e.getEntity().getName();
        String killer = e.getEntity().getKiller().getName();
        e.setDeathMessage(ChatColor.RED + "☠ Игрок " + killed + ChatColor.WHITE + " был убит игроком " + ChatColor.GREEN + killer);
    }

    @EventHandler
    public void onMove(CraftItemEvent e) {

    }
}
