package Game;

import PluginUtilities.LocationUtulities;
import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class RoundHideUnderBlocks implements Listener {
    protected static boolean isActivated = false;

    public static void hideUnderBlocks() {
        isActivated = true;
        aRoundSystem.roundSeconds = 60;
        MapRebuild.loadSchematic("arena");
        GameRules.EntityDamageOff();

        World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

        for (int i = 0; i < 5; i++) {
            int randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 32) - 16;
            int randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 32) - 16;
            int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y")) + 4;

            world.getBlockAt(randX, y, randZ).setType(Material.DIORITE_SLAB);
        }

        Location loc1 = new Location(world, LocationUtulities.getSpawnLocation().getX() + 16, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() + 16);
        Location loc2 = new Location(world, LocationUtulities.getSpawnLocation().getX() - 16, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() - 16);


        for (Player player : Queue.redQueueList) {
            player.sendTitle(ChatColor.GREEN + "Спрячьтесь от", "снежков", 20, 20, 20);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спрячьтесь от снежков!");


        }

        for (Block block : LocationUtulities.getblocksFromTwoPoints(loc2, loc1)) {
            Snowball snowball = world.spawn(block.getLocation(), Snowball.class);
            Vector vector = new Vector();
            snowball.setVelocity(vector);
        }
    }

    public void onSnowball(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
            aRoundSystem.playerLose(player);
    }
}
