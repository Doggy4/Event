package Game;

import PluginUtilities.LocationUtulities;
import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

// ~~~~~~~~ TEST ~~~~~~~~
public class RoundFeedBob {
    protected static boolean isActivated = false;

    private static ArrayList<ItemStack> foods = new ArrayList<ItemStack>();

    static {
        for (Material mat : Material.values()) if (mat.isEdible()) foods.add(new ItemStack(mat));
    }

    protected static void DropTheItem() {
        // Опциально:
        isActivated = true;
        aRoundSystem.roundSeconds = 60;
        GameRules.EntityDamageOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
        }

    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Соберите еду", "Покормите Боба", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Соберите еду и покормите Боба!");
    }

    public static void spawnMob() {
        Zombie zombie = (Zombie) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(LocationUtulities.getSpawnLocation(), EntityType.ZOMBIE);

        ItemStack food = foods.get(Utilities.getRandom(0, foods.size()));
        zombie.setCustomName(ChatColor.GREEN + "Вкусность");
        zombie.setCustomNameVisible(true);
        zombie.setGlowing(true);
        zombie.setBaby(true);

        zombie.getEquipment().setItemInMainHand(food);
        zombie.getEquipment().setItemInOffHand(food);
        zombie.getEquipment().setHelmet(new ItemStack(Material.FURNACE));

    }

    @EventHandler
    public void onPlayerFeedBob(PlayerInteractEntityEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        if (!event.getRightClicked().getCustomName().contains("Боб")) return;
        aRoundSystem.addScore(player, 1);

    }
}
