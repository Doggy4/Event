package event.main;

import Constructors.InventoryConstructor;
import PluginUtils.DiscordWebHooks;
import PluginUtils.Items;
import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainPlayerHandler implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        player.sendTitle(ChatColor.GREEN + "Привет, " + ChatColor.YELLOW + player.getName(), ChatColor.AQUA + "Добро пожаловать!", 50, 50, 50);
        player.playSound(location, Sound.ENTITY_CAT_PURREOW, 10, 1);

        event.setJoinMessage(ChatColor.GOLD + "[EVENT]" + ChatColor.WHITE + " Игрок " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " зашел на " + ChatColor.GOLD + "EVENT" + ChatColor.WHITE + "!");

        player.getInventory().setItem(0, Items.joinQueue);
        player.getInventory().addItem(Items.particleSelect);
        player.getInventory().setItem(8, Items.spectatorMode);

        DiscordWebHooks.playerJoin(player.getName());
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(ChatColor.GOLD + "[EVENT]" + ChatColor.WHITE + " Игрок " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " вышел с " + ChatColor.GOLD + "EVENT" + ChatColor.WHITE + "!");
        Queue.LeaveQueue(player);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getItemMeta() == null)
            return;

        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Items.joinQueue.getItemMeta().getDisplayName())) {
            player.openInventory(InventoryConstructor.queueInventory());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 10, 1);
        } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Items.spectatorMode.getItemMeta().getDisplayName())) {
            if (player.getGameMode() == GameMode.SPECTATOR){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1);
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы вышли из режима наблюдателя!");
                LocationUtils.teleportToLobby(player);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1);
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы вошли в режим наблюдателя! Выйти из режима: " + ChatColor.BLUE + "ЛКМ");
                LocationUtils.teleportToLobby(player);
            }
        }
    }
}
