package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.menu.SearchUsersMenu;
import dev.lbuddyboy.flash.user.model.UserPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandAlias("lookup|paramlookup")
@CommandPermission("flash.command.lookup")
public class LookUpCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public void lookup(Player sender, @Name("params {rank:<rank>|permission:<perm>}") String param) {
        List<User> users = new ArrayList<>();

        if (param.startsWith("permission:")) {
            String[] args = param.split(":");

            for (UUID target : Flash.getInstance().getCacheHandler().getUserCache().allUUIDs()) {
                User user = Flash.getInstance().getUserHandler().tryUser(target, true);
                if (user == null) continue;

                if (user.getActivePermissions().stream().map(UserPermission::getNode).collect(Collectors.toList()).contains(args[1])) {
                    users.add(user);
                }
            }
        } else if (param.startsWith("rank:")) {
            String[] args = param.split(":");

            for (UUID target : Flash.getInstance().getCacheHandler().getUserCache().allUUIDs()) {
                User user = Flash.getInstance().getUserHandler().tryUserRank(target, true);
                if (user == null) continue;

                if (user.getActiveRank().getName().equalsIgnoreCase(args[1])) {
                    users.add(user);
                }
            }
        }


        new SearchUsersMenu(users).openMenu(sender);

    }

}
