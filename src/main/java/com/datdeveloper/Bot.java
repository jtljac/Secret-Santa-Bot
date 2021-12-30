package com.datdeveloper;

import com.datdeveloper.command.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bot {
    // Activities to display in discord
    static final List<Activity> activities = Arrays.asList(Activity.playing("that one christmas level from Viscera Cleanup Detail"), Activity.playing("with some christmas gifts"), Activity.watching("Christmas Films"), Activity.listening("some Christmas music"), Activity.of(Activity.ActivityType.DEFAULT, "Delivering some presents"));
    // The delay before selecting a new activity
    static final long activityDelay = 60000L;

    public static final Logger logger = LoggerFactory.getLogger(Bot.class);
    public static final boolean DEBUG = true;

    public static void main(String[] args) throws LoginException, InterruptedException {
        HashMap<String, Command> commands = new HashMap<>();

        commands.put("prime-santa", new SecretSantaCommand("prime-santa", "Send a message to start opt-ins for the secret santa", 2));
        commands.put("start-santa", new StartSecretSantaCommand("start-santa", "Sends the PMs the people who've opted in with their targets", 2));
        commands.put("reset-santa", new SecretSantaResetCommand("reset-santa", "Resets the secret santa list", 2));
        commands.put("list-santa", new ListSecretSantaCommand("list-santa", "Lists all the people who's so far opted in", 2));

        JDA jda = JDABuilder
                .createDefault("")
                .addEventListeners(new CommandListener(commands))
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        if (!DEBUG) {
            for (String key : commands.keySet()) {
                jda.upsertCommand(commands.get(key).getCommandData()).queue();
            }
        }

        Activity nextActivityStatus;

        while(true){
            nextActivityStatus = activities.get(new Random().nextInt(activities.size()));
            logger.info("Next activity: " + nextActivityStatus.getType() + " " + nextActivityStatus);

            jda.getPresence().setActivity(nextActivityStatus);

            // the event listeners are in another thread, we're safe to put this one to sleep
            Thread.sleep(activityDelay);
        }
    }
}
