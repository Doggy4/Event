package RoundSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class GameRules implements Listener {

    private static boolean playerDamageRULE = false;
    private static boolean breakBlockRULE = false;
    private static boolean dropItemRULE = false;
    private static boolean pickUpItemRULE = false;
    private static boolean entityDamageRULE = false;
    private static boolean placeBlockRULE = false;

    public static void TurnOnAllRules() {
        playerDamageRULE = true;
        breakBlockRULE = true;
        dropItemRULE = true;
        pickUpItemRULE = true;
        entityDamageRULE = true;
        placeBlockRULE = true;
    }

    public static void TurnOffAllRules() {
        playerDamageRULE = false;
        breakBlockRULE = false;
        dropItemRULE = false;
        pickUpItemRULE = false;
        entityDamageRULE = false;
        placeBlockRULE = false;
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
        e.setCancelled(playerDamageRULE);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        e.setCancelled(breakBlockRULE);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(dropItemRULE);
    }

    @EventHandler
    public void onPickItem(EntityPickupItemEvent e) {
        e.setCancelled(pickUpItemRULE);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) return;
        e.setCancelled(entityDamageRULE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(placeBlockRULE);
    }
}