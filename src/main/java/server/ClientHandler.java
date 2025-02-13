package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {
            String requestLine = in.readLine();
            if (requestLine == null) return;
            System.out.println("Solicitud recibida: " + requestLine);
            int contentLength = 0;
            String headerLine;
            while (!(headerLine = in.readLine()).isEmpty()) {
                System.out.println("Header: " + headerLine);
                if (headerLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(headerLine.split(": ")[1].trim());
                }
            }
            String requestBody = "";
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                in.read(bodyChars, 0, contentLength);
                requestBody = new String(bodyChars);
            }
            System.out.println("Cuerpo de la solicitud: " + requestBody);

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];

            if (path.startsWith("/books")) {
                HttpController.handleBooksRequest(method, path, requestBody, out);
            } else {
                HttpServer.sendResponse(out, 404, "Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}