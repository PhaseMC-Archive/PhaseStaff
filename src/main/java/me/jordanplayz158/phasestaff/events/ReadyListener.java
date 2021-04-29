package me.jordanplayz158.phasestaff.events;

import me.jordanplayz158.phasestaff.PhaseStaff;
import me.jordanplayz158.phasestaff.jobs.TimeZoneMessageJob;
import me.jordanplayz158.phasestaff.json.Config;
import me.jordanplayz158.phasestaff.json.Time;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class ReadyListener extends ListenerAdapter {
    private final SchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler scheduler;

    private static Guild guild;
    private static Message message;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Config config = PhaseStaff.getInstance().getConfig();

        if(config.getChannelTime() != 0) {
            Time time = PhaseStaff.getInstance().getTime();

            AtomicReference<Guild> guildAtom = new AtomicReference<>();
            AtomicReference<TextChannel> channel = new AtomicReference<>();

            PhaseStaff.getInstance().getJDA().getGuilds().forEach(g -> {
                try {
                    channel.set(g.getTextChannelById(config.getChannelTime()));
                    guildAtom.set(channel.get().getGuild());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            guild = guildAtom.get();

            if (time.getMessageId() == 0) {
                time.setMessageId(channel.get().sendMessage(new EmbedBuilder()
                        .setTitle("Staff Clocks")
                        .build()
                ).complete().getIdLong());

                try {
                    time.writeJson();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            message = channel.get().retrieveMessageById(time.getMessageId()).complete();

            try {
                scheduler = sf.getScheduler();

                scheduler.scheduleJob(JobBuilder.newJob(TimeZoneMessageJob.class).build(),
                        TriggerBuilder.newTrigger()
                                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
                                .build());

                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    public static Guild getGuild() {
        return guild;
    }

    public static Message getMessage() {
        return message;
    }
}
