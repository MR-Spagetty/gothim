package ecs.engr302.team14.gothim.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.IntStream;

import javax.swing.SwingUtilities;

import ecs.engr302.team14.gothim.app.ActionHandler;
import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.app.Main;
import ecs.engr302.team14.gothim.entities.Disguise;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.renderer.Renderer;
import ecs.engr302.team14.gothim.util.Direction;

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
    @Override
    protected void recvPackets() {
        super.recvPackets();
        while (hasPacket()) {
            Packet packet = nextPacket();
            switch (packet) {
                case Packet.Request req -> {
                    if (ActionHandler.currMove != Direction.None) {
                        sendPacket(new Packet.Action(Main.playerID, PlayerAction.values()[ActionHandler.currMove.ordinal()]));
                    }
                }
                case Packet.Update up -> {
                    var data = up.update();
                    if (!data.levelID().equals(LevelManager.getLevelData().levelID())) {
                        LevelManager.setLevel(data.levelID());
                    }
                    IntStream.range(0, data.playerData().size()).forEach(i -> {
                        var recvP = data.playerData().get(i); var currP = LevelManager.getLevelData().getPlayer(i);
                        if (!recvP.getPosition().equals(currP.getPosition())){
                            LevelManager.getLevelData().map().getTile(recvP.getPosition()).setOccupant(currP);
                            LevelManager.getLevelData().map().getTile(currP.getPosition()).setOccupant(null);
                            currP.setPosition(recvP.getPosition());
                        }
                    });
                    IntStream.range(0, data.nonPlayersData().size()).forEach(i -> {
                        var recvE = data.nonPlayersData().get(i); var currE = LevelManager.getLevelData().entities().get(i);
                        if (!recvE.getPosition().equals(currE.getPosition())) {
                            var occupant = LevelManager.getLevelData().map().getTile(currE.getPosition()).getOccupant();
                            if (currE instanceof Disguise d && occupant.isPresent() && occupant.get() instanceof Player p && p.getDisguise() != d) {
                                d.interact(p);
                            } else {
                                LevelManager.getLevelData().map().getTile(recvE.getPosition()).setOccupant(currE);
                            }
                            var oldPosOcc = LevelManager.getLevelData().map().getTile(currE.getPosition()).getOccupant();
                            if (oldPosOcc.isPresent() && oldPosOcc.get() == currE) {
                                LevelManager.getLevelData().map().getTile(currE.getPosition()).setOccupant(null);
                            }
                            currE.setPosition(recvE.getPosition());
                        }
                    });
                    data.foundClues().forEach(LevelManager.getLevelData().clues()::findClue);
                    SwingUtilities.invokeLater(Renderer.getInstance()::repaint);
                }
                default -> {System.out.println("Ignored Packet: "+ packet);}
            }
        }
    }
}
