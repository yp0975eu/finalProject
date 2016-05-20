package com.branden;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Broadcast network information to clients for connecting to game server
 * uses a singleton pattern to create only one instance of the broadcast
 * based on http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
 */
public class Broadcast implements Runnable{
    private final int BROADCAST_PORT = 58943;
    private static Integer GAME_PORT;

    private static final String ClIENT_REQUEST_MESSAGE = "SNAKE_GAME_CLIENT_REQUEST";
    //Keep a socket open to listen to all the UDP trafic that is destined for this port
    // http://www.howtogeek.com/225487/what-is-the-difference-between-127.0.0.1-and-0.0.0.0/
    Broadcast(int portNum){
        // set game port for broadcasting to clients
        GAME_PORT = portNum;
    }
    @Override
    public void run() {

        // use try with resources to close any ports and sockets upon exit of program or exception
        try (
                DatagramSocket socket = new DatagramSocket( BROADCAST_PORT, InetAddress.getByName("0.0.0.0") )
                //DatagramSocket socket = new DatagramSocket()

        ) {

            socket.setBroadcast(true);
            while (true) {
                System.out.println("Broadcasting Packets on : "+socket.getLocalPort());

                //Receive a packet

                byte[] recvBuf = new byte[15000];

                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

                socket.receive(packet);

                //Packet received

                System.out.println("Discovery packet received from: " + packet.getAddress().getHostAddress());
                String message = new String(packet.getData()).trim();
                System.out.println("Packet received. Data: " + message );

                //See if the packet holds the right command (message)
                // SNAKE_GAME_CLIENT_REQUEST

                //String message = new String(packet.getData()).trim();

                if (message.equalsIgnoreCase("SNAKE_GAME_CLIENT_REQUEST")) {
                    System.out.println("Trying to send response");
                    byte[] responseBuf = ("GAME_PORT="+GAME_PORT).getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(responseBuf, responseBuf.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                    System.out.println("Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("problems with opening socket" + ex);
        }
        catch (IOException ex) {
            System.out.println("Error sending packets" + ex);
        }

    }
    public static Broadcast getBroadcastInstance(){
        return BroadcastHolder.INSTANCE;
    }
    public static class BroadcastHolder{
        public final static Broadcast INSTANCE = new Broadcast(GAME_PORT);
    }
}

