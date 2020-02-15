package ImageMaps;

import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public class ImageMapCommands {
    private Main plugin;

    public static void imageMapCommand(Player player, String[] args) {

        double finalScale = 1.0D;
        boolean fastsend = false;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "/event image [файл]");
            return;
        }

        String filename = args[1];
        BufferedImage image = ImageMaps.loadImage(args[1]);

        if (image == null) {
            player.sendMessage(ChatColor.RED + "Файл не существует!");
            return;
        }

        ImageMaps.startPlacing(player, args[1], fastsend, finalScale);

        int width = (int) Math.ceil(image.getWidth() / 128.0D * finalScale - 1.0E-4D);
        int height = (int) Math.ceil(image.getHeight() / 128.0D * finalScale - 1.0E-4D);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
        player.sendMessage(ChatColor.GREEN + String.format("Установка %s, зона: %dx%d.", new Object[]{args[1], Integer.valueOf(width), Integer.valueOf(height)}));
        player.sendMessage(ChatColor.GREEN + "Нажмите ПКМ по левому верхнему углу зоны.");

    }
}
