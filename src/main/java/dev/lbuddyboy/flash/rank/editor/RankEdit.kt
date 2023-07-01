package dev.lbuddyboy.flash.rank.editor;

import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class RankEdit {

    private Rank rank;
    private EditorType type;

}
