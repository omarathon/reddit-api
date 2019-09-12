package dev.omarathon.redditapi.auth;

public class CredentialsData {
    private String username;
    private String password;
    private String clientID;
    private String clientSecret;

    public CredentialsData(String username, String password, String clientID, String clientSecret) {
        this.username = username;
        this.password = password;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
