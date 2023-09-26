package dev.meinmex.discordtwitch.connection.listeners;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import dev.meinmex.discordtwitch.connection.Bot;

public class TwitchMessageListener {

    public TwitchMessageListener(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent event) {
        String user = event.getUser().getName();
        String message = event.getMessage();

        if (user.equalsIgnoreCase(Bot.name) || message.startsWith("!")) return;

        String msg = "**Twitch | " + user + ": ** " + message;
        Bot.sendDiscordMessage(msg);

        System.out.println("Twitch -> Discord [" + user + "]: " + message);
    }
}
