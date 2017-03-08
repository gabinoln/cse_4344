package main;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * ClientSocketHandler class will be listening on the specified port for requests and
 * display the message received on the console. This class will run as a thread to handle
 * client requests as needed
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
    private String queryFromHeader; // specifies what the client requests
    private ClientResponse clientResponse; // handles response to client
    private String contentTypeExpected; // what kind of content the client expects (text\image)

    /*
     * ClientSocketHandler(Socket clientSocket)
     * inputs: Socket clientSocket is the socket in which the client has connected to.
     *
     * outputs: none
     *
     * purpose: declare client socket and create ClientResponse object to be used with
     * the client socket.
     */
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


    /*
     * run()
     * inputs: none
     *
     * outputs: none
     *
     * purpose: run the thread to handle the client socket which will first get the IO streams from the socket
     * in order to read/write to the socket.
     *
     * a BufferedReader object is used to read the input stream from the socket
     *
     * the input String object is used to store each line from the input stream as a string
     *
     * the header is stored to retrieve the HTTP method used and request from the client, which is the first line of
     * the input stream and this is done using the StringTokenizer object
     *
     * after these variables are initiated the request body is printed on the console
     * and the ClientResponse object is used to handle the request accordingly
     */
    @Override
    public void run() {
        try {
            this.outputStreamToClient = clientSocket.getOutputStream();
            this.inputStreamFromClient = clientSocket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFromClient));

            this.input = bufferedReader.readLine();
            this.header = input;
            this.stringTokenizer = new StringTokenizer(header);
            this.HTTPmethod = stringTokenizer.nextToken();
            this.queryFromHeader = stringTokenizer.nextToken();

            System.out.println("Start of request body.......");
            while (!input.isEmpty()) {
                input = bufferedReader.readLine();
                System.out.println(input);
                if (input.contains("Accept:")) {
                    contentTypeExpected = input.split(" ")[1]; // getting the type of content the client is expecting
                }
            }
            System.out.println("End of request body");

            clientResponse.processInput(HTTPmethod, queryFromHeader, outputStreamToClient, contentTypeExpected);


            clientSocket.close();
            inputStreamFromClient.close();
            bufferedReader.close();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
