package com.branden;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            // show network info
            int portNumber = 8915;
            String host = "10.0.56.134";
            InetAddress hostname = InetAddress.getByName(host);
            System.out.println("Localhost          : "+InetAddress.getLocalHost());
            System.out.println("Host Address       : "+InetAddress.getLocalHost().getHostAddress());
            System.out.println("Hostname           : "+InetAddress.getLocalHost().getHostName());
            System.out.println("Canonical Hostname : "+InetAddress.getLocalHost().getCanonicalHostName());
                try(
                        // connect to server
                        Socket echoSocket = new Socket( hostname, portNumber);
                        PrintWriter out = new PrintWriter( echoSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                        Scanner scanner = new Scanner(System.in)
                ){
                    System.out.println("Press enter to get a quote from Bruce Lee, type exit to quit\n");
                    String userInput;
                    while ( !( userInput = scanner.nextLine() ).equalsIgnoreCase("quit") ) {
                        out.println(userInput);
                        System.out.println("\"" + in.readLine() + "\"\n");
                    }
                } catch (Exception ex){

                    System.out.println("Exception: \n");
                    ex.printStackTrace();
                }
        } catch (Exception ex){
            System.out.println("Cannot connect. Try again later");
            //System.out.println(ex);
        }

    }
}
