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

public class ShearSheep implements Listener {
    private static boolean isShearSheepActivated = false;

    private static DyeColor randomColor;
    private static ArrayList<Sheep> sheeps = new ArrayList<Sheep>();


    public static void ShearSheep() {

        isShearSheepActivated = aGameCycle.isAnyBattleEnabled;

        randomColor = Utilities.getRandomColor();

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
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
            sheeps.add(sheep);
        }

        for (int i = 0; i < 3; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            sheep.setColor(randomColor);
            sheep.setCustomName(Chat.colors.get(randomColor.toString()) + randomColor.toString());
            sheeps.add(sheep);
        }
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerShearSheep(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Sheep sheep = (Sheep) event.getEntity();
        if (!isShearSheepActivated)
            return;
        if (sheep.getColor() == randomColor) {
            aGameCycle.playerWin(player, place);
            place++;
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
        } else {
            aGameCycle.playerLose(player);
        }
        if (place > 3) {
            isShearSheepActivated = false;
            aGameCycle.isAnyBattleEnabled = false;
            place = 1;

            for (Sheep sheepFromList : sheeps) {
                sheepFromList.remove();
            }
            sheeps.clear();
        }
    }
}
