package dev.omarathon.redditapi.helper;

import dev.omarathon.redditapi.RedditAPI;
import dev.omarathon.redditapi.auth.AuthData;
import dev.omarathon.redditapi.auth.CredentialsData;
import dev.omarathon.redditapi.auth.UserAgentData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static AuthData unpack() {
        FileConfiguration config = RedditAPI.getInstance().getConfig();
        return new AuthData(
                unpackUserAgent(config.getConfigurationSection("userAgent")),
                unpackCredentials(config.getConfigurationSection("credentials"))
        );
    }

    private static UserAgentData unpackUserAgent(ConfigurationSection userAgentSection) {
        return new UserAgentData(
                userAgentSection.getString("platform"),
                userAgentSection.getString("uid"),
                userAgentSection.getString("version"),
                userAgentSection.getString("username")
        );
    }

    private static CredentialsData unpackCredentials(ConfigurationSection credentialsSection) {
        return new CredentialsData(
                credentialsSection.getString("username"),
                credentialsSection.getString("password"),
                credentialsSection.getString("clientID"),
                credentialsSection.getString("clientSecret")
        );
    }

    public static boolean canConnect() {
        FileConfiguration config = RedditAPI.getInstance().getConfig();
        return config.getBoolean("connect");
    }

    public static String getAccountUsername() {
        FileConfiguration config = RedditAPI.getInstance().getConfig();
        return config.getString("credentials.username");
    }
}
