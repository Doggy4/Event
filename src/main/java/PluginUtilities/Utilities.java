package PluginUtilities;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    public static int getRandom(int min, int max) {
        Random random = new Random();
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
}
