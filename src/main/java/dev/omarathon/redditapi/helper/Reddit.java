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
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

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

        ProxySelector proxySelector = ProxySelector.getDefault() == null ? new NullProxySelector() : ProxySelector.getDefault();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().proxySelector(proxySelector).build();

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent, okHttpClient);

        return OAuthHelper.automatic(adapter, credentials);
    }

    private static class NullProxySelector extends ProxySelector {
        @Override
        public List<Proxy> select(URI uri) {
            return Collections.singletonList(Proxy.NO_PROXY);
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            // pass
        }
    }
}
