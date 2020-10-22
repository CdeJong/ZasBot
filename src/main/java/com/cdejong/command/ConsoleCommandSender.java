package com.cdejong.command;

import com.cdejong.Bot;

public class ConsoleCommandSender implements ICommandSender, IConsoleCommandSender {

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        Bot.getLogger().info(message);
    }

    @Override
    public boolean isOwner() {
        return true;
    }
}
