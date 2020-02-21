package WebHooks;

import PluginUtilities.Chat;
import QueueSystem.Queue;
import RoundSystem.GameCycle;
import RoundSystem.RoundSystem;
import event.main.Main;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;


public class DiscordWebhook {

    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<EmbedObject> embeds = new ArrayList<>();

    public DiscordWebhook(String url) {
        this.url = url;
    }

    public static void gameStarted() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setContent("");
            webhook.setAvatarUrl("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Игра началась!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Количество раундов:**", RoundSystem.round - 1 + "/" + RoundSystem.roundCount, false)
                    .setThumbnail("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setFooter("Мы вас ждем!", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    //.setImage("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setAuthor("BestLife Official Event", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public static void roundEnded() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setAvatarUrl("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Раунд завершен!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Раунд:**", Chat.roundNames.get(RoundSystem.randomGame), false)
                    .addField("**Количество раундов:**", RoundSystem.round - 1 + "/" + RoundSystem.roundCount, false)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Текущий лидер:**", GameCycle.getWinner().getName() + " [" + RoundSystem.roundStats.get(GameCycle.getWinner()) + "]", false)
                    .setThumbnail("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setFooter("Мы вас ждем!", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    //.setImage("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setAuthor("BestLife Official Event", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public static void gameEnded() {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/673392196493377566/aVunHRsD_JzjelGwNMygS67vo_qM_Bu1diLbInwszQt82HAuQrVcG6382dDFiYkmlI2J");
            webhook.setContent("");
            webhook.setAvatarUrl("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png");
            webhook.setUsername("BestLife Official Event");
            webhook.setTts(false);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Игра завершена!")
                    .setDescription("Ознакомиться с правилами эвента можно [тут](https://forum.excalibur-craft.ru/forum/125-BestLife/)!")
                    .setColor(Color.CYAN)
                    .addField("**Количество игроков:**", Queue.redQueueList.size() + "/10", false)
                    .addField("**Победитель:**", "\uD83D\uDC8E " + GameCycle.getWinner().getName() + " \uD83D\uDC8E", false)
                    .setThumbnail("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setFooter("Мы вас ждем!", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    //.setImage("https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png")
                    .setAuthor("BestLife Official Event", "https://forum.excalibur-craft.ru/forum/125-BestLife/", "https://i.ya-webdesign.com/images/a-letter-logo-design-png-7.png"));
            //.setUrl("https://forum.excalibur-craft.ru/forum/125-BestLife/"));
            webhook.execute();
        } catch (IOException e) {
            Main.main.getLogger().severe(e.getMessage());
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : this.embeds) {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.getText());
                    jsonFooter.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.getUrl());
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.getUrl());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<JSONObject> jsonFields = new ArrayList<>();
                for (EmbedObject.Field field : fields) {
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.getName());
                    jsonField.put("value", field.getValue());
                    jsonField.put("inline", field.isInline());

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Doggy4");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, StandardCharsets.UTF_8));
        writer.write(json.toString());
        writer.flush();
        writer.close();
        wr.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private Color color;

        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;
        private List<Field> fields = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        public Color getColor() {
            return color;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public Footer getFooter() {
            return footer;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        public Image getImage() {
            return image;
        }

        public EmbedObject setImage(String url) {
            this.image = new Image(url);
            return this;
        }

        public Author getAuthor() {
            return author;
        }

        public List<Field> getFields() {
            return fields;
        }

        public EmbedObject setFooter(String text, String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private class Footer {
            private String text;
            private String iconUrl;

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            private String getText() {
                return text;
            }

            private String getIconUrl() {
                return iconUrl;
            }
        }

        private class Thumbnail {
            private String url;

            private Thumbnail(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }
        }

        private class Image {
            private String url;

            private Image(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }
        }

        private class Author {
            private String name;
            private String url;
            private String iconUrl;

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            private String getName() {
                return name;
            }

            private String getUrl() {
                return url;
            }

            private String getIconUrl() {
                return iconUrl;
            }
        }

        private class Field {
            private String name;
            private String value;
            private boolean inline;

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            private String getName() {
                return name;
            }

            private String getValue() {
                return value;
            }

            private boolean isInline() {
                return inline;
            }
        }
    }

    private class JSONObject {

        private final HashMap<String, Object> map = new HashMap<>();

        void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val.toString());
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }

}