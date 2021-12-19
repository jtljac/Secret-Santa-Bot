package com.datdeveloper;

import com.datdeveloper.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandListener extends ListenerAdapter {
    final HashMap<String, Command> commands;

    public CommandListener(HashMap<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        if (Bot.DEBUG) {
            event.getJDA().getGuilds().forEach(guild -> {
                for (String key : commands.keySet()) {
                    guild.upsertCommand(commands.get(key).getCommandData()).queue();
                }
            });
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);

        if (Bot.DEBUG) {
            for (String key : commands.keySet()) {
                event.getGuild().upsertCommand(commands.get(key).getCommandData()).queue();
            }
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);
        if (!commands.containsKey(event.getName())) {
            event.reply("Unknown command").queue();
            return;
        }

        Command command = commands.get(event.getName());

        int userPermissionLevel = getMemberPermissionLevel(event.getMember());

        if (userPermissionLevel < command.getPermissionLevel()) {
            event.reply("You don't have permission to do that").setEphemeral(true).queue();
            return;
        }

        if (!command.execute(event)) {
            event.reply("Failed to execute command").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        super.onButtonClick(event);
        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(event.getGuild());

        switch (event.getComponentId()) {
            case "Optin":
                if (guildStore.partakers.contains(event.getMember().getId())) {
                    event.reply("You cannot opt in more than once").setEphemeral(true).queue();
                    break;
                }

                guildStore.partakers.add(event.getMember().getId());
                DataStore.INSTANCE.saveDataStore();
                event.reply("Successfully opted in").setEphemeral(true).queue();
                break;
            case "Optout":
                if (!guildStore.partakers.contains(event.getMember().getId())) {
                    event.reply("You cannot opt-out without having first opted-in").setEphemeral(true).queue();
                    break;
                }

                guildStore.partakers.remove(event.getMember().getId());
                DataStore.INSTANCE.saveDataStore();
                event.reply("Successfully opted out").setEphemeral(true).queue();
                break;
        }
    }

    private int getMemberPermissionLevel(Member user) {
        if (user.hasPermission(Permission.ADMINISTRATOR)) return 2;

        return 0;
    }
}
