package com.datdeveloper.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

public class SecretSantaCommand extends BaseCommand {

    public SecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData()
                .addOption(OptionType.STRING, "date", "The date that the secret santa will occur (Not enforced, you will still have to start it manually)", true)
                .addOption(OptionType.CHANNEL, "sharechannel", "The channel that users should go to in order to share their details");
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        String message = "@everyone It is time for a secret santa!\n" +
                "Click the \"Opt in\" button below to partake in the secret santa, on " + event.getOption("date").getAsString() + " everyone who has opted in will receive a DM telling you who you need to get a gift for.\n" +
                "You can opt out anytime before " + event.getOption("date").getAsString() + " with the \"Opt out\" button below \n" +
                "Please populate your steam wishlist so people know what to get you";

        if (event.getOption("sharechannel") != null) {
            message += "\n" +
                    "Please post your details (steam profile, etc) in " + event.getOption("sharechannel").getAsGuildChannel().getAsMention() + " so people know where to send you your gift,\n" +
                    "Try not to just add the person you're getting a gift for as a friend, that's fairly suspicious";
        }

        event.getChannel().sendMessage(message).setActionRow(
                Button.primary("Optin", "Opt-in"),
                Button.primary("Optout", "Opt-out")
        ).queue();

        event.reply("Successfully sent message").setEphemeral(true).queue();
        return true;
    }
}
