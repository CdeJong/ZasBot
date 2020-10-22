package com.cdejong.reddit;

import com.cdejong.Bot;
import com.cdejong.rest.RedditResponse;
import com.cdejong.rest.RestService;
import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class RedditManager {

    private Bot bot;
    private String subReddit;
    private HashMap<String, List<String>> showedPosts = new HashMap<>();
    private HashMap<String, Long> lastReqeusts = new HashMap<>();
    private HashMap<String, List<RedditResponse.Post>> posts = new HashMap<>();
    private Random random = new Random();

    private static final int MAX_CACHED_SHOWED_POSTS = 50;


    public RedditManager(Bot bot, String subReddit) {
        this.bot = bot;
        this.subReddit = subReddit;
        List<RedditResponse.Post> posts = getPosts();
        bot.getJda().getGuilds().forEach(g -> this.posts.put(g.getId(), posts));
        bot.getJda().getGuilds().forEach(g -> lastReqeusts.put(g.getId(), Instant.now().getEpochSecond()));
        bot.getJda().getGuilds().forEach(g -> showedPosts.put(g.getId(), new ArrayList<>()));
    }

    private List<RedditResponse.Post> getPosts() {
        Optional<RedditResponse> response = Optional.empty();
        try {
            response = RestService.getRedditResponse("https://www.reddit.com/r/" + subReddit + "/hot/.json?&limit=100");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.map(redditResponse -> redditResponse.data.children.stream().filter(p -> p.data.url.endsWith(".jpg") || p.data.url.endsWith(".png")).collect(Collectors.toList())).orElse(Collections.emptyList());

    }

    public Optional<RedditResponse.Post> getNextPost(Guild guild) {

        if (lastReqeusts.get(guild.getId()) < Instant.now().getEpochSecond() - 300) {
            posts.put(guild.getId(), getPosts());
        }
        lastReqeusts.put(guild.getId(), Instant.now().getEpochSecond());

        List<RedditResponse.Post> posts = this.posts.get(guild.getId());
        posts = posts.stream().filter(p -> !showedPosts.get(guild.getId()).contains(p.data.id)).collect(Collectors.toList());

        if (posts.size() < 1) {
            return Optional.empty();
        }

        RedditResponse.Post post = posts.get(random.nextInt(posts.size()));

        List<String> showed = showedPosts.get(guild.getId());
        showed.add(post.data.id);

        if (showed.size() > MAX_CACHED_SHOWED_POSTS) {
            showed.remove(0);
        }

        showedPosts.put(guild.getId(), showed);
        return Optional.of(post);

    }

}
