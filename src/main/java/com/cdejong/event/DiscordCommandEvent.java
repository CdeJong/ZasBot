package com.cdejong.event;

import com.cdejong.Bot;
import com.cdejong.command.CommandManager;
import com.cdejong.command.DiscordCommandSender;
import com.cdejong.config.Config;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class DiscordCommandEvent extends ListenerAdapter {

    private Bot bot;
    private Config config;
    private CommandManager commandManager;

    public DiscordCommandEvent(Bot bot) {
        this.bot = bot;
        this.config = bot.getConfig();
        this.commandManager = bot.getCommandManager();
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) return;

        String prefix = config.getPrefix();
        String rawMessage = event.getMessage().getContentRaw();

        if (rawMessage.startsWith(prefix)) {
            String[] args = rawMessage.replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");

            commandManager.executeCommand(new DiscordCommandSender(bot, event), args);

        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
    }
}
