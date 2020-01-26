package Game;

import PluginUtilities.Chat;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CowMilk implements Listener {
    private static boolean isActivated = false;

    public static void CowMilk() {
        isActivated = aGameCycle.isAnyBattleEnabled;

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Подоите корову!", "Скорее!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подоите корову!");

            for (int i = 0; i < 36; i++)
                player.getInventory().addItem(new ItemStack(Material.BUCKET, 16));
        }

        for (int i = 0; i < 10; i++) {
            Cow cow = (Cow) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.COW);
            cow.setCustomName(ChatColor.YELLOW + "Милка");
        }
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerCowMilk(PlayerInteractEntityEvent event) {
        if (!isActivated)
            return;

        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof Cow && player.getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) {
            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getRightClicked().getLocation(), 10);
            aGameCycle.playerWin(player, place);
            place++;
        }

        if (place > 3){
            isActivated = false;
            aGameCycle.isAnyBattleEnabled = false;
            place = 1;
        }
    }
}