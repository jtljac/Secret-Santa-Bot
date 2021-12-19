package com.datdeveloper.command;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class BaseCommand implements Command {
    public final String name;
    public final String description;
    public final int permissionLevel;

    public BaseCommand(String name, String description, int permissionLevel) {
        this.name = name;
        this.description = description;
        this.permissionLevel = permissionLevel;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getCommandName(), getDescription());
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
