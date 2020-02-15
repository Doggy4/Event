package ImageMaps;

import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ImageMaps implements Listener {

    public static Map<UUID, PlacingCacheEntry> placing = new HashMap<>();

    public static Map<Integer, ImageMap> maps = new HashMap<>();

    public static Map<String, BufferedImage> images = new HashMap<>();

    public static List<Integer> sendList = new ArrayList<>();

    public static FastSendTask sendTask;


    public static List<Integer> getFastSendList() {
        return sendList;
    }

    public static void saveMaps() {
        File file = new File(Main.main.getDataFolder(), "maps.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        for (String key : yamlConfiguration.getKeys(false))
            yamlConfiguration.set(key, null);
        for (Map.Entry<Integer, ImageMap> e : maps.entrySet()) {
            yamlConfiguration.set((new StringBuilder()).append(e.getKey()).append(".image").toString(), e.getValue().getImage());
            yamlConfiguration.set((new StringBuilder()).append(e.getKey()).append(".x").toString(), Integer.valueOf(e.getValue().getX()));
            yamlConfiguration.set((new StringBuilder()).append(e.getKey()).append(".y").toString(), Integer.valueOf(e.getValue().getY()));
            yamlConfiguration.set((new StringBuilder()).append(e.getKey()).append(".fastsend").toString(), Boolean.valueOf(e.getValue().isFastSend()));
            yamlConfiguration.set((new StringBuilder()).append(e.getKey()).append(".scale").toString(), Double.valueOf(e.getValue().getScale()));
        }
        try {
            yamlConfiguration.save(file);
        } catch (IOException e1) {
            Main.main.getLogger().log(Level.SEVERE, "Failed to save maps.yml!", e1);
        }
    }

    public static void startPlacing(Player p, String image, boolean fastsend, double scale) {
        placing.put(p.getUniqueId(), new PlacingCacheEntry(image, fastsend, scale));
    }

    public static boolean placeImage(Block block, BlockFace face, PlacingCacheEntry cache) {
        int xMod = 0;
        int zMod = 0;
        switch (face) {
            case EAST:
                zMod = -1;
                break;
            case WEST:
                zMod = 1;
                break;
            case SOUTH:
                xMod = 1;
                break;
            case NORTH:
                xMod = -1;
                break;
            default:
                return false;
        }
        BufferedImage image = loadImage(cache.getImage());
        if (image == null) {
            return false;
        }
        Block b = block.getRelative(face);
        int width = (int) Math.ceil(image.getWidth() / 128.0D * cache.getScale() - 1.0E-4D);
        int height = (int) Math.ceil(image.getHeight() / 128.0D * cache.getScale() - 1.0E-4D);
        int x;
        for (x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!block.getRelative(x * xMod, -y, x * zMod).getType().isSolid())
                    return false;
                if (block.getRelative(x * xMod - zMod, -y, x * zMod + xMod).getType().isSolid())
                    return false;
            }
        }
        try {
            for (x = 0; x < width; x++) {
                for (int y = 0; y < height; y++)
                    setItemFrame(b.getRelative(x * xMod, -y, x * zMod), image, face, x * 128, y * 128, cache);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public static void setItemFrame(Block bb, BufferedImage image, BlockFace face, int x, int y, PlacingCacheEntry cache) {
        ItemFrame i = null;
        i = bb.getWorld().spawn(bb.getLocation(), ItemFrame.class);
        i.setFacingDirection(face, false);
        ItemStack item = getMapItem(cache.getImage(), x, y, image, cache.getScale());
        i.setItem(item);
        int id = ((MapMeta) item.getItemMeta()).getMapId();
        if (cache.isFastSend() && !sendList.contains(Integer.valueOf(id))) {
            sendList.add(Integer.valueOf(id));
            sendTask.addToQueue(id);
        }
        maps.put(Integer.valueOf(id), new ImageMap(cache.getImage(), x, y, sendList.contains(Integer.valueOf(id)), cache.getScale()));
    }

    public static ItemStack getMapItem(String file, int x, int y, BufferedImage image, double scale) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);

        for (Map.Entry<Integer, ImageMap> entry : maps.entrySet()) {
            if (entry.getValue().isSimilar(file, x, y, scale)) {
                MapMeta mapMeta = (MapMeta) item.getItemMeta();

                mapMeta.setMapId(entry.getKey().intValue());
                item.setItemMeta(mapMeta);
                return item;
            }
        }

        MapView map = Main.main.getServer().createMap(Main.main.getServer().getWorlds().get(0));
        for (MapRenderer r : map.getRenderers())
            map.removeRenderer(r);

        map.addRenderer(new ImageMapRenderer(image, x, y, scale));

        if (item.getItemMeta() instanceof MapMeta) {
            MapMeta meta = (MapMeta) item.getItemMeta();
            meta.setMapId(map.getId());
            item.setItemMeta(meta);
        }

        return item;
    }

    public static BufferedImage loadImage(String file) {
        if (images.containsKey(file))
            return images.get(file);

        File f = new File(Main.main.getDataFolder().getAbsolutePath() + "/images/" + file);
        BufferedImage image = null;

        try {
            image = ImageIO.read(f);
            images.put(file, image);
        } catch (IOException e) {
            Main.main.getLogger().log(Level.SEVERE, "Error! Image: " + f.getName());
        }

        return image;
    }

    public static void loadMaps() {
        File file = new File(Main.main.getDataFolder(), "maps.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        Set<String> warnedFilenames = new HashSet<>();
        for (String key : yamlConfiguration.getKeys(false)) {
            int id = Integer.parseInt(key);
            MapView map = Main.main.getServer().getMap(id);
            if (map == null)
                continue;
            for (MapRenderer r : map.getRenderers())
                map.removeRenderer(r);
            String image = yamlConfiguration.getString(key + ".image");
            int x = yamlConfiguration.getInt(key + ".x");
            int y = yamlConfiguration.getInt(key + ".y");
            boolean fastsend = yamlConfiguration.getBoolean(key + ".fastsend", false);
            double scale = yamlConfiguration.getDouble(key + ".scale", 1.0D);
            BufferedImage bimage = loadImage(image);
            if (bimage == null) {
                warnedFilenames.add(image);
                continue;
            }
            if (fastsend)
                sendList.add(Integer.valueOf(id));
            map.addRenderer(new ImageMapRenderer(loadImage(image), x, y, scale));
            maps.put(Integer.valueOf(id), new ImageMap(image, x, y, fastsend, scale));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        if (!placing.containsKey(e.getPlayer().getUniqueId()))
            return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            e.getPlayer().sendMessage(ChatColor.RED + "Отменено!");
            placing.remove(e.getPlayer().getUniqueId());
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (!ImageMaps.placeImage(e.getClickedBlock(), e.getBlockFace(), placing.get(e.getPlayer().getUniqueId()))) {
            e.getPlayer().sendMessage(ChatColor.RED + "Ошибка. Недостаточно большая площадь.");
        } else {
            ImageMaps.saveMaps();
        }
        e.setCancelled(true);
        placing.remove(e.getPlayer().getUniqueId());
    }
}
