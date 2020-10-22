package com.cdejong.command;

public interface ICommandSender {
    String getName();

    void sendMessage(String message);

    boolean isOwner();
}
