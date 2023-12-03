package com.datdeveloper.command;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * An abstraction to quickly set up some of a command
 */
public abstract class BaseCommand implements Command {
    public final String name;
    public final String description;
    public final int permissionLevel;

    protected BaseCommand(String name, String description, int permissionLevel) {
        this.name = name;
        this.description = description;
        this.permissionLevel = permissionLevel;
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash(getCommandName(), getDescription());
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPermissionLevel() {
        return permissionLevel;
    }
}
