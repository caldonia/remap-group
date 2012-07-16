package net.caldonia.bukkit.plugins.RemapGroup;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerMonitor implements Listener {
    private PermissionsPlugin permissionsPlugin = null;
    private RGPlugin ourPlugin = null;

    public PlayerMonitor(PermissionsPlugin permissionsPlugin, RGPlugin ourPlugin) {
        this.permissionsPlugin = permissionsPlugin;
        this.ourPlugin = ourPlugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        String playerName = player.getDisplayName();

        Server server = player.getServer();

        List<Group> groupList = permissionsPlugin.getGroups(playerName);

        // Cycle through users groups.
        for (Group group : groupList) {
            if (ourPlugin.getGroupMappings().containsKey(group.getName())) {
                Mapping map = ourPlugin.getGroupMappings().get(group.getName());

                ourPlugin.consoleOut(playerName + " | " + map.getInfo());

                server.dispatchCommand(server.getConsoleSender(), "permissions player addgroup " + playerName + " " + map.getTo());
                server.dispatchCommand(server.getConsoleSender(), "permissions player removegroup " + playerName + " " + group.getName());

                String announce = map.getAnnounce(playerName);

                if (announce != null)
                    server.broadcastMessage(announce);


                String direct = map.getDirect(playerName);

                if (direct != null)
                    player.sendMessage(direct);
            }
        }
    }
}
