package me.jordanplayz158.phasestaff.commands;

import com.google.gson.*;
import me.jordanplayz158.phasestaff.PhaseStaff;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;

public class TimeZoneCommand extends ArgumentCommand {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TimeZoneCommand() {
        super("timezone",
                null,
                "Add your timezone for staff members to proactively see what time it is for you!",
                null,
                PhaseStaff.getInstance().getJDA().getRoleById(PhaseStaff.getInstance().getConfig().getRoleStaff()),
                "timezone <timeZone> || timezone list");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (args[1].equals("list")) {
            StringBuilder zones = new StringBuilder();

            if (args.length == 3) {
                for (String zone : ZoneId.getAvailableZoneIds()) {
                    if (zones.length() + (zone.length() + " ".length()) <= 2000) {
                        if (zone.startsWith(args[2])) {
                            String[] regionZone = zone.split("/");

                            zones.append(regionZone[1]).append(" ");
                        }
                    } else {
                        event.getChannel().sendMessage(zones).queue();
                        zones = new StringBuilder();
                    }
                }

                event.getChannel().sendMessage(zones).queue();

                return;
            }

            for (String zone : ZoneId.getAvailableZoneIds()) {
                int index = zone.indexOf("/");

                if (index == -1) {
                    index = zone.length();
                }

                String region = zone.substring(0, index);

                if (zones.length() + (region.length() + " ".length()) <= 2000) {
                    if (zones.indexOf(region) < 0) {
                        zones.append(region).append(" ");
                    }
                } else {
                    event.getChannel().sendMessage(zones).queue();
                    zones = new StringBuilder();
                }
            }

            event.getChannel().sendMessage(zones).queue();

            return;
        }

        try {
            ZoneId zoneId = ZoneId.of(args[1]);

            JsonArray jsonArray = PhaseStaff.getInstance().getTime().getStaffMembers();

            for(int i = 0; i < jsonArray.size(); i++) {
                if(jsonArray.get(i).getAsJsonObject().has(event.getAuthor().getId())) {
                    jsonArray.remove(i);
                    break;
                }
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(event.getAuthor().getId(), args[1]);

            jsonArray.add(jsonObject);

            PhaseStaff.getInstance().getTime().writeJson();

            event.getChannel().sendMessage("Successfully added timezone, " + zoneId.toString() + " for " + event.getAuthor().getName()).queue();
        } catch (ZoneRulesException | IOException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}
