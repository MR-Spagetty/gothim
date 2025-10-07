package ecs.engr302.team14.gothim.networking;

import ecs.engr302.team14.gothim.persistancy.JSONObject;
import ecs.engr302.team14.gothim.persistancy.Serialization;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.Duration;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

/**
 * Connection class for handling connections between hosts and clients.
 *
 * @author MR-Spagetty
 */
public abstract class Connection extends Thread implements Closeable {
    static final int DEFAULT_PORT = 1234567;
    static final Duration PACKET_WAIT = Duration.ZERO;
    final Socket socket;

    private final Queue<Packet> packetQueue = new LinkedList<>();
    private final Scanner in;
    private final PrintStream out;

    protected Connection(Socket sock) {
        this.socket = new Socket();
        try {
            this.out = new PrintStream(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
            this.in.useDelimiter("\\Z");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet) {
        out.println(Serialization.toJSON(packet));
    }

    /**
     * Gets the next available packet from this connection.
     *
     * @return the packet
     * @throws NoSuchElementException if no packet is available
     * @see #hasPacket()
     */
    public synchronized Packet nextPacket() throws NoSuchElementException {
        if (packetQueue.isEmpty()) {
            throw new NoSuchElementException("No packets to get");
        }
        return packetQueue.poll();
    }

    public synchronized boolean hasPacket() {
        return !packetQueue.isEmpty();
    }

    protected synchronized void addPacket(Packet p) {
        packetQueue.offer(p);
    }

    private void recvPackets() {
        String working = "";
        while (in.hasNext()) {
            working += in.next();
            try {
                var found = JSONObject.parse(working);
                addPacket((Packet) Serialization.fromJSON(found.getKey()));
                working = working.substring(found.getValue());
            } catch (IllegalArgumentException iae) {
                continue; // not got a whole packet yet
            }
        }
    }

    @Override
    public void run() {
        while (socket.isConnected() && !socket.isClosed()) {
            recvPackets();
            try {
                Thread.sleep(PACKET_WAIT);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }
}
