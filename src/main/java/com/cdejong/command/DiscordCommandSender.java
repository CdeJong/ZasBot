package com.cdejong.command;

import com.cdejong.Bot;
import com.cdejong.config.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DiscordCommandSender implements ICommandSender, IDiscordCommandSender {

    private Bot bot;
    private Config config;
    private GuildMessageReceivedEvent event;

    public DiscordCommandSender(Bot bot, GuildMessageReceivedEvent event) {
        this.bot = bot;
        this.config = bot.getConfig();
        this.event = event;
    }

    @Override
    public String getName() {
        return event.getAuthor().getAsTag() + "/" + event.getAuthor().getId();
    }

    @Override
    public void sendMessage(String message) {
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public boolean isOwner() {
        return config.getOwners().contains(event.getAuthor().getId());
    }

    @Override
    public String getId() {
        return event.getAuthor().getId();
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    @Override
    public TextChannel getChannel() {
        return event.getChannel();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    @Override
    public void sendMessage(Message message) {
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void sendMessage(MessageEmbed embed) {
        event.getChannel().sendMessage(embed).queue();
    }

    @Override
    public void sendPrivateMessage(String message) {
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(message).queue());
    }

    @Override
    public void sendPrivateMessage(Message message) {
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(message).queue());
    }

    @Override
    public void sendPrivateMessage(MessageEmbed embed) {
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(embed).queue());
    }
}
