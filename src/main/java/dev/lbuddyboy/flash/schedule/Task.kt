package dev.lbuddyboy.flash.schedule

import lombok.AllArgsConstructor
import lombok.Data
import java.util.*

@AllArgsConstructor
@Data
class Task {
    private val id: String? = null
    private val date: Date? = null
    private val command: String? = null
}