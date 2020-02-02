package PluginUtilities;

import java.util.ArrayList;

public class BlackList {

    private static ArrayList<String> blacklist = new ArrayList<String>();

    static {
        blacklist.add("POTATO");
        blacklist.add("BLUET");
        blacklist.add("LECTERN");
        blacklist.add("BEETROOTS");
        blacklist.add("CARROTS");
        blacklist.add("SEEDS");
        blacklist.add("STEM");
        blacklist.add("AIR");
        blacklist.add("BAMBOO");
        blacklist.add("STAND");
        blacklist.add("COMMAND");
        blacklist.add("BARRIER");
        blacklist.add("PORTAL");
        blacklist.add("MUSIC");
        blacklist.add("BANNER");
        blacklist.add("HEAD");
        blacklist.add("WALL");
    }

    public static boolean isItemBlocked(String item) {
        for (int i = 0; i < blacklist.size() - 1; i++)
            if (item.contains(blacklist.get(i)))
                return true;
        return false;
    }
}
