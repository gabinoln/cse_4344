package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 * Created by Gabino Luna on 3/6/2017.
 */
public class ClientResponse {
    private String output;

    public ClientResponse() {
        this.output = "";
    }

    public void processInput(String method, String query, PrintWriter outputStreamToClient) {
        if (!method.equals("GET")) {
            sendResponse(405, outputStreamToClient, null);
            System.out.println("Invalid/unsupported HTTP method");
        } else if (!(query.charAt(0) == '/')) {
            sendResponse(404, outputStreamToClient, null);
            System.out.println("Not a valid path requested");
        }

        String fileName = query.replaceFirst("/", "");
        File file = new File(fileName);
        if (file.isFile()) {
            System.out.println("FILE FOUND");
            sendResponse(200, outputStreamToClient, file);
        } else {
            System.out.println("FILE NOT FOUND");
            sendResponse(404, outputStreamToClient, null);
        }
    }

    public void sendResponse(int status, PrintWriter outputStreamToClient, File file) {
        String statusString = "";
        String response = "";
        String contentType = "";
        String contentLength = "Content-Length: ";
        String closeConnection = "Connection: close\r\n";
        String myServer = "Server: Gabino HTTPServer";


        switch (status) {
            case 200:
                statusString = "HTTP/1.1 200 OK\r\n";
                contentType = "Content-Type: text/html\r\n";
                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    int content;
                    while ((content = fileIn.read()) != -1) {
                        response += (char)content;
                    }
//                    System.out.println(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contentLength = contentType + response.length() + "\r\n";

                outputStreamToClient.write(statusString);
                outputStreamToClient.write(myServer);
                outputStreamToClient.write(contentType);
                outputStreamToClient.write(contentLength);
                outputStreamToClient.write(closeConnection);
                outputStreamToClient.write("\r\n");
                outputStreamToClient.write(response);
                outputStreamToClient.close();


                break;
            case 301:
                statusString = "HTTP/1.1 301 Moved Permanently\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 303 FILE PERMANENTLY</h>" +
                        "</body>"+
                        "</html>";
                contentLength = contentType + response.length() + "\r\n";

                outputStreamToClient.write(statusString);
                outputStreamToClient.write(myServer);
                outputStreamToClient.write(contentType);
                outputStreamToClient.write(contentLength);
                outputStreamToClient.write(closeConnection);
                outputStreamToClient.write("\r\n");
                outputStreamToClient.write(response);

                break;
            case 404:
                statusString = "HTTP/1.1 404 Not Found\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                            "<body>" +
                                "<h>ERROR 404 FILE NOT FOUND</h>" +
                            "</body>"+
                            "</html>";
                System.out.println(response);
                contentLength = contentType + response.length() + "\r\n";

                outputStreamToClient.write(statusString);
                outputStreamToClient.write(myServer);
                outputStreamToClient.write(contentType);
                outputStreamToClient.write(contentLength);
                outputStreamToClient.write(closeConnection);
                outputStreamToClient.write("\r\n");
                outputStreamToClient.write(response);
                outputStreamToClient.close();
                break;
            case 405:
                statusString = "HTTP/1.1 405 Method Not Allowed\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 405 METHOD NOT ALLOWED</h>" +
                        "</body>"+
                        "</html>";
                contentLength = contentType + response.length() + "\r\n";

                outputStreamToClient.write(statusString);
                outputStreamToClient.write(myServer);
                outputStreamToClient.write(contentType);
                outputStreamToClient.write(contentLength);
                outputStreamToClient.write(closeConnection);
                outputStreamToClient.write("\r\n");
                outputStreamToClient.write(response);
                break;
            default:
                statusString = "HTTP/1.1 404 Not Found\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 404 FILE NOT FOUND</h>" +
                        "</body>"+
                        "</html>";
                contentLength = contentType + response.length() + "\r\n";

                outputStreamToClient.write(statusString);
                outputStreamToClient.write(myServer);
                outputStreamToClient.write(contentType);
                outputStreamToClient.write(contentLength);
                outputStreamToClient.write(closeConnection);
                outputStreamToClient.write("\r\n");
                outputStreamToClient.write(response);
                break;
        }

    }
}
