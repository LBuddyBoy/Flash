package dev.lbuddyboy.flash.user.model

import lombok.AllArgsConstructor
import lombok.Data
import java.util.*

@AllArgsConstructor
@Data
class PermissionBuild {
    private val target: UUID? = null
    private val node: String? = null
    private val time: String? = null
    private val reason: String? = null
}