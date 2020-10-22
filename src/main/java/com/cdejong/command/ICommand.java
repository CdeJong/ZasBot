package com.cdejong.command;

import java.util.List;

public interface ICommand {
    void onCommand(ICommandSender sender, String[] args);

    String getName();

    String getDescription();

    String getUsage();

    List<String> getAliases();

    boolean isOwnerCommand();
}
