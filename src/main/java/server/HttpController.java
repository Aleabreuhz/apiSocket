package server;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;


public class HttpController {
    public static void handleBooksRequest(String method, String path, String requestBody, OutputStream out) throws
    IOException {
        try {
            if ("GET".equals(method)) {
                if (path.equals("/books")) {
                    HttpServer.sendResponse(out, 200, HttpDao.getAllBooks());
                } else {
                    int id = extractIdFromPath(path);
                    if (id != -1) HttpServer.sendResponse(out, 200, HttpDao.getBookById(id));
                    else HttpServer.sendResponse(out, 404, "Not Found");
                }
            } else if ("POST".equals(method)) {
                JSONObject json = new JSONObject(requestBody);
                HttpServer.sendResponse(out, 201, HttpDao.createBook(json));
            } else if ("PUT".equals(method)) {
                int id = extractIdFromPath(path);
                if (id != -1) {
                    JSONObject json = new JSONObject(requestBody);
                    HttpServer.sendResponse(out, HttpDao.updateBook(id, json) ? 200 : 404, "Updated");
                } else {
                    HttpServer.sendResponse(out, 404, "Not Found");
                }
            } else if ("DELETE".equals(method)) {
                int id = extractIdFromPath(path);
                HttpServer.sendResponse(out, HttpDao.deleteBook(id) ? 200 : 404, "Deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServer.sendResponse(out, 500, "Internal Server Error");
        }
    }

    private static int extractIdFromPath(String path) {
        try {
            String[] parts = path.split("/");
            if (parts.length > 2) {
                return Integer.parseInt(parts[2]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error al extraer ID de la URL: " + e.getMessage());
        }
        return -1;
    }
}
