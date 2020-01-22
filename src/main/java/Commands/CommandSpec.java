package Commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpec implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[EVENT] Only players can use this command!");
            return false;
        }
        Player player = (Player) sender;

        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы вышли из режима наблюдателя!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1);
            player.setGameMode(GameMode.ADVENTURE);
            CommandEvent.teleportToLobby(player);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы не находитесь в режиме наблюдателя!");
        }
        return true;
    }
}
