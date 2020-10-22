package com.cdejong.command.commands;

import com.cdejong.Bot;
import com.cdejong.command.DiscordCommandSender;
import com.cdejong.command.ICommand;
import com.cdejong.command.ICommandSender;
import com.cdejong.command.IConsoleCommandSender;
import com.cdejong.command.IDiscordCommandSender;
import com.cdejong.config.Config;
import com.cdejong.reddit.RedditManager;
import com.cdejong.rest.RedditResponse;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HentaiCommand implements ICommand {

    private Bot bot;
    private RedditManager redditManager;

    public HentaiCommand(Bot bot) {
        this.bot = bot;
        this.redditManager = new RedditManager(bot, "hentai");
    }

    @Override
    public void onCommand(ICommandSender sender, String[] args) {
        if (sender instanceof IConsoleCommandSender) {
            sender.sendMessage("This Command can not be executed in a Terminal");
            return;
        }

        if (sender instanceof IDiscordCommandSender) {
            DiscordCommandSender discordSender = (DiscordCommandSender) sender;

            Config config = bot.getConfig();
            boolean isBotChannel = config.getBotChannels().contains(discordSender.getChannel().getId());

            if (isBotChannel) {
                Optional<RedditResponse.Post> post = redditManager.getNextPost(discordSender.getEvent().getGuild());

                EmbedBuilder hentaiEmbed = new EmbedBuilder();
                hentaiEmbed.setColor(bot.getBotColor(discordSender.getGuild()));
                if (!post.isPresent()) {

                    hentaiEmbed.setTitle("Error");
                    hentaiEmbed.setDescription("There where no posts found, sorry :(");

                    discordSender.sendMessage(hentaiEmbed.build());
                    return;
                }


                hentaiEmbed.setAuthor("Posted by /u/" + post.get().data.author, "https://reddit.com/u/" + post.get().data.author);
                hentaiEmbed.setTitle(post.get().data.title, "https://reddit.com" + post.get().data.permalink);
                hentaiEmbed.setImage(post.get().data.url);
                hentaiEmbed.setFooter("Post from " + post.get().data.subRedditName + " | \uD83D\uDC4D " + post.get().data.score);


                discordSender.sendMessage(hentaiEmbed.build());
            }
        }

    }

    @Override
    public String getName() {
        return "hentai";
    }

    @Override
    public String getDescription() {
        return "Shows images from Reddit's subreddit r/hentai.";
    }

    @Override
    public String getUsage() {
        return "hentai";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("h");
    }

    @Override
    public boolean isOwnerCommand() {
        return false;
    }
}
