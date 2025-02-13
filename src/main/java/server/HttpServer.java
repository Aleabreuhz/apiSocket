package server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

class LibraryServer {
    public static JSONObject parseRequestBody(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBody.append(line);
        }
        System.out.println("Request Body: " + requestBody.toString());
        return new JSONObject(requestBody.toString());
    }

    public static void sendHttpResponse(OutputStream outputStream, int statusCode, String responseBody) throws IOException {
        String httpResponse = "HTTP/1.1 " + statusCode + " OK\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + responseBody.length() + "\r\n"
                + "\r\n"
                + responseBody;
        outputStream.write(httpResponse.getBytes());
        outputStream.flush();
    }
}