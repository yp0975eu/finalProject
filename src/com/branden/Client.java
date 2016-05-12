package com.branden;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * Created by badams on 5/12/16.
 * Based on http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
 * Open socket,
 * try to send broadcast
 * get ip address of game server
 * close socket
 *
 * re-open socket on game server ip address
 */
public class Client {
    private final int BROADCAST_PORT = 58943;
    int port;
    InetAddress ipAddress;
    DatagramSocket clientSocket;
    Client() {
        if ( discover() ){
            clientSocket = connect();
        }
    }
    public boolean discover(){
        // open socket
        try(
                DatagramSocket clientSocket = new DatagramSocket()
        ){
            // set broadcast flag to true
            clientSocket.setBroadcast(true);

            // create data packet buffer
            byte[] buf = new byte[256];
            buf = "SNAKE_GAME_CLIENT_REQUEST".getBytes();

            // try to send packet to the the default broadcast address
            try {
                System.out.println("Trying to send packet to default address");
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), BROADCAST_PORT);
                clientSocket.send(datagramPacket);
            } catch (UnknownHostException ex ){
                System.out.println("Error creating datagram packet");
            } catch (IOException ex){
                System.out.println("Error sending packet");
            }

            // try to receive packet

            byte[] recBuf = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);
            try {
                clientSocket.receive(receivePacket);
                String message = new String(receivePacket.getData()).trim();
                System.out.println(message);
                port = Integer.parseInt(message.split("=")[1]);
                ipAddress = receivePacket.getAddress();
                System.out.println(
                        "IP Address: "+ ipAddress.getCanonicalHostName() +"\nPort: "+receivePacket.getPort());
                // close port
                clientSocket.close();
                return true;
            } catch (IOException ex){
                System.out.println("Error receiving packet");
            }
            catch (InputMismatchException ex){
                System.out.println("Error receiving game port message" + ex);
            }
        }catch (
                SocketException ex
                ){
            System.out.println("Error with creating socket" + ex);
        }
        return false;
    }
    public DatagramSocket connect(){
        System.out.println("Trying to connect to game server");
        System.out.println(ipAddress.getCanonicalHostName());
        System.out.println(port);
        try(
                DatagramSocket clientSocket = new DatagramSocket()
        ){
            return clientSocket;

        } catch (SocketException ex){
            System.out.println("Error connecting directly to socket" ); ex.printStackTrace();
        }
        return clientSocket;
    }
    public void sendPosition(Snake s) {
        LinkedList<Point> segments;
        byte[] buf = new byte[256];
        int headX = s.getSnakeHeadX();
        int headY = s.getSnakeHeadX();
        // convert segments to bytes to send
        buf = (headX + ","+headY).getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                ipAddress, port);
        try {
            clientSocket.send(packet);
        } catch (IOException ex) {
            System.out.println("Error sending to socket");
        }
    }

}

