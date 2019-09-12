package dev.omarathon.redditapi.connect;

import net.dean.jraw.RedditClient;

public interface ConnectHandler {
    void onConnect(RedditClient client);
}
