package PluginUtilities;

import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import static QueueSystem.Queue.*;

public class InventoryConstructor implements Listener {
    public static Inventory queueInventory() {
        Inventory queueInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Выбор очереди");

        for (int i = 0; i < queueInventory.getSize(); i++)
            queueInventory.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1));

        queueInventory.setItem(19, Items.redQueue());
        queueInventory.setItem(22, Items.yellowQueue());
        queueInventory.setItem(25, Items.greenQueue());

        queueInventory.setItem(28, Items.redQueuePlayers);
        queueInventory.setItem(31, Items.yellowQueuePlayers);
        queueInventory.setItem(34, Items.greenQueuePlayers);

        queueInventory.setItem(53, Items.leaveQueue);

        return queueInventory;

    }

    public static Inventory redWaiters() {
        Inventory redWaiters = Bukkit.createInventory(null, 18, ChatColor.RED + "Список ожидающих RED");

        int i = 0;
        for (String name : Queue.redQueueList) {

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwningPlayer(Bukkit.getPlayer(name));
            meta.setDisplayName(ChatColor.RED + "#" + (i + 1) + " " + ChatColor.LIGHT_PURPLE + name);
            skull.setItemMeta(meta);

            if (i >= 5)
                redWaiters.setItem(i + 6, skull);
            else
                redWaiters.setItem(i + 2, skull);
            i++;
        }

        return redWaiters;

    }

    public static Inventory yellowWaiters() {
        Inventory yellowWaiters = Bukkit.createInventory(null, 18, ChatColor.YELLOW + "Список ожидающих YELLOW");

        int i = 0;
        for (String name : Queue.yellowQueueList) {

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwningPlayer(Bukkit.getPlayer(name));
            meta.setDisplayName(ChatColor.YELLOW + "#" + (i + 1) + " " + ChatColor.LIGHT_PURPLE + name);
            skull.setItemMeta(meta);

            if (i >= 5)
                yellowWaiters.setItem(i + 6, skull);
            else
                yellowWaiters.setItem(i + 2, skull);
            i++;
        }

        return yellowWaiters;
    }

    public static Inventory greenWaiters() {
        Inventory greenWaiters = Bukkit.createInventory(null, 18, ChatColor.GREEN + "Список ожидающих GREEN");

        int i = 0;
        for (String name : greenQueueList) {

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwningPlayer(Bukkit.getPlayer(name));
            meta.setDisplayName(ChatColor.GREEN + "#" + (i + 1) + " " + ChatColor.LIGHT_PURPLE + name);
            skull.setItemMeta(meta);

            greenWaiters.setItem(i, skull);
            i++;
        }

        return greenWaiters;
    }

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        if (event.getCurrentItem() == null)
            return;

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals(ChatColor.GREEN + "Выбор очереди")) {
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.greenQueue().getItemMeta().getDisplayName()))
                AddToGreenQueue(player);
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.yellowQueue().getItemMeta().getDisplayName()))
                AddToYellowQueue(player);
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.redQueue().getItemMeta().getDisplayName()))
                AddToRedQueue(player);
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.leaveQueue.getItemMeta().getDisplayName()))
                QueueSystem.Queue.LeaveQueueBroadcaster(player);
            player.closeInventory();
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.redQueuePlayers.getItemMeta().getDisplayName()))
                player.openInventory(InventoryConstructor.redWaiters());
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.yellowQueuePlayers.getItemMeta().getDisplayName()))
                player.openInventory(InventoryConstructor.yellowWaiters());
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Items.greenQueuePlayers.getItemMeta().getDisplayName()))
                player.openInventory(greenWaiters());

        } else if (event.getView().getTitle().equals(ChatColor.RED + "Список ожидающих RED") || event.getView().getTitle().equals(ChatColor.YELLOW + "Список ожидающих YELLOW") || event.getView().getTitle().equals(ChatColor.GREEN + "Список ожидающих GREEN"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String displayName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();

        if (displayName.equals(Items.joinQueue.getItemMeta().getDisplayName()) || displayName.equals(Items.spectatorMode.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы не можете это сделать!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }
}

