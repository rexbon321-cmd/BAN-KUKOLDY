package pl.opban;

import org.bukkit.plugin.java.JavaPlugin;
import pl.opban.core.BanManager;
import pl.opban.commands.*;

public class OpBanPro extends JavaPlugin {
 public void onEnable(){
  BanManager.init(this);
  getCommand("baninfo").setExecutor(new BanInfoCommand());
 }
}
