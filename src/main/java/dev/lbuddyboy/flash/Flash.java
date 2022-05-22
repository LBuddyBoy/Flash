package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.handler.*;
import dev.lbuddyboy.flash.util.YamlDoc;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Flash extends JavaPlugin {

    @Getter private static Flash instance;

    private YamlDoc menusYML;

    private CommandHandler commandHandler;
    private MongoHandler mongoHandler;
    private RedisHandler redisHandler;
    private CacheHandler cacheHandler;
    private UserHandler userHandler;
    private RankHandler rankHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.setupConfig();
        this.loadHandlers();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
        this.menusYML = new YamlDoc(Flash.getInstance().getDataFolder(), "menus.yml");

        for (FlashLanguage language : FlashLanguage.values()) language.loadDefault();
        for (FlashMenuLanguage language : FlashMenuLanguage.values()) language.loadDefault();
    }

    private void loadHandlers() {
        this.mongoHandler = new MongoHandler();
        this.redisHandler = new RedisHandler();
        this.userHandler = new UserHandler();
        this.cacheHandler = new CacheHandler();
        this.rankHandler = new RankHandler();

        this.commandHandler = new CommandHandler();
    }

}
