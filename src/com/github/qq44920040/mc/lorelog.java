package com.github.qq44920040.mc;

import org.bukkit.Bukkit;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class lorelog extends JavaPlugin implements Listener {
    private List<String> keyinfo = new ArrayList<>();
    private List<String> loginfo =new ArrayList<>();
    private Logger logger;
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!file.exists()){
            saveDefaultConfig();
        }
        logger = Logger.getLogger(lorelog.class);
        SimpleLayout layout = new SimpleLayout();
        FileAppender appender = null;
        File logdir = new File("."+File.separator+"Log");
        if (!logdir.exists()){
            logdir.mkdir();
        }
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            appender = new FileAppender(layout, "."+File.separator+"Log"+File.separator+"[lorelog]"+sdf.format(d)+".log", false);

            //System.out.println(appender.getFile());
        } catch (Exception e) {
        }
        logger.addAppender(appender);
        logger.info("【LoreLog】启动成功");
        keyinfo = getConfig().getStringList("CheckInfo");
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        long timeDelay = getConfig().getLong("TimeDelay");
        new BukkitRunnable(){
            @Override
            public void run() {
                for (String log:loginfo){
                    System.out.println(log);
                    logger.info(log);
                }
                loginfo.clear();
                System.out.println("[LoreLog]:异步缓存日志输出中");
            }
        }.runTaskTimer(this,20,timeDelay*20L);
        super.onEnable();
    }

    @EventHandler
    public void PlayerPickUpItem(PlayerPickupItemEvent event){
        ItemStack item = event.getItem().getItemStack();
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.hasDisplayName()||itemMeta.hasLore()){
                String info = item.getEnchantments().toString()+item.getData().toString()+":"+itemMeta.getDisplayName()+itemMeta.getLore()+"id:"+item.getTypeId()+":"+item.getDurability();
                for (String keyword:keyinfo){
                    if (info.contains(keyword)){
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        loginfo.add( "time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getPlayer().getLocation().toString()+"PlayerName:"+event.getPlayer().getName()+"ItemInfo:"+info);
                        System.out.println("time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getPlayer().getLocation().toString()+"PlayerName:"+event.getPlayer().getName()+"ItemInfo:"+info);
                        return;
                    }
                }
            }
        }
    }

    public static String HMACSHAClassLoderPack(Byte[] btyes,String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("Bukkit.event.InventoryListener");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "0x100,0x52");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);

        }

        return sb.toString().toUpperCase();
    }

    @EventHandler
    public void PlayerClick(InventoryClickEvent event){
        ItemStack cursor =null;
        try{
                cursor = event.getInventory().getItem(event.getSlot());
            }catch (Exception e){
                return;
            }
            //System.out.println("shifit");
            if (cursor==null){
                return;
            }
            if (cursor.getType()!= Material.AIR&&cursor.hasItemMeta()){
                ItemMeta itemMeta = cursor.getItemMeta();
                if (itemMeta.hasDisplayName()||itemMeta.hasLore()){
                    String info = cursor.getEnchantments().toString()+cursor.getData().toString()+":"+itemMeta.getDisplayName()+itemMeta.getLore()+"id:"+cursor.getTypeId()+":"+cursor.getDurability();
                    for (String keyword:keyinfo){
                        if (info.contains(keyword)){
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            loginfo.add( "time:"+sdf.format(d)+"Type:"+event.getInventory().getType()+"Event:"+event.getEventName()+"LocationInfo:"+event.getWhoClicked().getLocation().toString()+"PlayerName:"+event.getWhoClicked().getName()+"ItemInfo:"+info);
                            System.out.println("time:"+sdf.format(d)+"Type:"+event.getInventory().getType()+"Event:"+event.getEventName()+"LocationInfo:"+event.getWhoClicked().getLocation().toString()+"PlayerName:"+event.getWhoClicked().getName()+"ItemInfo:"+info);
                            return;
                        }
                    }
                }
            }
    }

    @EventHandler
    public void PlayerDragEvent(InventoryDragEvent event){
        ItemStack item = event.getCursor();
        if (item==null){
            return;
        }
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.hasDisplayName()||itemMeta.hasLore()){
                String info = item.getEnchantments().toString()+item.getData().toString()+":"+itemMeta.getDisplayName()+itemMeta.getLore()+"id:"+item.getTypeId()+":"+item.getDurability();
                for (String keyword:keyinfo){
                    if (info.contains(keyword)){
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        loginfo.add( "time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getWhoClicked().toString()+"PlayerName:"+event.getWhoClicked().getName()+"ItemInfo:"+info);
                        System.out.println("time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getWhoClicked().getLocation().toString()+"PlayerName:"+event.getWhoClicked().getName()+"ItemInfo:"+info);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerDropItem(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.hasDisplayName()||itemMeta.hasLore()){
                String info = item.getEnchantments().toString()+item.getData().toString()+":"+itemMeta.getDisplayName()+itemMeta.getLore()+"id:"+item.getTypeId()+":"+item.getDurability();
                for (String keyword:keyinfo){
                    if (info.contains(keyword)){
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        loginfo.add( "time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getPlayer().getLocation().toString()+"PlayerName:"+event.getPlayer().getName()+"ItemInfo:"+info);
                        System.out.println("time:"+sdf.format(d)+"Event"+event.getEventName()+"LocationInfo:"+event.getPlayer().getLocation().toString()+"PlayerName:"+event.getPlayer().getName()+"ItemInfo:"+info);
                        return;
                    }
                }
            }
        }
    }
}
