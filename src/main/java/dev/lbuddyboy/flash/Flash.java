package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.handler.*;
import dev.lbuddyboy.flash.thread.BatchExecuteTask;
import dev.lbuddyboy.flash.thread.TipsMessageTask;
import dev.lbuddyboy.flash.thread.UserUpdateTask;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.YamlDoc;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.CustomColor;
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
    private ScheduleHandler scheduleHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.setupConfig();
        this.loadHandlers();
        this.loadThreads();

        new StaffMessagePacket("&g&l[SERVER] &h" + FlashLanguage.SERVER_NAME.getString() + " &fhas just came &aonline&f.").send();
    }

    @Override
    public void onDisable() {
        new StaffMessagePacket("&g&l[SERVER] &h" + FlashLanguage.SERVER_NAME.getString() + " &fhas just went &coffline&f.").send();
        this.userHandler.getUsers().values().forEach(user -> user.save(false));
    }

    private void setupConfig() {
        this.saveDefaultConfig();
        this.menusYML = new YamlDoc(Flash.getInstance().getDataFolder(), "menus.yml");

        for (FlashLanguage language : FlashLanguage.values()) language.loadDefault();
        for (FlashMenuLanguage language : FlashMenuLanguage.values()) language.loadDefault();
        FlashLanguage.ESSENTIALS_CUSTOM_COLORS.getStringList().forEach(string -> CC.customColors.add(new CustomColor(string.split(";")[0], string.split(";")[1])));
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
//        this.scheduleHandler = new ScheduleHandler();
    }

    private void loadThreads() {
        new TipsMessageTask().runTaskTimer(this, 20 * 10, 20L * FlashLanguage.TIPS_DELAY_SECONDS.getInt());
        new UserUpdateTask().start();
        new BatchExecuteTask().start();
    }

}
