package dev.lbuddyboy.flash

import dev.lbuddyboy.flash.handler.*
import dev.lbuddyboy.flash.thread.BatchExecuteTask
import dev.lbuddyboy.flash.thread.TipsMessageTask
import dev.lbuddyboy.flash.thread.UserUpdateTask
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket
import dev.lbuddyboy.flash.util.YamlDoc
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CustomColor
import lombok.*
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

@Getter
class Flash : JavaPlugin() {
    private var menusYML: YamlDoc? = null
    private var commandHandler: CommandHandler? = null
    private var mongoHandler: MongoHandler? = null
    private var redisHandler: RedisHandler? = null
    private var cacheHandler: CacheHandler? = null
    private var userHandler: UserHandler? = null
    private var rankHandler: RankHandler? = null
    private var transportHandler: TransportHandler? = null
    private var serverHandler: ServerHandler? = null
    private var chatHandler: ChatHandler? = null
    private val scheduleHandler: ScheduleHandler? = null
    override fun onEnable() {
        instance = this
        setupConfig()
        loadHandlers()
        loadThreads()
        StaffMessagePacket("&g&l[SERVER] &h" + FlashLanguage.SERVER_NAME.string + " &fhas just came &aonline&f.").send()
    }

    override fun onDisable() {
        StaffMessagePacket("&g&l[SERVER] &h" + FlashLanguage.SERVER_NAME.string + " &fhas just went &coffline&f.").send()
        userHandler.getUsers().values().forEach { user -> user.save(false) }
    }

    private fun setupConfig() {
        saveDefaultConfig()
        menusYML = YamlDoc(Flash.instance.getDataFolder(), "menus.yml")
        for (language in FlashLanguage.values()) language.loadDefault()
        for (language in FlashMenuLanguage.values()) language.loadDefault()
        FlashLanguage.ESSENTIALS_CUSTOM_COLORS.stringList!!
            .forEach(Consumer { string: String? ->
                CC.customColors.add(CustomColor(
                    string!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
                    string.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1]
                ))
            })
    }

    private fun loadHandlers() {
        mongoHandler = MongoHandler()
        redisHandler = RedisHandler()
        serverHandler = ServerHandler()
        userHandler = UserHandler()
        cacheHandler = CacheHandler()
        rankHandler = RankHandler()
        transportHandler = TransportHandler()
        chatHandler = ChatHandler()
        commandHandler = CommandHandler()
        //        this.scheduleHandler = new ScheduleHandler();
    }

    private fun loadThreads() {
        TipsMessageTask().runTaskTimer(this, (20 * 10).toLong(), 20L * FlashLanguage.TIPS_DELAY_SECONDS.int)
        UserUpdateTask().start()
        BatchExecuteTask().start()
    }

    companion object {
        lateinit var instance: Flash
    }
}