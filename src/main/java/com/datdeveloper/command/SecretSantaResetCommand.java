package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;

public class SecretSantaResetCommand extends BaseCommand {
    public SecretSantaResetCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        DataStore.INSTANCE.getGuildStore(event.getGuild()).partakers = new ArrayList<>();
        DataStore.INSTANCE.saveDataStore();
        event.reply("Cleared the current opt-ins").setEphemeral(true).queue();
        return true;
    }
}
