package Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BaseClass implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!GameCycle.isAnyBattleEnabled)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (!GameCycle.isAnyBattleEnabled)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!GameCycle.isAnyBattleEnabled)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickItem(EntityPickupItemEvent e) {
        if (!GameCycle.isAnyBattleEnabled)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!GameCycle.isAnyBattleEnabled)
            return;
        e.setCancelled(true);
    }
}

