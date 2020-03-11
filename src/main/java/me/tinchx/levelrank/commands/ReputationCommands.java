package me.tinchx.levelrank.commands;

import me.tinchx.levelrank.LevelPlugin;
import me.tinchx.levelrank.config.BasicConfigurationFile;
import me.tinchx.levelrank.events.impl.ReputationDecrementEvent;
import me.tinchx.levelrank.events.impl.ReputationIncrementEvent;
import me.tinchx.levelrank.utils.ColorText;
import me.tinchx.levelrank.utils.command.Command;
import me.tinchx.levelrank.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class ReputationCommands {

    private LevelPlugin plugin = LevelPlugin.getInstance();
    private BasicConfigurationFile messages = plugin.getMessages(), settings = plugin.getSettings();

    @Command(name = "reputation", aliases = {"reputations"}, permission = "bukkit.command.reputation")
    public void help(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();

        sender.sendMessage(ColorText.translate("&cUsage: /reputation <increment/decrement> <playerName> <reputationType>"));

        sender.sendMessage(ColorText.translate("&eIncrement reputation types:"));
        for (String string : settings.getConfiguration().getConfigurationSection("INCREMENT-REPUTATION-BY").getKeys(false)) {
            sender.sendMessage(ColorText.translate("&7- &c" + string.toUpperCase()));
        }
        sender.sendMessage(" ");
        sender.sendMessage(ColorText.translate("&eDecrement reputation types:"));
        for (String string : settings.getConfiguration().getConfigurationSection("DECREMENT-REPUTATION-BY").getKeys(false)) {
            sender.sendMessage(ColorText.translate("&7- &c" + string.toUpperCase()));
        }
    }

    @Command(name = "reputation.increment", aliases = {"reputations.increment"}, permission = "bukkit.command.reputation")
    public void increment(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /reputation increment <playerName> <reputationType>"));
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            if ((!player.hasPlayedBefore()) && (!player.isOnline())) {
                sender.sendMessage(messages.getString("PLAYER-NEVER-PLAYED").replace("{PLAYERNAME}", args[0]));
                return;
            }

            String reputationName = args[1].toUpperCase();

            if (!settings.contains("INCREMENT-REPUTATION-BY." + reputationName)) {
                sender.sendMessage(messages.getString("REPUTATION-TYPE-NULL").replace("{REPUTATION}", reputationName));
                return;
            }

            ReputationIncrementEvent event = new ReputationIncrementEvent(player, reputationName);

            if (!event.call()) {
                sender.sendMessage(messages.getString("REPUTATION-EVENT-CANCELLED"));
                return;
            }

            String incrementedMessage = messages.getString("PLAYER-REPUTATION-INCREMENTED");

            incrementedMessage = incrementedMessage.replace("{PLAYERNAME}", player.getName());
            incrementedMessage = incrementedMessage.replace("{REPUTATION}", reputationName);
            incrementedMessage = incrementedMessage.replace("{FROMREP}", String.valueOf(event.getFrom()));
            incrementedMessage = incrementedMessage.replace("{TOREP}", String.valueOf(event.getTo()));

            sender.sendMessage(incrementedMessage);
        }
    }

    @Command(name = "reputation.decrement", aliases = {"reputations.decrement"}, permission = "bukkit.command.reputation")
    public void decrement(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /reputation decrement <playerName> <reputationType>"));
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            if ((!player.hasPlayedBefore()) && (!player.isOnline())) {
                sender.sendMessage(messages.getString("PLAYER-NEVER-PLAYED").replace("{PLAYERNAME}", args[0]));
                return;
            }

            String reputationName = args[1].toUpperCase();

            if (!settings.contains("DECREMENT-REPUTATION-BY." + reputationName)) {
                sender.sendMessage(messages.getString("REPUTATION-TYPE-NULL").replace("{REPUTATION}", reputationName));
                return;
            }

            ReputationDecrementEvent event = new ReputationDecrementEvent(player, reputationName);

            if (!event.call()) {
                sender.sendMessage(messages.getString("REPUTATION-EVENT-CANCELLED"));
                return;
            }

            String decrementedMessage = messages.getString("PLAYER-REPUTATION-DECREMENTED");

            decrementedMessage = decrementedMessage.replace("{PLAYERNAME}", player.getName());
            decrementedMessage = decrementedMessage.replace("{REPUTATION}", reputationName);
            decrementedMessage = decrementedMessage.replace("{FROMREP}", String.valueOf(event.getFrom()));
            decrementedMessage = decrementedMessage.replace("{TOREP}", String.valueOf(event.getTo()));

            sender.sendMessage(decrementedMessage);
        }
    }
}