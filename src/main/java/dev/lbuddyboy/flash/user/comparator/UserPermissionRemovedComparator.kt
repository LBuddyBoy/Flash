package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.UserPermission
import java.lang.Boolean
import kotlin.Comparator
import kotlin.Int

class UserPermissionRemovedComparator : Comparator<UserPermission> {
    override fun compare(permission: UserPermission, otherPermission: UserPermission): Int {
        return Boolean.compare(permission.isRemoved, otherPermission.isRemoved)
    }
}