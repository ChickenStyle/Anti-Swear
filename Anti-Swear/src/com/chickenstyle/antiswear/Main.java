package com.chickenstyle.antiswear;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener{
	
	HashMap<Player,Integer> cursed = new HashMap<Player,Integer>();
	@Override
	public void onEnable() {
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Anti-Swear was Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		this.getConfig().options().copyDefaults();
	    saveDefaultConfig();
	    getCommand("antiswear").setExecutor(new AntiSwear());
	}

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void messageSend(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String[] messageList = e.getMessage().split(" ");
		List<String> kickCommands = getConfig().getStringList("kickcommands");
		String message = e.getMessage();
		String newmessage = message;
		String titlemessage = getConfig().getString("titlemessage");
		String text = getConfig().getString("message");
		String subtitletext = getConfig().getString("subtitle");
		String type = getConfig().getString("type");
		String sound = getConfig().getString("sound").toUpperCase();
		int duration = getConfig().getInt("messageduraion") * 20;
		long checkDelay = getConfig().getLong("timeframe") * 20;
		int maxamount = getConfig().getInt("maxamountswears");
		boolean containsCurse = false;
		
		
		for (String messageword: messageList) {
			for (String CurseWord: getConfig().getStringList("swearwords")) {
				if (messageword.equalsIgnoreCase(CurseWord)) {
					String dots = "";
					for (int i = 0; i < messageword.length(); i++) {
						dots = dots + "*";
					}
					newmessage = newmessage.replace(messageword, dots);
					containsCurse = true;
				}
				e.setMessage(newmessage);
			}
		}
		
	if (containsCurse == true) {
		if (type.equals("title")) {
				try {
					Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
					Object subTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
					Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + titlemessage.replace("&", "§") + "\"}");
					Object subline = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitletext.replace('&', '§') + "\"}");
				
					Constructor<?> titleConstuctor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
					Object packet = titleConstuctor.newInstance(enumTitle,chat, 10, duration, 10);
					Object subpacket = titleConstuctor.newInstance(subTitle,subline, 10, duration, 10);
					sendPacket(player,packet);
					sendPacket(player,subpacket);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				player.playSound(player.getLocation(), Sound.valueOf(sound), 1f, 1f);
		} else if (type.equals("text")) {
            new BukkitRunnable() {
                private int i = 0;
                @Override
                public void run() {
                    if(i == 1) {
                        cancel();
            			player.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
            			player.playSound(player.getLocation(), Sound.valueOf(sound), 1f, 1f);
                    }
                    ++i;
                }
            }.runTaskTimer(this, 0, 5L);
		} else if (type.equals("both")) {
			try {
				Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				Object subTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + titlemessage.replace("&", "§") + "\"}");
				Object subline = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitletext.replace('&', '§') + "\"}");
			
				Constructor<?> titleConstuctor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
				Object packet = titleConstuctor.newInstance(enumTitle,chat, 10, duration, 10);
				Object subpacket = titleConstuctor.newInstance(subTitle,subline, 10, duration, 10);
				sendPacket(player,packet);
				sendPacket(player,subpacket);
				player.playSound(player.getLocation(), Sound.valueOf(sound), 1f, 1f);
	            new BukkitRunnable() {
	                private int i = 0;
	                @Override
	                public void run() {
	                    if(i == 1) {
	                        cancel();
	            			player.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	                    }
	                    ++i;
	                }
	            }.runTaskTimer(this, 0, 5L);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "AntiSwear >> Invalid type!");
		}
        new BukkitRunnable() {
            private int i = 0;
            @Override
            public void run() {
                if(i == 1) {
            		if (cursed.containsKey(player)) {
            			int amountcursed = cursed.get(player);
            			if (amountcursed == maxamount - 1) {
            				for (String s:kickCommands) {
            					getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),s.replace("%player%", player.getName()));
            				}
            			} else {
            				amountcursed++;
            				cursed.put(player, amountcursed);
            			}
            		} else {
            			cursed.put(player, 1);
    					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
    					    @Override
    					    public void run() {
    					        cursed.remove(player);
    					    }
    					},checkDelay);
            		}
                    cancel();
                }
                ++i;
            }
        }.runTaskTimer(this, 0, 5L);
	}

	}
	
	public void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket",getNMSClass("Packet")).invoke(playerConnection, packet);
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	
	
	
	
	}
}
