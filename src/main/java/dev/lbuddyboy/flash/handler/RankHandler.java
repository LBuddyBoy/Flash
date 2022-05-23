package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.command.RankCommand;
import dev.lbuddyboy.flash.rank.command.param.RankParam;
import dev.lbuddyboy.flash.rank.comparator.RankWeightComparator;
import dev.lbuddyboy.flash.rank.impl.FlatFileRank;
import dev.lbuddyboy.flash.rank.impl.MongoRank;
import dev.lbuddyboy.flash.rank.impl.RedisRank;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.impl.FlatFileUser;
import dev.lbuddyboy.flash.user.impl.MongoUser;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.util.Tasks;
import dev.lbuddyboy.flash.util.YamlDoc;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class RankHandler {

    private Map<UUID, Rank> ranks;
    private YamlDoc ranksYML;
    private Rank defaultRank;

    public RankHandler() {
        this.ranks = new ConcurrentHashMap<>();

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "FLATFILE":
            case "YAML":
                this.ranksYML = new YamlDoc(Flash.getInstance().getDataFolder(), "ranks.yml");
                break;
        }

        Tasks.run(() -> {
            loadAll();

            for (Rank rank : ranks.values()) {
                if (rank.isDefaultRank()) {
                    this.defaultRank = rank;
                    break;
                }
            }
            if (getDefaultRank() == null) {
                this.defaultRank = createRank("Default");
                this.defaultRank.setDefaultRank();
                ranks.put(this.defaultRank.getUuid(), this.defaultRank);
            }

            new RanksUpdatePacket(this.ranks).send();
        });
    }

    public void loadAll() {
        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS": {
                for (Map.Entry<String, String> entry : RedisHandler.requestJedis().getResource().hgetAll("Ranks").entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    this.ranks.put(UUID.fromString(key), GSONUtils.getGSON().fromJson(value, GSONUtils.REDIS_RANK));
                }
                break;
            }
            case "MONGO": {
                for (Document document : Flash.getInstance().getMongoHandler().getRankCollection().find()) {
                    UUID uuid = UUID.fromString(document.getString("uuid"));

                    this.ranks.put(uuid, new MongoRank(uuid, document.getString("name")));
                }
                break;
            }
            case "FLATFILE":
            case "YAML": {
                try {
                    for (String key : this.ranksYML.gc().getConfigurationSection("ranks").getKeys(false)) {
                        UUID uuid = UUID.fromString(key);
                        String name = this.ranksYML.gc().getString("ranks." + key + ".name");

                        this.ranks.put(uuid, new FlatFileRank(uuid, name));
                    }
                } catch (Exception ignored) {

                }
                break;
            }
        }
    }

    public Rank getRank(String name) {
        for (Rank rank : this.ranks.values()) {
            if (rank.getName().equals(name)) return rank;
        }
        return null;
    }

    public Rank createRank(String name) {
        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS": return new RedisRank(name);
            case "MONGO": return new MongoRank(name);
            case "FLATFILE":
            case "YAML": return new FlatFileRank(name);
            default: return null;
        }
    }

    public List<Rank> getSortedRanks() {
        return ranks.values().stream().sorted(new RankWeightComparator().reversed()).collect(Collectors.toList());
    }

    public Rank getDefaultRank() {
        for (Rank rank : getRanks().values()) {
            if (rank.isDefaultRank()) return rank;
        }
        return null;
    }

}
