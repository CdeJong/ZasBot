package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StopCommand implements ICommand {

    private Bot bot;

    public StopCommand(Bot bot) {
        this.bot = bot;
    }


    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        if (!sender.isOwner()) {
            sender.sendMessage("This command only can be executed by a Bot owner!");
            return;
        }

        String reason = "No reason specified";
        if (args.length > 0) {
            reason = String.join(" ", args);
        }

        sender.sendMessage("Stopped the bot: " + reason);
        bot.shutdown();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "stop the bot!";
    }

    @Override
    public String getUsage() {
        return "stop [reason]";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("shutdown");
    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }
}
