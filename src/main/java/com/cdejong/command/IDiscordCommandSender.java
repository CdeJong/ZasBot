package com.cdejong.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface IDiscordCommandSender {
    String getId();

    Guild getGuild();

    TextChannel getChannel();

    GuildMessageReceivedEvent getEvent();

    void sendMessage(Message message);

    void sendMessage(MessageEmbed embed);

    void sendPrivateMessage(String message);

    void sendPrivateMessage(Message message);

    void sendPrivateMessage(MessageEmbed embed);
}
