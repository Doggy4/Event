package Game;

import PluginUtilities.Chat;
import PluginUtilities.Items;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.ArrayList;
// Доделать
public class ShearSheep implements Listener {
    private static boolean isShearSheepActivated = false;
    private static DyeColor randomColor;
    private static int score = 10;

    public static void ShearSheep() {
        isShearSheepActivated = true;

        randomColor = Utilities.getRandomColor();

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Подстригите овцу", Chat.colors.get(randomColor.toString()) + randomColor.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подстригите овцу " + ChatColor.LIGHT_PURPLE + "[" + Chat.colors.get(randomColor.toString()) + randomColor.name() + ChatColor.LIGHT_PURPLE + "]");
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().addItem(Items.ShearEventShears);
        }

        for (int i = 0; i < 50; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            DyeColor color = Utilities.getRandomColor();
            sheep.setColor(color);
            sheep.setCustomName(Chat.colors.get(color.toString()) + color.toString());
        }

        for (int i = 0; i < 3; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            sheep.setColor(randomColor);
            sheep.setCustomName(Chat.colors.get(randomColor.toString()) + randomColor.toString());
        }
    }

    @EventHandler
    public void onPlayerShearSheep(PlayerShearEntityEvent event) {
        if (!isShearSheepActivated) return;

        Player player = event.getPlayer();
        Sheep sheep = (Sheep) event.getEntity();

        if (sheep.getColor() == randomColor) {
            RoundSystem.addScore(player, score);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            score--;
        } else {
            RoundSystem.playerLose(player);
        }
        if (RoundSystem.roundSeconds <= 0) {
            isShearSheepActivated = false;
           RoundSystem.endRound();
        }
    }
}
