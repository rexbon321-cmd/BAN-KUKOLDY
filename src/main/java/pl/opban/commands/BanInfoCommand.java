package pl.opban.commands;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import pl.opban.core.BanManager;
import pl.opban.util.TimeUtil;

public class BanInfoCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)||!p.isOp())return true;
  if(a.length!=1){p.sendMessage("/baninfo <nick>");return true;}
  BanManager.Ban b=BanManager.getByName(a[0]);
  if(b==null){p.sendMessage("Brak aktywnego bana");return true;}

  p.sendMessage("§8§m────────────────────────");
  p.sendMessage("§c§lINFORMACJE O BANIE");
  p.sendMessage("§7Gracz: §e"+b.name);
  p.sendMessage("§7Admin: §f"+b.admin);
  p.sendMessage("§7Powód: §f"+b.reason);
  p.sendMessage("§7Czas: §f"+TimeUtil.format(b.expires-System.currentTimeMillis()));
  p.sendMessage("§7Licencja: §e"+b.license);

  TextComponent copy=new TextComponent("§a[📋 KOPIUJ LICENCJĘ]");
  copy.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,b.license));
  p.spigot().sendMessage(copy);
  p.sendMessage("§8§m────────────────────────");
  return true;
 }
}
