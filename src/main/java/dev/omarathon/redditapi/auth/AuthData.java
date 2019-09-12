package dev.omarathon.redditapi.auth;

public class AuthData {
    private UserAgentData userAgentData;
    private CredentialsData credentialsData;

    public AuthData(UserAgentData userAgentData, CredentialsData credentialsData) {
        this.userAgentData = userAgentData;
        this.credentialsData = credentialsData;
    }

    public UserAgentData getUserAgentData() {
        return userAgentData;
    }

    public CredentialsData getCredentialsData() {
        return credentialsData;
    }
}
