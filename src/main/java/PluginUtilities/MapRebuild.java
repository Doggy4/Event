package PluginUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.function.operation.Operation;

import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MapRebuild {

    // Не знаю как, но это работает
    // Ребилд карты под схематик

    public static void loadSchematic(String scematic) {

        File myfile = new File(Main.main.getDataFolder().getAbsolutePath() + "/" + scematic + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(myfile);

        try {

            ClipboardReader reader = format.getReader(new FileInputStream(myfile));
            Clipboard clipboard = reader.read();

            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")));
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1);

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(Main.main.getConfig().getDouble("spawn.x"), Main.main.getConfig().getDouble("spawn.y"), Main.main.getConfig().getDouble("spawn.z"))).ignoreAirBlocks(false).build();

            try {
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}