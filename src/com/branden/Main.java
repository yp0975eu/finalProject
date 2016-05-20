package com.branden;

import java.net.InetAddress;

public class Main {

    private static int GAMING_PORT_NUMBER;

    public static void main(String[] args) {
        try {
            // set gaming port and show host address
            String host = InetAddress.getLocalHost().getCanonicalHostName();
            System.out.println("Canonical Hostname : "+host);
            GameServer gameServer = new GameServer();

            Thread gameServerThread = new Thread( gameServer );
            gameServerThread.start();
            GAMING_PORT_NUMBER = gameServer.getPortNum();
            System.out.println(GAMING_PORT_NUMBER);
            Thread broadcastThread = new Thread(new Broadcast(GAMING_PORT_NUMBER).getBroadcastInstance());
            broadcastThread.start();
        } catch (Exception ex){
            System.out.println("Cannot connect. Try again later: "); ex.printStackTrace();
            //System.out.println(ex);
        }
        new Options();
    }
}
