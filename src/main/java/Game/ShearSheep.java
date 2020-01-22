package Game;

import PluginUtilities.Items;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;

public class ShearSheep implements Listener {
    private static boolean isShearSheepActivated = false;
    private static ArrayList<DyeColor> colors = new ArrayList<DyeColor>();
    private static DyeColor randomColor;
    private static ArrayList<Sheep> sheeps = new ArrayList<Sheep>();

    static {
        for (DyeColor color : DyeColor.values())
            colors.add(color);
    }

    public static void ShearSheep() {

        isShearSheepActivated = GameCycle.isAnyBattleEnabled;
        int randomColorIndex = Utilities.getRandom(0, colors.size() - 1);
        randomColor = colors.get(randomColorIndex);

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Подстригите овцу", randomColor.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Подстригите овцу " + ChatColor.LIGHT_PURPLE + "[" + randomColor.name() + "]");
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().addItem(Items.ShearEventShears);
        }

        for (int i = 0; i < 50; i++) {
            Sheep sheep = (Sheep) Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).spawnEntity(Commands.CommandEvent.randLocationSpawn(), EntityType.SHEEP);
            sheep.setColor(colors.get(Utilities.getRandom(0, colors.size() - 1)));
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
            GameCycle.addScore(winner, place);
            place++;
            winner.setGameMode(GameMode.ADVENTURE);
            winner.getInventory().clear();
        }
        if (place > 3) {
            isShearSheepActivated = false;
            GameCycle.isAnyBattleEnabled = false;
            place = 1;

            for (Sheep sheepFromList : sheeps) {
                sheepFromList.remove();
            }
            sheeps.clear();
        }
    }
}
