package pl.opban.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import pl.opban.core.BanManager;
import pl.opban.util.TimeUtil;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        String title = e.getView().getTitle();
        e.setCancelled(true);

        if (title.equals("Panel Banów")) {
            if (e.getCurrentItem() == null) return;
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            p.closeInventory();
            if (name.contains("ZBANUJ")) {
                p.sendMessage("Wpisz nick gracza na czacie:");
                GuiState.target.put(p.getUniqueId(), "__WAIT_NICK__");
            } else if (name.contains("ODBANUJ")) {
                p.sendMessage("Wpisz LICENCJĘ bana na czacie:");
                GuiState.target.put(p.getUniqueId(), "__WAIT_LICENSE__");
            }
        }

        if (title.equals("Wybierz czas bana")) {
            int d = e.getSlot() + 1;
            GuiState.days.put(p.getUniqueId(), d);
            openConfirm(p);
        }

        if (title.equals("Potwierdzenie")) {
            if (e.getSlot() == 11) {
                String t = GuiState.target.get(p.getUniqueId());
                int d = GuiState.days.getOrDefault(p.getUniqueId(), 0);
                BanManager.ban(t, "Ban z GUI", p.getName(), TimeUtil.days(d));
                p.sendMessage("Zbanowano " + t + " na " + d + " dni");
                p.closeInventory();
            }
            if (e.getSlot() == 15) {
                p.closeInventory();
                p.sendMessage("Anulowano.");
            }
        }
    }

    private void openConfirm(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "Potwierdzenie");
        inv.setItem(11, wool(Material.LIME_WOOL, "TAK"));
        inv.setItem(15, wool(Material.RED_WOOL, "NIE"));
        p.openInventory(inv);
    }

    private ItemStack wool(Material m, String n) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        i.setItemMeta(im);
        return i;
    }
}
