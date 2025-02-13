package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class LibraryClientHandler implements Runnable {
    private Socket clientSocket;

    public LibraryClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {
            String requestLine = inputReader.readLine();
            if (requestLine == null) return;
            System.out.println("Request Received: " + requestLine);

            int contentLength = 0;
            String headerLine;
            while (!(headerLine = inputReader.readLine()).isEmpty()) {
                System.out.println("Header: " + headerLine);
                if (headerLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(headerLine.split(": ")[1].trim());
                }
            }

            String requestBody = "";
            if (contentLength > 0) {
                char[] bodyBuffer = new char[contentLength];
                inputReader.read(bodyBuffer, 0, contentLength);
                requestBody = new String(bodyBuffer);
            }
            System.out.println("Request Body: " + requestBody);

            String[] requestParts = requestLine.split(" ");
            String httpMethod = requestParts[0];
            String httpPath = requestParts[1];

            if (httpPath.startsWith("/books")) {
                LibraryController.processBookRequest(httpMethod, httpPath, requestBody, outputStream);
            } else {
                LibraryServer.sendHttpResponse(outputStream, 404, "{\"error\": \"Resource Not Found\"}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}