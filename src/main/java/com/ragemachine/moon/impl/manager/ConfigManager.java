package com.ragemachine.moon.impl.manager;

import com.google.gson.*;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.EnumConverter;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigManager implements Util {

    public ArrayList<Hack> hacks = new ArrayList<>();
    public String config = "moonhack/config/";

    public void loadConfig(String name) {
        List<File> files = Arrays.stream(Objects.requireNonNull(new File("moonhack").listFiles()))
                .filter(File::isDirectory)
                .collect(Collectors.toList());
        if (files.contains(new File("moonhack/" + name + "/"))) {
            config = "moonhack/" + name + "/";
        } else {
            config = "moonhack/config/";
        }
        MoonHack.getFriendManager.onLoad();
        for(Hack hack : this.hacks) {
            try {
                loadSettings(hack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public void saveConfig(String name) {
        config = "moonhack/" + name + "/";
        File path = new File(config);
        if (!path.exists()) {
            path.mkdir();
        }
        MoonHack.getFriendManager.saveFriends();
        for(Hack hack : this.hacks) {
            try {
                saveSettings(hack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("moonhack/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("moonhack", ""));
                writer.close();
            } else {
                currentConfig.createNewFile();
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("moonhack", ""));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadCurrentConfig() {
        File currentConfig = new File("moonhack/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for(Hack hack : this.hacks) {
            hack.reset();
        }
        if(saveConfig) saveConfig(name);
    }

    public void saveSettings(Hack hack) throws IOException {
        JsonObject object = new JsonObject();
        File directory = new File(config + getDirectory(hack));
        if (!directory.exists()) {
            directory.mkdir();
        }
        String featureName = config + getDirectory(hack) + hack.getName() + ".json";
        Path outputFile = Paths.get(featureName);
        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(writeSettings(hack));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
        writer.write(json);
        writer.close();
    }

    //TODO: String[] Array for FriendList? Also ENUMS!!!!!
    public static void setValueFromJson(Hack hack, Value setting, JsonElement element) {
        switch (setting.getType()) {
            case "Boolean":
                setting.setValue(element.getAsBoolean());
                break;
            case "Double":
                setting.setValue(element.getAsDouble());
                break;
            case "Float":
                setting.setValue(element.getAsFloat());
                break;
            case "Integer":
                setting.setValue(element.getAsInt());
                break;
            case "String":
                String str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                break;
            case "Bind":
                setting.setValue(new Bind.BindConverter().doBackward(element));
                break;
            case "Enum":
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue(value == null ? setting.getDefaultValue() : value);
                    break;
                } catch(Exception e) {
                    break;
                }
            default:
        }
    }

    //TODO: add everything with Settings here
    public void init() {
        hacks.addAll(MoonHack.getModuleManager.modules);
        hacks.add(MoonHack.getFriendManager);

        String name = loadCurrentConfig();
        loadConfig(name);
        MoonHack.LOGGER.info("Config loaded.");
    }

    private void loadSettings(Hack hack) throws IOException {
        String featureName = config + getDirectory(hack) + hack.getName() + ".json";
        Path featurePath = Paths.get(featureName);
        if (!Files.exists(featurePath)) {
            return;
        }
        loadPath(featurePath, hack);
    }

    private void loadPath(Path path, Hack hack) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject(), hack);
        } catch (IllegalStateException e) {
            MoonHack.LOGGER.error("Bad Config File for: " + hack.getName() + ". Resetting...");
            loadFile(new JsonObject(), hack);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Hack hack) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String settingName = entry.getKey();
            JsonElement element = entry.getValue();
            if(hack instanceof FriendManager) {
                try {
                    MoonHack.getFriendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                boolean settingFound = false;
                for (Value value : hack.getSettings()) {
                    if (settingName.equals(value.getName())) {
                        try {
                            setValueFromJson(hack, value, element);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        settingFound = true;
                    }
                }

                if(settingFound) {
                    continue;
                }
            }
        }
    }

    public JsonObject writeSettings(Hack hack) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Value value : hack.getSettings()) {
            if (value.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum) value.getValue()).getClass());
                object.add(value.getName(), converter.doForward((Enum) value.getValue()));
                continue;
            }

            if(value.isStringSetting()) {
                String str = (String) value.getValue();
                value.setValue(str.replace(" ", "_"));
            }

            try {
                object.add(value.getName(), jp.parse(value.getValueAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return object;
    }

    public String getDirectory(Hack hack) {
        String directory = "";
        if(hack instanceof Module) {
            directory = directory + ((Module) hack).getCategory().getName() + "/";
        }
        return directory;
    }
}
