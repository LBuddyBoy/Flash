package dev.lbuddyboy.flash.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.lbuddyboy.flash.rank.impl.RedisRank;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import lombok.Getter;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class GSONUtils {

    @Getter public static Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().create();

    public static final Type USER_PERMISSION = new TypeToken<List<UserPermission>>() {}.getType();
    public static final Type REDIS_USER = new TypeToken<RedisUser>() {}.getType();
    public static final Type REDIS_RANK = new TypeToken<RedisRank>() {}.getType();
    public static final Type STRING = new TypeToken<List<String>>() {}.getType();
    public static final Type GRANT = new TypeToken<List<Grant>>() {}.getType();
    public static final Type UUID = new TypeToken<List<UUID>>() {}.getType();

}
