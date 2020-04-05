package com.chickenstyle.antiswear;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AntiSwear implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Main main = Main.getPlugin(Main.class);
		File file = new File(Main.getPlugin(Main.class).getDataFolder(), "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length >= 1) {
				switch (args[0].toLowerCase()) {
				case "addkickcommand":
					if (player.hasPermission("antiswear.addkickcommand") || player.hasPermission("antiswear.admin")) {
						if (args.length >= 2) {
						List<String> commands = config.getStringList("kickcommands");
						StringBuilder builder = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
						    builder.append(args[i]).append(" ");
						}
						String msg = builder.toString();
						commands.add(msg);
						config.set("kickcommands", commands);
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						player.sendMessage(ChatColor.GREEN + "Successfully added word/words to the list!");
					  } else {
						  player.sendMessage(ChatColor.RED + "Invalid Usage");
						  player.sendMessage(ChatColor.RED + "/as add [word/words]");
					  }
					} else {
						player.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "add":
					if (player.hasPermission("antiswear.addword") || player.hasPermission("antiswear.admin")) {
						if (args.length >= 2) {
						List<String> words = config.getStringList("swearwords");
						for (int i = 1; i < args.length; i++) {
						    words.add(args[i].toLowerCase());
						}
						config.set("swearwords", words);
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(ChatColor.GREEN + "Successfully added word/words to the list!");
					  } else {
						  player.sendMessage(ChatColor.RED + "Invalid Usage");
						  player.sendMessage(ChatColor.RED + "/as add [word/words]");
					  }
					} else {
						player.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "remove":
			if (player.hasPermission("antiswear.removeword") || player.hasPermission("antiswear.admin")) {
				if (args.length == 2) {
					List<String> list =  config.getStringList("swearwords");
					if (list != null) {
						int amounttime = 0;
						for (String s:list) {
							if (s.toLowerCase().equals(args[1].toLowerCase())) {
								list.remove(s);
								config.set("swearwords", list);
								amounttime++;
								try {
									config.save(file);
									main.reloadConfig();
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.sendMessage(ChatColor.GREEN + "The word " + s + " has been deleted from the list!");
								
								return true;
							}
						}
						if (amounttime == 0) {
							player.sendMessage(ChatColor.RED + "List doesnt contains this word!");
						}
						
					} else {
						player.sendMessage(ChatColor.RED + "Swear words list is empty!");
					}
 				} else {
					player.sendMessage(ChatColor.RED + "Invalid Usage");
					player.sendMessage(ChatColor.RED + "/antiswear remove [word]");
				}
			}
				break;
				
				
				case "reload":
					if (player.hasPermission("antiswear.reload") || player.hasPermission("antiswear.admin")) {
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(ChatColor.GREEN + "Config Has Been Reloaded");
					} else {
						player.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "setsound":
					if (sender.hasPermission("antiswear.setsound") || sender.hasPermission("antiswear.admin")) {
						if (isSound(args[1].toUpperCase())) {
							config.set("sound", args[1].toUpperCase());
							try {
								config.save(file);
								main.reloadConfig();
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(ChatColor.GREEN + "Sound was set successfully");
						} else {
							sender.sendMessage(ChatColor.RED + "Unvalid Sound");
						}
					}
				break;
				
				case "setmessage":
					if (sender.hasPermission("antiswear.setmessage") || sender.hasPermission("antiswear.admin")) {
						StringBuilder builder = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
						    builder.append(args[i]).append(" ");
						}
						String msg = builder.toString();
						config.set("message", msg);
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage(ChatColor.GREEN + "Message was set successfully");
					} else {
						sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "settitlemessage":
					if (sender.hasPermission("antiswear.settitlemessage") || sender.hasPermission("antiswear.admin")) {
						StringBuilder builder = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
						    builder.append(args[i]).append(" ");
						}
						String msg = builder.toString();
						config.set("titlemessage", msg);
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage(ChatColor.GREEN + "Message was set successfully");
					} else {
						sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "setsubtitle":
					if (sender.hasPermission("antiswear.setsubtitle") || sender.hasPermission("antiswear.admin")) {
						StringBuilder builder = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
						    builder.append(args[i]).append(" ");
						}
						String msg = builder.toString();
						config.set("subtitle", msg);
						try {
							config.save(file);
							main.reloadConfig();
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage(ChatColor.GREEN + "Subtitle was set successfully");
					} else {
						sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				break;
				
				case "settype":
					 if (sender.hasPermission("antiswear.admin") || sender.hasPermission("antiswear.settype")) {
							if (args[1].toLowerCase().equals("text") || args[1].toLowerCase().equals("title") ||args[1].toLowerCase().equals("both")) {
									config.set("type", args[1].toLowerCase());
									try {
										config.save(file);
										main.reloadConfig();
									} catch (IOException e) {
										e.printStackTrace();
									}
									sender.sendMessage(ChatColor.GREEN + "Type was set successfully");
							} else {
								  sender.sendMessage(ChatColor.RED + "Invalid Usage");
								  sender.sendMessage(ChatColor.RED + "Type: /inv help");
							}
						  } else {
							  sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
						  }
				break;

				case "setmaxamount":
					if (sender.hasPermission("antiswear.setmaxamount") || sender.hasPermission("antiswear.admin")) {
						if (isInteger(args[1])) {
							config.set("maxamountswears", Integer.valueOf(args[1]));
							try {
								config.save(file);
								main.reloadConfig();
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(ChatColor.GREEN + "Max amount of swears was set successfully");
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid Number");
							sender.sendMessage(ChatColor.RED + "Type: /inv help");
						}
					} else {
						  sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					  }
				break;
				
				case "setduration":
					if (sender.hasPermission("antiswear.setduration") || sender.hasPermission("antiswear.admin")) {
						if (isDouble(args[1])) {
							config.set("messageduraion", Double.valueOf(args[1]));
							try {
								config.save(file);
								main.reloadConfig();
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(ChatColor.GREEN + "Message's duration was set successfully");
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid Number");
						}
					}
				break;
				
				case "settimeframe":
					if (sender.hasPermission("antiswear.settimeframe") || sender.hasPermission("antiswear.admin")) {
						if (isInteger(args[1])) {
							config.set("timeframe", Integer.valueOf(args[1]));
							try {
								config.save(file);
								main.reloadConfig();
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(ChatColor.GREEN + "Time Frame was set successfully");
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid Number");
						}
					}
				break;
				
				case "help":
					if (sender.hasPermission("inventory.help") || sender.hasPermission("inventory.admin")) {
						sender.sendMessage(ChatColor.GRAY + "<---------------------------------->");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as add [word/words] // adds word/words to the list");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as remove [word] // remove word from the list");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as reload // reloads you config");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as settype (text/title) // sets the type of the message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as setmessage [message] // sets the message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as setsound [sound's name] // sets the sound for the message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as setduration [seconds] // sets the sound for the message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as setsubtitle [message] // sets the subtitle for the message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as setmaxamount [number] // sets max swear amount!");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as settitlemessage [message] // sets the title's message");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as addkickcommand [message] // add command that will run after the kick.");
						sender.sendMessage(ChatColor.GRAY + "");
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "/as settimeframe [seconds] // sets the time frame!");
						sender.sendMessage(ChatColor.GRAY + "<---------------------------------->");
					}
				break;
				
				default:
					  player.sendMessage(ChatColor.RED + "Invalid Usage");
					  player.sendMessage(ChatColor.RED + "/as help");
				break;
				}
			} else {
				player.sendMessage(ChatColor.RED + "Invalid Usage");
				player.sendMessage(ChatColor.GRAY + "/as help");
			}
		}
		return false;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isSound(String s) {
	    try { 
	       Sound.valueOf(s); 
	    } catch(Exception e) { 
	        return false; 
	    }
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try { 
	       Double.valueOf(s); 
	    } catch(Exception e) { 
	        return false; 
	    }
		return true;
	}

}
