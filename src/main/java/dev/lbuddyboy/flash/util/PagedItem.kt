package dev.lbuddyboy.flash.util

import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.AllArgsConstructor
import lombok.Getter
import org.bukkit.command.CommandSender
import java.util.function.Consumer

@AllArgsConstructor
@Getter
class PagedItem {
    private val items: List<String>? = null
    private val header: List<String>? = null
    private val maxItemsPerPage = 0
    fun getMaxPages(): Int {
        return items!!.size / maxItemsPerPage + 1
    }

    fun send(sender: CommandSender, page: Int) {
        if (page > getMaxPages()) {
            sender.sendMessage(CC.translate("&cThat page is not within the bounds of " + getMaxPages() + "."))
            return
        }
        header!!.forEach(Consumer { s: String ->
            sender.sendMessage(
                CC.translate(
                    s
                        .replace("%page%".toRegex(), "" + page)
                        .replace("%max-pages%".toRegex(), "" + getMaxPages())
                )
            )
        })
        for (i in page * maxItemsPerPage - maxItemsPerPage until page * maxItemsPerPage) {
            if (items!!.size <= i) continue
            sender.sendMessage(CC.translate(items[i]))
        }
    }
}