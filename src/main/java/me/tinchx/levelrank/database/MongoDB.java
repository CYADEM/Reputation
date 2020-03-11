package me.tinchx.levelrank.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.tinchx.levelrank.LevelPlugin;
import me.tinchx.levelrank.config.BasicConfigurationFile;

@Getter
public class MongoDB {

    private BasicConfigurationFile config = LevelPlugin.getInstance().getSettings();

    @Getter
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    public void connect() {
        String path = "DATABASE.MONGODB.";
        if (config.getBoolean(path + "AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(config.getString(path + "HOST"),
                    config.getInteger(path + "PORT"));

            path += "AUTHENTICATION.";

            MongoCredential mongoCredential = MongoCredential.createCredential(
                    config.getString(path + "USERNAME"),
                    config.getString(path + "DATABASE"),
                    config.getString(path + "PASSWORD").toCharArray()
            );
            mongoClient = new MongoClient(serverAddress, mongoCredential, MongoClientOptions.builder().build());

            mongoDatabase = mongoClient.getDatabase(config.getString("mongo.database"));
        } else {
            mongoClient = new MongoClient(
                    config.getString(path + "HOST"),
                    config.getInteger(path + "HOST")
            );
            mongoDatabase = mongoClient.getDatabase(config.getString(path + "DATABASE"));
        }

    }

    public void close() {
        mongoClient.close();
    }

}
