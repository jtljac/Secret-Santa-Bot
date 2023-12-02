package com.datdeveloper.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {
    /**
     * Get the name of the command
     * @return The name of the command
     */
    String getCommandName();

    /**
     * Get the commands description
     * @return The description of the command
     */
    String getDescription();

    /**
     * Get the data required to upsert the command
     * @return The command data to upsert the command
     */
    SlashCommandData getCommandData();

    /**
     * 0: Anyone
     * 1: Moderator
     * 2: Admin
     * @return The required permission level
     */
    int getPermissionLevel();

    /**
     * Execute the command
     * @param event The slash command event
     * @return Whether the command was successful or not
     */
    boolean execute(SlashCommandInteractionEvent event);
}
