package me.third.right.youtubedl.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.third.right.youtubedl.settings.EnumSetting;
import me.third.right.youtubedl.settings.SettingBase;
import me.third.right.youtubedl.settings.StringSetting;
import me.third.right.youtubedl.utils.Utils;
import me.third.right.youtubedl.utils.YTFork;

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
    // ** Universal
    private final EnumSetting<YTFork> sourceSetting = register(new EnumSetting<>("Fork", X -> {
        for(YTFork x : YTFork.values()) {
            Utils.deleteFile(Utils.mainPath.resolve(x.getName()));
        }

        Utils.displayMessage("Done", "Deleted all downloaders.");
        return null;
    }, "Select YTDL fork to use", "Deletes all YT Downloaders to redownload the latest versions.", YTFork.values(), YTFork.yt_dlp));
    // ** YouTube
    private final StringSetting ytRename = register(new StringSetting("YTRename", "%(title)s-%(id)s.%(ext)s", "Don't touch if you don't know what you're doing."));
    // ** M3U8
    private final StringSetting m3Rename = register(new StringSetting("M3U8Rename", "EP %d", "Reset the settings to default state."));
    // ** Use Cookies
    private final StringSetting cookiesType = register(new StringSetting("CookiesType", "none", "Use cookies to bypass restrictions or login requirements. Leave none to deactivate. To use type path or name of cookies."));


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
                if (!e.getValue().isJsonPrimitive()) continue;

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
