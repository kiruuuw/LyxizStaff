package fr.kiruw.staff.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class StaffInventoryListener implements Listener {

    private final EventListener chatLockListener;
    private final Map<Player, Boolean> vanishedPlayers = new HashMap<>();
    private final Map<Player, PotionEffect> frozenPlayers = new HashMap<>();

    public StaffInventoryListener(EventListener chatLockListener) {
        this.chatLockListener = chatLockListener;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("lyxiz.staff") && event.getItem() != null) {
            ItemStack item = event.getItem();

            if (item.getType() == Material.ICE) {
                Player targetPlayer = getTargetPlayer(player, 5);
                if (targetPlayer != null) {
                    if (frozenPlayers.containsKey(targetPlayer)) {
                        targetPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                        frozenPlayers.remove(targetPlayer);
                        player.sendMessage(ChatColor.RED + "Le joueur " + targetPlayer.getName() + " a été dégelé.");
                        targetPlayer.sendMessage(ChatColor.RED + "Vous avez été dégelé !");
                    } else {
                        PotionEffect freezeEffect = new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false);
                        targetPlayer.addPotionEffect(freezeEffect);
                        frozenPlayers.put(targetPlayer, freezeEffect);
                        player.sendMessage(ChatColor.RED + "Le joueur " + targetPlayer.getName() + " a été gelé.");
                        targetPlayer.sendMessage(ChatColor.RED + "Vous avez été gelé !");
                    }
                }
            } else if (item.getType() == Material.SULPHUR) {
                if (chatLockListener.toggleChatLock(player)) {
                    player.sendMessage(ChatColor.GREEN + "Le LockChat a été activé.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Le LockChat a été désactivé.");
                }
            } else if (item.getType() == Material.NETHER_STAR) {
                Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);

                if (onlinePlayers.length > 1) {
                    int randomIndex;
                    Player randomPlayer;

                    do {
                        randomIndex = (int) (Math.random() * onlinePlayers.length);
                        randomPlayer = onlinePlayers[randomIndex];
                    } while (randomPlayer == player);

                    player.teleport(randomPlayer);
                    player.sendMessage(ChatColor.YELLOW + "Vous avez été téléporté vers " + randomPlayer.getName());
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Aucun autre joueur à qui se téléporter.");
                }

            } else if (item.getType() == Material.SUGAR) {
                if (player.hasPermission("lyxiz.vanish")) {
                    if (isVanished(player)) {
                        setVanished(player, false);
                        player.sendMessage(ChatColor.AQUA + "Vous êtes maintenant visible.");
                    } else {
                        setVanished(player, true);
                        player.sendMessage(ChatColor.AQUA + "Vous êtes maintenant invisible.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de faire cela.");
                }
            } else if (item.getType() == Material.CHEST) {
                Player targetPlayer = getTargetPlayer(player, 5);
                if (targetPlayer != null) {
                    PlayerInventory targetInventory = targetPlayer.getInventory();
                    player.openInventory(targetInventory);
                    player.sendMessage(ChatColor.BLUE + "Consultation de l'inventaire de " + targetPlayer.getName());
                } else {
                    player.sendMessage(ChatColor.BLUE + "Aucun joueur à inspecter.");
                }
            }
        }
    }

    private Player getTargetPlayer(Player player, int range) {
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof Player) {
                return (Player) entity;
            }
        }
        return null;
    }

    private boolean isVanished(Player player) {
        return vanishedPlayers.getOrDefault(player, false);
    }

    private void setVanished(Player player, boolean vanished) {
        if (vanished) {
            vanishedPlayers.put(player, true);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player) {
                    onlinePlayer.hidePlayer(player);
                }
            }
        } else {
            vanishedPlayers.put(player, false);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player) {
                    onlinePlayer.showPlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.containsKey(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.containsKey(player)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            double x = Math.floor(from.getX());
            double z = Math.floor(from.getZ());
            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                x += 0.5;
                z += 0.5;
                event.setCancelled(true);
                event.getPlayer()
                        .teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
            }
        }
    }

}
