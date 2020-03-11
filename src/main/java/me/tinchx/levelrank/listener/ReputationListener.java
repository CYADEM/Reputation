package me.tinchx.levelrank.listener;

import me.tinchx.levelrank.LevelPlugin;
import me.tinchx.levelrank.config.BasicConfigurationFile;
import me.tinchx.levelrank.events.impl.ReputationDecrementEvent;
import me.tinchx.levelrank.events.impl.ReputationIncrementEvent;
import me.tinchx.levelrank.profile.Profile;
import me.tinchx.levelrank.utils.Checker;
import me.tinchx.levelrank.utils.ColorText;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ReputationListener implements Listener {

    private LevelPlugin plugin = LevelPlugin.getInstance();
    private BasicConfigurationFile file = plugin.getSettings(), messages = plugin.getMessages();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity(), killer = player.getKiller();

        new ReputationDecrementEvent(player, "DEATH").call();

        new Checker(killer != null, () -> new ReputationIncrementEvent(killer, "KILL").call());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player);

        new Checker((int) profile.getReputation(true) > 0 && profile.getReputation(false) != null, () -> event.setFormat(ColorText.translate(profile.getReputation(false) + "&r") + event.getFormat()));
    }

    @EventHandler
    public void onReputationIncrement(ReputationIncrementEvent event) {
        if (!event.getPlayer().isOnline()) {
            return;
        }
        Player player = (Player) event.getPlayer();

        new Checker(!event.isCancelled(), () -> {
            if (event.getTo() > 0) {

                if (file.getBoolean("REPUTATION-SETTINGS.INCREMENTED-MESSAGE") && messages.contains("INCREMENTED-MESSAGE")) {
                    String message = messages.getString("INCREMENTED-MESSAGE");

                    message = message.replace("{FROM}", String.valueOf(event.getFrom()));
                    message = message.replace("{TO}", String.valueOf(event.getTo()));

                    player.sendMessage(message);
                }
            }
        });
    }

    @EventHandler
    public void onReputationDecrement(ReputationDecrementEvent event) {
        if (!event.getPlayer().isOnline()) {
            return;
        }
        Player player = (Player) event.getPlayer();

        new Checker(!event.isCancelled(), () -> {
            if (event.getTo() > 0) {

                if (file.getBoolean("REPUTATION-SETTINGS.DECREMENTED-MESSAGE") && messages.contains("DECREMENTED-MESSAGE")) {
                    String message = messages.getString("DECREMENTED-MESSAGE");

                    message = message.replace("{FROM}", String.valueOf(event.getFrom()));
                    message = message.replace("{TO}", String.valueOf(event.getTo()));

                    player.sendMessage(message);
                }
            }
        });
    }
}