package Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BaseClass implements Listener {

    private static boolean playerDamageRULE = false;
    private static boolean breakBlockRULE = false;
    private static boolean dropItemRULE = false;
    private static boolean pickUpItemRULE = false;
    private static boolean entityDamageRULE = false;
    private static boolean placeBlockRULE = false;

    public static void TurnOffAllRules() {
        playerDamageRULE = true;
        breakBlockRULE = true;
        dropItemRULE = true;
        pickUpItemRULE = true;
        entityDamageRULE = true;
        placeBlockRULE = true;
    }

    public static void PlaceBlockOff() {
        placeBlockRULE = true;
    }

    public static void PlayerDamageOff() {
        playerDamageRULE = true;
    }

    public static void BreakBlockOff() {
        breakBlockRULE = true;
    }

    public static void DropItemOff() {
        dropItemRULE = true;
    }

    public static void PickUpItemOff() {
        pickUpItemRULE = true;
    }

    public static void EntityDamageOff() {
        entityDamageRULE = true;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!playerDamageRULE) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (!breakBlockRULE) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!dropItemRULE) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickItem(EntityPickupItemEvent e) {
        if (!pickUpItemRULE) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!entityDamageRULE) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!placeBlockRULE) return;
        e.setCancelled(true);
    }
}

