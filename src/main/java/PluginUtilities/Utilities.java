package PluginUtilities;

import org.bukkit.DyeColor;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    public static int getRandom(int min, int max) {
        Random random = new Random();
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }

    public static DyeColor getRandomColor() {
        ArrayList<DyeColor> colors = new ArrayList<DyeColor>();

        for (DyeColor color : DyeColor.values())
            colors.add(color);

        int randomColorIndex = Utilities.getRandom(0, colors.size() - 1);

        return colors.get(randomColorIndex);
    }
}
