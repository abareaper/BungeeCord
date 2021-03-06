package net.md_5.bungee;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import static net.md_5.bungee.Logger.$;
import net.md_5.bungee.api.config.ListenerInfo;

/**
 * Thread to listen and dispatch incoming connections to the proxy.
 */
public class ListenThread extends Thread
{

    public final ServerSocket socket;
    private final ListenerInfo info;

    public ListenThread(ListenerInfo info) throws IOException
    {
        super("Listen Thread - "+ info);
        this.info = info;
        socket = new ServerSocket();
        socket.bind(info.getHost());
    }

    @Override
    public void run()
    {
        while (BungeeCord.getInstance().isRunning)
        {
            try
            {
                Socket client = socket.accept();
                BungeeCord.getInstance().setSocketOptions(client);
                $().info(client.getInetAddress() + " has connected");
                InitialHandler handler = new InitialHandler(client,info);
                BungeeCord.getInstance().threadPool.submit(handler);
            } catch (SocketException ex)
            {
            } catch (IOException ex)
            {
                ex.printStackTrace(); // TODO
            }
        }
    }
}
