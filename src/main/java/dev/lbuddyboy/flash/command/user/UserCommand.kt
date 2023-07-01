package dev.lbuddyboy.flash.command.user

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.user.model.Promotion
import dev.lbuddyboy.flash.user.model.UserPermission
import dev.lbuddyboy.flash.user.packet.GlobalMessagePacket
import dev.lbuddyboy.flash.user.packet.GrantAddPacket
import dev.lbuddyboy.flash.user.packet.PermissionAddPacket
import dev.lbuddyboy.flash.util.*
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.gson.GSONUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("user|profile")
@CommandPermission("flash.command.user")
object UserCommand : BaseCommand() {
    @Default
    fun def(sender: CommandSender, @Name("page") @Default("1") page: Int) {
        val item = PagedItem(COMMANDS, FlashLanguage.USER_COMMAND_HELP.stringList, 5)
        item.send(sender, page)
        sender.sendMessage(CC.CHAT_BAR)
    }

    @Subcommand("help")
    fun help(sender: CommandSender, @Name("page") @Default("1") page: Int) {
        def(sender, page)
    }

    @Subcommand("info")
    @CommandCompletion("@target")
    fun info(sender: CommandSender, @Name("target") uuid: UUID?) {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        sender.sendMessage(GSONUtils.getGSON().toJson(user, GSONUtils.USER))
    }

    @Subcommand("grantperm|addperm|addpermission")
    @CommandCompletion("@target @permissions")
    fun permissionAdd(
        sender: CommandSender,
        @Name("user") uuid: UUID,
        @Split @Name("permissions") permissions: Array<String?>,
        @Single @Name("duration") duration: String,
        @Name("reason") reason: String?
    ) {
        var time = JavaUtils.parse(duration)
        if (duration.equals("perm", ignoreCase = true)) time = Long.MAX_VALUE
        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."))
            return
        }
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (Arrays.asList(*permissions).contains("*") && sender !is Player) {
            sender.sendMessage(CC.translate("&cOnly console can grant * permissions."))
            return
        }
        for (permission in permissions) {
            val userPermission = UserPermission(
                permission,
                time,
                System.currentTimeMillis(),
                if (sender is Player) sender.uniqueId else null,
                reason
            )
            user.getPermissions().add(userPermission)
            sender.sendMessage(
                CC.translate(
                    FlashLanguage.GRANTED_USER_PERMISSION_SENDER.string,
                    "%PLAYER_DISPLAY%", user.displayName,
                    "%PERMISSION%", userPermission.node,
                    "%DURATION%", userPermission.expireString!!
                )
            )
            val message = CC.translate(
                FlashLanguage.GRANTED_USER_PERMISSION_TARGET.string,
                "%PERMISSION%", userPermission.node,
                "%DURATION%", userPermission.expireString!!
            )
            GlobalMessagePacket(uuid, message).send()
        }
        if (Bukkit.getPlayer(uuid) == null) {
            user.save(true)
        }
        user.updatePerms()
        PermissionAddPacket(uuid, user.getPermissions()).send()
    }

    @Subcommand("addpromotion")
    @CommandCompletion("@target")
    fun promotionAdd(
        sender: CommandSender,
        @Name("user") uuid: UUID?,
        @Name("time-long") time: Long,
        @Single @Name("fromRank") fromRank: String?,
        @Single @Name("toRank") toRank: String?
    ) {
        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."))
            return
        }
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        val promotion = Promotion(fromRank, toRank, time)
        user.getPromotions().add(promotion)
        user.save(true)
    }

    @Subcommand("setjoinedstaffteam")
    @CommandCompletion("@target")
    fun setjoinedstaffteam(sender: CommandSender, @Name("user") uuid: UUID?, @Name("time-long") time: Long) {
        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."))
            return
        }
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        user.getStaffInfo().joinedStaffTeam = time
        user.save(true)
    }

    @Subcommand("grantrank|addrank")
    @CommandCompletion("@target @rank")
    fun rankAdd(
        sender: CommandSender,
        @Name("user") uuid: UUID,
        @Single @Name("rank") rank: Rank?,
        @Name("duration") @Single duration: String,
        @Name("scopes") @Split scopes: Array<String?>?,
        @Name("reason") reason: String?
    ) {
        var time = JavaUtils.parse(duration)
        if (duration.equals("perm", ignoreCase = true)) time = Long.MAX_VALUE
        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."))
            return
        }
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (sender is Player) {
            val senderUser: User = Flash.instance.userHandler.tryUser(
                sender.uniqueId, true
            )
            if (senderUser.activeRank.getWeight() < rank.getWeight()) {
                sender.sendMessage(CC.translate("&cThat rank is too high for you to grant..."))
                return
            }
            if (!sender.hasPermission("grant.staff") && rank.isStaff()) {
                sender.sendMessage(CC.translate("&cYou do not have clearance to grant staff ranks..."))
                return
            }
        }
        val grant = Grant(
            UUID.randomUUID(),
            rank.getUuid(),
            rank.getName(),
            if (sender is Player) sender.uniqueId else null,
            reason,
            System.currentTimeMillis(),
            time,
            scopes
        )
        if (user.activeRank.isStaff && user.activeRank.getWeight() < grant.rank.getWeight()) {
            val promotion = Promotion(user.activeRank.coloredName, grant.rank.coloredName, System.currentTimeMillis())
            user.getPromotions().add(promotion)
        }
        if (grant.rank.isStaff && !user.activeRank.isStaff) {
            user.getStaffInfo().joinedStaffTeam = System.currentTimeMillis()
        }
        user.getGrants().add(grant)
        if (Bukkit.getPlayer(uuid) == null) {
            user.save(true)
        }
        user.updateGrants()
        GrantAddPacket(uuid, grant).send()
        sender.sendMessage(
            CC.translate(
                FlashLanguage.GRANTED_USER_RANK_SENDER.string,
                "%PLAYER_DISPLAY%", user.displayName,
                "%RANK%", rank!!.getDisplayName()!!,
                "%DURATION%", grant.expireString!!
            )
        )
        val message = CC.translate(
            FlashLanguage.GRANTED_USER_RANK_TARGET.string,
            "%RANK%", rank!!.getDisplayName()!!,
            "%DURATION%", grant.expireString!!
        )
        GlobalMessagePacket(uuid, message).send()
    }

    private const val itemsPerPage = 5
    private val COMMANDS: List<String> = mutableListOf(
        "&c/user editor &7- &fdisplay a menu to edit all users",
        "&c/user editor <user> &7- &fdisplay a menu to edit a specific user",
        "&c/user info <target> &7- &fdisplay a list of the users attributes",
        "&c/grants <target> &7- &fdisplay a list of the users grants (ranks & perms)",
        "&c/user grantrank <user> <rank> <time> <servers> <reason> &7- &fapplies a rank grant to an user",
        "&c/user grantperm <user> <permission> <duration> <reason> &7- &fadds a permission to a user"
    )
}