package com.datdeveloper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataStore {
    public static final DataStore INSTANCE = new DataStore();
    protected Map<String, GuildStore> guildStores = new HashMap<>();
    File store = new File("./SecretSantaData.json");

    private static final Logger logger = LoggerFactory.getLogger(DataStore.class);

    private DataStore() {
        if (store.exists() && store.isFile()) {
            loadDataStore();
        }
    }

    public GuildStore getGuildStore(Guild guild) {
        return getGuildStore(guild.getId());
    }

    public GuildStore getGuildStore(String guildId) {
        GuildStore store;
        if (guildStores.containsKey(guildId)) {
            store = guildStores.get(guildId);
        } else {
            store = new GuildStore();
            guildStores.put(guildId, store);
        }

        return store;
    }

    public void saveDataStore() {
        try (FileWriter fileWriter = new FileWriter(store)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(guildStores, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataStore() {
        try(FileInputStream stream = new FileInputStream(store);
                InputStreamReader reader = new InputStreamReader(stream)) {
            Gson gson = new Gson();
            guildStores = gson.fromJson(reader, new TypeToken<Map<String, GuildStore>>() {}.getType());
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            logger.error("Failed to read stored config, config file may be corrupted or inaccessable");
        }
    }
}

