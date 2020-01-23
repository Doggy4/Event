package event.main;


import Game.*;
import PluginUtilities.InventoryConstructor;
import QueueSystem.PrestartScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main main;

    @Override
    public void onEnable() {
        main = this;

        Bukkit.getLogger().info(ChatColor.GOLD + "[RavagerRush] Plugin enabled!");
        Bukkit.getPluginCommand("event").setExecutor(new Commands.CommandEvent());
        Bukkit.getPluginCommand("spec").setExecutor(new Commands.CommandSpec());

        this.getServer().getPluginManager().registerEvents(new MainPlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryConstructor(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        this.getServer().getPluginManager().registerEvents(new DropItem(), this);
        this.getServer().getPluginManager().registerEvents(new BowShoot(), this);
        this.getServer().getPluginManager().registerEvents(new ShearSheep(), this);
        this.getServer().getPluginManager().registerEvents(new BaseClass(), this);

        PrestartScoreBoard.PrestartScoreboard();
        this.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
