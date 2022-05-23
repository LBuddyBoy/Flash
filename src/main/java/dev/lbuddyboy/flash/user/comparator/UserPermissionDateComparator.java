package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;

import java.util.Comparator;

public class UserPermissionDateComparator implements Comparator<UserPermission> {

    @Override
    public int compare(UserPermission permission, UserPermission otherPermission) {
        return Long.compare(permission.getSentAt(), otherPermission.getSentAt());
    }
}