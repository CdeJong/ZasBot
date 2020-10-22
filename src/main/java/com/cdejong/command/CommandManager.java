package com.cdejong.command;

import com.cdejong.Bot;
import com.cdejong.command.commands.HentaiCommand;
import com.cdejong.command.commands.ReloadCommand;
import com.cdejong.command.commands.StopCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    private Bot bot;

    public CommandManager(Bot bot) {
        this.bot = bot;
    }

    public void addCommand(ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present: " + command.getName());
        }

        commands.add(command);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(String command) {
        ICommand result = null;

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(command.toLowerCase()) || cmd.getAliases().contains(command.toLowerCase())) {
                result = cmd;
                break;
            }
        }

        return result;
    }

    public void executeCommand(ICommandSender sender, String[] args) {
        ICommand command = getCommand(args[0]);

        if (command == null) {
            return;
        }

        Bot.getLogger().info(sender.getName() + " executed the command: " + command.getName());

        command.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }
}
