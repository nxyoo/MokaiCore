package org.nxyo.mokaicore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(cmd.getName().equalsIgnoreCase("help")) {
                player.sendMessage("§7------------------------------------");
                player.sendMessage("§ePour toutes demande aide");
                player.sendMessage("§eVennez sur notre serveur discord!");
                player.sendMessage("§6https://dsc.gg/mokaiterium");
                player.sendMessage("§7------------------------------------");
                return true;
            }

            if(cmd.getName().equalsIgnoreCase("broadcast")) {
                if(args.length == 0) {
                    player.sendMessage("§4Vous devez mettre un argument! (/broadcast <message>)");
                }
                if (args.length >= 1) {
                    StringBuilder bc = new StringBuilder();
                    for(String part : args) {
                        bc.append(part).append(" ");
                    }
                    Bukkit.broadcastMessage("§4[§cAnnonce§4] §f" + bc);
                }
            }

            if(cmd.getName().equalsIgnoreCase("spawn")) {
                Location spawn = new Location(player.getWorld(), -326.5,69, 7.5, -89.8f, 0.6f);
                player.teleport(spawn);
            }

            if(cmd.getName().equalsIgnoreCase("discord")) {
                player.sendMessage("§7------------------------------------");
                player.sendMessage("§6https://dsc.gg/mokaiterium");
                player.sendMessage("§7------------------------------------");
                return true;
            }

            if(cmd.getName().equalsIgnoreCase("site")) {
                player.sendMessage("§7------------------------------------");
                player.sendMessage("§6http://mokaiterium.legameeer.fr/");
                player.sendMessage("§7------------------------------------");
                return true;
            }

            if(cmd.getName().equalsIgnoreCase("fly")) {
                if(args.length == 0) {
                    player.sendMessage("§4Vous devez mettre un argument! (/fly <on/off>)");
                }
                if(args[0].equalsIgnoreCase("on")) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage("Envole toi petit oiseau!");
                }
                if(args[0].equalsIgnoreCase("off")) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage("Mince! Tu à perdu t'es ailes :'(");
                }
            }

            if(cmd.getName().equalsIgnoreCase("mokaicore")) {
                if(args.length == 0) {
                    player.sendMessage("§7§l§m---------------------------------");
                    player.sendMessage("§f§l        MokaiCore, by §cnxyo");
                    player.sendMessage("§f§l             /mc reload");
                    player.sendMessage("§7§l§m---------------------------------");
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")) {
                    Plugin mokaicore = pluginManager.getPlugin("MokaiCore");
                    pluginManager.disablePlugin(mokaicore);
                    pluginManager.enablePlugin(mokaicore);
                    player.sendMessage("§fPlugin §c§l" + mokaicore.getName() + " §rrechargé avec succès !");
                    return true;
                } else {
                    player.sendMessage("§c");
                }
                return true;
            }

        }
        return false;
    }
}
