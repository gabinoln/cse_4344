package main;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gabino Luna
 * CSE-4344
 * Lab 1
 */
public class Main {

    static ConnectionThread connectionThread;
    static Thread listenerThread;
    static ServerSocket server;

    public static void main(String [] args) {
        int portNumber;
        if (args.length == 0) {
            portNumber = 6789;
        } else {
            portNumber = Integer.valueOf(args[0]);
        }

        try {
            server = new ServerSocket(portNumber);

            while (true) {
                Socket clientSocket = server.accept();
                new ClientSocketHandler(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
