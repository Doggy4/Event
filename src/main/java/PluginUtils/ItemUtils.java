package PluginUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static ArrayList<Material> materials = new ArrayList<Material>();
    public static ArrayList<Material> craftableMaterials = new ArrayList<Material>();

    static {
        for (Material material : Material.values())
            if (!BlackList.isItemBlocked(material.name())) ItemUtils.materials.add(material);
    }

    static {
        for (Material material : Material.values())
            if (isCraftable(material)) ItemUtils.craftableMaterials.add(material);
    }

    private static boolean isCraftable(Material mat) {
        if (Bukkit.getServer().getRecipesFor(new ItemStack(mat)).size() < 1) return false;
        Recipe recipe = Bukkit.getRecipesFor(new ItemStack(mat)).get(0);
        return (recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe);
    }

    public static List<ItemStack> getIngredients(Material mat) {
        Recipe recipe = Bukkit.getRecipesFor(new ItemStack(mat)).get(0);
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            return new ArrayList<ItemStack>(shapedRecipe.getIngredientMap().values());
        }
        if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
            return new ArrayList<ItemStack>(shapelessRecipe.getIngredientList());
        }
        return null;
    }

    public static boolean isContains(List<ItemStack> items, ItemStack itemStack) {
        for (ItemStack item : items) {
            if (item == null) return false;
            if (itemStack == null) return false;
            if (item.getType().equals(itemStack.getType())) return true;
        }
        return false;
    }


}
