package RoundList;

import PluginUtils.Chat;
import PluginUtils.ItemUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoundCraftItem implements Listener {
    public static boolean isActivated = false;
    private static HashMap<Player, Material> playerMaterialHashMap = new HashMap<Player, Material>();
    private static ArrayList<Material> craftableMaterials = ItemUtils.craftableMaterials;
    private static ArrayList<Material> materials = ItemUtils.materials;

    public static void craftItem() {
        // Опциально:
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        GameRules.EntityDamageOff();
        MapRebuild.loadSchematic("craft-arena");

        Bukkit.getLogger().info(craftableMaterials.toString());

        int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utils.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);

        playerMaterialHashMap.clear();

        for (Player player : Queue.redQueueList) {

            playerMaterialHashMap.put(player, craftableMaterials.get(Utils.getRandom(0, craftableMaterials.size() - 1)));

            for (Material material : allowedMaterials) player.getInventory().addItem(new ItemStack(material, 1));

            gameRulesAnnouncements(player);

            for (ItemStack material : ItemUtils.getIngredients(playerMaterialHashMap.get(player))) {
                int random = Utils.getRandom(0, 35);
                while (ItemUtils.isContains(ItemUtils.getIngredients(playerMaterialHashMap.get(player)), player.getInventory().getItem(random))) {
                    random = Utils.getRandom(0, 35);
                }
                player.getInventory().setItem(random, material);
            }
        }
    }

    private static void nextItem(Player player) {
        int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utils.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);

        playerMaterialHashMap.clear();

        Material randomMaterial = craftableMaterials.get(Utils.getRandom(0, craftableMaterials.size() - 1));

        player.getInventory().clear();

        playerMaterialHashMap.put(player, randomMaterial);

        for (Material material : allowedMaterials) player.getInventory().addItem(new ItemStack(material, 1));

        gameRulesAnnouncements(player);

        for (ItemStack material : ItemUtils.getIngredients(playerMaterialHashMap.get(player))) {
            int random = Utils.getRandom(0, 35);
            while (ItemUtils.isContains(ItemUtils.getIngredients(playerMaterialHashMap.get(player)), player.getInventory().getItem(random))) {
                random = Utils.getRandom(0, 35);
            }
            player.getInventory().setItem(random, material);
        }
    }

    private static void gameRulesAnnouncements(Player player) {
        player.sendTitle(ChatColor.GREEN + "Скрафтите предмет", Chat.translate(playerMaterialHashMap.get(player).toString()), 20, 20, 20);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Скрафтите предмет [" + ChatColor.WHITE + Chat.translate(playerMaterialHashMap.get(player).toString()) + ChatColor.GREEN + "]!");
    }


    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        if (!isActivated) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!Queue.redQueueList.contains(player)) return;

        if (event.getCurrentItem().getType().equals(playerMaterialHashMap.get(player))) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            RoundSystem.addScore(player, 5);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный предмет!");
            RoundSystem.addScore(player, -1);
        }
        nextItem(player);
    }
}
