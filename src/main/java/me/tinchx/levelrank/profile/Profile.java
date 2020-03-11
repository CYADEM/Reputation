package me.tinchx.levelrank.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import me.tinchx.levelrank.LevelPlugin;
import me.tinchx.levelrank.utils.Checker;
import org.bson.Document;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile {

    private static Map<UUID, Profile> profileMap = new HashMap<>();

    private UUID uuid;
    private int reputation;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.reputation = LevelPlugin.getInstance().getSettings().getInteger("DEFAULT-REPUTATION");

        load();
        profileMap.put(uuid, this);
    }

    public Object getReputation(boolean integer) {
        if (!integer) {
            for (String string : LevelPlugin.getInstance().getSettings().getStringList("REPUTATION-VALUES-COLOR")) {
                String[] args = string.split(";");
                try {
                    if (reputation <= Integer.valueOf(args[0])) {
                        return args[1].replace("{REPUTATION}", String.valueOf(reputation));
                    }
                } catch (Exception ignored) {
                }
            }
            return null;
        }
        return reputation;
    }

    public int increment(int reputation) {
        this.reputation += reputation;
        return this.reputation;
    }

    public int decrement(int reputation) {
        if ((this.reputation - reputation) <= 0) {
            reputation = 0;
        }
        this.reputation -= reputation;
        return this.reputation;
    }

    private void load() {
        Document document = LevelPlugin.getInstance().getMongoDB().getMongoDatabase().getCollection("profiles").find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            new Checker(document.containsKey("reputation"), () -> reputation = document.getInteger("reputation"));
        }
    }

    void save() {
        Document document = new Document();

        document.put("uuid", uuid.toString());
        document.put("reputation", reputation);

        LevelPlugin.getInstance().getMongoDB().getMongoDatabase().getCollection("profiles").replaceOne(Filters.eq("uuid", uuid.toString()), document, new UpdateOptions().upsert(true));
    }

    public static Profile get(OfflinePlayer player) {
        return profileMap.get(player.getUniqueId());
    }
}