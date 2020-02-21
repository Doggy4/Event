package PluginUtilities;

import event.main.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CooldownKostil {

    private HashMap<String, Integer> cooldown = new HashMap<String, Integer>();
    private HashMap<String, Boolean> bool = new HashMap<String, Boolean>();
    private boolean isRunning;

    public CooldownKostil(String cooldownName, Integer cooldownTime) {
        cooldown.put(cooldownName, cooldownTime);
    }

    public Integer getCooldownTime(String cooldownName) {
        return cooldown.get(cooldownName);
    }

    public void tickCooldown(String cooldownName) {
        int timeLeft = cooldown.get(cooldownName);
        isRunning = bool.get(cooldownName);
        if (isRunning) return;
        new BukkitRunnable() {
            public void run() {
                if (timeLeft == 0) {
                    isRunning = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.main, 20, 20);
    }

    public void removeCooldown(String cooldownName) {
        cooldown.remove(cooldownName);
        bool.remove(cooldownName);
    }
}
