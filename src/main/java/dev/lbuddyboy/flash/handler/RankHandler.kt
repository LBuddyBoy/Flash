package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.rank.comparator.RankWeightComparator
import dev.lbuddyboy.flash.rank.editor.listener.RankEditorListener
import dev.lbuddyboy.flash.rank.impl.FlatFileRank
import dev.lbuddyboy.flash.rank.impl.MongoRank
import dev.lbuddyboy.flash.rank.impl.RedisRank
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket
import dev.lbuddyboy.flash.util.YamlDoc
import dev.lbuddyboy.flash.util.bukkit.Tasks
import dev.lbuddyboy.flash.util.gson.GSONUtils
import lombok.Getter
import lombok.Setter
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Getter
@Setter
class RankHandler {
    private val ranks: MutableMap<UUID, Rank?>
    private var ranksYML: YamlDoc? = null
    private var defaultRank: Rank? = null

    init {
        ranks = ConcurrentHashMap()
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "FLATFILE", "YAML" -> ranksYML = YamlDoc(
                Flash.instance.dataFolder, "ranks.yml"
            )
        }
        Bukkit.getServer().pluginManager.registerEvents(RankEditorListener(), Flash.instance)
        Tasks.run { loadAll() }
    }

    fun loadAll() {
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> {
                for ((key, value) in RedisHandler.Companion.requestJedis().getResource().hgetAll("Ranks").entries) {
                    ranks[UUID.fromString(key)] = GSONUtils.getGSON().fromJson<Rank>(value, GSONUtils.REDIS_RANK)
                }
            }

            "MONGO" -> {
                for (document in Flash.instance.mongoHandler.rankCollection.find()) {
                    val uuid = UUID.fromString(document.getString("uuid"))
                    ranks[uuid] = MongoRank(uuid, document.getString("name"))
                }
            }

            "FLATFILE", "YAML" -> {
                try {
                    for (key in ranksYML!!.gc().getConfigurationSection("ranks").getKeys(false)) {
                        val uuid = UUID.fromString(key)
                        val name = ranksYML.gc().getString("ranks.$key.name")
                        ranks[uuid] = FlatFileRank(uuid, name)
                    }
                } catch (ignored: Exception) {
                }
            }
        }
        for (rank in ranks.values) {
            if (rank!!.isDefaultRank) {
                defaultRank = rank
                break
            }
        }
        if (getDefaultRank() == null) {
            defaultRank = createRank("Default")
            defaultRank!!.isDefaultRank = true
            ranks[defaultRank!!.getUuid()] = defaultRank
            defaultRank!!.save(true)
        }
        RanksUpdatePacket(ranks).send()
    }

    fun getRank(name: String): Rank? {
        for (rank in ranks.values) {
            if (rank!!.getName() == name) return rank
        }
        return null
    }

    fun createRank(name: String?): Rank? {
        return when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> RedisRank(name)
            "MONGO" -> MongoRank(name)
            "FLATFILE", "YAML" -> FlatFileRank(name)
            else -> null
        }
    }

    val sortedRanks: List<Rank?>
        get() = ranks.values.stream().sorted(RankWeightComparator().reversed()).collect(Collectors.toList())

    fun getDefaultRank(): Rank? {
        for (rank in getRanks().values) {
            if (rank.isDefaultRank) return rank
        }
        return null
    }
}