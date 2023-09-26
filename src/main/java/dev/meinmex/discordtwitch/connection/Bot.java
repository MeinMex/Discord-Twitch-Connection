package dev.meinmex.discordtwitch.connection;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.google.common.io.Files;
import dev.meinmex.discordtwitch.connection.listeners.DiscordMessageListener;
import me.limeglass.streamelements.api.StreamElements;
import me.limeglass.streamelements.api.StreamElementsBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

public class Bot {
    private static TwitchClient twitchClient;
    private static JDA jda;
    private static StreamElements streamelements;
    private static JSONObject configuration = loadConfiguration();

    public static final String guildID = configuration.getJSONObject("discord").getString("guildid");
    public static final String channelID = configuration.getJSONObject("discord").getString("channelid");
    public static final String streamer = configuration.getJSONObject("twitch").getString("streamer-name");
    public static final String name = configuration.getJSONObject("twitch").getJSONObject("bot").getString("name");

    private static final String[] forbiddenChars = {"@", "<", ">"};
    private static final String[] filter = configuration.getJSONObject("filter").getString("blacklist").split(",");

    public static void main(String[] args) {
        startDiscordBot();
        startTwitchBot();
        startStreamElements();
    }

    private static JSONObject loadConfiguration() {
        try {
            StringBuilder content = new StringBuilder();
            for (String line : Files.readLines(new File("config.json"), Charset.defaultCharset())) {
                content.append(line);
            }
            return new JSONObject(content.toString());
        } catch (Exception e) {
            System.out.println("Error loading config:");
            e.printStackTrace();
            return null;
        }
    }

    public static String recreateMessage(String old) {
        for (String m : forbiddenChars) {
            old = old.replace(m, "");
        }
        for (String m : filter) {
            old = old.replace(m, "<censored>");
        }
        return old;
    }

    private static void startStreamElements() {
        String token = configuration.getJSONObject("twitch").getJSONObject("bot").getString("streamelements-token");
        streamelements = new StreamElementsBuilder()
                .withAccountID(token)
                .withToken(token)
                .withConnectionTimeout(60000)
                .build();
    }

    private static void startDiscordBot() {
        String token = configuration.getJSONObject("discord").getJSONObject("bot").getString("token");
        String activity = configuration.getJSONObject("discord").getJSONObject("bot").getString("activity");

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(new DiscordMessageListener());
        builder.setActivity(Activity.playing(activity));

        try {
            jda = builder.build();
        } catch (Exception e) {
            System.out.println("ERROR STARTING JDA!!");
        }
    }

    private static void startTwitchBot() {
        String accessToken = configuration.getJSONObject("twitch").getJSONObject("bot").getString("access-token");

        OAuth2Credential credential = new OAuth2Credential(name, accessToken);
        OAuth2Credential oAuth2 = new OAuth2Credential(name, accessToken);
        twitchClient = TwitchClientBuilder.builder()
                .withChatAccount(credential)
                .withDefaultAuthToken(oAuth2)
                .withEnableChat(true)
                .withEnableHelix(true)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        twitchClient.getClientHelper().enableStreamEventListener(streamer);
        twitchClient.getChat().joinChannel(streamer);

        try {
            Thread.sleep(60 * 60 * 1000);
            twitchClient.getChat().disconnect();
            twitchClient.close();
            twitchClient = null;
            startTwitchBot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTwitchMessage(String message) {
        twitchClient.getChat().sendMessage(streamer, recreateMessage(message));
    }

    public static void sendDiscordMessage(String message) {
        TextChannel textChannel = Objects.requireNonNull(getJDA()).getTextChannelById(channelID);
        assert textChannel != null;
        textChannel.sendMessage(recreateMessage(message)).queue();
    }

    public static JDA getJDA() {
        try {
            return jda.awaitReady();
        } catch (Exception e) {
            return null;
        }
    }

    public static TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
