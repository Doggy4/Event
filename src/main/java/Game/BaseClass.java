package Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class BaseClass implements Listener {
    private static boolean PlayerDamageRule = false;
    private static boolean PlayerBlockBreakRule = false;
    private static boolean PlayerDropItemRule = false;

    public static void  PlayerDamageOn() {
        PlayerDamageRule = true;
    }

    public static void  PlayerBlockBreakRuleOn() {
        PlayerBlockBreakRule = true;
    }

    public static void  PlayerDropItemRuleOn() {
        PlayerDropItemRule = true;
    }

    /////////////////////////////////////////////////
    public static void  PlayerDamageOff() {
        PlayerDamageRule = false;
    }

    public static void  PlayerBlockBreakRuleOff() {
        PlayerBlockBreakRule = false;
    }

    public static void  PlayerDropItemRuleOff() {
        PlayerDropItemRule = false;
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (PlayerDamageRule == true)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (PlayerBlockBreakRule == true)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (PlayerDropItemRule == true)
            return;
        e.setCancelled(true);
    }
}

