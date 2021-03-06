package net.md_5.bungee.tablist;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.packet.PacketC9PlayerListItem;

public class GlobalPingTabList extends GlobalTabList
{

    private static final int PING_THRESHOLD = 20;
    private final Map<ProxiedPlayer, Integer> lastPings = new ConcurrentHashMap<>();

    @Override
    public void onDisconnect(ProxiedPlayer player)
    {
        lastPings.remove(player);
        super.onDisconnect(player);
    }

    @Override
    public void onPingChange(ProxiedPlayer player, int ping)
    {
        Integer lastPing = lastPings.get(player);
        if (lastPing == null || (ping - PING_THRESHOLD > lastPing && ping + PING_THRESHOLD < lastPing))
        {
            BungeeCord.getInstance().broadcast(new PacketC9PlayerListItem(player.getDisplayName(), true, ping));
            lastPings.put(player, ping);
        }
    }
}
