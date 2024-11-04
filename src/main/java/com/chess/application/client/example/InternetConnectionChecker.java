package com.chess.application.client.example;

import java.io.IOException;
import java.net.InetAddress;

public class InternetConnectionChecker {
    public static void main(String[] args) {
        if (isInternetConnected()) {
            System.out.println("Internet is connected.");
        } else {
            System.out.println("Internet is not connected.");
        }
    }

    public static boolean isInternetConnected() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(5000); // 5-second timeout, adjust as needed
        } catch (IOException e) {
            return false;
        }
    }
}
