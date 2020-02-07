package NBS;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import event.main.Main;
import org.bukkit.entity.Player;

import java.io.File;

public class NoteBlockPlayer {

    public static void playMusic(Player player, String songName) {
        Song song = NBSDecoder.parse(new File(Main.main.getDataFolder().getAbsolutePath() + "/songs/" + songName + ".nbs"));
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.addPlayer(player);
        rsp.setPlaying(true);
    }
}
