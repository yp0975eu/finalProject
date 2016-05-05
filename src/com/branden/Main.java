package com.branden;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            System.out.println(InetAddress.getLocalHost());
        } catch ( UnknownHostException ex){
            System.out.println(ex);
        }
    }
}
