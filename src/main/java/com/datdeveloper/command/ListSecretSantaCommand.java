package com.datdeveloper.command;

import com.datdeveloper.DataStore;
import com.datdeveloper.GuildStore;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ListSecretSantaCommand extends BaseCommand {
    public ListSecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(event.getGuild());
        StringBuilder builder = new StringBuilder();
        builder.append("The members who have opted into the secret santa on this server are:");
        for (String memberId : guildStore.partakers) {
            Member member = event.getGuild().getMemberById(memberId);
            builder.append("\n").append(member.getEffectiveName())
                    .append(":    ").append(member.getUser().getName()).append("#").append(member.getUser().getDiscriminator());
        }

        event.reply(builder.toString()).setEphemeral(true).queue();
        return true;
    }
}
