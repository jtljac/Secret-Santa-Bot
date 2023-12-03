package com.datdeveloper;

import com.datdeveloper.command.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bot {
    /** Activities to display in discord */
    static final List<Activity> activities = Arrays.asList(Activity.playing("that one christmas level from Viscera Cleanup Detail"),
            Activity.playing("with some Christmas gifts"),
            Activity.watching("Christmas Films"),
            Activity.listening("some Christmas music"),
            Activity.customStatus("Delivering some presents"));

    /** The delay before selecting a new activity */
    static final long ACTIVITY_DELAY = 60000L;

    public static final Logger logger = LoggerFactory.getLogger(Bot.class);

    public static void main(String[] args) throws InterruptedException {
        final String token = System.getenv("SECRET_SANTA_TOKEN");

        if (token == null) {
            logger.error("Failed to find a discord bot token.\n"
                + "This needs to be passed as the environment variable: \"SECRET_SANTA_TOKEN\"");
            return;
        }

        final HashMap<String, Command> commands = new HashMap<>();

        commands.put("prime-santa", new SecretSantaCommand("prime-santa", "Send a message to start opt-ins for the secret santa", 2));
        commands.put("start-santa", new StartSecretSantaCommand("start-santa", "Sends the PMs the people who've opted in with their targets", 2));
        commands.put("reset-santa", new SecretSantaResetCommand("reset-santa", "Resets the secret santa list", 2));
        commands.put("list-santa", new ListSecretSantaCommand("list-santa", "Lists all the people who's so far opted in", 2));


        final JDA jda;
        try {
            jda = JDABuilder
                    .createDefault(token)
                    .addEventListeners(new CommandListener(commands))
                    .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                    .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
        } catch (final InvalidTokenException | IllegalArgumentException e) {
            logger.error("Bad token passed, please check the Discord API token.");
            return;
        }

        Activity nextActivityStatus;
        while(true){
            nextActivityStatus = activities.get(new Random().nextInt(activities.size()));
            logger.info("Next activity: " + nextActivityStatus.getType() + " " + nextActivityStatus);

            jda.getPresence().setActivity(nextActivityStatus);

            // the event listeners are in another thread, we're safe to put this one to sleep
            Thread.sleep(ACTIVITY_DELAY);
        }
    }
}
