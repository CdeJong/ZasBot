package com.cdejong;

import com.cdejong.command.CommandManager;
import com.cdejong.command.commands.HelpCommand;
import com.cdejong.command.commands.HentaiCommand;
import com.cdejong.command.commands.MemeCommand;
import com.cdejong.command.commands.OwnerCommand;
import com.cdejong.command.commands.ReloadCommand;
import com.cdejong.command.commands.StopCommand;
import com.cdejong.command.commands.WhoisCommand;
import com.cdejong.config.Config;
import com.cdejong.config.ConfigManager;
import com.cdejong.console.BotConsole;
import com.cdejong.event.DiscordCommandEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Bot {

    private JDA jda;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private boolean shutdown = false;

    public Bot(JDA jda) {
        this.jda = jda;
        configManager = new ConfigManager();
        Config config = getConfig();
        commandManager = new CommandManager(this);

        //commands
        commandManager.addCommand(new WhoisCommand(this));
        commandManager.addCommand(new HentaiCommand(this));
        commandManager.addCommand(new MemeCommand(this));
        commandManager.addCommand(new OwnerCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
        commandManager.addCommand(new StopCommand(this));
        commandManager.addCommand(new HelpCommand(this));

        //activity
        jda.getPresence().setActivity(Activity.watching(config.getActivity()));

        //commands discord
        jda.addEventListener(new DiscordCommandEvent(this));

        //commands console
        BotConsole botConsole = new BotConsole(this);
        botConsole.start();
    }

    public JDA getJda() {
        return jda;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Config getConfig() {
        return configManager.getConfig();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void shutdown() {
        shutdown = true;
        jda.shutdown();
        System.exit(0);
    }

    public Color getBotColor(Guild guild) {
        return guild.getSelfMember().getColor();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            getLogger().warn("Token not found!");
            return;
        }

        JDA jda;
        try {
            JDABuilder builder = JDABuilder.createDefault(args[0]);
            jda = builder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            getLogger().warn("invalid token!");
            e.printStackTrace();
            return;
        }

        new Bot(jda);
    }

    public static Logger getLogger() {
        return LogManager.getLogger();
    }

}


