package me.involuting.blockhunt.config;


import me.involuting.blockhunt.BlockHunt;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaFile {

    private final File file;
    private final FileConfiguration config;

    public ArenaFile(BlockHunt plugin) {

        this.file = new File(
                plugin.getDataFolder(),
                "arenas.yml"
        );

        if (!file.exists()) {

            try {

                plugin.getDataFolder().mkdirs();
                file.createNewFile();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        this.config =
                YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {

        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
