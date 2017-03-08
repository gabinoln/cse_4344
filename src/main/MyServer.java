package main;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gabino Luna
 * CSE-4344
 * Lab 1
 *
 * This program will serve as a server which will constantly listen on the port number specified through the command line,
 * or the default port number of 6789. When the server receives a HTTP request the server will respond accordingly with
 * error codes 200, 301, 404, or 405. The only files accessible on this server are 'index.html' and 'checkmark.jpg' which
 * is referenced on 'index.html'.
 *
 * The server will run until the process is killed through the terminal,
 */

public class MyServer {

    /*
     * Our server which will operate on the port number as previously mentioned.
     */
    static ServerSocket server;

    public static void main(String[] args) {
        int portNumber;
        if (args.length == 0) { // default port number
            portNumber = 6789;
        } else { // if the port number is specified
            portNumber = Integer.valueOf(args[0]);
        }

        try {
            // start the server
            server = new ServerSocket(portNumber);

            /*
             * the following loop runs indefinitely until interrupted and listens on specified port, and
             * starts a thread to handle the client
             */
            while (true) {
                Socket clientSocket = server.accept();
                new ClientSocketHandler(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
