package com.datdeveloper.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

/**
 * A command to begin the secret santa
 * <br>
 * Creates a message that allows users to opt in and opt out
 */
public class SecretSantaCommand extends BaseCommand {
    private static final String DATE_ARGUMENT = "date";
    private static final String DUE_DATE_ARGUMENT = "duedate";
    private static final String SHARE_CHANNEL_ARGUMENT = "sharechannel";

    public SecretSantaCommand(String name, String description, int permissionLevel) {
        super(name, description, permissionLevel);
    }

    @Override
    public SlashCommandData getCommandData() {
        return super.getCommandData()
                .addOption(OptionType.STRING, DATE_ARGUMENT, "The date that the secret santa will occur (Not enforced, you will still have to start it manually)", true)
                .addOption(OptionType.STRING, DUE_DATE_ARGUMENT, "The date that people should have their gifts deliver for")
                .addOption(OptionType.CHANNEL, SHARE_CHANNEL_ARGUMENT, "The channel that users should go to in order to share their details");
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        String message = String.format("@everyone It is time for a secret santa!\n" +
                "Click the \"Opt in\" button below to partake in the secret santa, on %1$s everyone who has opted in will receive a DM telling you who you need to get a gift for.\n" +
                "You can opt out anytime before %1$s with the \"Opt out\" button below\n" +
                "Please populate your steam wishlist with games around £10 so people know what to get you.",
                event.getOption(DATE_ARGUMENT).getAsString());

        OptionMapping dueDate = event.getOption(DUE_DATE_ARGUMENT);
        if (dueDate != null) {
            message+= String.format("\n\n" +
                    "Please ensure that your gifts are delivered for %s.",
                    dueDate.getAsString());
        }

        OptionMapping shareChannel = event.getOption(SHARE_CHANNEL_ARGUMENT);
        if (shareChannel != null) {
            message += String.format("\n\n" +
                    "Please post your details (steam profile, etc) in %s so people know where to send you your gift,\n" +
                    "Try not to just add the person you're getting a gift for as a friend, that's fairly suspicious.",
                    shareChannel.getAsChannel().getAsMention());
        }

        event.getChannel().sendMessage(message).setActionRow(
                Button.primary("Optin", "Opt-in"),
                Button.primary("Optout", "Opt-out")
        ).queue();

        event.reply("Successfully sent message").setEphemeral(true).queue();
        return true;
    }
}
