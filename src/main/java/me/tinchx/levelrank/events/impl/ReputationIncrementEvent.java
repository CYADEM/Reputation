package me.tinchx.levelrank.events.impl;

import lombok.Getter;
import me.tinchx.levelrank.LevelPlugin;
import me.tinchx.levelrank.events.PlayerBase;
import me.tinchx.levelrank.profile.Profile;
import org.bukkit.OfflinePlayer;

@Getter
public class ReputationIncrementEvent extends PlayerBase {

    private String type;
    private int from, to;

    public ReputationIncrementEvent(OfflinePlayer player, String type) {
        super(player);

        Profile profile = Profile.get(player);

        if (profile == null) {
            profile = new Profile(player.getUniqueId());
        }

        this.type = type.toUpperCase();
        this.from = (int) profile.getReputation(true);
        this.to = profile.increment(LevelPlugin.getInstance().getSettings().getInteger("INCREMENT-REPUTATION-BY." + this.type));
    }
}