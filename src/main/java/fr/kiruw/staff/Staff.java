package fr.kiruw.staff;

import org.bukkit.plugin.java.JavaPlugin;
import fr.kiruw.staff.commands.LockChatCommand;
import fr.kiruw.staff.commands.StaffChatCommand;
import fr.kiruw.staff.commands.StaffCommand;
import fr.kiruw.staff.listeners.EventListener;
import fr.kiruw.staff.listeners.StaffInventoryListener;

public class Staff extends JavaPlugin {
    private static Staff instance;

    @Override
    public void onEnable() {
        EventListener eventListener = new EventListener();
        StaffInventoryListener staffInventoryListener = new StaffInventoryListener(eventListener);

        getCommand("staffchat").setExecutor(new StaffChatCommand(eventListener));
        getCommand("lockchat").setExecutor(new LockChatCommand(eventListener));
        getCommand("staff").setExecutor(new StaffCommand());

        getServer().getPluginManager().registerEvents(eventListener, this);
        getServer().getPluginManager().registerEvents(staffInventoryListener, this);

        instance = this;
    }

    @Override
    public void onDisable() {
    }

    // Méthode pour obtenir l'instance
    public static Staff getInstance() {
        return instance;
    }
}