package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * ClientResponse class is used to handle the client request. Status codes and responses will be generated accordingly
 */
public class ClientResponse {

    /*
     * processInput(String method, String query, OutputStream outputStreamToClient, String contentExpexted)
     * inputs: String method - specifies HTTP method used, String query - specifies file/path requested
     * OutputStream outputStreamToClient - the output stream to the client socket,
     * String contentExpexted - what kind of response the client is expecting
     *
     * outputs: none
     *
     * purpose: handle input and start sending appropriate response based on the status code
     */
    public void processInput(String method, String query, OutputStream outputStreamToClient, String contentExpected) {
        if (!method.equals("GET")) { // case if the method isn't GET
            sendResponse(405, outputStreamToClient, null, "");
            System.out.println("Invalid/unsupported HTTP method");
        } else if (!(query.charAt(0) == '/')) { // Case if not a valid path
            sendResponse(404, outputStreamToClient, null, "");
            System.out.println("Not a valid path requested");
        }

        String fileName = query.replaceFirst("/", ""); // replacing the first character of the query string to get fname
        File file = new File(fileName);
        if (file.isFile()) { // if the file is found in specified path
            System.out.println("FILE FOUND");
            sendResponse(200, outputStreamToClient, file, contentExpected);
        } else { // else not found
            System.out.println("FILE NOT FOUND");
            sendResponse(404, outputStreamToClient, null, "");
        }
    }

    /*
     * prosendResponse(int status, OutputStream outputStreamToClient, File file, String contentExpected)
     * inputs: int status - specifies the status of the request,
     * OutputStream outputStreamToClient - the output stream to the client socket,
     * File file - the file to be sent to the client,
     * String contentExpected - the content0type to be sent
     *
     * outputs: none
     *
     * purpose: send the response to the client with the appropriate response header and body
     * at the end of transmission the output stream is closed
     */
    public void sendResponse(int status, OutputStream outputStreamToClient, File file, String contentExpected) {
        String statusString = "";
        String response = "";
        String contentType = "";
        String contentLength = "Content-Length: ";
        String closeConnection = "Connection: close\r\n";
        String myServer = "Server: Gabino HTTPServer";


        switch (status) { // switch depending on the status code of the request
            case 200:
                statusString = "HTTP/1.1 200 OK\r\n";
                if (contentExpected.contains("text/html")) {
                    contentType = "Content-Type: text/html\r\n";

                    try {
                        FileInputStream fileIn = new FileInputStream(file);
                        int content;
                        while ((content = fileIn.read()) != -1) {
                            response += (char) content;
                        }
                        contentLength = contentType + response.length() + "\r\n";

                        outputStreamToClient.write(statusString.getBytes());
                        outputStreamToClient.write(myServer.getBytes());
                        outputStreamToClient.write(contentType.getBytes());
                        outputStreamToClient.write(contentLength.getBytes());
                        outputStreamToClient.write(closeConnection.getBytes());
                        outputStreamToClient.write("\r\n".getBytes());
                        outputStreamToClient.write(response.getBytes());
                        outputStreamToClient.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (contentExpected.contains("image")) {
                    contentType = "Content-Type: image/webp\r\n";

                    try {
                        BufferedImage image = ImageIO.read(file);
                        ByteArrayOutputStream imageByteArrOut = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", imageByteArrOut);
                        imageByteArrOut.flush();
                        byte[] imageArr = imageByteArrOut.toByteArray();

                        contentLength = contentType + imageArr.length + "\r\n";

                        outputStreamToClient.write(statusString.getBytes());
                        outputStreamToClient.write(myServer.getBytes());
                        outputStreamToClient.write(contentType.getBytes());
                        outputStreamToClient.write(contentLength.getBytes());
                        outputStreamToClient.write(closeConnection.getBytes());
                        outputStreamToClient.write("\r\n".getBytes());
                        outputStreamToClient.write(imageArr);
                        outputStreamToClient.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendResponse(404, outputStreamToClient, null, "");
                    break;
                }

                break;
            case 301:
                statusString = "HTTP/1.1 301 Moved Permanently\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 303 FILE PERMANENTLY</h>" +
                        "</body>" +
                        "</html>";
                try {
                    contentLength = contentType + response.length() + "\r\n";

                    outputStreamToClient.write(statusString.getBytes());
                    outputStreamToClient.write(myServer.getBytes());
                    outputStreamToClient.write(contentType.getBytes());
                    outputStreamToClient.write(contentLength.getBytes());
                    outputStreamToClient.write(closeConnection.getBytes());
                    outputStreamToClient.write("\r\n".getBytes());
                    outputStreamToClient.write(response.getBytes());
                    outputStreamToClient.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case 404:
                statusString = "HTTP/1.1 404 Not Found\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 404 FILE NOT FOUND</h>" +
                        "</body>" +
                        "</html>";

                try {
                    contentLength = contentType + response.length() + "\r\n";

                    outputStreamToClient.write(statusString.getBytes());
                    outputStreamToClient.write(myServer.getBytes());
                    outputStreamToClient.write(contentType.getBytes());
                    outputStreamToClient.write(contentLength.getBytes());
                    outputStreamToClient.write(closeConnection.getBytes());
                    outputStreamToClient.write("\r\n".getBytes());
                    outputStreamToClient.write(response.getBytes());
                    outputStreamToClient.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 405:
                statusString = "HTTP/1.1 405 Method Not Allowed\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 405 METHOD NOT ALLOWED</h>" +
                        "</body>" +
                        "</html>";

                try {
                    contentLength = contentType + response.length() + "\r\n";

                    outputStreamToClient.write(statusString.getBytes());
                    outputStreamToClient.write(myServer.getBytes());
                    outputStreamToClient.write(contentType.getBytes());
                    outputStreamToClient.write(contentLength.getBytes());
                    outputStreamToClient.write(closeConnection.getBytes());
                    outputStreamToClient.write("\r\n".getBytes());
                    outputStreamToClient.write(response.getBytes());
                    outputStreamToClient.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                statusString = "HTTP/1.1 404 Not Found\r\n";
                contentType = "Content-Type: \r\n";
                response = "<html>" +
                        "<body>" +
                        "<h>ERROR 404 FILE NOT FOUND</h>" +
                        "</body>" +
                        "</html>";

                try {
                    contentLength = contentType + response.length() + "\r\n";

                    outputStreamToClient.write(statusString.getBytes());
                    outputStreamToClient.write(myServer.getBytes());
                    outputStreamToClient.write(contentType.getBytes());
                    outputStreamToClient.write(contentLength.getBytes());
                    outputStreamToClient.write(closeConnection.getBytes());
                    outputStreamToClient.write("\r\n".getBytes());
                    outputStreamToClient.write(response.getBytes());
                    outputStreamToClient.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
