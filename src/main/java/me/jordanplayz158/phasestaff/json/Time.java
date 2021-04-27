package me.jordanplayz158.phasestaff.json;

import com.google.gson.JsonArray;

import java.io.File;

public class Time extends Template {
    public Time() {
        super(new File("data", "time.json"));
    }

    public long getMessageId() {
        return json.get("messageId").getAsLong();
    }

    public void setMessageId(long messageId) {
        json.remove("messageId");
        json.addProperty("messageId", messageId);
    }

    public JsonArray getStaffMembers() {
        return json.getAsJsonArray("staffMembers");
    }
}
