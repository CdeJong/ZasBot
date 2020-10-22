package com.cdejong.config;

import com.cdejong.Bot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {
    private String name;
    private File file;
    private Config config;
    private Gson gson;

    public ConfigManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.name = "config.json";
        this.file = new File(this.name);

        if (!file.exists()) {
            try {
                file.createNewFile();

                InputStream input = this.getClass().getClassLoader().getResourceAsStream(this.name);

                if (input == null) {
                    Bot.getLogger().warn("Resource not found!");
                    return;
                }

                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(this.config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try {
            this.config = gson.fromJson(new FileReader(this.file), Config.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
        saveConfig();
    }
}
