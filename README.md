[![Latest Release](https://img.shields.io/github/release/omarathon/reddit-api.svg)](https://github.com/omarathon/reddit-api/releases/latest) [![](https://jitpack.io/v/omarathon/reddit-api.svg)](https://jitpack.io/#omarathon/reddit-api)

# RedditAPI

A very light Minecraft Server plugin that provides a reddit API interface.

Essentially, it allows a plugin to obtain data from reddit via a registered bot, through the [**J**ava **R**eddit **A**PI **W**rapper](https://github.com/mattbdean/JRAW).

The main class for the plugin is [RedditAPI](src/main/java/dev/omarathon/redditapi/RedditAPI.java).

## Usage

1. Import the plugin JAR to your project.

3. Create a reddit OAuth2 app [here](https://www.reddit.com/prefs/apps). Note the app type is a **script**, and is intended to be specific to your Minecraft server.

5. Run the plugin to generate the ``config.yml.``.

4. Fill out the fields in the ``config.yml`` to allow the plugin access to your bot.
    - The details for the UserAgent must be provided in the ``userAgent`` section. See [this section](https://mattbdean.gitbooks.io/jraw/quickstart.html#choose-a-user-agent) in the JRAW Wiki for more information on choosing good UserAgent details. Since RedditAPI bots are supposed to be server-specific, the following settings could be used:
    
    ```yaml
    platform: 'bot'
    uid: 'com.<server name>.serverbot'
    version: 'v1.0'
    username: '<your personal reddit username>'
    ```
    
    - The second section of details are to allow JRAW to authenticate as the OAuth2 app. See [this section](https://mattbdean.gitbooks.io/jraw/quickstart.html#create-a-reddit-oauth2-app) in the JRAW Wiki for more information on these details. Essentially, ``username`` and ``password`` are the username and password to the reddit account which has the registered OAuth2 app (it's recommended to make a new account with it on), and the ``clientID`` and ``clientSecret`` may be found from your OAuth2 app page, shown on the following screenshot from the JRAW Wiki:
    
    ![OAuth2details](https://i.imgur.com/ILMeklr.png).
    
    - Once you have filled in the above details, change ``connect`` to ``true`` to connect on load/reload of RedditAPI, as well as from the **/redditapi connect** command.
    
5. Run the plugin, or call **/redditapi connect**, for the plugin to attempt to obtain a working [RedditClient](https://javadoc.jitpack.io/com/github/mattbdean/JRAW/v1.1.0/javadoc/net/dean/jraw/RedditClient.html). 
Once obtained, all regsitered [ConnectHandler](src/main/java/dev/omarathon/redditapi/connect/ConnectHandler.java)s will be called, with the working RedditClient supplied.

### API Usage

To obtain a RedditClient instance in your plugin, you will need to do the initialisation steps as above. 
Then, you will need to construct a ConnectHandler, which will handle the output valid RedditClient from RedditAPI.
Register this handler, from when you would like to begin waiting for a RedditClient, and you'll receieve a RedditClient as soon as RedditAPI obtains a valid one!

**NOTE**: You **must** add ``RedditAPI`` as a dependency in your ``plugin.yml``, like so:

```yml
depend:
  - RedditAPI
```

## Example

### Unsafe Usage

If one requires a RedditClient such that the requests may be cancelled if a connection doesn't exist, they may store it in a static field.

It will be null if no connection has been established, otherwise it will be a valid connection.

The ConnectHandler sets field, which may be static if you only require a central connection.

When using this RedditClient, one must firstly check whether it's null or not.

```java
public final class RedditClass {
    private static RedditClient redditClient = null;
    
    static {
        RedditAPI.registerConnectHandler((RedditClient redditClient) -> {
            RedditClass.redditClient = redditClient;
        });
    }

    // example usage of RedditClient. returns null if the RedditClient is null,
    // because cannot determine the result.
    private Boolean userExists(String username) {
        if (redditClient == null) {
            return null;
        }
        return redditClient.user(username).query().getStatus() == AccountStatus.EXISTS;
    }
}
```

### Safe Usage

In order to use the RedditClient "safely", all I/O that may trigger a use of the RedditClient must be locked until it's set.

Since usually I/O is enabled in the ``onEnable`` method, one may instead enable it within a registered ConnectHandler.

However, if we put the entire old ``onEnable`` in a ConnectHandler, then every time RedditAPI obtains a new RedditClient the entire plugin will essentially be reloaded.

To resolve this, supplied is an [InitialisingConnectHandler](src/main/java/dev/omarathon/redditapi/connect/InitialisingConnectHandler.java). One must specify the ``onFirstConnect`` and ``onNonFirstConnect`` handler methods, which are both passed the RedditClient.

In the ``onFirstConnect`` handler one may setup their entire plugin as before, however in the ``onNonFirstConnect`` it may not be neccessary to re-setup the plugin, and rather to just update the RedditClient instance, since the plugin was set-up already in the ``onFirstConnect`` handler.

Below is an example of a plugin that safely uses a ``RedditClient``:

```java
public final class ExamplePlugin extends JavaPlugin {
    private static ExamplePlugin instance;
    private RedditClient redditClient;

    public ExamplePlugin() {
        instance = this;
    }

    public static ExamplePlugin getInstance() {
        return instance;
    }

    public void setRedditClient(RedditClient redditClient) {
        this.redditClient = redditClient;
    }

    @Override
    public void onEnable() {
        RedditAPI.registerConnectHandler(new InitialisingConnectHandler() {
            @Override
            public void onFirstConnect(RedditClient redditClient) {
                onNonFirstConnect(redditClient);
                // rest of onEnable logic...
                // (no I/O involving RedditClient is to be enabled outside of here!!)
            }

            @Override
            public void onNonFirstConnect(RedditClient redditClient) {
                ExamplePlugin.getInstance().setRedditClient(redditClient);
            }
        });
    }

    // example use of redditClient, where the I/O to call this method
    // was only set-ip within the ConnectHandler
    private boolean userExists(String username) {
        return redditClient.user(username).query().getStatus() == AccountStatus.EXISTS;
    }
}
```

In both the ``firstConnect`` and ``nonFirstConnect`` handlers, we update the RedditClient instance, however we only do the original plugin ``onEnable`` in the ``firstConnect`` handler.

## Commands / Permissions

The central command is **/redditapi**, with an alias of **/ra**. It requires the ``redditapi`` permission node (but it's advised that this node is reserved for admins). Called without children, it shows help.

It has a single child: **connect**, with an alias of **con**. This command will make RedditAPI reload the config and attempt to obtain a new RedditClient from the details provided within. It's essentially equivalent to reloading the plugin.

The ``redditapi`` permission node gives a player access to the help message from **/redditapi**, and the **/redditapi connect** command.

## Disclaimer

I take no liability for any of the actions performed, or the security of, any of the authenticated reddit accounts or bots that the plugin may interface with.
