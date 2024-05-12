package org.nxyo.mokaicore;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.nxyo.mokaicore.fastboard.FastBoard;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener, PluginMessageListener {

    public final Map<UUID, FastBoard> boards = new HashMap<>();
    private static String headerFieldName;
    private static String footerFieldName;
    File newConfig;
    FileConfiguration newConfigz;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "----------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "   MOKAICORE MADE BY NXYO");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "      COPYRIGHT 2024");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "----------------------------");
        getCommand("help").setExecutor(new Commands());
        getCommand("broadcast").setExecutor(new Commands());
        getCommand("spawn").setExecutor(new Commands());
        getCommand("discord").setExecutor(new Commands());
        getCommand("site").setExecutor(new Commands());
        getCommand("fly").setExecutor(new Commands());
        getCommand("mokaicore").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new MainListeners(), this);
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, 20);
        try {
            initFieldNames(); // Initialiser les noms de champs
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateTablist(player); // Mettre à jour la tablist pour tous les joueurs en ligne
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        newConfig = new File(getDataFolder(), "config.yml");
        newConfigz = YamlConfiguration.loadConfiguration(newConfig);
        saveNewConfig();
    }

    public void saveNewConfig(){
        try{
            newConfigz.save(newConfig);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "----------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "   MOKAICORE MADE BY NXYO");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "      COPYRIGHT 2024");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "----------------------------");
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FastBoard board = new FastBoard(player);
        board.updateTitle(ChatColor.GOLD + "Mokaiterium");
        this.boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        FastBoard board = this.boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public static void updateBoard(FastBoard board) {
        Player player = board.getPlayer();
        board.updateLines(
                "      ",
                PlaceholderAPI.setPlaceholders(player, "§7Connectés§f: %server_online%"),
                "§7Lobby§f: n°1",
                "§1",
                "§a§l✔§f " + player.getName(),
                "       ",
                "§6§lorg.fr"
        );
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }

    private void updateTablist(Player player) {
        String header = "§a§l---------------------------\\n §2§lMokaiterium - Serveur PVP Faction Modée \\n§a§l---------------------------\\n\\n     §7IP: §bA venir !\\n§7Site Web: §bA venir !\\n§7Boutique: §bA venir !\\n§7Discord: §bA venir !\\n\\n§a§l---------------------------";
        String footer = PlaceholderAPI.setPlaceholders(player, "§a§l---------------------------\\n\\n   §7Joueurs Connecté : §b%server_online%\\n §7Version du serveur : §b1.12.2");

        try {
            Object headerFooterPacket = createPacket(header, footer); // Créer le paquet avec l'en-tête et le pied de page
            sendPacket(player, headerFooterPacket); // Envoyer le paquet au joueur
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object createPacket(String header, String footer) throws Exception {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        IChatBaseComponent headerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent footerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        Field headerField = packet.getClass().getDeclaredField(headerFieldName); // Utiliser le nom du champ pour obtenir la référence au champ
        headerField.setAccessible(true);
        headerField.set(packet, headerComponent);

        Field footerField = packet.getClass().getDeclaredField(footerFieldName);
        footerField.setAccessible(true);
        footerField.set(packet, footerComponent);

        return packet;
    }

    private void sendPacket(Player player, Object packet) throws Exception {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection != null) {
            playerConnection.getClass().getMethod("sendPacket", getNMSClass()).invoke(playerConnection, packet);
        }
    }

    private Object getPlayerConnection(Player player) throws Exception {
        Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
        return craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
    }

    private Class<?> getNMSClass() throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + "Packet");
    }

    public void initFieldNames() {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter");
            boolean headerFound = false;
            boolean footerFound = false;
            for (Field field : packetClass.getDeclaredFields()) {
                String fieldName = field.getName();
                if (fieldName.contains("header")) {
                    headerFieldName = fieldName;
                    headerFound = true;
                } else if (fieldName.contains("footer")) {
                    footerFieldName = fieldName;
                    footerFound = true;
                }
            }
            if (!headerFound || !footerFound) {
                Bukkit.getLogger().log(Level.WARNING, "Could not find header or footer field in PacketPlayOutPlayerListHeaderFooter");
            } else {
                Bukkit.getLogger().log(Level.INFO, "Header field: " + headerFieldName + ", Footer field: " + footerFieldName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.WARNING, "Failed to initialize field names: " + e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(PlaceholderAPI.setPlaceholders(player,getConfig().getString("prefix") + " " + player.getDisplayName() + getConfig().getString("suffix") + getConfig().getString("separator") + " §f" +  event.getMessage()));
    }

}
