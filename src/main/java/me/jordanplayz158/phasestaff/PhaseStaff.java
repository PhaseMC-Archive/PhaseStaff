package me.jordanplayz158.phasestaff;

import me.jordanplayz158.phasestaff.commands.Command;
import me.jordanplayz158.phasestaff.commands.TimeZoneCommand;
import me.jordanplayz158.phasestaff.commands.UserInfoCommand;
import me.jordanplayz158.phasestaff.events.CommandsListener;
import me.jordanplayz158.phasestaff.events.MemberJoinListener;
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
    private final Logger logger = Initiate.log();
    private Time time;
    private JDA jda;

    private List<Command> commands = new ArrayList<>();

    public static void main(String[] args) throws LoginException, IOException {
        //Copy config
        instance.config = new Config();
        FileUtils.copyFile(Config.getFile());
        instance.config.loadJson();

        final String token = instance.config.getJson().get("token").getAsString();
        // Checks if the Token is 1 character or less and if so, tell the person they need to provide a token
        if(token.length() <= 1) {
            instance.logger.fatal("You have to provide a token in your config file!");
            System.exit(1);
        }

        // Token and activity is read from and set in the config.json
        JDABuilder jdaBuilder = JDABuilder.createLight(token);

        if(instance.config.getRoleOnJoin() != 0) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.addEventListeners(new MemberJoinListener());
        }

        boolean hasPrefix = !instance.config.getPrefix().isEmpty();

        if(hasPrefix) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES);
            jdaBuilder.addEventListeners(new CommandsListener());
        }

        if(instance.config.getChannelTime() != 0) {
            instance.time = new Time();

            if (Time.getFile().getParentFile().mkdir() || Time.getFile().getParentFile().exists()) {
                FileUtils.copyFile(new File("time.json"), Time.getFile());
                instance.time.loadJson();
            }

            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        }

        jdaBuilder.addEventListeners(new ReadyListener());

        instance.jda = jdaBuilder
                .setActivity(Activity.of(instance.config.getActivityType(), instance.config.getActivityName()))
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        if(hasPrefix) {
            instance.commands.addAll(Arrays.asList(
                    new TimeZoneCommand(),
                    new UserInfoCommand()
            ));
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Command: ");
        String line = br.readLine();

        while(!line.equals("exit")) {
            String[] command = line.split(" ");

            switch(command[0]) {
                case "listGuilds":
                    instance.logger.info(instance.jda.getGuilds());
                    break;
                case "leaveGuild":
                    Objects.requireNonNull(instance.jda.getGuildById(command[1])).leave().queue(success ->
                            instance.logger.info("Bot has successfully left the guild with the id of " + command[1]));
                    break;
            }

            System.out.print("Enter Command: ");
            line = br.readLine();
        }

        instance.jda.shutdown();

        System.exit(0);
    }

    public static PhaseStaff getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public Time getTime() {
        return time;
    }

    public JDA getJDA() {
        return jda;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommandsList(List<Command> commands) {
        this.commands = commands;
    }

    public static EmbedBuilder getTemplate(User author) {
        return new EmbedBuilder()
                .setFooter("Faster | " + MessageUtils.nameAndTag(author));
    }
}
