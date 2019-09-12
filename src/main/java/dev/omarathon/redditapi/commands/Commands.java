package dev.omarathon.redditapi.commands;

import dev.omarathon.redditapi.RedditAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.sendMessage(ChatColor.RED + "[RedditAPI] " + ChatColor.WHITE + "/[redditapi | ra] [connect | con] - Attempt to connect the bot.");
            }
            else {
                RedditAPI.getInstance().getLogger().info("/[redditapi | ra] [connect | con] - Attempt to connect the bot.");
            }
        }
        else if (Arrays.asList("connect", "con").contains(args[0])) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.sendMessage(ChatColor.RED + "[RedditAPI] " + ChatColor.WHITE + "Attempting to connect, see console for further information!");
            }
            RedditAPI.getInstance().connectIfAllowed();
        }
        return true;
    }
}
