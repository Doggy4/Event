package PluginUtilities;

import java.util.Random;

public class Utilities {

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        int randomNumber = random.nextInt((upper - lower) + 1 + lower);

        return randomNumber;
    }

}
