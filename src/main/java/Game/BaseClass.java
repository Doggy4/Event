package Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BaseClass implements Listener {

    private static boolean isActivated = false;

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!isActivated)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (!isActivated)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!isActivated)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickItem(EntityPickupItemEvent e) {
        if (!isActivated)
            return;
        e.setCancelled(true);
    }
}

