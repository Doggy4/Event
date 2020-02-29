package RoundList;

import PluginUtils.Items;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RoundHotPotato implements Listener {
    public static boolean isActivated = false;

    public static void startRound() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");
        RoundRules.PlayerDamageOff();

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 4));
        }

        for (int i = 0; i < Queue.redQueueList.size() / 2; i++) {
            Player player = Queue.redQueueList.get(Utils.getRandom(0, Queue.redQueueList.size() - 1));
            passHotPotato(player);
        }
    }

    private static void passHotPotato(Player player) {
        for (int j = 0; j < 36; j++)
            player.getInventory().setItem(j, Items.hotPotato);
        player.getInventory().setHelmet(new ItemStack(Material.TNT));
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы стали горячей картошкой!");
        player.sendTitle(ChatColor.RED + "Вы стали", "горячей картошкой", 20, 20, 20);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000, 100));
        //player.setParticle(ParticleType.HOT_POTATO);
    }


    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Спаситесь от", "горячей картошки", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спаситесь от горячей картошки!");
    }

    public static void endHotPotato() {
        isActivated = false;

        for (Player player : Queue.redQueueList) {
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                RoundSystem.playerWin(player);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
                RoundSystem.playerLose(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        if (!Queue.redQueueList.contains(damager)) return;
        if (!Queue.redQueueList.contains(damaged)) return;
        if (damager.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!damager.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Items.hotPotato.getItemMeta().getDisplayName())) return;
        passHotPotato(damaged);
        damager.getInventory().clear();
        damager.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы передали горячую картошку!");
    }
}
