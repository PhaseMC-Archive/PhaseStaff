package me.jordanplayz158.phasestaff.jobs;

import com.google.gson.JsonElement;
import me.jordanplayz158.phasestaff.PhaseStaff;
import me.jordanplayz158.phasestaff.events.ReadyListener;
import net.dv8tion.jda.api.EmbedBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TimeZoneMessageJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        StringBuilder stringBuilder = new StringBuilder();
        final String pattern = "hh:mm a";
        PhaseStaff.getInstance().getTime().getStaffMembers().forEach(staff -> {
            for (Map.Entry<String, JsonElement> map : staff.getAsJsonObject().entrySet()) {
                stringBuilder
                        .append(ReadyListener.getGuild().getMemberById(map.getKey()).getEffectiveName())
                        .append(" ")
                        .append(LocalTime.now(ZoneId.of(map.getValue().getAsString())).format(DateTimeFormatter.ofPattern(pattern)))
                        .append("\n");
            }
        });

        ReadyListener.getMessage().editMessage(new EmbedBuilder()
                .setTitle("Staff Clocks")
                .setDescription(stringBuilder.toString())
                .build())
                .queue();
    }
}
