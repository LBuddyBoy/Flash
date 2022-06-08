package dev.lbuddyboy.flash.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
@Getter
public class Batch {

    private String name;
    private CommandSender sender;
    private List<Callable> callbacks;
    private long startedAt;
    @Setter private boolean done;

}
