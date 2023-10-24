package fr.kiruw.staff.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffCommand implements CommandExecutor {

    private final Map<Player, Boolean> staffModeMap = new HashMap<>();
    private final Map<Player, ItemStack[]> savedInventories = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lyxiz.staff")) {
            player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        // Inversez l'état du mode personnel (staff)
        boolean staffModeEnabled = toggleStaffMode(player);

        if (staffModeEnabled) {
            // Le mode personnel est activé, enregistrez l'inventaire actuel du joueur et donnez les objets du mode staff.
            savedInventories.put(player, player.getInventory().getContents());
            ItemStack[] staffInventoryContents = getStaffInventoryContents();
            player.getInventory().setContents(staffInventoryContents);
            player.updateInventory();
            player.sendMessage("Le mode personnel a été activé.");
        } else {
            // Le mode personnel est désactivé, restaurez l'inventaire précédent du joueur.
            if (savedInventories.containsKey(player)) {
                player.getInventory().setContents(savedInventories.get(player));
                player.updateInventory();
                savedInventories.remove(player);
                player.sendMessage("Le mode personnel a été désactivé.");
            }
        }

        return true;
    }

    public boolean isStaffModeEnabled(Player player) {
        return staffModeMap.getOrDefault(player, false);
    }

    public boolean toggleStaffMode(Player player) {
        boolean currentMode = isStaffModeEnabled(player);
        staffModeMap.put(player, !currentMode);
        return !currentMode;
    }

    private ItemStack[] getStaffInventoryContents() {
        ItemStack ice = new ItemStack(Material.ICE);
        ItemMeta iceMeta = ice.getItemMeta();
        iceMeta.setDisplayName(ChatColor.AQUA + "Freeze");
        ice.setItemMeta(iceMeta);

        ItemStack creeperDust = new ItemStack(Material.SULPHUR);
        ItemMeta creeperDustMeta = creeperDust.getItemMeta();
        creeperDustMeta.setDisplayName(ChatColor.GREEN + "Toggle Chat");
        creeperDust.setItemMeta(creeperDustMeta);

        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStar.getItemMeta();
        netherStarMeta.setDisplayName(ChatColor.YELLOW + "TP aléatoire");
        netherStar.setItemMeta(netherStarMeta);

        ItemStack vanishSugar = new ItemStack(Material.SUGAR);
        ItemMeta sugarMeta = vanishSugar.getItemMeta();
        sugarMeta.setDisplayName(ChatColor.AQUA + "Vanish");
        vanishSugar.setItemMeta(sugarMeta);

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.BLUE + "Voir l'inventaire");
        chest.setItemMeta(chestMeta);

        return new ItemStack[]{ice, creeperDust, netherStar, vanishSugar, chest};
    }
}
