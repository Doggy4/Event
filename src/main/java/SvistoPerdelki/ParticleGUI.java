package SvistoPerdelki;

import PluginUtilities.ItemStackConstructor;
import PluginUtilities.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static PluginUtilities.Items.ParticleSample;

public class ParticleGUI implements Listener {

    private static Inventory particleTypes;
    private static Inventory particles;
    private static Inventory auras;
    private static Inventory trails;
    private static Inventory bursts;
    private static Inventory shoots;

    private static ItemStack particleTypesParticles = new ItemStackConstructor(Material.FIREWORK_STAR).amount(1).displayName(ChatColor.RED + "Партиклы").lore("Одиночные эффекты без циклов").build();
    private static ItemStack particleTypesAuras = new ItemStackConstructor(Material.FIREWORK_STAR).amount(1).displayName(ChatColor.RED + "Ауры").lore("Эффекты в виде ауры").build();
    private static ItemStack particleTypesTrails = new ItemStackConstructor(Material.FIREWORK_STAR).amount(1).displayName(ChatColor.RED + "Следы").lore("Эффекты следов под ногами").build();
    private static ItemStack particleTypesBursts = new ItemStackConstructor(Material.FIREWORK_STAR).amount(1).displayName(ChatColor.RED + "Выбросы").lore("Резкий выброс партиклов").build();
    private static ItemStack particleTypesShoots = new ItemStackConstructor(Material.FIREWORK_STAR).amount(1).displayName(ChatColor.RED + "Выстрелы").lore("Партиклы по направлению взгяда").build();


    public static void openParticleTypes(Player player) {
        int size = 9;
        if (particleTypes == null) {
            particleTypes = Bukkit.createInventory(null, size, ChatColor.GREEN + "Типы эффектов");
            particleTypes.setItem(0, particleTypesParticles);
            particleTypes.setItem(1, particleTypesAuras);
            particleTypes.setItem(2, particleTypesTrails);
            particleTypes.setItem(3, particleTypesBursts);
            particleTypes.setItem(4, particleTypesShoots);
        }
        player.openInventory(particleTypes);
    }

    public static void openParticles(Player player) {
        int size = 27;
        if (particles == null) {
            particles = Bukkit.createInventory(null, size, ChatColor.GREEN + "Партиклы");
            for (int i = 0; i < size; i++) particles.setItem(i, ParticleSample);
        }
        player.openInventory(particles);
    }

    public static void openAuras(Player player) {
        int size = 27;
        if (auras == null) {
            auras = Bukkit.createInventory(null, size, ChatColor.GREEN + "Ауры");
            for (int i = 0; i < size; i++) auras.setItem(i, ParticleSample);
        }
        player.openInventory(auras);
    }

    public static void openTarils(Player player) {
        int size = 27;
        if (trails == null) {
            trails = Bukkit.createInventory(null, size, ChatColor.GREEN + "Следы");
            for (int i = 0; i < size; i++) trails.setItem(i, ParticleSample);
        }
        player.openInventory(trails);
    }

    public static void openBursts(Player player) {
        int size = 27;
        if (bursts == null) {
            bursts = Bukkit.createInventory(null, size, ChatColor.GREEN + "Выбросы");
            for (int i = 0; i < size; i++) bursts.setItem(i, ParticleSample);
        }
        player.openInventory(bursts);
    }

    public static void openShoots(Player player) {
        int size = 27;
        if (shoots == null) {
            shoots = Bukkit.createInventory(null, size, ChatColor.GREEN + "Выстрелы");
            for (int i = 0; i < size; i++) shoots.setItem(i, ParticleSample);
        }
        player.openInventory(shoots);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Items.particleSelect.getItemMeta().getDisplayName())) {
            openParticleTypes(player);
        }
    }

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(ChatColor.GREEN + "Типы эффектов")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(particleTypesParticles.getItemMeta().getDisplayName()))
                openParticles(player);
            else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(particleTypesAuras.getItemMeta().getDisplayName()))
                openAuras(player);
            else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(particleTypesTrails.getItemMeta().getDisplayName()))
                openTarils(player);
            else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(particleTypesBursts.getItemMeta().getDisplayName()))
                openBursts(player);
            else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(particleTypesShoots.getItemMeta().getDisplayName()))
                openShoots(player);
        }
    }
}
