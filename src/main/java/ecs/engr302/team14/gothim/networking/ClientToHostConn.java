package ecs.engr302.team14.gothim.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Basic implementation of a connection for client to host communication.
 * includes supplemental connect static methods for initiating the connection.
 *
 * @author MR-Spagetty
 */
public class ClientToHostConn extends Connection {

    protected ClientToHostConn(Socket sock) {
        super(sock);
    }

    /**
     * Connect to a host at the specified address using the default port.
     *
     * @param addr the address/hostname of the host
     * @return the connection
     * @throws UnknownHostException if the ip address of the host could not be determined
     */
    public static Connection connect(String addr) throws UnknownHostException {
        return connect(addr, DEFAULT_PORT);
    }

    /**
     * Connect to a host at the specified address on the specified port.
     *
     * @param addr the address/hostname of the host
     * @param port the port to connect on
     * @return the connection
     * @throws UnknownHostException if the ip address of the host could not be determined
     */
    public static Connection connect(String addr, int port) throws UnknownHostException {
        try {
            return new ClientToHostConn(new Socket(addr, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
