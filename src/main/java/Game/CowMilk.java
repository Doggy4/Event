package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
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

public class CowMilk implements Listener {
    private static String commonCow = (ChatColor.GRAY + "[✶] Милка");
    private static String uncommonCow = (ChatColor.GREEN + "[✷] Милка");
    private static String rareCow = (ChatColor.BLUE + "[✸] Милка");
    private static String specialCow = (ChatColor.LIGHT_PURPLE + "[✹] Милка");
    private static String boomCow = (ChatColor.RED + "[\uD83D\uDC80] Бум-корова");

    private static int boomCowChance = 50;
    private static int uncommonCowChance = 70;
    private static int rareCowChance = 85;
    private static int specialCowChance = 95;

    public static boolean isActivated = false;

    public static void MilkCow() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Подоите коров!", "Скорее!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подоите коров!");

            player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

            for (int i = 0; i < 10; i++) {
                int chance = Utilities.getRandom(0, 100);
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

    private static void spawnCow(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

        int chance = Utilities.getRandom(0, 100);
        Cow cow = (Cow) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.COW);

        if (chance > boomCowChance && chance < uncommonCowChance) cow.setCustomName(boomCow);
        else if (chance < uncommonCowChance) cow.setCustomName(commonCow);
        else if (chance > uncommonCowChance && chance < rareCowChance) cow.setCustomName(uncommonCow);
        else if (chance > rareCowChance && chance < specialCowChance) cow.setCustomName(rareCow);
        else if (chance > specialCowChance) cow.setCustomName(specialCow);
        else cow.setCustomName(commonCow);
    }

    @EventHandler
    public void onPlayerMilkCow(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Cow) || !isActivated) return;
        LivingEntity cow = (Cow) e.getRightClicked();
        if (cow.getCustomName() == null) return;
        Player player = e.getPlayer();
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) return;

        Location cowLoc = cow.getLocation();
        World world = cow.getWorld();

        int aScore = 0;

        if (cow.getCustomName().equals(commonCow)) {
            world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 1, 1);
            aScore = 1;
        } else if (cow.getCustomName().equals(uncommonCow)) {
            world.spawnParticle(Particle.DRIP_WATER, cowLoc.add(0, 1.2, 0), 10);
            world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 1, 2);
            aScore = 2;
        } else if (cow.getCustomName().equals(rareCow)) {
            world.spawnParticle(Particle.HEART, cowLoc.add(0, 1.2, 0), 1);
            world.playSound(cowLoc, Sound.ENTITY_COW_HURT, 1, 3);
            aScore = 5;
        } else if (cow.getCustomName().equals(specialCow)) {
            world.spawnParticle(Particle.DRAGON_BREATH, cowLoc.add(0, 1.2, 0), 1);
            world.playSound(cowLoc, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 1, 1);
            aScore = 10;
        } else if (cow.getCustomName().equals(boomCow)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
            world.spawnParticle(Particle.EXPLOSION_LARGE, cowLoc.add(0, 0, 0), 1);
            world.playSound(cowLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            aScore = -5;
        }

        RoundSystem.addScore(player, aScore);
        cow.remove();
        spawnCow(player);
    }
}