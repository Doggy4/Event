package RoundList;

import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RoundMilkTheCow implements Listener {
    public static boolean isActivated = false;

    private static String commonCow = (ChatColor.GRAY + "[✶] Милка");
    private static String uncommonCow = (ChatColor.GREEN + "[✷] Милка");
    private static String rareCow = (ChatColor.BLUE + "[✸] Милка");
    private static String specialCow = (ChatColor.LIGHT_PURPLE + "[✳] Милка");
    private static String boomCow = (ChatColor.RED + "[✹] Бум-корова");

    private static int boomCowChance = 50;
    private static int uncommonCowChance = 70;
    private static int rareCowChance = 85;
    private static int specialCowChance = 95;

    public static void milkTheCow() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

            for (int i = 0; i < 10; i++) {
                int chance = Utils.getRandom(0, 100);
                Cow cow = (Cow) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.COW);

                if (chance > boomCowChance && chance < uncommonCowChance) cow.setCustomName(boomCow);
                else if (chance < uncommonCowChance) cow.setCustomName(commonCow);
                else if (chance > uncommonCowChance && chance < rareCowChance) cow.setCustomName(uncommonCow);
                else if (chance > rareCowChance && chance < specialCowChance) cow.setCustomName(rareCow);
                else if (chance > specialCowChance) cow.setCustomName(specialCow);
                else cow.setCustomName(commonCow);
            }
        }
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Подоите корову!", "Скорее!", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подоите корову!");
    }


    private static void spawnCow(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

        int chance = Utils.getRandom(0, 100);
        Cow cow = (Cow) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.COW);

        if (chance > boomCowChance && chance < uncommonCowChance) cow.setCustomName(boomCow);
        else if (chance < uncommonCowChance) cow.setCustomName(commonCow);
        else if (chance > uncommonCowChance && chance < rareCowChance) cow.setCustomName(uncommonCow);
        else if (chance > rareCowChance && chance < specialCowChance) cow.setCustomName(rareCow);
        else if (chance > specialCowChance) cow.setCustomName(specialCow);
        else cow.setCustomName(commonCow);
    }

    @EventHandler
    public void onPlayerMilksCow(PlayerInteractEntityEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Cow)) return;
        LivingEntity cow = (Cow) event.getRightClicked();
        if (!(Queue.redQueueList.contains(player))) return;

        Location cowLoc = cow.getLocation();
        World world = cow.getWorld();
        int aScore = 0;

        if (player.getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) {
            if (cow.getCustomName().equals(commonCow)) {
                aScore = 1;
                world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 10, 1);
            } else if (cow.getCustomName().equals(uncommonCow)) {
                aScore = 2;
                world.spawnParticle(Particle.DRIP_WATER, cowLoc.add(0, 1.2, 0), 1);
                world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 10, 3);
            } else if (cow.getCustomName().equals(rareCow)) {
                aScore = 3;
                world.spawnParticle(Particle.HEART, cowLoc.add(0, 1.2, 0), 1);
                world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 10, 5);
            } else if (cow.getCustomName().equals(specialCow)) {
                aScore = 8;
                world.spawnParticle(Particle.DRAGON_BREATH, cowLoc.add(0, 1.2, 0), 1);
                world.playSound(cowLoc, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 10, 7);
            } else if (cow.getCustomName().equals(boomCow)) {
                aScore = -5;
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                world.spawnParticle(Particle.EXPLOSION_LARGE, cowLoc.add(0, 0, 0), 1);
                world.playSound(cowLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            }

            RoundSystem.addScore(player, aScore);
            cow.remove();
            spawnCow(player);
        }
    }
}