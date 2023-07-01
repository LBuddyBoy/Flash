package dev.lbuddyboy.flash.util;

import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
@Getter
public class PagedItem {

    private List<String> items, header;
    private int maxItemsPerPage;

    public int getMaxPages() {
        return (items.size() / maxItemsPerPage) + 1;
    }

    public void send(CommandSender sender, int page) {

        if (page > getMaxPages()) {
            sender.sendMessage(CC.translate("&cThat page is not within the bounds of " + getMaxPages() + "."));
            return;
        }

        header.forEach(s -> sender.sendMessage(CC.translate(s
                .replaceAll("%page%", "" + page)
                .replaceAll("%max-pages%", "" + getMaxPages())
        )));

        for (int i = (page * maxItemsPerPage) - maxItemsPerPage; i < (page * maxItemsPerPage); i++) {
            if (items.size() <= i) continue;
            sender.sendMessage(CC.translate(items.get(i)));
        }

    }

}
