package me.jordanplayz158.phasestaff.json;

import com.google.gson.JsonObject;
import me.jordanplayz158.phasestaff.PhaseStaff;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.util.Arrays;

public class Config extends Template {
    public Config() {
        super(new File("config.json"));
    }

    public String getLogLevel() {
        return json.get("logLevel").getAsString();
    }

    public String getPrefix() {
        return json.get("prefix").getAsString();
    }

    private JsonObject getActivity() {
        return json.getAsJsonObject("activity");
    }

    public String getActivityName() {
        return getActivity().get("name").getAsString();
    }

    public Activity.ActivityType getActivityType() {
        Activity.ActivityType activityType = null;

        try {
            activityType = Activity.ActivityType.valueOf(getActivity().get("type").getAsString().toUpperCase());
        } catch (IllegalArgumentException e) {
            PhaseStaff.logger.fatal("The following ActivityType's are accepted (non-case sensitive): " + Arrays.toString(Activity.ActivityType.values()));
            e.printStackTrace();
            PhaseStaff.shutdown(1);
        }

        return activityType;
    }

    private JsonObject getChannels() {
        return json.getAsJsonObject("channels");
    }

    public long getChannelTime() {
        return getChannels().get("time").getAsLong();
    }
    private JsonObject getRoles() {
        return json.getAsJsonObject("roles");
    }

    public long getRoleOnJoin() {
        return getRoles().get("onJoin").getAsLong();
    }

    public long getRoleStaff() {
        return getRoles().get("staff").getAsLong();
    }

    public long getVcRoleId() {
        return getRoles().get("vc").getAsLong();
    }
}
