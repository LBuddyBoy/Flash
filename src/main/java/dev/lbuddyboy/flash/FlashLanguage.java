package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.util.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum FlashLanguage {

    CACHE_TYPE("SETTINGS.CACHE_TYPE", "REDIS"),
    SERVER_NAME("SETTINGS.SERVER_NAME", "Flash"),
    TIMEZONE("SETTINGS.TIMEZONE", "EST"),

    SERVER_CHANGE_JOIN("SERVER_CHANGE_JOIN", "&4&l[Staff] &c%PLAYER% &fhas just &ajoined&f &9%TO-SERVER% &ffrom &9%FROM-SERVER%&f."),
    SERVER_CHANGE_JOIN_NETWORK("SERVER_CHANGE_JOIN_NETWORK", "&4&l[Staff] &c%PLAYER% &fhas just &ajoined&f the network."),
    SERVER_CHANGE_LEAVE("SERVER_CHANGE_LEAVE", "&4&l[Staff] &c%PLAYER% &fhas just &cleft&f &9%FROM-SERVER% &ffrom &9%TO-SERVER%&f."),
    SERVER_CHANGE_LEAVE_NETWORK("SERVER_CHANGE_LEAVE_NETWORK", "&4&l[Staff] &c%PLAYER% &fhas just &cleft&f the network from &9%FROM-SERVER%&f."),

    RANK_SET_NAME("RANK_SET_NAME", "&aUpdated the &r%rank%'s&a name to &r%new-rank%&f."),
    RANK_SET_DISPLAY_NAME("RANK_SET_DISPLAY_NAME", "&aUpdated the &r%rank%'s&a display name from &r%old-display%&f to &r%new-display%&f."),
    RANK_SET_COLOR("RANK_SET_COLOR", "&aUpdated the &r%rank%'s&a color from &r%old-color%&f to &r%new-color%&f."),
    RANK_SET_WEIGHT("RANK_SET_WEIGHT", "&aUpdated the &r%rank%'s&a weight from &r%old-weight%&f to &r%new-weight%&f."),
    RANK_SET_PREFIX("RANK_SET_PREFIX", "&aUpdated the &r%rank%'s&a prefix from &r%old-prefix%&f to &r%new-prefix%&f."),
    RANK_SET_SUFFIX("RANK_SET_SUFFIX", "&aUpdated the &r%rank%'s&a suffix from &r%old-suffix%&f to &r%new-suffix%&f."),
    RANK_ADD_PERM("RANK_ADD_PERM", "&aAdded the &f%permission%&a to the &r%rank%'s permission list&a."),
    RANK_REMOVE_PERM("RANK_REMOVE_PERM", "&aRemoved the &f%permission%&a from the &r%rank%'s permission list&a."),
    RANK_CREATE("RANK_CREATE", "&aCreated the &r%rank%&f."),
    RANK_DELETE("RANK_DELETE", "&aDeleted the &r%rank%&f."),
    RANK_SET_DEFAULT("RANK_SET_DEFAULT", "&aDeleted the &r%rank%&f."),
    RANK_LIST_HEADER("RANK_LIST_HEADER", Arrays.asList(
            CC.CHAT_BAR,
            "&4&lRank List",
            CC.CHAT_BAR
    )),
    RANK_LIST_FORMAT("RANK_LIST_FORMAT", CC.UNICODE_ARROW_RIGHT + " &r%display% &7(%rank%) (W: %weight%) (P: %prefix%) (S: %suffix%) (D: %default%)"),

    GRANTED_USER_PERMISSION("GRANTED_USER_PERMISSION", "&aGranted &r%PLAYER_DISPLAY% &athe &r%PERMISSION%&a. Expires in: &e%DURATION%"),
    GRANTED_USER_RANK("GRANTED_USER_RANK", "&aGranted &r%PLAYER_DISPLAY% &athe &r%RANK%&a. Expires in: &e%DURATION%");

    private final String path;
    private final Object value;
    
    public String getString() {
        if (Flash.getInstance().getConfig().contains(this.path)) return Flash.getInstance().getConfig().getString(this.path);

        loadDefault();

        return String.valueOf(this.value);
    }

    public int getInt() {
        if (Flash.getInstance().getConfig().contains(this.path)) return Flash.getInstance().getConfig().getInt(this.path);

        loadDefault();

        return Integer.parseInt(String.valueOf(this.value));
    }

    public List<String> getStringList() {
        if (Flash.getInstance().getConfig().contains(this.path)) return Flash.getInstance().getConfig().getStringList(this.path);

        loadDefault();

        return (List<String>) this.value;
    }

    public void loadDefault() {
        Flash.getInstance().getConfig().set(this.path, this.value);
        Flash.getInstance().saveConfig();
    }

}
