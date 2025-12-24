package pl.opban.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import pl.opban.core.BanManager;

public class UnbanCommand implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || !p.isOp()) return true;
        if (a.length != 1) {
            p.sendMessage("/unban <licencja>");
            return true;
        }
        p.sendMessage(BanManager.unban(a[0]) ? "Odbanowano" : "Nie znaleziono bana");
        return true;
    }
}
