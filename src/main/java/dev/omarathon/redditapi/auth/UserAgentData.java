package dev.omarathon.redditapi.auth;

public class UserAgentData {
    private String platform;
    private String uid;
    private String version;
    private String username;

    public UserAgentData(String platform, String uid, String version, String username) {
        this.platform = platform;
        this.uid = uid;
        this.version = version;
        this.username = username;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUID() {
        return uid;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }
}
