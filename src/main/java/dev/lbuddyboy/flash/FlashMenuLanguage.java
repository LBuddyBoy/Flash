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
public enum FlashMenuLanguage {

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

    GRANTS_MENU_DEFAULT_GRANT_BUTTON_NAME("GRANTS_MENU.GRANT-DEFAULT-BUTTON.NAME", "&4%ADDEDAT%"),
    GRANTS_MENU_DEFAULT_GRANT_BUTTON_DATA("GRANTS_MENU.GRANT-DEFAULT-BUTTON.DATA", 5),
    GRANTS_MENU_DEFAULT_GRANT_BUTTON_LORE("GRANTS_MENU.GRANT-DEFAULT-BUTTON.LORE", Arrays.asList(
            CC.MENU_BAR,
            "&4Added By&7:&f %ADDEDBY%",
            "&4Added For&7:&f %ADDEDFOR%",
            "&4Added Rank&7: &f%RANK%",
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
            CC.MENU_BAR,
            "&4&lRemoved Information",
            CC.MENU_BAR,
            "&4Removed By&7:&f %REMOVEDBY%",
            "&4Removed For&7:&f %REMOVEDFOR%",
            "&4Removed At&7: &f%REMOVEDAT%",
            CC.MENU_BAR
    )),
    GRANTS_MENU_REMOVED_PERMISSION_BUTTON_MATERIAL("GRANT_MENU.PERMISSION-REMOVED-BUTTON.MATERIAL", "WOOL");


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
        Flash.getInstance().getMenusYML().gc().set(this.path, this.value);
        try {
            Flash.getInstance().getMenusYML().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
