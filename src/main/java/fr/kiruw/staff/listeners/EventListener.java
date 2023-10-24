package fr.kiruw.staff.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class EventListener implements Listener {

    private final Set<Player> lockedPlayers = new HashSet<>();
    private final Set<Player> staffChatEnabledPlayers = new HashSet<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isChatLocked() && !player.hasPermission("lyxiz.lockchat")) {
            event.setCancelled(true);
            player.sendMessage("§cLe chat est verrouillé. Vous ne pouvez pas envoyer de messages.");
            return;
        }

        if (isStaffChatEnabled(player) && player.hasPermission("lyxiz.staffchat")) {
            event.setCancelled(true);
            String message = "§6LYXIZ-STAFF §8>> §5" + player.getName() + " §8: §b" + event.getMessage();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("lyxiz.staffchat")) {
                    p.sendMessage(message);
                }
            }
        }
    }

    //ToggleChat

    public boolean toggleChatLock(Player player) {
        if (lockedPlayers.contains(player)) {
            lockedPlayers.remove(player);
            return false;
        } else {
            lockedPlayers.add(player);
            return true;
        }
    }

    public boolean isChatLocked() {
        return !lockedPlayers.isEmpty();
    }

    //StaffChat

    public boolean isStaffChatEnabled(Player player) {
        return staffChatEnabledPlayers.contains(player);
    }

    public boolean toggleStaffChat(Player player) {
        if (staffChatEnabledPlayers.contains(player)) {
            staffChatEnabledPlayers.remove(player);
            player.sendMessage(" §b" + player.getName() + " §ca désactivé le chat du personnel.");
            return false;
        } else {
            staffChatEnabledPlayers.add(player);
            player.sendMessage(" §b" + player.getName() + " §aa activé le chat du personnel.");
            return true;
        }
    }
}
