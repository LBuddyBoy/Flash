package dev.lbuddyboy.flash.user.model

import lombok.AllArgsConstructor
import lombok.Data
import java.util.*

@AllArgsConstructor
@Data
class PlayerInfo {
    private val pmsOn = false
    private val claimedNameMC = false
    private val offlineInventoryEdited = false
    private val syncCode: Long = 0
    private val reply: UUID? = null
    private val lastRequestSent: Long = 0
    private val lastMessageSent: Long = 0
    private val readNotifications: List<UUID>? = null
}