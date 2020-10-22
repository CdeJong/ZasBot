package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;
import com.cdejong.config.Config;
import com.cdejong.config.ConfigManager;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements ICommand {

    private Bot bot;
    private ConfigManager configManager;

    public ReloadCommand(Bot bot) {
        this.bot = bot;
        this.configManager = bot.getConfigManager();
    }

    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        if (!sender.isOwner()) {
            sender.sendMessage("This command only can be executed by a Bot owner!");
            return;
        }

        configManager.reloadConfig();
        Config config = bot.getConfig();

        bot.getJda().getPresence().setActivity(Activity.watching(config.getActivity()));

        sender.sendMessage("Reloaded config files from the bot!");
        Bot.getLogger().info(sender.getName() + " Reloaded the bot!");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the config files.";
    }

    @Override
    public String getUsage() {
        return "reload";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }
}
