package com.cdejong.console;

import com.cdejong.Bot;
import com.cdejong.command.CommandManager;
import com.cdejong.command.ConsoleCommandSender;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

public class BotConsole extends SimpleTerminalConsole {

    private Bot bot;
    private CommandManager commandManager;

    public BotConsole(Bot bot) {
        this.bot = bot;
        this.commandManager = bot.getCommandManager();
    }

    @Override
    protected boolean isRunning() {
        return !bot.isShutdown();
    }

    @Override
    protected void runCommand(String s) {
        String[] args = s.split("\\s+");
        commandManager.executeCommand(new ConsoleCommandSender(), args);
    }

    @Override
    protected void shutdown() {
        bot.shutdown();
    }
}
