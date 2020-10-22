package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.DiscordCommandSender;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;
import com.cdejong.command.IDiscordCommandSender;
import com.cdejong.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhoisCommand implements ICommand {

    private Bot bot;

    public WhoisCommand(Bot bot) {
        this.bot = bot;
    }


    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        if (sender instanceof IDiscordCommandSender) {
            DiscordCommandSender discordSender = (DiscordCommandSender) sender;

            Config config = bot.getConfig();
            boolean isBotChannel = config.getBotChannels().contains(discordSender.getChannel().getId());

            if (!isBotChannel) {
                return;
            }

            Member selectedMember;
            User selectedUser;
            String memberContent;



            if (args.length == 0) {
                selectedMember = discordSender.getEvent().getMember();
                selectedUser = selectedMember.getUser();
                memberContent = getMemberContent(selectedMember);

            } else if (args.length == 1) {
                if (!getUser(args[0]).isPresent()) {
                    discordSender.getChannel().sendMessage("Invalid Mention or Id!").queue();
                    return;
                }
                selectedUser = getUser(args[0]).get();

                Optional<Member> member = getMember(discordSender.getGuild(), selectedUser);
                memberContent = "Member: `false`";
                if (member.isPresent()) {
                    memberContent = getMemberContent(member.get());
                }

            } else {
                discordSender.getChannel().sendMessage("Invalid usage of this command.").queue();
                return;
            }


            EmbedBuilder whoisEmbed = new EmbedBuilder();
            whoisEmbed.setTitle("Lookup " + selectedUser.getName());
            whoisEmbed.setThumbnail(selectedUser.getEffectiveAvatarUrl());
            whoisEmbed.setColor(bot.getBotColor(discordSender.getGuild()));
            whoisEmbed.addField("User info:", getUserContent(discordSender.getGuild(), selectedUser), false);
            whoisEmbed.addField("Member info:", memberContent, false);




            discordSender.getChannel().sendMessage(whoisEmbed.build()).queue();

        }
    }

    @Override
    public String getName() {
        return "whois";
    }

    @Override
    public String getDescription() {
        return "Whois a discord user / guild member.";
    }

    @Override
    public String getUsage() {
        return "whois [id|mention]";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("lookup");
    }

    @Override
    public boolean isOwnerCommand() {
        return false;
    }

    public String getUserContent(Guild guild, User user) {
        String username = String.format("Name: `%s`", user.getAsTag());
        String userId = String.format("Id: `%s`", user.getId());
        String isBot = String.format("Bot: `%b`", user.isBot());
        String isBotOwner = String.format("Bot owner: `%b`", bot.getConfig().getOwners().contains(user.getId()));
        String isBanned = String.format("Banned: `%b`", isBanned(guild, user));
        //String mutualGuilds = String.format("Mutual guilds: %s", getMutualGuilds(user)); Werk niet altijd :s
        String creation = String.format("Creation: `%s`", getTime(user.getTimeCreated()));

        StringJoiner userContent = new StringJoiner("\n");
        userContent.add(username).add(userId).add(isBot).add(isBotOwner).add(isBanned).add(creation);
        return userContent.toString();
    }





    public String getMemberContent(Member member) {
        String effectiveName = String.format("Effective name: `%s`", member.getEffectiveName());
        String isOwner = String.format("Owner: `%b`", member.isOwner());
        String isAdmin = String.format("Admin: `%b`", member.hasPermission(Permission.ADMINISTRATOR));
        String roles = String.format("Roles: %s", getRoles(member));
        String timeJoined = String.format("Joined: `%s`", getTime(member.getTimeJoined()));

        StringJoiner memberContent = new StringJoiner("\n");
        memberContent.add(effectiveName).add(isOwner).add(isAdmin).add(roles).add(timeJoined);
        return memberContent.toString();

    }

    public Optional<User> getUser(String user) {
        Pattern idPattern = Pattern.compile("([0-9]{18})");
        Matcher idMatcher = idPattern.matcher(user);

        if (!idMatcher.find()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(bot.getJda().retrieveUserById(idMatcher.group(1)).complete());
        } catch (ErrorResponseException e) {
            return Optional.empty();
        }
    }

    public boolean isBanned(Guild guild, User user) {
        return guild.retrieveBanList().complete().stream().anyMatch(ban -> ban.getUser().getId().equals(user.getId()));
    }

    private String getTime(OffsetDateTime time) {
        ZonedDateTime dutchTime = time.atZoneSameInstant(ZoneId.of("Europe/Amsterdam"));
        return dutchTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    private String getMutualGuilds(User user) {
        StringJoiner guildList = new StringJoiner(", ");
        user.getMutualGuilds().forEach(g -> guildList.add("`" + g.getName() + "`"));
        return guildList.toString();
    }

    private Optional<Member> getMember(Guild guild, User user) {
        try {
            return Optional.ofNullable(guild.retrieveMember(user).complete());
        } catch (ErrorResponseException ex) {
            return Optional.empty();
        }
    }

    private String getRoles(Member member) {
        StringJoiner roleList = new StringJoiner(", ");
        member.getRoles().forEach(r -> roleList.add(r.getAsMention()));
        return roleList.toString();
    }
}
