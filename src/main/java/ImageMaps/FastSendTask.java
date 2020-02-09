package ImageMaps;

import event.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FastSendTask extends BukkitRunnable implements Listener {
    private final Main plugin;
    private final int mapsPerRun;
    private Map<UUID, Queue<Integer>> status = new HashMap<>();

    public FastSendTask(Main plugin, int mapsPerSend) {
        this.plugin = plugin;
        this.mapsPerRun = mapsPerSend;
    }

    public void run() {
        if (ImageMaps.getFastSendList().isEmpty())
            return;
        for (Player p : this.plugin.getServer().getOnlinePlayers()) {
            Queue<Integer> state = getStatus(p);
            for (int i = 0; i < this.mapsPerRun && !state.isEmpty(); i++)
                p.sendMap(this.plugin.getServer().getMap(state.poll().intValue()));
        }
    }

    private Queue<Integer> getStatus(Player p) {
        if (!this.status.containsKey(p.getUniqueId()))
            this.status.put(p.getUniqueId(), new LinkedList<>(ImageMaps.getFastSendList()));
        return this.status.get(p.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.status.put(e.getPlayer().getUniqueId(), new LinkedList<>(ImageMaps.getFastSendList()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.status.remove(e.getPlayer().getUniqueId());
    }

    public void addToQueue(int mapId) {
        for (Queue<Integer> queue : this.status.values())
            queue.add(Integer.valueOf(mapId));
    }
}