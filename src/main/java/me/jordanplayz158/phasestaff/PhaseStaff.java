package me.jordanplayz158.phasestaff;

import me.jordanplayz158.phasestaff.commands.Command;
import me.jordanplayz158.phasestaff.commands.TimeZoneCommand;
import me.jordanplayz158.phasestaff.commands.UserInfoCommand;
import me.jordanplayz158.phasestaff.events.CommandsListener;
import me.jordanplayz158.phasestaff.events.MemberJoinListener;
import me.jordanplayz158.phasestaff.events.MemberVcListener;
import me.jordanplayz158.phasestaff.events.ReadyListener;
import me.jordanplayz158.phasestaff.json.Config;
import me.jordanplayz158.phasestaff.json.Time;
import me.jordanplayz158.utils.FileUtils;
import me.jordanplayz158.utils.Initiate;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PhaseStaff {
    private static final PhaseStaff instance = new PhaseStaff();
    private Config config;
    public static Logger logger;
    private Time time;
    private JDA jda;

    public static void main(String[] args) throws LoginException, IOException {
        // Copy config and loads it into memory
        instance.config = new Config();
        FileUtils.copyFile(Config.getFile());
        instance.config.loadJson();

        logger = Initiate.log(Level.toLevel(instance.config.getLogLevel()));

        final String token = instance.config.getJson().get("token").getAsString();
        // Checks if token is invalid, informs the user they need to provide a token and exits
        if(token.length() <= 1) {
            logger.fatal("You have to provide a token in your config file!");
            System.exit(1);
        }

        JDABuilder jdaBuilder = JDABuilder.createLight(token);

        // If there is a role id for onJoin then it will enable role on join functionality
        if(instance.config.getRoleOnJoin() != 0) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.addEventListeners(new MemberJoinListener());
        }

        // If there is a prefix then allow command functionality
        boolean hasPrefix = !instance.config.getPrefix().isEmpty();

        if(hasPrefix) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES);
            jdaBuilder.addEventListeners(new CommandsListener());
        }

        // If there is an id for the time channel then enable time channel functionality
        if(instance.config.getChannelTime() != 0) {
            instance.time = new Time();

            if (Time.getFile().getParentFile().mkdir() || Time.getFile().getParentFile().exists()) {
                FileUtils.copyFile(new File("time.json"), Time.getFile());
                instance.time.loadJson();
            }

            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        }

        // If there is an id for vc role then enable role on vc join functionality
        if(instance.config.getVcRoleId() != 0) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
            jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);

            jdaBuilder.addEventListeners(new MemberVcListener());
        }

        jdaBuilder.addEventListeners(new ReadyListener());

        if(!instance.config.getActivityName().isBlank() && !instance.config.getActivityType().name().isBlank()) {
            jdaBuilder.setActivity(Activity.of(instance.config.getActivityType(), instance.config.getActivityName()));
        }

        instance.jda = jdaBuilder
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        if(hasPrefix) {
            CommandHandler.addCommands(new TimeZoneCommand(), new UserInfoCommand());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Command: ");
        String line = br.readLine();

        while(!line.equals("exit")) {
            String[] command = line.split(" ");

            switch(command[0]) {
                case "listGuilds":
                    logger.info(instance.jda.getGuilds());
                    break;
                case "leaveGuild":
                    Objects.requireNonNull(instance.jda.getGuildById(command[1])).leave().queue(success ->
                            logger.info("Bot has successfully left the guild with the id of " + command[1]));
                    break;
            }

            System.out.print("Enter Command: ");
        }

        shutdown(0);
    }

    public static PhaseStaff getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public Time getTime() { return time; }

    public JDA getJDA() {
        return jda;
    }

    public static EmbedBuilder getTemplate(User author) {
        return new EmbedBuilder()
                .setFooter("Faster | " + MessageUtils.nameAndTag(author));
    }

    public static void shutdown(int errorCode) {
        instance.jda.shutdown();

        System.exit(errorCode);
    }
}
