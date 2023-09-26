package dev.meinmex.discordtwitch.connection.listeners;

import dev.meinmex.discordtwitch.connection.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getChannel().asTextChannel();
        Message message = event.getMessage();

        if (author.isBot()) return;

        if (guild.getId().equals(Bot.guildID) && textChannel.getId().equals(Bot.channelID)) {
            String translatedMessage = "Discord | " + author.getName() + ": " + message.getContentDisplay();
            System.out.println("Discord -> Twitch [" + author.getName() + "]: " + translatedMessage);
            Bot.sendTwitchMessage(translatedMessage);
        }
    }
}
