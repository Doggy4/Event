package ImageMaps;

import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public class ImageMapCommands implements CommandExecutor {
    private Main plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        double finalScale = 1.0D;
        boolean fastsend = false;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "/img [файл.png]");
            return true;
        }

        String filename = args[0];
        BufferedImage image = ImageMaps.loadImage(args[0]);

        if (image == null) {
            player.sendMessage(ChatColor.RED + "Файл не существует!");
            return true;
        }

        ImageMaps.startPlacing(player, args[0], fastsend, finalScale);

        int width = (int) Math.ceil(image.getWidth() / 128.0D * finalScale - 1.0E-4D);
        int height = (int) Math.ceil(image.getHeight() / 128.0D * finalScale - 1.0E-4D);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
        player.sendMessage(ChatColor.GREEN + String.format("Установка %s, зона: %dx%d.", new Object[]{args[0], Integer.valueOf(width), Integer.valueOf(height)}));
        player.sendMessage(ChatColor.GREEN + "Нажмите ПКМ по левому верхнему углу зоны.");

        return true;
    }
}
