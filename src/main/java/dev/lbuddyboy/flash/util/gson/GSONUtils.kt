package dev.lbuddyboy.flash.util.gson

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.lbuddyboy.flash.rank.impl.RedisRank
import dev.lbuddyboy.flash.server.Server
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.impl.RedisUser
import dev.lbuddyboy.flash.user.model.*
import lombok.Getter
import org.bukkit.inventory.ItemStack
import java.util.*

object GSONUtils {
    @Getter
    var GSON = GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().create()
    val PREFIX = object : TypeToken<Prefix?>() {}.type
    val USER_PERMISSION = object : TypeToken<List<UserPermission?>?>() {}.type
    val PUNISHMENTS = object : TypeToken<List<Punishment?>?>() {}.type
    val REDIS_USER = object : TypeToken<RedisUser?>() {}.type
    val USER = object : TypeToken<User?>() {}.type
    val REDIS_RANK = object : TypeToken<RedisRank?>() {}.type
    val NOTIFICATION = object : TypeToken<Notification?>() {}.type
    val SERVER = object : TypeToken<Server?>() {}.type
    val NOTE = object : TypeToken<List<Note?>?>() {}.type
    val SERVER_INFO = object : TypeToken<List<ServerInfo?>?>() {}.type
    val ITEMSTACKS = object : TypeToken<Array<ItemStack?>?>() {}.type
    val PLAYER_INFO = object : TypeToken<PlayerInfo?>() {}.type
    val STAFF_INFO = object : TypeToken<StaffInfo?>() {}.type
    val STRING = object : TypeToken<List<String?>?>() {}.type
    val GRANT = object : TypeToken<List<Grant?>?>() {}.type
    val PROMOTIONS = object : TypeToken<List<Promotion?>?>() {}.type
    val DEMOTIONS = object : TypeToken<List<Demotion?>?>() {}.type
    val UUID = object : TypeToken<List<UUID?>?>() {}.type
}