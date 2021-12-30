package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import com.datdeveloper.GuildStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StartSecretSantaCommand extends BaseCommand{
    private static final Random random = new Random();

    public StartSecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(event.getGuild());

        // Validate people
        guildStore.partakers = guildStore.partakers.stream().filter(id -> event.getGuild().getMemberById(id) != null).collect(Collectors.toList());

        Map<String, String> gifts = getGiftMappings(guildStore.partakers);

        for (String key : gifts.keySet()) {
            event.getGuild().getMemberById(key).getUser().openPrivateChannel().flatMap(privateChannel -> {
                String message = "The secret santa for " + event.getGuild().getName() + " has begun! \n" +
                        "You're buying a gift for: " + event.getGuild().getMemberById(gifts.get(key)).getEffectiveName() + "\n" +
                        "They're full username is: " + event.getGuild().getMemberById(gifts.get(key)).getUser().getName() + "#" + event.getGuild().getMemberById(gifts.get(key)).getUser().getDiscriminator();
                return privateChannel.sendMessage(message);
            }).queue();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Targets:");
        for (String person : gifts.keySet()) {
            builder.append("\n").append(person).append(":    ").append(gifts.get(person));
        }

        try (FileWriter fileWriter = new FileWriter("SecretSantaTargets.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(gifts, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.reply("Successfully sent PMs").setEphemeral(true).queue();

        return true;
    }

    private Map<String, String> getGiftMappings(List<String> partakers) {
        List<String> users = new ArrayList<>(partakers);
        Map<String, String> gifts = new HashMap<>();

        Collections.shuffle(users);

        for (int i = 0; i < users.size(); i++) {
            gifts.put(users.get(i), users.get((i + 1) % users.size()));
        }

        return gifts;
    }
}
