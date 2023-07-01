package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.UserPermission;

import java.util.Comparator;

public class UserPermissionRemovedComparator implements Comparator<UserPermission> {

    @Override
    public int compare(UserPermission permission, UserPermission otherPermission) {
        return Boolean.compare(permission.isRemoved(), otherPermission.isRemoved());
    }
}