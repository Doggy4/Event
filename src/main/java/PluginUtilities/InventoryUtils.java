package PluginUtilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static void removeItem(Player p, Material mat, int removeAmount) {
        for (int i = 0; i < p.getInventory().getContents().length; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;
            if (item.getType() != mat) continue;
            int itemAmount = item.getAmount();
            if (itemAmount == 1) {
                p.getInventory().remove(item);
                return;
            }
            item.setAmount(itemAmount - removeAmount);
            return;
        }
    }
}
