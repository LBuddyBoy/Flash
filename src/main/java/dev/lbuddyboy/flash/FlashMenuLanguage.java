package dev.lbuddyboy.flash;

import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum FlashMenuLanguage {

    PUNISHMENT_SELECTION_MENU_TITLE("PUNISHMENT_SELECTION_MENU.TITLE", "Punishment Selection"),
    PUNISHMENTS_SELECTION_MENU_FILL("PUNISHMENT_SELECTION_MENU.FILL", true),

    PUNISHMENTS_SELECTION_SELECT_BUTTON_NAME("PUNISHMENT_SELECTION_MENU.SELECT-BUTTON.NAME", "%PUNISHMENT_TYPE%'s"),
    PUNISHMENTS_SELECTION_SELECT_BUTTON_LORE("PUNISHMENT_SELECTION_MENU.SELECT-BUTTON.LORE", Arrays.asList(
            " ",
            "&7Click to view the %PUNISHMENT_PLURAL%&7 of %TARGET_COLORED%&7.",
            " "
    )),

    PUNISHMENTS_MENU_TITLE("PUNISHMENTS_MENU.TITLE", "%PLAYER_DISPLAY%'s %PUNISHMENTS%"),
    PUNISHMENTS_MENU_FILL("PUNISHMENTS_MENU.FILL", true),

    PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_NAME("PUNISHMENTS_MENU.PUNISHMENT-REMOVED-BUTTON.NAME", "&4%ADDEDAT%"),
    PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_DATA("PUNISHMENTS_MENU.PUNISHMENT-REMOVED-BUTTON.DATA", 14),
    PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_LORE("PUNISHMENTS_MENU.PUNISHMENT-REMOVED-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4&lAdded Information",
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added On&7:&f %SERVER%",
            CC.MENU_BAR,
            "&4&lRemoved Information",
            CC.MENU_BAR,
            "&4Removed By&7:&f %REMOVEDBY%",
            "&4Removed For&7:&f %REMOVEDFOR%",
            "&4Removed At&7: &f%REMOVEDAT%",
            CC.MENU_BAR
    )),
    PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_MATERIAL("PUNISHMENTS_MENU.PUNISHMENT-REMOVED-BUTTON.MATERIAL", "WOOL"),

    PUNISHMENTS_MENU_PUNISHMENT_BUTTON_NAME("PUNISHMENTS_MENU.PUNISHMENT-BUTTON.NAME", "&4%ADDEDAT%"),
    PUNISHMENTS_MENU_PUNISHMENT_BUTTON_DATA("PUNISHMENTS_MENU.PUNISHMENT-BUTTON.DATA", 5),
    PUNISHMENTS_MENU_PUNISHMENT_BUTTON_LORE("PUNISHMENTS_MENU.PUNISHMENT-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added On&7:&f %SERVER%",
            "&4Time Left&7: &f%TIMELEFT%",
            "",
            "&fClick to remove this punishment.",
            CC.MENU_BAR
    )),
    PUNISHMENTS_MENU_PUNISHMENT_BUTTON_MATERIAL("PUNISHMENTS_MENU.PUNISHMENT-BUTTON.MATERIAL", "WOOL"),

    GRANT_MENU_TITLE("GRANT_MENU.TITLE", "Granting: %PLAYER%"),
    GRANT_MENU_SIZE("GRANT_MENU.SIZE", 27),
    GRANT_MENU_FILL("GRANT_MENU.FILL", true),

    GRANTS_MENU_TITLE("GRANTS_MENU.TITLE", "Grants: %PLAYER%"),

    GRANT_MENU_RANK_BUTTON_NAME("GRANT_MENU.RANK-BUTTON.NAME", "&bGrant a Rank"),
    GRANT_MENU_RANK_BUTTON_DATA("GRANT_MENU.RANK-BUTTON.DATA", 0),
    GRANT_MENU_RANK_BUTTON_SLOT("GRANT_MENU.RANK-BUTTON.SLOT", 13),
    GRANT_MENU_RANK_BUTTON_LORE("GRANT_MENU.RANK-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&fClick to grant &r%PLAYER%&f a rank.",
            CC.MENU_BAR
    )),
    GRANT_MENU_RANK_BUTTON_MATERIAL("GRANT_MENU.RANK-BUTTON.MATERIAL", "DIAMOND"),

    GRANT_MENU_PERMISSION_BUTTON_NAME("GRANT_MENU.PERMISSION-BUTTON.NAME", "&dGrant a Permission"),
    GRANT_MENU_PERMISSION_BUTTON_DATA("GRANT_MENU.PERMISSION-BUTTON.DATA", 0),
    GRANT_MENU_PERMISSION_BUTTON_SLOT("GRANT_MENU.PERMISSION-BUTTON.SLOT", 15),
    GRANT_MENU_PERMISSION_BUTTON_LORE("GRANT_MENU.PERMISSION-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&fClick to grant &r%PLAYER%&f a permission.",
            CC.MENU_BAR
    )),
    GRANT_MENU_PERMISSION_BUTTON_MATERIAL("GRANT_MENU.PERMISSION-BUTTON.MATERIAL", "PAPER"),

    GRANT_MENU_RANK_MENU_TITLE("GRANT_MENU.RANK-MENU.TITLE", "All Ranks"),
    GRANT_MENU_RANK_MENU_RANK_BUTTON_NAME("GRANT_MENU.RANK-MENU.RANK-BUTTON.NAME", "%RANK_COLORED%"),
    GRANT_MENU_RANK_MENU_RANK_BUTTON_LORE("GRANT_MENU.RANK-MENU.RANK-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&fClick to grant &r%PLAYER%&f the &r%RANK_COLORED% &frank.",
            CC.MENU_BAR
    )),

    GRANTS_MENU_DEFAULT_GRANT_BUTTON_NAME("GRANTS_MENU.GRANT-DEFAULT-BUTTON.NAME", "&4%ADDEDAT%"),
    GRANTS_MENU_DEFAULT_GRANT_BUTTON_DATA("GRANTS_MENU.GRANT-DEFAULT-BUTTON.DATA", 5),
    GRANTS_MENU_DEFAULT_GRANT_BUTTON_LORE("GRANTS_MENU.GRANT-DEFAULT-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added Rank&7: &f%RANK%",
            "&4Scopes&7: &f%SCOPES%",
            "&4Time Left&7: &f%TIMELEFT%",
            "",
            "&fClick to remove this grant.",
            CC.MENU_BAR
    )),
    GRANTS_MENU_DEFAULT_GRANT_BUTTON_MATERIAL("GRANTS_MENU.GRANT-DEFAULT-BUTTON.MATERIAL", "WOOL"),

    GRANTS_MENU_REMOVED_GRANT_BUTTON_NAME("GRANTS_MENU.GRANT-REMOVED-BUTTON.NAME", "&4%ADDEDAT%"),
    GRANTS_MENU_REMOVED_GRANT_BUTTON_DATA("GRANTS_MENU.GRANT-REMOVED-BUTTON.DATA", 14),
    GRANTS_MENU_REMOVED_GRANT_BUTTON_LORE("GRANTS_MENU.GRANT-REMOVED-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4&lAdded Information",
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added Rank&7: &f%RANK%",
            "&4Scopes&7: &f%SCOPES%",
            CC.MENU_BAR,
            "&4&lRemoved Information",
            CC.MENU_BAR,
            "&4Removed By&7:&f %REMOVEDBY%",
            "&4Removed For&7:&f %REMOVEDFOR%",
            "&4Removed At&7: &f%REMOVEDAT%",
            CC.MENU_BAR
    )),
    GRANTS_MENU_REMOVED_GRANT_BUTTON_MATERIAL("GRANTS_MENU.GRANT-REMOVED-BUTTON.MATERIAL", "WOOL"),


    GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_NAME("GRANTS_MENU.PERMISSION-DEFAULT-BUTTON.NAME", "&4%ADDEDAT%"),
    GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_DATA("GRANTS_MENU.PERMISSION-DEFAULT-BUTTON.DATA", 5),
    GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_LORE("GRANTS_MENU.PERMISSION-DEFAULT-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added Permission&7: &f%PERMISSION%",
            "&4Scopes&7: &f%SCOPES%",
            "&4Time Left&7: &f%TIMELEFT%",
            "",
            "&fClick to remove this permission.",
            CC.MENU_BAR
    )),
    GRANTS_MENU_DEFAULT_PERMISSION_BUTTON_MATERIAL("GRANTS_MENU.PERMISSION-DEFAULT-BUTTON.MATERIAL", "WOOL"),

    GRANTS_MENU_REMOVED_PERMISSION_BUTTON_NAME("GRANT_MENU.PERMISSION-REMOVED-BUTTON.NAME", "&4%ADDEDAT%"),
    GRANTS_MENU_REMOVED_PERMISSION_BUTTON_DATA("GRANT_MENU.PERMISSION-REMOVED-BUTTON.DATA", 14),
    GRANTS_MENU_REMOVED_PERMISSION_BUTTON_LORE("GRANT_MENU.PERMISSION-REMOVED-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4&lAdded Information",
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added Permission&7: &f%PERMISSION%",
            "&4Scopes&7: &f%SCOPES%",
            CC.MENU_BAR,
            "&4&lRemoved Information",
            CC.MENU_BAR,
            "&4Removed By&7:&f %REMOVEDBY%",
            "&4Removed For&7:&f %REMOVEDFOR%",
            "&4Removed At&7: &f%REMOVEDAT%",
            CC.MENU_BAR
    )),
    GRANTS_MENU_REMOVED_PERMISSION_BUTTON_MATERIAL("GRANT_MENU.PERMISSION-REMOVED-BUTTON.MATERIAL", "WOOL"),

    GRANTS_MENU_SWITCH_BUTTON_RANKS_NAME("GRANTS_MENU.SWITCH-BUTTON.RANKS.NAME", "&b&lView Ranks"),
    GRANTS_MENU_SWITCH_BUTTON_RANKS_DATA("GRANTS_MENU.SWITCH-BUTTON.RANKS.DATA", 0),
    GRANTS_MENU_SWITCH_BUTTON_RANKS_SLOT("GRANTS_MENU.SWITCH-BUTTON.RANKS.SLOT", 5),
    GRANTS_MENU_SWITCH_BUTTON_RANKS_LORE("GRANTS_MENU.SWITCH-BUTTON.RANKS.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&fClick to view all &brank&f grants.",
            CC.MENU_BAR
    )),
    GRANTS_MENU_SWITCH_BUTTON_RANKS_MATERIAL("GRANTS_MENU.SWITCH-BUTTON.RANKS.MATERIAL", "DIAMOND"),

    GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_NAME("GRANTS_MENU.SWITCH-BUTTON.PERMISSIONS.NAME", "&e&lView Permissions"),
    GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_DATA("GRANTS_MENU.SWITCH-BUTTON.PERMISSIONS.DATA", 0),
    GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_SLOT("GRANTS_MENU.SWITCH-BUTTON.PERMISSIONS.SLOT", 5),
    GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_LORE("GRANTS_MENU.SWITCH-BUTTON.PERMISSIONS.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&fClick to view all &epermission&f grants.",
            CC.MENU_BAR
    )),
    GRANTS_MENU_SWITCH_BUTTON_PERMISSIONS_MATERIAL("GRANTS_MENU.SWITCH-BUTTON.PERMISSIONS.MATERIAL", "PAPER");


    private final String path;
    private final Object value;
    
    public String getString() {
        if (Flash.getInstance().getMenusYML().gc().contains(this.path)) return Flash.getInstance().getMenusYML().gc().getString(this.path);

        loadDefault();

        return String.valueOf(this.value);
    }

    public boolean getBoolean() {
        if (Flash.getInstance().getMenusYML().gc().contains(this.path)) return Flash.getInstance().getMenusYML().gc().getBoolean(this.path);

        loadDefault();

        return Boolean.parseBoolean(String.valueOf(this.value));
    }

    public Material getMaterial() {
        return Material.getMaterial(getString());
    }

    public int getInt() {
        if (Flash.getInstance().getMenusYML().gc().contains(this.path)) return Flash.getInstance().getMenusYML().gc().getInt(this.path);

        loadDefault();

        return Integer.parseInt(String.valueOf(this.value));
    }

    public List<String> getStringList() {
        if (Flash.getInstance().getMenusYML().gc().contains(this.path)) return Flash.getInstance().getMenusYML().gc().getStringList(this.path);

        loadDefault();

        return (List<String>) this.value;
    }

    public void loadDefault() {
        if (Flash.getInstance().getMenusYML().gc().contains(this.path)) return;

        Flash.getInstance().getMenusYML().gc().set(this.path, this.value);
        try {
            Flash.getInstance().getMenusYML().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
