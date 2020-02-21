package RoundList;

import PluginUtilities.GetMapFromImage;
import PluginUtilities.LocationUtulities;
import PluginUtilities.Utilities;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RoundJumpOfToilet {
    public static boolean isActivated = false;

    public static void setupArena() {
        int random = Utilities.getRandom(1, 1);
        File imgFile = new File(Main.main.getDataFolder().getAbsolutePath() + "/plots/plot" + random + ".png");

        World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

        try {
            BufferedImage img = ImageIO.read(imgFile);
            Material[][] woolMatrix = GetMapFromImage.makeWoolPattern(img);

            Block blockPlaceLoc;

            for (int i = 0; i < img.getHeight(); i++)
                for (int j = 0; j < img.getWidth(); j++) {
                    Location leftUpCorner = new Location(world, LocationUtulities.getSpawnLocation().getX() + 16, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() + 16);
                    blockPlaceLoc = leftUpCorner.add(i, 0, j).getBlock();
                    blockPlaceLoc.setType(woolMatrix[i][j]);
                }

        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }


    }
}

