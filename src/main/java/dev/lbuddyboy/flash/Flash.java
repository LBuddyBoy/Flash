package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.handler.*;
import dev.lbuddyboy.flash.thread.BatchExecuteThread;
import dev.lbuddyboy.flash.thread.TipsMessageThread;
import dev.lbuddyboy.flash.thread.UserUpdateThread;
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
    private TransportHandler transportHandler;
    private ServerHandler serverHandler;
    private ChatHandler chatHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.setupConfig();
        this.loadHandlers();
        this.loadThreads();
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
        this.serverHandler = new ServerHandler();
        this.userHandler = new UserHandler();
        this.cacheHandler = new CacheHandler();
        this.rankHandler = new RankHandler();
        this.transportHandler = new TransportHandler();
        this.chatHandler = new ChatHandler();

        this.commandHandler = new CommandHandler();
    }

    private void loadThreads() {
        new UserUpdateThread().start();
        new TipsMessageThread().start();
        new BatchExecuteThread().start();
    }

}
