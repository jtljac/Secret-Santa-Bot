package com.datdeveloper;

import com.datdeveloper.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {
    final Map<String, Command> commands;

    public static final Logger logger = LoggerFactory.getLogger(CommandListener.class);

    public CommandListener(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        event.getJDA().updateCommands()
                .addCommands(commands.values().stream().map(Command::getCommandData).collect(Collectors.toList()))
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        if (!commands.containsKey(event.getName())) {
            event.reply("Unknown command").setEphemeral(true).queue();
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

        logger.info("{} just executed {} on {}", event.getMember().getEffectiveName(), command.getCommandName(), event.getGuild().getName());
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);
        GuildStore guildStore = DataStore.INSTANCE.getGuildStore(event.getGuild());

        Member caller = event.getMember();

        if (caller == null) return;

        switch (event.getComponentId()) {
            case "Optin":
                if (guildStore.partakers.contains(caller.getId())) {
                    event.reply("You cannot opt in more than once").setEphemeral(true).queue();
                    break;
                }

                guildStore.partakers.add(caller.getId());
                DataStore.INSTANCE.saveDataStore();
                event.reply("Successfully opted in").setEphemeral(true).queue();
                logger.info("{} just opted in on {}",
                        caller.getEffectiveName(), event.getGuild().getName());
                break;
            case "Optout":
                if (!guildStore.partakers.contains(caller.getId())) {
                    event.reply("You cannot opt-out without having first opted-in").setEphemeral(true).queue();
                    break;
                }

                guildStore.partakers.remove(caller.getId());
                DataStore.INSTANCE.saveDataStore();
                event.reply("Successfully opted out").setEphemeral(true).queue();
                logger.info(caller.getEffectiveName() + " just opted out on " + event.getGuild().getName());
                break;
        }
    }

    private int getMemberPermissionLevel(Member user) {
        if (user.hasPermission(Permission.ADMINISTRATOR)) return 2;

        return 0;
    }
}
