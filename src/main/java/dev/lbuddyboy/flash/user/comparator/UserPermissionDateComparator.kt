package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.UserPermission

class UserPermissionDateComparator : Comparator<UserPermission> {
    override fun compare(permission: UserPermission, otherPermission: UserPermission): Int {
        return java.lang.Long.compare(permission.sentAt, otherPermission.sentAt)
    }
}