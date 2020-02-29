package RoundList;

import PluginUtils.Chat;
import PluginUtils.Items;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.HashMap;

public class RoundTrimTheSheep implements Listener {
    public static boolean isActivated = false;

    private static DyeColor randomColor;
    private static HashMap<Player, DyeColor> univPlayerColorHashMap = new HashMap<Player, DyeColor>();

    public static void startRound() {
        univPlayerColorHashMap.clear();

        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        randomColor = Utils.getRandomColor();

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            univPlayerColorHashMap.put(player, randomColor);
            player.getInventory().addItem(Items.shearEventShears);
        }

        for (int i = 0; i < 50; i++) {
            Sheep sheep = (Sheep) LocationUtils.world.spawnEntity(LocationUtils.getRandomLocation().add(0, 1, 0), EntityType.SHEEP);
            DyeColor color = Utils.getRandomColor();
            sheep.setColor(color);
            sheep.setCustomName(Chat.colors.get(color.toString()) + Chat.translate(color.name()));
        }
    }

    private static void spawnSheep(Player player) {
        Sheep sheep = (Sheep) LocationUtils.world.spawnEntity(LocationUtils.getRandomLocation().add(0, 1, 0), EntityType.SHEEP);
        DyeColor color = Utils.getRandomColor();
        sheep.setColor(color);
        sheep.setCustomName(Chat.colors.get(color.toString()) + Chat.translate(color.name()));
    }

    private static void nextSheep(Player player) {
        randomColor = Utils.getRandomColor();
        univPlayerColorHashMap.put(player, randomColor);
        gameRulesAnnouncement(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Подстригите овцу", Chat.colors.get(randomColor.toString()) + Chat.translate(randomColor.name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подстригите овцу " + ChatColor.LIGHT_PURPLE + "[" + Chat.colors.get(randomColor.toString()) + Chat.translate(randomColor.name()) + ChatColor.LIGHT_PURPLE + "]");
    }

    @EventHandler
    public void onPlayerTrimsSheep(PlayerShearEntityEvent event) {
        if (!isActivated) return;

        Player player = event.getPlayer();
        Sheep sheep = (Sheep) event.getEntity();

        if (sheep.getColor() == univPlayerColorHashMap.get(player)) {
            RoundSystem.addScore(player, 1);
            LocationUtils.world.spawnParticle(Particle.VILLAGER_HAPPY, sheep.getLocation(), 1);
        } else {
            RoundSystem.addScore(player, -1);
            LocationUtils.world.spawnParticle(Particle.VILLAGER_ANGRY, sheep.getLocation(), 0);
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неправильный цвет!");
        }

        nextSheep(player);
        sheep.remove();
        spawnSheep(player);
    }
}
