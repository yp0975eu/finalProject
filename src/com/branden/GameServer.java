package com.branden;

import java.io.IOException;
import java.net.*;

/**
 * Creates game server on specified port
 * Created by badams
 */
public class GameServer implements Runnable {

    int portNum;
    GameServer(){
        // get random port from a thread and then close it. pass it to the broadcast
    try (
                DatagramSocket socket = new DatagramSocket()
        ) {
            portNum = socket.getLocalPort();
            socket.close();
        } catch (SocketException ex) {
            System.out.println("Error connecting games server socket");
        }
    }
    @Override
    public void run() {
        System.out.println("Creating game socket");
        // create game server UDP socket for communicating with clients
        try {
            System.out.println("Local host address " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex){
            System.out.println("error with host");
        }try (

                DatagramSocket socket = new DatagramSocket(portNum)
        ) {
            portNum = socket.getLocalPort();
            byte[] buf = new byte[256];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(p);
                String message = new String(p.getData()).trim();

                System.out.println( "Received Message" + message);
            } catch (IOException ex) {
                System.out.println("Error with receiving packets" + ex);
            }
        } catch (SocketException ex) {
            System.out.println("Error connecting games server socket");
        }
    }

    public int getPortNum() {
        return portNum;
    }


}
