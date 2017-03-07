package main;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * ClientSocketHandler class will be listening on the specified port for requests and
 * display the message received on the console
 */
public class ClientSocketHandler extends Thread {

    private Socket clientSocket;
    private OutputStream outputStreamToClient;
    private BufferedReader bufferedReader;
    private InputStream inputStreamFromClient;
    private String input;
    private String header;
    private StringTokenizer stringTokenizer;
    private String HTTPmethod;
    private String queryFromHeader;
    private ClientResponse clientResponse;
    private String contentTypeExpected;

    public ClientSocketHandler(Socket clientSocket) {
        System.out.println("ClientSocketHandler Thread Created");
        clientResponse = new ClientResponse();

        try {
            this.clientSocket = clientSocket;
            this.outputStreamToClient = null;
            this.bufferedReader = null;
            this.inputStreamFromClient = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        try {
//            this.outputStreamToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            this.outputStreamToClient = clientSocket.getOutputStream();
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
                if (input.contains("Accept:")) {
                    contentTypeExpected = input.split(" ")[1]; // getting the type of content the client is expecting
                    System.out.println("******** " + contentTypeExpected);
                }
            }
            System.out.println("End of request body");

            System.out.println("PROCESSING INPUT....");
            clientResponse.processInput(HTTPmethod, queryFromHeader, outputStreamToClient, contentTypeExpected);
            System.out.println("END PROCESSING INPUT....");


            clientSocket.close();
            inputStreamFromClient.close();
            bufferedReader.close();


        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
