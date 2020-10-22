package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.CommandManager;
import com.cdejong.command.DiscordCommandSender;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;
import com.cdejong.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class HelpCommand implements ICommand {

    private Bot bot;
    private CommandManager commandManager;

    public HelpCommand(Bot bot) {
        this.bot = bot;
        this.commandManager = bot.getCommandManager();
    }

    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        if (sender instanceof DiscordCommandSender) {
            DiscordCommandSender discordSender = (DiscordCommandSender) sender;
            String botName = bot.getJda().getSelfUser().getName();
            String botPrefix = bot.getConfig().getPrefix();

            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setColor(bot.getBotColor(discordSender.getGuild()));
            helpEmbed.setThumbnail(bot.getJda().getSelfUser().getEffectiveAvatarUrl());
            helpEmbed.setTitle(botName + " Help");
            helpEmbed.setDescription(botName + "'s commands. The bot prefix is at the moment `" + bot.getConfig().getPrefix() + "`. `<>` is required and `[]` is optional.");
            if (sender.isOwner()) {
                commandManager.getCommands()
                        .forEach(c -> helpEmbed.addField(c.getName() + getAliasList(c.getAliases()), c.getDescription() + " - `" + bot.getConfig().getPrefix() + c.getUsage() + "`", false));
            } else {
                commandManager.getCommands().stream()
                        .filter(c -> !c.isOwnerCommand())
                        .forEach(c -> helpEmbed.addField(c.getName() + getAliasList(c.getAliases()), c.getDescription() + " - `" + bot.getConfig().getPrefix() + c.getUsage() + "`", false));
            }

            discordSender.sendMessage(helpEmbed.build());

        }

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows all commands.";
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean isOwnerCommand() {
        return false;
    }

    private String getAliasList(List<String> aliases) {
        if (aliases.isEmpty()) {
            return "";
        }
        StringJoiner aliasList = new StringJoiner(", ");
        aliases.forEach(aliasList::add);
        return " (" + aliasList.toString() + ")";
    }
}
