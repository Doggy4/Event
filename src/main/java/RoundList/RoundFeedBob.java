package RoundList;

import PluginUtils.InventoryUtils;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

// ~~~~~~~~ TEST ~~~~~~~~
public class RoundFeedBob implements Listener {
    public static boolean isActivated = false;
    private static BukkitRunnable runnable;

    private static ArrayList<ItemStack> foods = new ArrayList<ItemStack>();

    static {
        for (Material mat : Material.values()) if (mat.isEdible()) foods.add(new ItemStack(mat));
    }

    public static void feedBob() {
        // Опциально:
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        RoundRules.EntityDamageOff();
        MapRebuild.loadSchematic("default-arena");

        for (Player player : Queue.redQueueList)
            gameRulesAnnouncement(player);

        for (int i = 0; i < 20; i++)
            spawnMob();
        spawnBob();

    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Соберите еду", "Накормите Боба", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Соберите еду и накормите Боба!");
    }

    public static void spawnMob() {
        Zombie zombie = (Zombie) LocationUtils.world.spawnEntity(LocationUtils.getRandomLocation(), EntityType.ZOMBIE);

        ItemStack food = foods.get(Utils.getRandom(0, foods.size() - 1));
        zombie.setGlowing(true);
        zombie.setBaby(true);

        zombie.setHealth(1.0);

        zombie.getEquipment().setItemInMainHand(food);
        zombie.getEquipment().setItemInOffHand(food);
        zombie.getEquipment().setHelmet(new ItemStack(Material.FURNACE));

        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 9999));

        zombie.setCustomName(ChatColor.GREEN + "Вкусность");
        zombie.setCustomNameVisible(true);
    }

    public static void spawnBob() {
        Panda panda = (Panda) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(LocationUtils.getSpawnLocation(), EntityType.PANDA);

        panda.setGlowing(true);
        panda.setBaby();

        panda.setCustomName(ChatColor.YELLOW + "Боб :3");
        panda.setCustomNameVisible(true);

        panda.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 9999));

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this;
                panda.teleport(LocationUtils.getSpawnLocation());
            }
        }.runTaskTimer(Main.main, 1, 1);
    }

    public static void endFeedBob() {
        isActivated = false;
        for (Player player : Queue.redQueueList)
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Поздравляю! Боб теперь сыт!");
        runnable.cancel();
    }

    @EventHandler
    public void onPlayerFeedBob(PlayerInteractEntityEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getHand().equals(EquipmentSlot.HAND)) return;
        if (event.getRightClicked().getCustomName() == null) return;
        if (!event.getRightClicked().getCustomName().contains("Боб")) return;
        if (player.getInventory().getItemInMainHand().getType().isEdible()) {
            player.sendMessage(ChatColor.GOLD + "<Боб> " + ChatColor.WHITE + "Спасибо, что накормил меня, " + player.getName() + "! :3");
            InventoryUtils.removeItem(player, player.getInventory().getItemInMainHand().getType(), 1);
            RoundSystem.addScore(player, 1);
        } else
            player.sendMessage(ChatColor.GOLD + "<Боб> " + ChatColor.WHITE + "Я хочу кушать, это смешно, " + player.getName() + "? :c");

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!isActivated) return;
        if (event.getEntity().getCustomName() == null) return;
        if (!(event.getEntity() instanceof Zombie)) return;
        if (!event.getEntity().getCustomName().contains("Вкусность")) return;
        spawnMob();
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.getEntity().getKiller().getInventory().addItem(foods.get(Utils.getRandom(0, foods.size() - 1)));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!isActivated) return;
        if (event.getEntity().getCustomName() == null) return;
        if (!(event.getEntity() instanceof Panda)) return;
        if (!event.getEntity().getCustomName().contains("Боб")) return;
        event.setCancelled(true);
    }
}
