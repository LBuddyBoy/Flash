package dev.lbuddyboy.flash.user.menu.staffhistory;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashMenuLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Demotion;
import dev.lbuddyboy.flash.user.model.Promotion;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.*;
import dev.lbuddyboy.flash.util.menu.Button;
import dev.lbuddyboy.flash.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class StaffHistorySelectionMenu extends Menu {

    public UUID target;

    @Override
    public String getTitle(Player player) {
        return "Staff History: " + UserUtils.formattedName(target);
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new DemotionHistoryButton(target));
        buttons.add(new PromotionHistoryButton(target));
        buttons.add(new UserButton(target));

        buttons.add(new PunishmentSelectButton(28, PunishmentType.KICK, target));
        buttons.add(new PunishmentSelectButton(31, PunishmentType.MUTE, target));
        buttons.add(new PunishmentSelectButton(32, PunishmentType.BAN, target));
        buttons.add(new PunishmentSelectButton(33, PunishmentType.IP_BAN, target));
        buttons.add(new PunishmentSelectButton(36, PunishmentType.BLACKLIST, target));

        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return 45;
    }

    @Override
    public boolean autoFill() {
        return true;
    }

    @AllArgsConstructor
    private static class UserButton extends Button {

        private UUID target;

        @Override
        public int getSlot() {
            return 14;
        }

        @Override
        public ItemStack getItem() {
            User user = Flash.getInstance().getUserHandler().tryUser(target, true);

            return new ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                    .setName(UserUtils.formattedName(target))
                    .setLore(
                            CC.MENU_BAR,
                            "&gCurrently Staff&7: &f" + (user.getActiveRank().isStaff() ? "&aYes" : "&cNo"),
                            "&gPlay Time&7: &f" + TimeUtils.formatLongIntoMMSS(user.getStaffInfo().getPlayTime()),
                            "&gJoined Staff Team&7: &f" + user.getStaffInfo().getJoinedStaffTeamDate(),
                            CC.MENU_BAR
                    )
                    .setDurability(3)
                    .setOwner(Flash.getInstance().getCacheHandler().getUserCache().getName(target))
                    .create();
        }
    }

    @AllArgsConstructor
    private static class PromotionHistoryButton extends Button {

        public UUID target;

        @Override
        public int getSlot() {
            return 12;
        }

        @Override
        public ItemStack getItem() {
            ItemBuilder builder = new ItemBuilder(Material.EMERALD_BLOCK).setName("&g&lPromotion History");

            List<String> lore = new ArrayList<>(Collections.singletonList(
                    CC.MENU_BAR
            ));

            User user = Flash.getInstance().getUserHandler().tryUser(target, true);

            if (user.getPromotions().isEmpty()) {
                lore.add("&cNo promotions could be found.");
            }

            for (Promotion promotion : user.getPromotions()) {
                lore.add(promotion.getPromotedFrom() + " &f-> " + promotion.getPromotedTo() + " &7(" + promotion.getPromotedAtDate() + ")");
            }

            lore.add(CC.MENU_BAR);
            builder.setLore(lore);

            return builder.create();
        }
    }

    @AllArgsConstructor
    private static class DemotionHistoryButton extends Button {

        public UUID target;

        @Override
        public int getSlot() {
            return 16;
        }

        @Override
        public ItemStack getItem() {
            ItemBuilder builder = new ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&g&lDemotion History");

            List<String> lore = new ArrayList<>(Collections.singletonList(
                    CC.MENU_BAR
            ));

            User user = Flash.getInstance().getUserHandler().tryUser(target, true);

            if (user.getDemotions().isEmpty()) {
                lore.add("&cNo demotions could be found.");
            }

            for (Demotion demotion : user.getDemotions()) {
                lore.add(demotion.getDemotedFrom() + " &7(" + demotion.getDemotedAtDate() + ")");
            }

            lore.add(CC.MENU_BAR);
            builder.setLore(lore);

            return builder.create();
        }
    }

    @AllArgsConstructor
    private static class PunishmentSelectButton extends Button {

        public int slot;
        public PunishmentType type;
        public UUID target;

        @Override
        public int getSlot() {
            return slot;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.WOOL)
                    .setName(CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_NAME.getString(), target), "%PUNISHMENT_TYPE%", type.getDisplay())
                    .setLore(CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_LORE.getStringList(), target), "%PUNISHMENT_PLURAL%", type.getPlural())
                    .setDurability(ColorUtil.COLOR_MAP.get(type.getColor()).getWoolData())
                    .create();
        }

        @Override
        public void action(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();

            new StaffHistoryMenu(target, type).openMenu(player);
        }
    }

}
