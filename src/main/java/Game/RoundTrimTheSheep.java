package Game;

import PluginUtilities.Chat;
import PluginUtilities.Items;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class RoundTrimTheSheep implements Listener {
    protected static boolean isActivated = false;

    private static DyeColor randomColor;

    protected static void trimTheSheep() {
        aRoundSystem.roundSeconds = 30;

        isActivated = true;

        randomColor = Utilities.getRandomColor();

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.getInventory().addItem(Items.shearEventShears);
        }

        for (int i = 0; i < 50; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            DyeColor color = Utilities.getRandomColor();
            sheep.setColor(color);
            sheep.setCustomName(Chat.colors.get(color.toString()) + Chat.translate(color.name()));
        }
    }

    private static void spawnSheep(Player player) {
        Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
        DyeColor color = Utilities.getRandomColor();
        sheep.setColor(color);
        sheep.setCustomName(Chat.colors.get(color.toString()) + Chat.translate(color.name()));
    }

    private static void nextSheep(Player player) {
        randomColor = Utilities.getRandomColor();
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

        if (sheep.getColor() == randomColor) {
            aRoundSystem.addScore(player, 1);
        } else {
            aRoundSystem.addScore(player, -1);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неправильный цвет!");
        }

        nextSheep(player);
        sheep.remove();
        spawnSheep(player);
    }
}
