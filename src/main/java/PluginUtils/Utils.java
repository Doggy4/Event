package PluginUtils;

import org.bukkit.DyeColor;

import java.util.ArrayList;
import java.util.Random;


public class Utils {

    public static int getRandom(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static DyeColor getRandomColor() {
        ArrayList<DyeColor> colors = new ArrayList<DyeColor>();

        for (DyeColor color : DyeColor.values())
            colors.add(color);

        int randomColorIndex = Utils.getRandom(0, colors.size() - 1);

        return colors.get(randomColorIndex);
    }
}
