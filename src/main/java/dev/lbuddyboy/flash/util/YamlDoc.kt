package dev.lbuddyboy.flash.util;

import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class YamlDoc {

    public static boolean done = false;
    protected File file;
    protected YamlConfiguration config;
    private String configName;

    public YamlDoc(File folder, String configName) {
        this.configName = configName;
        file = new File(folder, configName);
        init();
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully created the &6" + configName + " &ffile."));
    }

    public void init() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                loadDefaults();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void loadDefaults() throws IOException {
        InputStream is = getClass().getResourceAsStream("/" + configName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(readFile(is));
        writer.close();
    }


    public String readFile(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String content = "";
        String line;

        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }

        reader.close();
        return content.trim();
    }


    public void save() throws IOException {
        if (!file.exists())
            file.createNewFile();

        config.save(file);
    }


    public YamlConfiguration gc() {
        return config;
    }

    public void reloadConfig() throws IOException {
        init();
    }

}
