package fr.kiruw.staff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kiruw.staff.listeners.EventListener;

public class StaffChatCommand implements CommandExecutor {

    private final EventListener eventListener;

    public StaffChatCommand(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lyxiz.staffchat")) {
            player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (eventListener.toggleStaffChat(player)) {
            player.sendMessage("Le chat du personnel a été activé. Vous pouvez désormais envoyer des messages au personnel.");
        } else {
            player.sendMessage("Le chat du personnel a été désactivé.");
        }

        return true;
    }
}
