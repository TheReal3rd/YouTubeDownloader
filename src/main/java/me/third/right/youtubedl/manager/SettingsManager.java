package me.third.right.youtubedl.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.settings.EnumSetting;
import me.third.right.youtubedl.settings.SettingBase;
import me.third.right.youtubedl.utils.YTFork;
import me.third.right.youtubedl.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public class SettingsManager {
    private final Path path = Utils.mainPath;
    public static SettingsManager INSTANCE;
    protected final HashMap<String, SettingBase> settingsMap = new HashMap<>();
    //Settings Here
    private final EnumSetting<YTFork> sourceSetting = register(new EnumSetting<>("Fork", X -> {
        JFrameManager.INSTANCE.getSettingsFrame().getSource().setSelectedItem(X.getSelected());
        System.out.println("Setting update applied.");
        return null;
    }, YTFork.values(), YTFork.yt_dlp));

    private <T> T register(SettingBase settingBase) {
        settingsMap.put(settingBase.getName().toLowerCase(Locale.ROOT), settingBase);
        return (T) settingBase;
    }

    private SettingBase getSetting(String name) {
        return settingsMap.get(name.toLowerCase(Locale.ROOT));
    }

    public void loadSettings() {
        if(!Files.exists(path.resolve("config.json"))) return;

        final JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(path.resolve("config.json").toFile())) {
            final JsonObject obj = jsonParser.parse(reader).getAsJsonObject();

            for(Map.Entry<String, JsonElement> e : obj.entrySet()) {
                //if (!e.getValue().isJsonObject()) continue;This fixed an issue.

                final SettingBase setting = getSetting(e.getKey());
                if (setting == null) continue;
                setting.fromJson(e.getValue().getAsJsonPrimitive());
                System.out.println(setting.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSettings() {
        final JsonObject json = new JsonObject();
        for(SettingBase settingBase : settingsMap.values()) {
            final JsonElement jsonObject = settingBase.toJson();
            if(jsonObject == null) continue;
            json.add(settingBase.getName(), jsonObject);
        }

        try (FileWriter file = new FileWriter(path.resolve("config.json").toFile())) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
