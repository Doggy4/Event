package Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class BaseClass implements Listener {

    private static boolean playerDamageRULE = true;
    private static boolean breakBlockRULE = true;
    private static boolean dropItemRULE = true;
    private static boolean pickUpItemRULE = true;
    private static boolean entityDamageRULE = true;
    private static boolean placeBlockRULE = true;

    public static void TurnOffAllRules() {
        playerDamageRULE = true;
        breakBlockRULE = true;
        dropItemRULE = true;
        pickUpItemRULE = true;
        entityDamageRULE = true;
        placeBlockRULE = true;
    }

    public static void PlaceBlockOff() {
        placeBlockRULE = false;
    }

    public static void PlayerDamageOff() {
        playerDamageRULE = false;
    }

    public static void BreakBlockOff() {
        breakBlockRULE = false;
    }

    public static void DropItemOff() {
        dropItemRULE = false;
    }

    public static void PickUpItemOff() {
        pickUpItemRULE = false;
    }

    public static void EntityDamageOff() {
        entityDamageRULE = false;
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