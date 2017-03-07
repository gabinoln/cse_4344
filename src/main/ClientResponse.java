package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Gabino Luna on 3/6/2017.
 */
public class ClientResponse {


    public void processInput(String method, String query, OutputStream outputStreamToClient, String contentExpexted) {
        if (!method.equals("GET")) {
            sendResponse(405, outputStreamToClient, null, "");
            System.out.println("Invalid/unsupported HTTP method");
        } else if (!(query.charAt(0) == '/')) {
            sendResponse(404, outputStreamToClient, null, "");
            System.out.println("Not a valid path requested");
        }

        String fileName = query.replaceFirst("/", "");
        File file = new File(fileName);
        if (file.isFile()) {
            System.out.println("FILE FOUND");
            sendResponse(200, outputStreamToClient, file, contentExpexted);
        } else {
            System.out.println("FILE NOT FOUND");
            sendResponse(404, outputStreamToClient, null, "");
        }
    }

    public void sendResponse(int status, OutputStream outputStreamToClient, File file, String contentExpected) {
        String statusString = "";
        String response = "";
        String contentType = "";
        String contentLength = "Content-Length: ";
        String closeConnection = "Connection: close\r\n";
        String myServer = "Server: Gabino HTTPServer";


        switch (status) {
            case 200:
                statusString = "HTTP/1.1 200 OK\r\n";
                if (contentExpected.contains("text/html")) {
                    contentType = "Content-Type: text/html\r\n";
                    System.out.println("SENDING TEXT");
                    try {
                        System.out.println(file.getName());
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
                    System.out.println("SENDING IMAGE");

                    try {
                        System.out.println(file.getName());
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
                System.out.println(response);

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
