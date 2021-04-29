package me.jordanplayz158.phasestaff.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;

public class Config extends Template {
    public Config() {
        super(new File("config.json"));
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
        return Activity.ActivityType.valueOf(getActivity().get("type").getAsString().toUpperCase());
    }

    private JsonObject getChannels() {
        return json.getAsJsonObject("channels");
    }
    public long getVcRoleId() { return json.get("vcroleid").getAsLong();
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

}
