package dev.lbuddyboy.flash.cache

import java.util.*

abstract class UserCache {
    abstract fun getUUID(name: String?): UUID?
    abstract fun getName(uuid: UUID?): String?
    abstract fun load()
    open fun allUUIDs(): List<UUID> {
        return ArrayList()
    }

    abstract fun update(uuid: UUID, name: String?, save: Boolean)
}