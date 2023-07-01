package dev.lbuddyboy.flash.command.rank

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.rank.editor.menu.RankEditorMenu
import dev.lbuddyboy.flash.rank.menu.RankListMenu
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket
import dev.lbuddyboy.flash.util.PagedItem
import dev.lbuddyboy.flash.util.bukkit.CC
import org.apache.commons.lang.StringUtils
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("rank|ranks")
@CommandPermission("flash.command.rank")
object RankCommand : BaseCommand() {
    @Default
    fun def(sender: CommandSender, @Name("page") @Default("1") page: Int) {
        val item = PagedItem(COMMANDS, FlashLanguage.RANK_HELP.stringList, 5)
        item.send(sender, page)
        sender.sendMessage(CC.CHAT_BAR)
    }

    @Subcommand("help")
    fun help(sender: CommandSender, @Name("page") @Default("1") page: Int) {
        def(sender, page)
    }

    @Subcommand("list|dump")
    fun list(sender: CommandSender) {
        for (s in FlashLanguage.RANK_LIST_HEADER.stringList!!) sender.sendMessage(CC.translate(s))
        for (rank in Flash.instance.rankHandler.sortedRanks) {
            sender.sendMessage(
                CC.translate(
                    FlashLanguage.RANK_LIST_FORMAT.string,
                    "%rank%", rank.getName(),
                    "%display%", rank.getDisplayName()!!,
                    "%default%", if (rank.isDefaultRank) "True" else "False",
                    "%weight%", rank.getWeight(),
                    "%prefix%", rank.getPrefix(),
                    "%suffix%", rank.getSuffix()
                )
            )
        }
    }

    @Subcommand("info")
    @CommandPermission("flash.command.rank.info")
    @CommandCompletion("@rank")
    fun info(sender: CommandSender, @Name("rank") rank: Rank) {
        sender.sendMessage(CC.translate("&cDefault: ") + if (rank.isDefaultRank) "&aYes" else "&cNo")
        sender.sendMessage(CC.translate("&cStaff: ") + if (rank.isStaff) "&aYes" else "&cNo")
        sender.sendMessage(CC.translate("&cPermissions: ") + StringUtils.join(rank.getPermissions(), ", "))
        sender.sendMessage(
            CC.translate("&cInherited Permissions: ") + StringUtils.join(
                rank.inheritedPermissions,
                ", "
            )
        )
        sender.sendMessage(CC.translate("&cInherited Ranks: " + StringUtils.join(rank.getInheritance(), ", ")))
    }

    @Subcommand("create")
    @CommandPermission("flash.command.rank.create")
    fun create(sender: CommandSender, @Name("rank") name: String?) {
        var rank = Flash.instance.rankHandler.getRank(name)
        if (rank != null) {
            sender.sendMessage(CC.translate(FlashLanguage.RANK_EXISTS.string))
            return
        }
        rank = Flash.instance.rankHandler.createRank(name)
        rank.setDisplayName(name)
        rank.save(true)
        Flash.instance.rankHandler.getRanks().put(rank.getUuid(), rank)
        sender.sendMessage(CC.translate(FlashLanguage.RANK_CREATE.string, "%rank%", rank.getColoredName()))
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("delete")
    @CommandPermission("flash.command.rank.delete")
    @CommandCompletion("@rank")
    fun delete(sender: CommandSender, @Name("rank") rank: Rank) {
        if (FlashLanguage.CACHE_TYPE.string.equals("YAML", ignoreCase = true) || FlashLanguage.CACHE_TYPE.string.equals(
                "FLATFILE",
                ignoreCase = true
            )
        ) {
            sender.sendMessage(CC.translate("&cRanks cannot be deleted due to the cache type you are using. Delete it manually in the ranks.yml."))
            return
        }
        rank.delete()
        sender.sendMessage(CC.translate(FlashLanguage.RANK_DELETE.string, "%rank%", rank.coloredName))
    }

    @Subcommand("toggledefault")
    @CommandPermission("flash.command.rank.toggledefault")
    @CommandCompletion("@rank")
    fun toggleDefault(sender: CommandSender, @Name("rank") rank: Rank) {
        val previous = if (rank.isDefaultRank) "True" else "False"
        rank.isDefaultRank = !rank.isDefaultRank
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_DEFAULT.string,
                "%rank%",
                rank.coloredName,
                "%old-status%",
                previous,
                "%new-status%",
                if (rank.isDefaultRank) "&aTrue" else "&cFalse"
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("togglestaff")
    @CommandPermission("flash.command.rank.togglestaff")
    @CommandCompletion("@rank")
    fun toggleStaff(sender: CommandSender, @Name("rank") rank: Rank) {
        val previous = if (rank.isStaff) "True" else "False"
        rank.isStaff = !rank.isStaff
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_STAFF.string,
                "%rank%",
                rank.coloredName,
                "%old-status%",
                previous,
                "%new-status%",
                if (rank.isStaff) "&aTrue" else "&cFalse"
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("setname|name|rename")
    @CommandPermission("flash.command.rank.setname")
    @CommandCompletion("@rank")
    fun setName(sender: CommandSender, @Name("rank") rank: Rank, @Name("newName") newName: String?) {
        val previous = rank.coloredName
        rank.setName(newName)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_NAME.string,
                "%rank%",
                previous!!,
                "%new-rank%",
                rank.coloredName
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("setdisplayname|displayname|setdisplay")
    @CommandPermission("flash.command.rank.setdisplayname")
    @CommandCompletion("@rank")
    fun setDisplayName(sender: CommandSender, @Name("rank") rank: Rank, @Name("newDisplay") newDisplayName: String?) {
        val previous = rank.getDisplayName()
        rank.setDisplayName(newDisplayName)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_DISPLAY_NAME.string,
                "%rank%",
                rank.coloredName,
                "%old-display%",
                previous!!,
                "%new-display%",
                rank.getDisplayName()!!
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("editor")
    @CommandPermission("flash.command.rank.editor")
    fun editor(sender: Player?) {
        RankListMenu { player: Player?, rank: Rank? ->
            RankEditorMenu(rank).openMenu(
                player!!
            )
        }.openMenu(sender!!)
    }

    @Subcommand("setcolor|color|setdisplaycolor")
    @CommandPermission("flash.command.rank.setcolor")
    @CommandCompletion("@rank @chatcolors")
    fun setColor(sender: CommandSender, @Name("rank") rank: Rank, @Name("newColor") color: ChatColor?) {
        val previous = rank.getColor().toString() + rank.getColor().name
        rank.setColor(color)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_COLOR.string,
                "%rank%",
                rank.coloredName,
                "%old-color%",
                previous,
                "%new-color%",
                rank.getColor().toString() + rank.getColor().name
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("setweight|weight|setpriority")
    @CommandPermission("flash.command.rank.setweight")
    @CommandCompletion("@rank")
    fun setWeight(sender: CommandSender, @Name("rank") rank: Rank, @Name("newWeight") weight: Int) {
        val previous = rank.getWeight()
        rank.setWeight(weight)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_WEIGHT.string,
                "%rank%",
                rank.coloredName,
                "%old-weight%",
                previous,
                "%new-weight%",
                rank.getWeight()
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("setprefix|prefix")
    @CommandPermission("flash.command.rank.setprefix")
    @CommandCompletion("@rank")
    fun setPrefix(sender: CommandSender, @Name("rank") rank: Rank, @Name("prefix") prefix: String?) {
        val previous = rank.getPrefix()
        rank.setPrefix(prefix)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_PREFIX.string,
                "%rank%",
                rank.coloredName,
                "%old-prefix%",
                previous,
                "%new-prefix%",
                rank.getPrefix()
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("setsuffix|suffix")
    @CommandPermission("flash.command.rank.setsuffix")
    @CommandCompletion("@rank")
    fun setSuffix(sender: CommandSender, @Name("rank") rank: Rank, @Name("prefix") suffix: String?) {
        val previous = rank.getPrefix()
        rank.setSuffix(suffix)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_SET_SUFFIX.string,
                "%rank%",
                rank.coloredName,
                "%old-suffix%",
                previous,
                "%new-suffix%",
                rank.getSuffix()
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("addpermission|addperm")
    @CommandPermission("flash.command.rank.addpermission")
    @CommandCompletion("@rank @permissions")
    fun addPermission(sender: CommandSender, @Name("rank") rank: Rank, @Name("permission") permission: String?) {
        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank already has that permission."))
            return
        }
        rank.getPermissions().add(permission)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_ADD_PERM.string,
                "%rank%",
                rank.coloredName,
                "%permission%",
                permission!!
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
        for (user in rank.usersWithRank) {
            user!!.setupPermissionsAttachment()
        }
    }

    @Subcommand("removepermission|removeperm|delperm|delpermission")
    @CommandPermission("flash.command.rank.removepermission")
    @CommandCompletion("@rank")
    fun removePermission(sender: CommandSender, @Name("rank") rank: Rank, @Name("permission") permission: String?) {
        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank does not have that permission."))
            return
        }
        rank.getPermissions().remove(permission)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_REMOVE_PERM.string,
                "%rank%",
                rank.coloredName,
                "%permission%",
                permission!!
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("addinheritance|addinherit|addparent")
    @CommandPermission("flash.command.rank.addinheritance")
    @CommandCompletion("@rank @rank")
    fun addInherit(sender: CommandSender, @Name("rank") rank: Rank, @Name("parent") @Single inherit: String?) {
        if (rank.getInheritance().contains(inherit)) {
            sender.sendMessage(CC.translate("&cThat rank already has that inheritance."))
            return
        }
        rank.getInheritance().add(inherit)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_ADD_INHERIT.string,
                "%rank%",
                rank.coloredName,
                "%inherit%",
                inherit!!
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    @Subcommand("removeinheritance|reminherit|reminheritance|removeinherit|delinherit|delinheritance")
    @CommandPermission("flash.command.rank.removepermission")
    @CommandCompletion("@rank @rank")
    fun removeInherit(sender: CommandSender, @Name("rank") rank: Rank, @Name("inherit") @Single inherit: String?) {
        if (!rank.getInheritance().contains(inherit)) {
            sender.sendMessage(CC.translate("&cThat rank doesn't have that inheritance."))
            return
        }
        rank.getInheritance().remove(inherit)
        rank.save(true)
        sender.sendMessage(
            CC.translate(
                FlashLanguage.RANK_REMOVE_INHERIT.string,
                "%rank%",
                rank.coloredName,
                "%inherit%",
                inherit!!
            )
        )
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    private const val itemsPerPage = 5
    private val COMMANDS: List<String> = mutableListOf(
        "&c/rank editor &7- &fdisplay a menu to edit all ranks",
        "&c/rank info <rank> &7- &fdisplay a list of the ranks attributes",
        "&c/rank create <name> &7- &fcreates a new rank",
        "&c/rank delete <rank> &7- &fdeletes an existing rank",
        "&c/rank setname <rank> <name> &7- &fsets the ranks name attribute",
        "&c/rank setprefix <rank> <prefix> &7- &fsets the ranks prefix attribute",
        "&c/rank setsuffix <rank> <suffix> &7- &fsets the ranks suffix attribute",
        "&c/rank addpermission <rank> <permission> &7- &fadds a permission to a rank",
        "&c/rank delpermission <rank> <permission> &7- &fdeletes a permission from a rank",
        "&c/rank addinheritance <rank> <rank> &7- &fadds an inheritance to a rank",
        "&c/rank delinheritance <rank> <rank> &7- &fdeletes an inheritance from a rank",
        "&c/rank setweight <rank> <weight> &7- &fsets the ranks weight attribute",
        "&c/rank setdisplayname <rank> <name> &7- &fsets the ranks display name attribute",
        "&c/rank togglestaff <rank> &7- &ftoggles the staff status attribute",
        "&c/rank toggledefault <rank> &7- &ftoggles the default rank status attribute",
        "&c/rank setcolor <rank> <color> &7- &fsets the ranks color attribute"
    )
}