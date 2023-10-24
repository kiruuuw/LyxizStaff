package fr.kiruw.staff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.kiruw.staff.listeners.EventListener;

public class LockChatCommand implements CommandExecutor {

    private final EventListener eventListener;

    public LockChatCommand(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lyxiz.lockchat")) {
            player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (eventListener.toggleChatLock(player)) {
            player.sendMessage("Le chat a été verrouillé. Seules les personnes ayant la permission lyxiz.lockchat peuvent envoyer des messages.");
        } else {
            player.sendMessage("Le chat a été déverrouillé.");
        }

        return true;
    }
}
