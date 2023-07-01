package dev.lbuddyboy.flash.user

import com.mongodb.client.model.Filters
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.comparator.*
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.user.model.Note
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.*
import org.apache.commons.lang.WordUtils
import org.bson.Document
import org.bukkit.*
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

@Data
abstract class User {
    var uuid: UUID? = null
    var name: String? = null
    var ip: String? = null
    var permissions: MutableList<UserPermission?> = ArrayList()
    var knownIps: List<String> = ArrayList()
    var blocked: List<UUID> = ArrayList()
    var grants: MutableList<Grant> = ArrayList()
    var notes: List<Note> = ArrayList()
    var punishments: List<Punishment> = ArrayList()
    var promotions: List<Promotion> = ArrayList()
    var demotions: List<Demotion> = ArrayList()
    var activeGrant: Grant? = null
    var activePrefix: Prefix? = null
    var playerInfo = PlayerInfo(true, false, false, -1, null, -1, -1, ArrayList())
    var serverInfo: List<ServerInfo> = ArrayList()
    var staffInfo = StaffInfo()
    var needsSaving = false
    abstract fun load()
    abstract fun save(async: Boolean)
    open fun loadRank() {}
    fun getDisplayName(): String {
        return if (activePrefix != null) CC.translate(getActivePrefix().getDisplay() + getActiveRank().getPrefix() + name + getActiveRank().getSuffix()) else CC.translate(
            getActiveRank().getPrefix() + name + getActiveRank().getSuffix()
        )
    }

    fun getColoredName(): String {
        return getActiveRank().getColor().toString() + name
    }

    fun getActivePunishment(type: PunishmentType): Punishment? {
        for (punishment in punishments) {
            if (punishment.type != type) continue
            if (punishment.isExpired || punishment.isRemoved) continue
            return punishment
        }
        return null
    }

    fun getNote(title: String): Note {
        return notes.stream().filter { note: Note -> note.title == title }
            .collect(Collectors.toList())[0]
    }

    fun getActivePrefix(): Prefix? {
        return if (activePrefix == null) null else Flash.instance.userHandler.getPrefix(activePrefix.getId())
    }

    fun hasActivePunishment(type: PunishmentType): Boolean {
        return getActivePunishment(type) != null
    }

    fun getSortedPunishments(): List<Punishment> {
        return punishments.stream()
            .sorted(PunishmentDateComparator().reversed().thenComparing(PunishmentRemovedComparator().reversed()))
            .collect(Collectors.toList())
    }

    fun getSortedPunishmentsByType(type: PunishmentType): List<Punishment> {
        return punishments.stream().filter { punishment: Punishment -> punishment.type == type }
            .sorted(PunishmentDateComparator().reversed().thenComparing(PunishmentRemovedComparator().reversed()))
            .collect(Collectors.toList())
    }

    fun getActiveRank(): Rank? {
        val active = activeGrant ?: return Flash.instance.rankHandler.getDefaultRank()
        return active.rank
    }

    fun getActiveGrant(): Grant {
        return activeGrant
            ?: return grants.stream()
                .filter { grant: Grant -> grant.rank != null && grant.rank.isDefaultRank }
                .collect(Collectors.toList())[0]
    }

    fun getActiveGrants(): List<Grant> {
        return grants.stream().filter { grant: Grant -> !grant.isExpired && !grant.isRemoved }
            .collect(Collectors.toList())
    }

    fun getSortedGrants(): List<Grant> {
        return grants.stream()
            .sorted(GrantWeightComparator().reversed().thenComparing(GrantDateComparator().reversed()))
            .collect(Collectors.toList())
    }

    fun getSortedNotes(): List<Note> {
        return notes.stream().sorted(NoteDateComparator()).collect(Collectors.toList())
    }

    fun getActivePermissions(): List<UserPermission?> {
        return permissions.stream()
            .filter { permission: UserPermission? -> !permission!!.isExpired && !permission.isRemoved }
            .collect(Collectors.toList())
    }

    fun getSortedPermissions(): List<UserPermission?> {
        return permissions.stream()
            .sorted(UserPermissionDateComparator().reversed().thenComparing(UserPermissionDateComparator().reversed()))
            .collect(Collectors.toList())
    }

    fun updateGrants() {
        for (grant in grants) {
            if (grant.isRemoved) continue
            if (!grant.isExpired) continue
            grant.removedAt = System.currentTimeMillis()
            grant.removedFor = "Expired"
            grant.removedBy = null
            if (activeGrant != null && activeGrant == grant) {
                activeGrant = null
            }
        }
        val grants = getActiveGrants().stream()
            .sorted(GrantWeightComparator().reversed().thenComparing(GrantDateComparator().reversed()))
            .collect(Collectors.toList())
        for (grant in grants) {
            if (grant.rank == null) continue
            if (!Arrays.stream(grant.scopes).map { obj: String -> obj.lowercase(Locale.getDefault()) }
                    .collect(Collectors.toList()).contains("global") && !Arrays.asList(*grant.scopes)
                    .contains(FlashLanguage.SERVER_NAME.string)) continue
            activeGrant = grant
            buildPlayer()
            break
        }
    }

    fun updatePerms() {
        for (permission in permissions) {
            if (!permission!!.isExpired) continue
            if (permission.isRemoved) continue
            permission.removedAt = System.currentTimeMillis()
            permission.removedBy = null
            permission.removedFor = "Expired"
            permission.isRemoved = true
        }
    }

    fun buildPlayer() {
        val player = Bukkit.getPlayer(uuid) ?: return
        setupPermissionsAttachment()
        player.displayName = getDisplayName()
        player.playerListName = getColoredName()
    }

    fun hasGrant(id: UUID): Boolean {
        return grants.stream().map { obj: Grant -> obj.uuid }.collect(Collectors.toList()).contains(id)
    }

    fun hasPunishment(id: UUID): Boolean {
        return punishments.stream().map { obj: Punishment -> obj.id }.collect(Collectors.toList()).contains(id)
    }

    fun hasNote(id: UUID): Boolean {
        return notes.stream().map { obj: Note -> obj.id }.collect(Collectors.toList()).contains(id)
    }

    fun setupPermissionsAttachment() {
        val player = Bukkit.getPlayer(uuid) ?: return
        for (attachmentInfo in player.effectivePermissions) {
            if (attachmentInfo.attachment == null) continue
            attachmentInfo.attachment.permissions.forEach { (permission: String?, value: Boolean?) ->
                attachmentInfo.attachment.unsetPermission(
                    permission
                )
            }
        }
        val attachment = player.addAttachment(Flash.instance)
        if (player.isOp) attachment.setPermission("op", true)
        getActiveRank().getPermissions()
            .forEach(Consumer { permission: String? -> attachment.setPermission(permission, true) })
        getActiveRank().getInheritedPermissions()
            .forEach(Consumer { permission: String? -> attachment.setPermission(permission, true) })
        getActivePermissions().stream().map { obj: UserPermission? -> obj.getNode() }
            .forEach { permission: String? -> attachment.setPermission(permission, true) }
        player.recalculatePermissions()
        try {
            Player::class.java.getMethod("updateCommands")
        } catch (ignored: NoSuchMethodException) {
        }
    }

    fun addPerm(permission: UserPermission) {
        if (!permission.node.startsWith("group.")) {
            permissions.add(permission)
            return
        }
        val striped = permission.node.replace("group".toRegex(), "").replace("\\.".toRegex(), "")
        val key = WordUtils.capitalize(striped)
        val rank = Flash.instance.rankHandler.getRank(key) ?: return
        val grant = Grant(
            UUID.randomUUID(),
            rank.getUuid(),
            key,
            null,
            "Imported from LuckPerms",
            System.currentTimeMillis(),
            permission.duration,
            arrayOf("GLOBAL")
        )
        grants.add(grant)
    }

    fun colorAlt(): String {
        var color = ChatColor.GRAY
        if (Bukkit.getPlayer(uuid) != null) color = ChatColor.GREEN
        if (hasActivePunishment(PunishmentType.MUTE)) color = getActivePunishment(PunishmentType.MUTE).getType().color
        if (hasActivePunishment(PunishmentType.BAN)) color = getActivePunishment(PunishmentType.BAN).getType().color
        if (hasActivePunishment(PunishmentType.IP_BAN)) color =
            getActivePunishment(PunishmentType.IP_BAN).getType().color
        if (hasActivePunishment(PunishmentType.BLACKLIST)) color =
            getActivePunishment(PunishmentType.BLACKLIST).getType().color
        return color.toString() + name
    }

    fun isDiscordSynced(): Boolean {
        return getDiscordSyncedId() != 0L
    }

    fun getDiscordRank(): Rank? {
        val document: Document = Flash.instance.mongoHandler.getSyncCollection()
            .find(Filters.eq("playerUUID", uuid.toString())).first()
            ?: return null
        return Flash.instance.userHandler.rankConversion[document.getLong("rankId")]
    }

    fun getDiscordSyncedId(): Long {
        val document: Document = Flash.instance.mongoHandler.getSyncCollection()
            .find(Filters.eq("playerUUID", uuid.toString())).first()
            ?: return 0
        return document.getLong("memberId")
    }

    fun getDiscordSyncedName(): String {
        val document: Document = Flash.instance.mongoHandler.getSyncCollection()
            .find(Filters.eq("playerUUID", uuid.toString())).first()
            ?: return "None"
        return document.getString("memberName")
    }
}