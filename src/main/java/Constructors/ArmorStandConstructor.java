package Constructors;

import PluginUtils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ArmorStandConstructor {
    public static ArmorStand ArmorStandConstructor(Location location, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();

        skullMeta.setOwningPlayer(Bukkit.getPlayer(name));
        head.setItemMeta(skullMeta);

        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);

        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(Utils.getRandomColor().getColor());
        chestplate.setItemMeta(meta);

        meta = (LeatherArmorMeta) leggings.getItemMeta();
        meta.setColor(Utils.getRandomColor().getColor());
        leggings.setItemMeta(meta);

        meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(Utils.getRandomColor().getColor());
        boots.setItemMeta(meta);

        stand.setArms(true);
        stand.setHelmet(head);
        stand.setChestplate(chestplate);
        stand.setLeggings(leggings);
        stand.setBoots(boots);

        stand.setCustomName(ChatColor.GREEN + name);
        stand.setCustomNameVisible(true);

        return stand;
    }
}
