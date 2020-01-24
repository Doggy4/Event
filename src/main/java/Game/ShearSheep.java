package Game;

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
            player.sendTitle(ChatColor.GREEN + "Подстригите овцу", randomColor.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подстригите овцу " + ChatColor.LIGHT_PURPLE + "[" + randomColor.name() + "]");
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().addItem(Items.ShearEventShears);
        }

        for (int i = 0; i < 80; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            sheep.setColor(Utilities.getRandomColor());
            sheeps.add(sheep);
        }
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerShearSheep(PlayerShearEntityEvent event) {
        Player winner = event.getPlayer();
        Sheep sheep = (Sheep) event.getEntity();
        if (!isShearSheepActivated)
            return;
        if (sheep.getColor() == randomColor) {
            aGameCycle.addScore(winner, place);
            place++;
            winner.setGameMode(GameMode.ADVENTURE);
            winner.getInventory().clear();
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
