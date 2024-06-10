package nya.tuyw.addurdisc.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("AddurDisc-common.json").toFile();
    private static Config config;

    static {
        if (!CONFIG_FILE.exists()){
            writeDefaultConfig();
        }else {
            readConfig();
        }
    }
    private static void writeDefaultConfig() {
        config = new Config();
        String json = GSON.toJson(config);
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        return config;
    }
}
