package ecs.engr302.team14.gothim.networking;

import java.net.Socket;

/**
 * Basic implementation of a connection for hot to client communication.
 *
 * <p>connection creation to occur in app?
 *
 * @author MR-Spagetty
 */
public class HostToClientConn extends Connection {
    public HostToClientConn(Socket sock) {
        super(sock);
    }

}
