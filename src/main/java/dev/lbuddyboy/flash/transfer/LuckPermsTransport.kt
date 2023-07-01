package dev.lbuddyboy.flash.transfer;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.util.Batch;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.Callable;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LuckPermsTransport {

    static LuckPerms l = LuckPermsProvider.get();

    public void transportUsers(CommandSender sender) {
        sender.sendMessage(CC.translate("&3&l[LUCK PERMS] &fStarted transfer."));

        List<Callable> callables = new ArrayList<>();
        Batch batch = new Batch("LP Users -> Flash Users conversion", sender, callables, System.currentTimeMillis(), false);

        for (UUID uuid : Flash.getInstance().getCacheHandler().getUserCache().allUUIDs()) {
            batch.getCallbacks().add(() -> Flash.getInstance().getUserHandler().deleteUser(uuid));
        }

        try {
            for (UUID uuid : l.getUserManager().getUniqueUsers().get()) {
                User user = l.getUserManager().loadUser(uuid).get();

                if (user == null) {
                    l.getUserManager().savePlayerData(uuid, "");
                    user = l.getUserManager().loadUser(uuid).get();
                }

                dev.lbuddyboy.flash.user.User flashUser = Flash.getInstance().getUserHandler().createUser(uuid, user.getUsername());
                for (Node node : user.getNodes()) {
                    if (node.getExpiryDuration() == null) {
                        flashUser.addPerm(new UserPermission(node.getKey(), Long.MAX_VALUE, System.currentTimeMillis(), null, "Imported from LuckPerms"));
                        continue;
                    }
                    flashUser.addPerm(new UserPermission(node.getKey(), node.getExpiryDuration().toMillis(), System.currentTimeMillis(), null, "Imported from LuckPerms"));
                }

                Flash.getInstance().getUserHandler().getUsers().put(uuid, flashUser);
            }
        } catch (InterruptedException | ExecutionException ignored) {

        }

        if (FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("YAML") || FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("YAML")) {
            try {
                Flash.getInstance().getUserHandler().getUsersYML().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (dev.lbuddyboy.flash.user.User user : Flash.getInstance().getUserHandler().getUsers().values()) {
            batch.getCallbacks().add(() -> {
                Flash.getInstance().getCacheHandler().getUserCache().update(user.getUuid(), user.getName(), true);
                user.save(false);
            });
        }

        Flash.getInstance().getTransportHandler().addBatch(batch);
    }

    public void transportRanks(CommandSender sender) {

        sender.sendMessage(CC.translate("&3&l[LUCK PERMS] &fStarted transfer."));

        List<Callable> callables = new ArrayList<>();
        Batch batch = new Batch("LP Ranks -> Flash Ranks conversion", sender, callables, System.currentTimeMillis(), false);

        Flash.getInstance().getRankHandler().getRanks().values().forEach(rank -> batch.getCallbacks().add(rank::delete));

        for (Group group : l.getGroupManager().getLoadedGroups()) {
            System.out.println(group.getName());
            Rank rank = Flash.getInstance().getRankHandler().createRank(WordUtils.capitalize(group.getName()));
            rank.setUuid(UUID.randomUUID());

            rank.setDisplayName(group.getDisplayName());

            String prefix = group.getNodes(NodeType.PREFIX).stream().map(Node::getKey).collect(Collectors.toList()).get(0);
            rank.setPrefix(prefix.split("\\.")[2]);
            try {
                String suffix = group.getNodes(NodeType.SUFFIX).stream().map(Node::getKey).collect(Collectors.toList()).get(0);
                rank.setSuffix(suffix.split("\\.")[2]);
            } catch (Exception ignored) {

            }
            rank.setPermissions(group.getNodes().stream().filter(node -> !node.isNegated()).map(Node::getKey).collect(Collectors.toList()));

            if (group.getWeight().isPresent()) {
                rank.setWeight(group.getWeight().getAsInt());
            }

            rank.setInheritance(group.getNodes().stream().map(Node::getKey).filter(s -> s.startsWith("group.")).map(s -> s.replaceAll("group.", "")).collect(Collectors.toList()));
            rank.setDefaultRank(group.getName().equalsIgnoreCase("default"));

            Flash.getInstance().getRankHandler().getRanks().put(rank.getUuid(), rank);

            callables.add(() -> rank.save(false));
        }

        Flash.getInstance().getTransportHandler().addBatch(batch);

    }

}
