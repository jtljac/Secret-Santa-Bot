package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import com.datdeveloper.GuildStore;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * A command to list the current people who have opted in on the server
 */
public class ListSecretSantaCommand extends BaseCommand {
    public ListSecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) return false;

        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(guild);
        StringBuilder builder = new StringBuilder();
        builder.append("The members who have opted into the secret santa on this server are:");
        for (String memberId : guildStore.partakers) {
            Member member = guild.getMemberById(memberId);
            builder.append("\n")
                    .append(String.format("%s:    %s", member.getEffectiveName(), member.getUser().getName()));
        }

        event.reply(builder.toString()).setEphemeral(true).queue();
        return true;
    }
}
