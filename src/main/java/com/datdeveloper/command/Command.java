package com.datdeveloper.command;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command {
    /**
     * @return The name of the command
     */
    String getCommandName();

    /**
     * @return The description of the command
     */
    String getDescription();

    /**
     * @return The command data to upsert the command
     */
    CommandData getCommandData();

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
    boolean execute(SlashCommandEvent event);
}
