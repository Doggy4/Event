package PluginUtilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorStandConstructor {
    public static ArmorStand ArmorStandConstructor(Location location, ItemStack head, String name) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);

        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(Utilities.getRandomColor().getColor());
        chestplate.setItemMeta(meta);

        meta = (LeatherArmorMeta) leggings.getItemMeta();
        meta.setColor(Utilities.getRandomColor().getColor());
        leggings.setItemMeta(meta);

        meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(Utilities.getRandomColor().getColor());
        boots.setItemMeta(meta);

        stand.setArms(true);
        stand.setHelmet(head);
        stand.setChestplate(chestplate);
        stand.setLeggings(leggings);
        stand.setBoots(boots);

        stand.setCustomName(name);
        stand.setCustomNameVisible(true);

        return stand;
    }
}
