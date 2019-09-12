package dev.omarathon.redditapi.connect;

import net.dean.jraw.RedditClient;

public abstract class InitialisingConnectHandler implements ConnectHandler {
    private boolean firstTimeConnected;

    public InitialisingConnectHandler() {
        firstTimeConnected = false;
    }

    public void onConnect(RedditClient redditClient) {
        if (!firstTimeConnected) {
            onFirstConnect(redditClient);
            firstTimeConnected = true;
        }
        else {
            onNonFirstConnect(redditClient);
        }
    }

    public abstract void onFirstConnect(RedditClient redditClient);
    public abstract void onNonFirstConnect(RedditClient redditClient);
}
