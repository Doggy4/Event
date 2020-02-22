package event.main;


import Constructors.InventoryConstructor;
import ImageMaps.FastSendTask;
import ImageMaps.ImageMaps;
import Particles.ParticleGUI;
import QueueSystem.MainScoreBoard;
import RoundList.*;
import RoundSystem.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    public static Main main;

    @Override
    public void onEnable() {
        // Ссылка на главный класс
        main = this;

        Bukkit.getLogger().info(ChatColor.GOLD + "[EVENT] Plugin enabled!");
        Bukkit.getPluginCommand("event").setExecutor(new Commands.CommandEvent());

        this.getServer().getPluginManager().registerEvents(new MainPlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryConstructor(), this);
        this.getServer().getPluginManager().registerEvents(new RoundPlaceTheBlock(), this);
        this.getServer().getPluginManager().registerEvents(new RoundDropTheItem(), this);
        this.getServer().getPluginManager().registerEvents(new RoundHitTheBlock(), this);
        this.getServer().getPluginManager().registerEvents(new RoundTrimTheSheep(), this);
        this.getServer().getPluginManager().registerEvents(new RoundThrowTheEgg(), this);
        this.getServer().getPluginManager().registerEvents(new GameRules(), this);
        this.getServer().getPluginManager().registerEvents(new RoundMilkTheCow(), this);
        this.getServer().getPluginManager().registerEvents(new RoundTheRightCombination(), this);
        this.getServer().getPluginManager().registerEvents(new RoundAnvilEscape(), this);
        this.getServer().getPluginManager().registerEvents(new ParticleGUI(), this);
        this.getServer().getPluginManager().registerEvents(new RoundCakeParkour(), this);
        this.getServer().getPluginManager().registerEvents(new RoundMath(), this);
        this.getServer().getPluginManager().registerEvents(new RoundKnockEveryoneOff(), this);
        this.getServer().getPluginManager().registerEvents(new RoundHarryPotter(), this);
        this.getServer().getPluginManager().registerEvents(new RoundSlimePvP(), this);
        this.getServer().getPluginManager().registerEvents(new RoundHideUnderBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new RoundFeedBob(), this);
        this.getServer().getPluginManager().registerEvents(new ImageMaps(), this);
        this.getServer().getPluginManager().registerEvents(new RoundCraftItem(), this);

        if (!(new File(getDataFolder(), "images")).exists())
            (new File(getDataFolder(), "images")).mkdirs();

        int sendPerTicks = getConfig().getInt("sendPerTicks", 20);
        int mapsPerSend = getConfig().getInt("mapsPerSend", 8);

        ImageMaps.loadMaps();

        ImageMaps.sendTask = new FastSendTask(this, mapsPerSend);
        ImageMaps.sendTask.runTaskTimer(this, sendPerTicks, sendPerTicks);

        MainScoreBoard.startPluginRunnable();
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        ImageMaps.saveMaps();
        getServer().getScheduler().cancelTasks(this);
    }

    public static boolean checkUrl(String url) {
        if (url.trim().isEmpty() || url.trim().equals("https://canary.discordapp.com/api/webhooks")) {
            Main.main.getServer().getLogger().severe("The Webhook URL is empty!");
            return false;
        }
        return true;
    }
}
