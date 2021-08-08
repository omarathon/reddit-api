package dev.omarathon.redditapi;

import dev.omarathon.redditapi.commands.Commands;
import dev.omarathon.redditapi.connect.ConnectHandler;
import dev.omarathon.redditapi.helper.Config;
import dev.omarathon.redditapi.helper.Reddit;
import net.dean.jraw.RedditClient;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public final class RedditAPI extends JavaPlugin {
    private static RedditAPI instance;
    private Set<ConnectHandler> connectHandlers = new HashSet<>();
    private static Logger logger;
    private RedditClient redditClient = null;

    public RedditAPI() {
    }

    @Override
    public void onEnable() {
        if (instance == null) instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();

        logger = getLogger();

        logger.info("Started RedditAPI!");

        logger.info("Attempting to connect the bot...");
        connectIfAllowed();

        getCommand("redditapi").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        logger.info("RedditAPI stopped!");
    }

    public static RedditAPI getInstance() {
        return instance;
    }

    public void connectIfAllowed() {
        if (Config.canConnect()) {
            logger.info("Allowed to connect, connecting...");
            RedditClient redditClient = connect();
            if (redditClient == null) {
                logger.info("Failed to connect, despite being allowed!");
            }
            else {
                logger.info("Successfully connected!");
                this.redditClient = redditClient;
                callConnectHandlers();
            }
        }
        else logger.info("Not allowed to connect!");
    }

    private void callConnectHandlers() {
        logger.info("Calling " + connectHandlers.size() + " connectHandlers");
        for (ConnectHandler connectHandler : connectHandlers) {
            connectHandler.onConnect(redditClient);
        }
    }

    private RedditClient connect() {
        logger.info("Attempting to obtain RedditClient...");
        RedditClient redditClient = Reddit.connect(Config.unpack());
        logger.info("Successfully obtained RedditClient!");
        boolean success = testConnection(redditClient);
        if (success) {
            logger.info("[SUCCESS] Connection successfully established!");
            return redditClient;
        }
        logger.info("[FAILURE] Connection failed to be established!");
        return null;
    }

    private boolean testConnection(RedditClient redditClient) {
        logger.info("Testing connection...");
        if (redditClient == null) return false;
        return redditClient.me().getUsername().equals(Config.getAccountUsername());
    }

    public void registerConnectHandler(ConnectHandler connectHandler) {
        connectHandlers.add(connectHandler);
        if (redditClient != null) {
            logger.info("Connected regsitered connectHandler");
            connectHandler.onConnect(redditClient);
        }
    }
}
