package pl.opban.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.*;

public class ChatInputListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!GuiState.target.containsKey(p.getUniqueId())) return;

        e.setCancelled(true);
        String state = GuiState.target.get(p.getUniqueId());
        String msg = e.getMessage();

        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("OpBanPro"), () -> {
            if (state.equals("__WAIT_NICK__")) {
                GuiState.target.put(p.getUniqueId(), msg);
                Inventory inv = Bukkit.createInventory(null, 27, "Wybierz czas bana");
                for (int i = 0; i < 27; i++) {
                    ItemStack it = new ItemStack(Material.PAPER);
                    var im = it.getItemMeta();
                    im.setDisplayName((i+1) + " dni");
                    it.setItemMeta(im);
                    inv.setItem(i, it);
                }
                p.openInventory(inv);
            }
            if (state.equals("__WAIT_LICENSE__")) {
                p.performCommand("unban " + msg);
                GuiState.target.remove(p.getUniqueId());
            }
        });
    }
}
