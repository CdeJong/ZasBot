package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.DiscordCommandSender;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;
import com.cdejong.config.Config;
import com.cdejong.config.ConfigManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwnerCommand implements ICommand {

    private Bot bot;
    private ConfigManager configManager;
    private List<String> owners;
    private Config config;

    public OwnerCommand(Bot bot) {
        this.bot = bot;
        this.configManager = bot.getConfigManager();
    }

    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        config = configManager.getConfig();
        owners = config.getOwners();


        if (sender.isOwner() && args.length > 0) {

            Optional<String> selectedMember = getMember(args[0]);
            if (!selectedMember.isPresent()) {
                ((DiscordCommandSender) sender).sendMessage("Member tag|id is not valid");
                return;
            }

            if (args.length == 1) {
                toggleOwner(selectedMember.get(), sender);
                return;
            }

            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("add")) {
                    setOwner(selectedMember.get(), sender, true);
                    return;
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    setOwner(selectedMember.get(), sender, false);
                    return;
                }
                sender.sendMessage("Syntax Error: ." + getUsage());
                return;
            }
        }

        sender.sendMessage("Owners (" + owners.size() + "): " + String.join(", ", owners));

    }

    @Override
    public String getName() {
        return "owner";
    }

    @Override
    public String getDescription() {
        return "Show, add or remove owners";
    }

    @Override
    public String getUsage() {
        return "owner [id|mention] [add|remove]";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("owners");
    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }

    public Optional<String> getMember(String arg) {
        Pattern pattern = Pattern.compile("([0-9]{18})");
        Matcher matcher = pattern.matcher(arg);

        if (!matcher.find()) {
            return Optional.empty();
        }
        return Optional.ofNullable(matcher.group(1));
    }

    public void toggleOwner(String selectedMember, ICommandSender sender) {
        if (owners.contains(selectedMember)) {
            setOwner(selectedMember, sender, false);
        } else {
            setOwner(selectedMember, sender, true);
        }
    }

    public void setOwner(String selectedMember, ICommandSender sender, boolean setOwner) {
        if (setOwner) {
            if (owners.contains(selectedMember)) {
                sender.sendMessage(selectedMember + " is already a owner.");
                return;
            }
            sender.sendMessage("Added " + selectedMember + " to OwnerList.");
            owners.add(selectedMember);
        } else {
            if (!owners.contains(selectedMember)) {
                sender.sendMessage(selectedMember + "is not a Owner.");
                return;
            }
            sender.sendMessage("Removed " + selectedMember + " from OwnerList.");
            owners.remove(selectedMember);
        }
        config.setOwners(owners);
        configManager.saveConfig();
    }
}
