package event.main;


import Game.*;
import PluginUtilities.InventoryConstructor;
import QueueSystem.MainScoreBoard;
import SvistoPerdelki.ParticleGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main main;

    @Override
    public void onEnable() {
        // Ссылка на главный класс
        main = this;

        Bukkit.getLogger().info(ChatColor.GOLD + "[EVENT] Plugin enabled!");
        Bukkit.getPluginCommand("event").setExecutor(new Commands.CommandEvent());

        this.getServer().getPluginManager().registerEvents(new MainPlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryConstructor(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        this.getServer().getPluginManager().registerEvents(new DropItem(), this);
        this.getServer().getPluginManager().registerEvents(new BowShoot(), this);
        this.getServer().getPluginManager().registerEvents(new ShearSheep(), this);
        this.getServer().getPluginManager().registerEvents(new EggThrow(), this);
        this.getServer().getPluginManager().registerEvents(new GameRules(), this);
        this.getServer().getPluginManager().registerEvents(new CowMilk(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceWool(), this);
        this.getServer().getPluginManager().registerEvents(new DodgeAnvils(), this);
        this.getServer().getPluginManager().registerEvents(new ParticleGUI(), this);
        this.getServer().getPluginManager().registerEvents(new ParkourEatCake(), this);

        MainScoreBoard.startPluginRunnable();
        this.saveDefaultConfig();

    }

    public static void logError(Exception e) {
        Main.main.getServer().getLogger().severe("Error with DiscordWebhook " + e);
        e.printStackTrace();
    }

    public static boolean checkUrl(String url) {
        Bukkit.broadcastMessage(url.trim());
        if (url.trim().isEmpty() || url.trim().equals("https://canary.discordapp.com/api/webhooks")) {
            Main.main.getServer().getLogger().severe("The Webhook URL is empty!");
            return false;
        }
        return true;
    }
}
