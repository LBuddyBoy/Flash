package dev.lbuddyboy.flash.thread

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TipsMessageTask : BukkitRunnable() {
    private var last = 0
    override fun run() {
        val tips = FlashLanguage.TIPS_LIST.stringList
        val tip = tips!![last++]
        Bukkit.getOnlinePlayers().forEach { player: Player -> player.sendMessage(CC.translate(tip)) }
        if (last >= tips!!.size) last = 0
    }
}