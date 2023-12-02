package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;

/**
 * A command to reset the Secret Santa by clearing all current opt ins for the guild
 */
public class SecretSantaResetCommand extends BaseCommand {
    public SecretSantaResetCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) return false;

        DataStore.INSTANCE.getGuildStore(guild).partakers = new ArrayList<>();
        DataStore.INSTANCE.saveDataStore();
        event.reply("Cleared the current opt-ins").setEphemeral(true).queue();
        return true;
    }
}
