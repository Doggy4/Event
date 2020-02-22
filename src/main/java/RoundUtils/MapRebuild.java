package RoundUtils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import event.main.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MapRebuild {

    // Не знаю как, но это работает
    // Ребилд карты под схематик

    public static void loadSchematic(String schematic) {

        File myFile = new File(Main.main.getDataFolder().getAbsolutePath() + "/schematics/" + schematic + ".schem");

        if (schematic.endsWith(".schem"))
            myFile = new File(Main.main.getDataFolder().getAbsolutePath() + "/schematics/" + schematic);

        ClipboardFormat format = ClipboardFormats.findByFile(myFile);

        try {

            ClipboardReader reader = format.getReader(new FileInputStream(myFile));
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