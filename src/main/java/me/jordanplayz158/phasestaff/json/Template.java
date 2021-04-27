package me.jordanplayz158.phasestaff.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.jordanplayz158.utils.LoadJson;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

public abstract class Template {
    private static File file;
    public JsonObject json;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Template(File file) {
        Template.file = file;
    }

    public static File getFile() {
        return file;
    }

    public JsonObject getJson() {
        return json;
    }

    public void loadJson() {
        this.json = LoadJson.linkedTreeMap(file);
    }

    public void writeJson() throws IOException {
        Writer writer = Files.newBufferedWriter(file.toPath());

        gson.toJson(json, writer);

        writer.close();
    }
}