package dev.lbuddyboy.flash.thread

import dev.lbuddyboy.flash.Flash

class UserUpdateTask : Thread() {
    override fun run() {
        for (user in Flash.instance.userHandler.getUsers().values()) {
            user.updatePerms()
            user.updateGrants()
            if (user.activeRank.isStaff) user.getStaffInfo().playTime = user.getStaffInfo().playTime + 10000
        }
        try {
            sleep(10000)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}