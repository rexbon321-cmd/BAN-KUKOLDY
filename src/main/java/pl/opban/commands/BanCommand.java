package pl.opban.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import pl.opban.core.BanManager;

public class BanCommand implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || !p.isOp()) return true;
        if (a.length < 2) {
            p.sendMessage("/ban <nick> <powód>");
            return true;
        }
        BanManager.ban(a[0], String.join(" ", java.util.Arrays.copyOfRange(a,1,a.length)), p.getName(), 0);
        p.sendMessage("Zbanowano " + a[0]);
        return true;
    }
}
