package dev.lbuddyboy.flash.transfer

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.UserPermission
import dev.lbuddyboy.flash.util.*
import dev.lbuddyboy.flash.util.bukkit.CC
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.Node
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.PrefixNode
import net.luckperms.api.node.types.SuffixNode
import org.apache.commons.lang.WordUtils
import org.bukkit.command.CommandSender
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.stream.Collectors

class LuckPermsTransport {
    fun transportUsers(sender: CommandSender) {
        sender.sendMessage(CC.translate("&3&l[LUCK PERMS] &fStarted transfer."))
        val callables: List<Callable> = ArrayList()
        val batch = Batch("LP Users -> Flash Users conversion", sender, callables, System.currentTimeMillis(), false)
        for (uuid in Flash.instance.cacheHandler.getUserCache().allUUIDs()) {
            batch.callbacks.add(Callable { Flash.instance.userHandler.deleteUser(uuid) })
        }
        try {
            for (uuid in l.userManager.uniqueUsers.get()) {
                var user = l.userManager.loadUser(
                    uuid!!
                ).get()
                if (user == null) {
                    l.userManager.savePlayerData(uuid, "")
                    user = l.userManager.loadUser(uuid).get()
                }
                val flashUser: User = Flash.instance.userHandler.createUser(uuid, user!!.username)
                for (node in user.nodes) {
                    if (node.expiryDuration == null) {
                        flashUser.addPerm(
                            UserPermission(
                                node.key,
                                Long.MAX_VALUE,
                                System.currentTimeMillis(),
                                null,
                                "Imported from LuckPerms"
                            )
                        )
                        continue
                    }
                    flashUser.addPerm(
                        UserPermission(
                            node.key,
                            node.expiryDuration!!.toMillis(),
                            System.currentTimeMillis(),
                            null,
                            "Imported from LuckPerms"
                        )
                    )
                }
                Flash.instance.userHandler.getUsers().put(uuid, flashUser)
            }
        } catch (ignored: InterruptedException) {
        } catch (ignored: ExecutionException) {
        }
        if (FlashLanguage.CACHE_TYPE.string.equals("YAML", ignoreCase = true) || FlashLanguage.CACHE_TYPE.string.equals(
                "YAML",
                ignoreCase = true
            )
        ) {
            try {
                Flash.instance.userHandler.getUsersYML().save()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        for (user in Flash.instance.userHandler.getUsers().values()) {
            batch.callbacks.add(Callable {
                Flash.instance.cacheHandler.getUserCache().update(user.getUuid(), user.getName(), true)
                user.save(false)
            })
        }
        Flash.instance.transportHandler.addBatch(batch)
    }

    fun transportRanks(sender: CommandSender) {
        sender.sendMessage(CC.translate("&3&l[LUCK PERMS] &fStarted transfer."))
        val callables: MutableList<Callable> = ArrayList()
        val batch = Batch("LP Ranks -> Flash Ranks conversion", sender, callables, System.currentTimeMillis(), false)
        Flash.instance.rankHandler.getRanks().values().forEach { rank -> batch.callbacks.add(rank::delete) }
        for (group in l.groupManager.loadedGroups) {
            println(group.name)
            val rank: Rank = Flash.instance.rankHandler.createRank(WordUtils.capitalize(group.name))
            rank.setUuid(UUID.randomUUID())
            rank.setDisplayName(group.displayName)
            val prefix = group.getNodes(NodeType.PREFIX).stream().map { obj: PrefixNode -> obj.key }
                .collect(Collectors.toList())[0]
            rank.setPrefix(prefix.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2])
            try {
                val suffix = group.getNodes(NodeType.SUFFIX).stream().map { obj: SuffixNode -> obj.key }
                    .collect(Collectors.toList())[0]
                rank.setSuffix(suffix.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2])
            } catch (ignored: Exception) {
            }
            rank.setPermissions(group.nodes.stream().filter { node: Node -> !node.isNegated }
                .map { obj: Node -> obj.key }.collect(Collectors.toList()))
            if (group.weight.isPresent) {
                rank.setWeight(group.weight.asInt)
            }
            rank.setInheritance(group.nodes.stream().map { obj: Node -> obj.key }
                .filter { s: String -> s.startsWith("group.") }
                .map { s: String -> s.replace("group.".toRegex(), "") }.collect(Collectors.toList()))
            rank.isDefaultRank = group.name.equals("default", ignoreCase = true)
            Flash.instance.rankHandler.getRanks().put(rank.getUuid(), rank)
            callables.add(Callable { rank.save(false) })
        }
        Flash.instance.transportHandler.addBatch(batch)
    }

    companion object {
        var l = LuckPermsProvider.get()
    }
}