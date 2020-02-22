package PluginUtils;

import Constructors.DiscordWebhook;
import QueueSystem.Queue;
import RoundSystem.GameCycle;
import RoundSystem.RoundSystem;
import event.main.Main;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.IOException;

public class DiscordWebHooks {
    public static void gameStarted() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setAvatarUrl("https://i.ibb.co/YfvrWVZ/image.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Игра началась!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Количество раундов:**", RoundSystem.round - 1 + "/" + RoundSystem.roundCount, false)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Игроков на сервере:**", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), false)
                    .setThumbnail("https://i.ibb.co/YfvrWVZ/image.png")
                    .setFooter("Мы вас ждем!", "https://i.ibb.co/YfvrWVZ/image.png")
                    .setImage("https://i.ibb.co/vDgfV1p/title-small.png")
                    .setAuthor("Amphitheatrum Flavium", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ibb.co/YfvrWVZ/image.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public static void roundEnded() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setAvatarUrl("https://i.ibb.co/YfvrWVZ/image.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Раунд завершен!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Раунд:**", Chat.roundNames.get(RoundSystem.randomGame), false)
                    .addField("**Количество раундов:**", RoundSystem.round - 1 + "/" + RoundSystem.roundCount, false)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Игроков на сервере:**", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), false)
                    .addField("**Текущий лидер:**", GameCycle.getWinner().getName() + " [" + RoundSystem.roundStats.get(GameCycle.getWinner()) + "]", false)
                    .setThumbnail("https://i.ibb.co/YfvrWVZ/image.png")
                    .setFooter("Мы вас ждем!", "https://i.ibb.co/YfvrWVZ/image.png")
                    .setImage("https://i.ibb.co/vDgfV1p/title-small.png")
                    .setAuthor("Amphitheatrum Flavium", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ibb.co/YfvrWVZ/image.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public static void gameEnded() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setAvatarUrl("https://i.ibb.co/YfvrWVZ/image.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Игра завершена!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Игроков на сервере:**", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), false)
                    .addField("**Победитель:**", "\uD83D\uDC8E " + GameCycle.getWinner().getName() + " \uD83D\uDC8E", false)
                    .setThumbnail("https://i.ibb.co/YfvrWVZ/image.png")
                    .setFooter("Мы вас ждем!", "https://i.ibb.co/YfvrWVZ/image.png")
                    .setImage("https://i.ibb.co/vDgfV1p/title-small.png")
                    .setAuthor("Amphitheatrum Flavium", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ibb.co/YfvrWVZ/image.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public static void playerJoin(String playerName) {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setAvatarUrl("https://i.ibb.co/YfvrWVZ/image.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Игрок __" + playerName + "__ зашел на сервер!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Игроков на сервере:**", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), false)
                    .setThumbnail("https://excalibur-craft.ru/engine/ajax/lk/skin3d.php?login=" + playerName + "&format=png&displayHairs=true&headOnly=true")
                    .setFooter("Поторпись!", "https://i.ibb.co/YfvrWVZ/image.png")
                    .setImage("https://i.ibb.co/vDgfV1p/title-small.png")
                    .setAuthor("Amphitheatrum Flavium", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ibb.co/YfvrWVZ/image.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }
}
