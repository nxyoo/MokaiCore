package org.nxyo.mokaicore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainListeners implements Listener {

    public MainListeners() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setDisplayName(PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix% %player_name%"));
        player.setPlayerListName(PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix% %player_name%"));
        event.setJoinMessage(PlaceholderAPI.setPlaceholders(player, "§8[§a+§8] §7%player_name%"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.setFlying(false);
        event.setQuitMessage(PlaceholderAPI.setPlaceholders(player, "§8[§c-§8] §7%player_name%"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/plugins")) {
            event.setCancelled(true);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            player.sendMessage("§7§l§m------------------------------------");
            player.sendMessage("§6Debug: " + formatter.format(date));
            player.sendMessage("§eEnvironement: §fProduction");
            player.sendMessage("§eServeur: §fFaction");
            player.sendMessage("§eMinecraft: §fgit-CatServer-§oXXX§r-§oXXX§r (MC: §o1.12.2§r)");
            player.sendMessage("§ePlugins Internes:");
            player.sendMessage("§7> §fMokaiCore");
            player.sendMessage("§7> §fMokaiFaction");
            player.sendMessage("§7> §fMokaiSkins");
            player.sendMessage("§7> §fMokaiPerms");
            player.sendMessage("§7§l§m------------------------------------");
        }
        if (message.startsWith("/pl")) {
            event.setCancelled(true);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            player.sendMessage("§7§l§m------------------------------------");
            player.sendMessage("§6Debug: " + formatter.format(date));
            player.sendMessage("§eEnvironement: §fProduction");
            player.sendMessage("§eServeur: §fFaction");
            player.sendMessage("§eMinecraft: §fgit-CatServer-§oXXX§r-§oXXX§r (MC: §o1.12.2§r)");
            player.sendMessage("§ePlugins Internes:");
            player.sendMessage("§7> §fMokaiCore");
            player.sendMessage("§7> §fMokaiFaction");
            player.sendMessage("§7> §fMokaiSkins");
            player.sendMessage("§7> §fMokaiPerms");
            player.sendMessage("§7§l§m------------------------------------");
        }
    }


    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(e.toWeatherState());
    }




}