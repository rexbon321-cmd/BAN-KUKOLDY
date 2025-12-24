package pl.opban.gui;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class BanMenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || !p.isOp()) return true;

        Inventory inv = Bukkit.createInventory(null, 9, "Panel Banów");

        inv.setItem(3, item(Material.RED_WOOL, "ZBANUJ GRACZA"));
        inv.setItem(5, item(Material.LIME_WOOL, "ODBANUJ (LICENCJA)"));

        p.openInventory(inv);
        return true;
    }

    private ItemStack item(Material m, String n) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        i.setItemMeta(im);
        return i;
    }
}
