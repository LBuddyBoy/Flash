package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.rank.Rank
import lombok.*
import java.util.*

@AllArgsConstructor
@Data
class GrantBuild {
    private val target: UUID? = null
    private val rank: UUID? = null
    private val time: String? = null
    private val scopes: Array<String>
    private val reason: String? = null
    fun getRank(): Rank {
        return Flash.instance.rankHandler.getRanks().get(rank)
    }
}