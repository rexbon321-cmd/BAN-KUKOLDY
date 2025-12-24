package pl.opban.core;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.opban.OpBanPro;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BanManager {

 public static class Ban {
  public String license,name,admin,reason;
  public long expires;
  public boolean active;
 }

 private static final Map<String,Ban> bans=new HashMap<>();
 private static File file;
 private static YamlConfiguration cfg;

 public static void init(OpBanPro p){
  file=new File(p.getDataFolder(),"bans.yml");
  if(!file.exists()) try{p.getDataFolder().mkdirs();file.createNewFile();}catch(IOException e){}
  cfg=YamlConfiguration.loadConfiguration(file);
  load();
  Bukkit.getScheduler().runTaskTimer(p,BanManager::tick,20*60,20*60);
 }

 public static String ban(String name,String reason,String admin,long dur){
  String lic=UUID.randomUUID().toString().substring(0,6);
  Ban b=new Ban();
  b.license=lic;b.name=name;b.admin=admin;b.reason=reason;
  b.expires=dur>0?System.currentTimeMillis()+dur:0;
  b.active=true;
  bans.put(lic,b); save();
  Bukkit.getBanList(BanList.Type.NAME).addBan(name,reason,b.expires==0?null:new Date(b.expires),admin);
  Player p=Bukkit.getPlayerExact(name);
  if(p!=null) p.kickPlayer("Zbanowany\nPowód: "+reason+"\nID: "+lic);
  return lic;
 }

 public static Ban getByName(String n){
  return bans.values().stream().filter(b->b.name.equalsIgnoreCase(n)&&b.active).findFirst().orElse(null);
 }

 public static void tick(){
  long now=System.currentTimeMillis();
  for(Ban b:bans.values()){
   if(b.active && b.expires>0 && now>=b.expires){
    b.active=false;
    Bukkit.getBanList(BanList.Type.NAME).pardon(b.name);
   }
  }
  save();
 }

 private static void load(){
  for(String k:cfg.getKeys(false)){
   Ban b=new Ban();
   b.license=k;
   b.name=cfg.getString(k+".name");
   b.admin=cfg.getString(k+".admin");
   b.reason=cfg.getString(k+".reason");
   b.expires=cfg.getLong(k+".expires");
   b.active=cfg.getBoolean(k+".active");
   bans.put(k,b);
  }
 }

 private static void save(){
  cfg.getKeys(false).forEach(k->cfg.set(k,null));
  for(Ban b:bans.values()){
   cfg.set(b.license+".name",b.name);
   cfg.set(b.license+".admin",b.admin);
   cfg.set(b.license+".reason",b.reason);
   cfg.set(b.license+".expires",b.expires);
   cfg.set(b.license+".active",b.active);
  }
  try{cfg.save(file);}catch(IOException e){}
 }
}
