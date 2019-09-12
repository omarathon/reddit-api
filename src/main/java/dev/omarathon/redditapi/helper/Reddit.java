package dev.omarathon.redditapi.helper;

import dev.omarathon.redditapi.auth.AuthData;
import dev.omarathon.redditapi.auth.CredentialsData;
import dev.omarathon.redditapi.auth.UserAgentData;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

public class Reddit {
    public static RedditClient connect(AuthData auth) {
        UserAgentData userAgentData = auth.getUserAgentData();
        CredentialsData credentialsData = auth.getCredentialsData();

        UserAgent userAgent = new UserAgent(
                userAgentData.getPlatform(),
                userAgentData.getUID(),
                userAgentData.getVersion(),
                userAgentData.getUsername()
        );

        Credentials credentials = Credentials.script(
                credentialsData.getUsername(),
                credentialsData.getPassword(),
                credentialsData.getClientID(),
                credentialsData.getClientSecret()
        );

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

        return OAuthHelper.automatic(adapter, credentials);
    }
}
