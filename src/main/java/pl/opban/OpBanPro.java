package pl.opban;

import org.bukkit.plugin.java.JavaPlugin;
import pl.opban.core.BanManager;
import pl.opban.commands.BanCommand;
import pl.opban.commands.BanInfoCommand;
import pl.opban.gui.BanMenuCommand;
import pl.opban.gui.GuiListener;
import pl.opban.gui.ChatInputListener;

public class OpBanPro extends JavaPlugin {

    @Override
    public void onEnable() {

        // init systemu banów
        BanManager.init(this);

        // === KOMENDY ===
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("baninfo").setExecutor(new BanInfoCommand());
        getCommand("banmenu").setExecutor(new BanMenuCommand());

        // === LISTENERY ===
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);

        getLogger().info("OpBanPro ENABLED");
    }

    @Override
    public void onDisable() {
        getLogger().info("OpBanPro DISABLED");
    }
}
