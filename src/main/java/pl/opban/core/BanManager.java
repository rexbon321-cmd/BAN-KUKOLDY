package pl.opban.core;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.opban.OpBanPro;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BanManager {

    public static class Ban {
        public String license;
        public String name;
        public String admin;
        public String reason;
        public long expires;
        public boolean active;
    }

    private static final Map<String, Ban> bans = new HashMap<>();
    private static File file;
    private static YamlConfiguration cfg;

    // === INIT ===
    public static void init(OpBanPro plugin) {
        file = new File(plugin.getDataFolder(), "bans.yml");
        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
        load();

        // auto-unban co 1 minutę
        Bukkit.getScheduler().runTaskTimer(plugin, BanManager::tick, 20 * 60, 20 * 60);
    }

    // === BAN ===
    public static String ban(String name, String reason, String admin, long durationMillis) {
        String license = UUID.randomUUID().toString().substring(0, 6);

        Ban ban = new Ban();
        ban.license = license;
        ban.name = name;
        ban.admin = admin;
        ban.reason = reason;
        ban.expires = durationMillis > 0
                ? System.currentTimeMillis() + durationMillis
                : 0;
        ban.active = true;

        bans.put(license, ban);
        save();

        Bukkit.getBanList(BanList.Type.NAME).addBan(
                name,
                "Powód: " + reason + "\nID: " + license,
                ban.expires == 0 ? null : new Date(ban.expires),
                admin
        );

        Player p = Bukkit.getPlayerExact(name);
        if (p != null) {
            p.kickPlayer(
                    "Zostałeś zbanowany!\n" +
                    "Powód: " + reason + "\n" +
                    "ID bana: " + license
            );
        }

        return license;
    }

    // === UNBAN (PO LICENCJI) ===
    public static boolean unban(String license) {
        Ban ban = bans.get(license);
        if (ban == null || !ban.active) {
            return false;
        }

        ban.active = false;
        Bukkit.getBanList(BanList.Type.NAME).pardon(ban.name);
        save();
        return true;
    }

    // === BAN INFO ===
    public static Ban getByName(String name) {
        return bans.values()
                .stream()
                .filter(b -> b.active && b.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // === AUTO UNBAN ===
    private static void tick() {
        long now = System.currentTimeMillis();

        for (Ban ban : bans.values()) {
            if (ban.active && ban.expires > 0 && now >= ban.expires) {
                ban.active = false;
                Bukkit.getBanList(BanList.Type.NAME).pardon(ban.name);
            }
        }
        save();
    }

    // === LOAD ===
    private static void load() {
        for (String key : cfg.getKeys(false)) {
            Ban ban = new Ban();
            ban.license = key;
            ban.name = cfg.getString(key + ".name");
            ban.admin = cfg.getString(key + ".admin");
            ban.reason = cfg.getString(key + ".reason");
            ban.expires = cfg.getLong(key + ".expires");
            ban.active = cfg.getBoolean(key + ".active");
            bans.put(key, ban);
        }
    }

    // === SAVE ===
    private static void save() {
        for (String key : cfg.getKeys(false)) {
            cfg.set(key, null);
        }

        for (Ban ban : bans.values()) {
            cfg.set(ban.license + ".name", ban.name);
            cfg.set(ban.license + ".admin", ban.admin);
            cfg.set(ban.license + ".reason", ban.reason);
            cfg.set(ban.license + ".expires", ban.expires);
            cfg.set(ban.license + ".active", ban.active);
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
