package server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

class HttpServer {
    public static JSONObject readRequestBody(BufferedReader in) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            body.append(line);
        }
        System.out.println(body.toString());
        return new JSONObject(body.toString());
    }
    public static void sendResponse(OutputStream out, int statusCode, String body) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " OK\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + body.length() + "\r\n"
                + "\r\n"
                + body;
        out.write(response.getBytes());
        out.flush();
    }
}