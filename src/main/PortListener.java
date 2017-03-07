package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * PortListener class will be listening on the specified port for requests and
 * display the message received on the console
 */
public class PortListener implements Runnable {

    private int portNumber;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outputStreamToClient;
    private BufferedReader bufferedReader;
    private InputStream inputStreamFromClient;
    private String input;
    private String header;
    private StringTokenizer stringTokenizer;
    private String HTTPmethod;
    private String queryFromHeader;
    private ClientResponse clientResponse;

    public PortListener() {
        System.out.println("Listener Thread Created");
        this.portNumber = 6789;
        clientResponse = new ClientResponse();

        try {
            this.serverSocket = new ServerSocket(portNumber);
            this.clientSocket = null;
            this.outputStreamToClient = null;
            this.bufferedReader = null;
            this.inputStreamFromClient = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PortListener(int portNumber) {
        System.out.println("Listener Thread Created");
        this.portNumber = portNumber;
        clientResponse = new ClientResponse();

        try {
            serverSocket = new ServerSocket(portNumber);
            this.clientSocket = null;
            this.outputStreamToClient = null;
            this.bufferedReader = null;
            this.inputStreamFromClient = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            System.out.printf("Checking for header on port %d....\n", this.portNumber);
            this.clientSocket = serverSocket.accept();
            this.outputStreamToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            this.inputStreamFromClient = clientSocket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFromClient));

            this.input = bufferedReader.readLine();
            this.header = input;
            this.stringTokenizer = new StringTokenizer(header);
            this.HTTPmethod = stringTokenizer.nextToken();
            this.queryFromHeader = stringTokenizer.nextToken();

            System.out.println(HTTPmethod + "**" + queryFromHeader);

            System.out.println("Start of request body.......");
            while (!input.isEmpty()) {
                input = bufferedReader.readLine();
                System.out.println(input);
            }
            System.out.println("End of request body");

            System.out.println("PROCESSING INPUT....");
            clientResponse.processInput(HTTPmethod, queryFromHeader, outputStreamToClient);
            System.out.println("END PROCESSING INPUT....");


//            clientSocket.close();
//            outputStreamToClient.close();
//            inputStreamFromClient.close();
//            bufferedReader.close();

//            clientSocket = serverSocket.accept();
//            outputStreamToClient = new PrintWriter(clientSocket.getOutputStream(), true);
//            inputStreamFromClient = clientSocket.getInputStream();
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFromClient));
//
//            input = bufferedReader.readLine();
//
//            while (!input.isEmpty()) {
//                input = bufferedReader.readLine();
//                System.out.println(input);
//            }
//            outputStreamToClient.println("GOT YOUR MESSAGE 2");


//
//            clientSocket.close();
//            outputStreamToClient.close();
//            inputStreamFromClient.close();
//            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
