package Game;

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

    private static int random = Utilities.getRandom(1,1);
    private static File imgFile = new File("areaPattern" + random + ".png");

    private void setupArena() throws IOException {
        World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));
        BufferedImage img = ImageIO.read(imgFile);
        Material[][] woolMatrix = GetMapFromImage.makeWoolPattern(img);
        Block blockPlaceLoc;

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Location leftUpCorner = new Location(world, LocationUtulities.getSpawnLocation().getX() + 16, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() + 16);
                blockPlaceLoc = leftUpCorner.add((double) i,0, (double) j).getBlock();
                blockPlaceLoc.setType(woolMatrix[i][j]);
            }
        }
    }
}

