package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import com.datdeveloper.GuildStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.datdeveloper.Bot.logger;

/**
 * A command that kicks off the secret santa gifting by assigning who each person whom has opted in is gifting
 */
public class StartSecretSantaCommand extends BaseCommand{
    public StartSecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) return false;

        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(guild);

        // Create gift mappings with validation
        Map<String, String> gifts = getGiftMappings(
                guildStore.partakers.stream().filter(id -> guild.getMemberById(id) != null).collect(Collectors.toList())
        );

        // Send messages
        for (Map.Entry<String, String> entry : gifts.entrySet()) {
            guild.getMemberById(entry.getKey()).getUser().openPrivateChannel().flatMap(privateChannel -> {
                Member target = guild.getMemberById(entry.getValue());
                String message = String.format("The secret santa for %s has begun! \n" +
                        "You're buying a gift for: %s\n" +
                        "They're full username is: %s",
                        guild.getName(),
                        target.getEffectiveName(),
                        target.getUser().getName());
                return privateChannel.sendMessage(message);
            }).queue();
        }

        // Write out targets for debug
        try (FileWriter fileWriter = new FileWriter(guild.getName() + "-SecretSantaTargets.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(gifts, fileWriter);
        } catch (IOException e) {
            logger.warn("Failed to write targets file for {}", guild.getName());
            logger.warn("Error:", e);
        }

        event.reply("Successfully sent PMs").setEphemeral(true).queue();
        return true;
    }

    /**
     * Get a map assigning gifters to giftees
     * @implNote This shuffles the list of users (producing a copy, not modifying the source list), then assigns each
     *           user the person after them in the new list, wrapping round for the final user.
     * @param partakers A list of the Users partaking, each element should be a user ID
     * @return A map, mapping gifters to their gifting targets
     */
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
