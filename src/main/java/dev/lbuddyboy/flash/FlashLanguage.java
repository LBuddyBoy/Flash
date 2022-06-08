package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum FlashLanguage {

    CACHE_TYPE("SETTINGS.CACHE_TYPE", "REDIS"),
    SERVER_NAME("SETTINGS.SERVER_NAME", "Flash"),
    SERVER_IP("SETTINGS.SERVER_IP", "lbuddyboy.me"),
    TIMEZONE("SETTINGS.TIMEZONE", "EST"),
    FORMAT_CHAT("SETTINGS.FORMAT-CHAT", true),
    CHAT_MUTED("SETTINGS.CHAT-MUTED", false),
    CHAT_SLOW("SETTINGS.CHAT-SLOW", 0),

    NAMEMC_RANK_CLAIM_COMMANDS("NAMEMC.CLAIMED_COMMANDS", Arrays.asList(
            "user addrank %TARGET% Flash perm Global Claimed NameMC",
            "bc &4&l[NAMEMC] &r%TARGET_COLORED% &fhas just liked &c&nlbuddyboy.me&f on NameMC by doing &9&n/namemc&f!"
    )),

    ESSENTIALS_HELPOP_COOLDOWN("ESSENTIALS.HELPOP.COOLDOWN", 60),
    ESSENTIALS_HELPOP_SENDER_MESSAGE("ESSENTIALS.HELPOP.MESSAGE.SENDER", "&aYou have requested for assistance and a staff will be with you shortly."),
    ESSENTIALS_HELPOP_STAFF_MESSAGE("ESSENTIALS.HELPOP.COOLDOWN", Arrays.asList(
            "&4&l[HELPOP] %PLAYER_COLORED% &fneeds &eassistance&f on &c%SERVER%&f.",
            "&7 - &cReason&7: &f%REASON%"
    )),
    ESSENTIALS_REPORT_SENDER_MESSAGE("ESSENTIALS.REPORT.MESSAGE.SENDER", "&aYou have requested for assistance and a staff will be with you shortly."),
    ESSENTIALS_REPORT_STAFF_MESSAGE("ESSENTIALS.REPORT.COOLDOWN", Arrays.asList(
            "&4&l[REPORT] %PLAYER_COLORED% &fneeds &eassistance&f on &c%SERVER%&f.",
            "&7 - &cReported&7: &f%TARGET_COLORED%",
            "&7 - &cReason&7: &f%REASON%"
    )),
    ESSENTIALS_BROADCAST_PREFIX("ESSENTIALS.BROADCAST.PREFIX", "&7[&cAlert&7] "),

    PUNISHMENT_WARN_COLOR("PUNISHMENTS.WARN.COLOR", "GRAY"),
    PUNISHMENT_KICK_COLOR("PUNISHMENTS.KICK.COLOR", "GREEN"),
    PUNISHMENT_MUTE_COLOR("PUNISHMENTS.MUTE.COLOR", "YELLOW"),
    PUNISHMENT_BAN_COLOR("PUNISHMENTS.BAN.COLOR", "RED"),
    PUNISHMENT_IP_BAN_COLOR("PUNISHMENTS.IP_BAN.COLOR", "AQUA"),
    PUNISHMENT_BLACKLIST_COLOR("PUNISHMENTS.BLACKLIST.COLOR", "BLACK"),

    PUNISHMENTS_DEFAULT_PUNISH_TIME("PUNISHMENTS.HIGHER-PRIORITY", "7d"),

    PUNISHMENT_KICK_FORMAT("PUNISHMENTS.KICK.KICK-FORMAT", "&cYour have been kicked from the Flash Network.\n&cReason: %REASON%"),
    PUNISHMENT_BAN_KICK_FORMAT("PUNISHMENTS.BAN.KICK-FORMAT", "&cYour account is banned from the Flash Network.\n%TEMP-FORMAT%\n\n&cIf you feel this punishment is unjust, you may appeal at:\nhttp://www.lbuddyboy.me"),
    PUNISHMENT_IP_BAN_KICK_FORMAT("PUNISHMENTS.IP_BAN.KICK-FORMAT", "&cYour account is ip banned from the Flash Network.\n%TEMP-FORMAT%\n\n&cIf you feel this punishment is unjust, you may appeal at:\nhttp://www.lbuddyboy.me"),
    PUNISHMENT_BLACKLIST_KICK_FORMAT("PUNISHMENTS.BLACKLIST.KICK-FORMAT", "&cYour account is blacklisted from the Flash Network.\n%TEMP-FORMAT%\n\n&cThis punishment can not be appealed."),
    PUNISHMENTS_ALREADY_PUNISHED("PUNISHMENTS.ALREADY-PUNISHED", "&cThat player is already %FORMAT%."),
    PUNISHMENTS_NOT_PUNISHED("PUNISHMENTS.NOT-PUNISHED", "&cThat player is not %FORMAT%."),
    PUNISHMENTS_HIGHER_PRIORITY("PUNISHMENTS.HIGHER-PRIORITY", "&cYou cannot punish that player, as their rank has more priority than yours."),

    PUNISHMENT_MUTED_FORMAT("PUNISHMENTS.MUTE.FORMAT", Arrays.asList(
            "&cYou are currently muted!",
            "&c- Reason: %REASON%",
            "&c- Expires in: %TIME-LEFT%"
    )),

    PUNISHMENT_TEMPORARY_FORMAT("PUNISHMENTS.TEMPORARY-FORMAT", "&cExpires: %TIME%"),
    PUNISHMENT_BANNED_IP_RELATIVE("PUNISHMENTS.IP_BAN.KICK-ALT-FORMAT", "&cYour account is banned from the Flash Network.\n&cDue to having relations with %OWNER%"),
    PUNISHMENT_PUNISH_PUBLIC_BROADCAST_FORMAT("PUNISHMENTS.PUNISH.PUBLIC-BROADCAST-FORMAT", "&r%TARGET_COLORED%&a has just been %TIME%&a %FORMAT% by &r%SENDER_DISPLAY%&a."),
    PUNISHMENT_PUNISH_SILENT_BROADCAST_FORMAT("PUNISHMENTS.PUNISH.SILENT-BROADCAST-FORMAT", "&r%TARGET_COLORED%&a has just been %TIME% &esilently&a %FORMAT% by &r%SENDER_DISPLAY%&a."),

    PUNISHMENT_UNPUNISH_PUBLIC_BROADCAST_FORMAT("PUNISHMENTS.UNPUNISH.PUBLIC-BROADCAST-FORMAT", "&r%TARGET_COLORED%&a has just been&a %FORMAT% by &r%SENDER_DISPLAY%&a."),
    PUNISHMENT_UNPUNISH_SILENT_BROADCAST_FORMAT("PUNISHMENTS.UNPUNISH.SILENT-BROADCAST-FORMAT", "&r%TARGET_COLORED%&a has just been &esilently&a %FORMAT% by &r%SENDER_DISPLAY%&a."),

    SERVER_CHANGE_JOIN("GLOBAL.SERVER_CHANGE_JOIN", "&4&l[Staff] &c%PLAYER% &fhas just &ajoined&f &9%SERVER%&f."),
//    SERVER_CHANGE_JOIN_NETWORK("GLOBAL.SERVER_CHANGE_JOIN_NETWORK", "&4&l[Staff] &c%PLAYER% &fhas just &ajoined&f the network."),
    SERVER_CHANGE_LEAVE("GLOBAL.SERVER_CHANGE_LEAVE", "&4&l[Staff] &c%PLAYER% &fhas just &cleft&f &9%SERVER%&f."),
//    SERVER_CHANGE_LEAVE_NETWORK("GLOBAL.SERVER_CHANGE_LEAVE_NETWORK", "&4&l[Staff] &c%PLAYER% &fhas just &cleft&f the network from &9%FROM-SERVER%&f."),

    RANK_EXISTS("RANK.EXISTS", "&cThat rank already exists."),
    RANK_DOES_NOT_EXIST("RANK.DOES_NOT_EXIST", "&cThat rank does not exist."),
    RANK_SET_NAME("RANK.SET_NAME", "&aUpdated the &r%rank%'s&a name to &r%new-rank%&f."),
    RANK_SET_DISPLAY_NAME("RANK.SET_DISPLAY_NAME", "&aUpdated the &r%rank%'s&a display name from &r%old-display%&f to &r%new-display%&f."),
    RANK_SET_COLOR("RANK.SET_COLOR", "&aUpdated the &r%rank%'s&a color from &r%old-color%&f to &r%new-color%&f."),
    RANK_SET_WEIGHT("RANK.SET_WEIGHT", "&aUpdated the &r%rank%'s&a weight from &r%old-weight%&f to &r%new-weight%&f."),
    RANK_SET_PREFIX("RANK.SET_PREFIX", "&aUpdated the &r%rank%'s&a prefix from &r%old-prefix%&f to &r%new-prefix%&f."),
    RANK_SET_SUFFIX("RANK.SET_SUFFIX", "&aUpdated the &r%rank%'s&a suffix from &r%old-suffix%&f to &r%new-suffix%&f."),
    RANK_ADD_PERM("RANK.ADD_PERM", "&aAdded the &f%permission%&a to the &r%rank%'s permission list&a."),
    RANK_REMOVE_PERM("RANK.REMOVE_PERM", "&aRemoved the &f%permission%&a from the &r%rank%'s permission list&a."),
    RANK_ADD_INHERIT("RANK.ADD_INHERIT", "&aAdded the &f%inherit%&a to the &r%rank%'s inheritance&a."),
    RANK_REMOVE_INHERIT("RANK.REMOVE_INHERIT", "&aRemoved the &f%inherit%&a from the &r%rank%'s inheritance&a."),
    RANK_CREATE("RANK.CREATE", "&aCreated the &r%rank%&f."),
    RANK_DELETE("RANK.DELETE", "&aDeleted the &r%rank%&f."),
    RANK_SET_DEFAULT("RANK.SET_DEFAULT", "&aToggled the default status of &r%rank%&f from %old-status% to %new-status%."),

    RANK_LIST_HEADER("RANK.LIST_HEADER", Arrays.asList(
            CC.CHAT_BAR,
            "&4&lRank List",
            CC.CHAT_BAR
    )),
    RANK_LIST_FORMAT("RANK.LIST_FORMAT", CC.UNICODE_ARROW_RIGHT + " &r%display%&7 &7(%rank%) (W: %weight%) (P: %prefix%&7) (S: %suffix%&7) (D: %default%)"),
    RANK_HELP("RANK.HELP_HEADER", Arrays.asList(
            CC.CHAT_BAR,
            "&4&lRank Command Help &7(%page%/%max-pages%)",
            CC.CHAT_BAR
    )),

    INVALID_USER("USER.INVALID_USER", "&cCould not find that user in our database."),
    USER_COMMAND_HELP("USER.HELP_HEADER", Arrays.asList(
            CC.CHAT_BAR,
            "&4&lUser Command Help &7(%page%/%max-pages%)",
            CC.CHAT_BAR
    )),

    GRANTED_USER_PERMISSION_TARGET("USER.GRANTED_PERMISSION_TARGET", "&aYou have been granted the &f%PERMISSION%&a permission. Expires in: &e%DURATION%"),
    GRANTED_USER_RANK_TARGET("USER.GRANTED_RANK_TARGET", "&aYou have been granted the &r%RANK%&a rank. Expires in: &e%DURATION%"),

    GRANTED_USER_PERMISSION_SENDER("USER.GRANTED_PERMISSION_SENDER", "&aGranted &r%PLAYER_DISPLAY% &athe &r%PERMISSION%&a permission. Expires in: &e%DURATION%"),
    GRANTED_USER_RANK_SENDER("USER.GRANTED_RANK_SENDER", "&aGranted &r%PLAYER_DISPLAY% &athe &r%RANK%&a rank. Expires in: &e%DURATION%"),

    TIPS_LIST("TIPS.TIPS", Arrays.asList(
            "&6&l[TIP-1] &fPut something here",
            "&6&l[TIP-2] &fPut something else here"
    )),

    TIPS_DELAY_SECONDS("TIPS.DELAY_SECONDS", 60);

    private final String path;
    private final Object value;
    
    public String getString() {
        if (Flash.getInstance().getConfig().contains(this.path))
            return Flash.getInstance().getConfig().getString(this.path);

        loadDefault();

        return String.valueOf(this.value);
    }

    public boolean getBoolean() {
        if (Flash.getInstance().getConfig().contains(this.path))
            return Flash.getInstance().getConfig().getBoolean(this.path);

        loadDefault();

        return Boolean.parseBoolean(String.valueOf(this.value));
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

    public void update(Object value) {
        Flash.getInstance().getConfig().set(this.path, value);
        Flash.getInstance().saveConfig();
    }

    public void loadDefault() {
        if (Flash.getInstance().getConfig().contains(this.path)) return;

        Flash.getInstance().getConfig().set(this.path, this.value);
        Flash.getInstance().saveConfig();
    }

}
