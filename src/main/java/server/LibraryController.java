package server;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class LibraryController {
    public static void processBookRequest(String httpMethod, String httpPath, String requestBody, OutputStream outputStream) throws IOException {
        try {
            if ("GET".equals(httpMethod)) {
                if (httpPath.equals("/books")) {
                    LibraryServer.sendHttpResponse(outputStream, 200, LibraryRepository.fetchAllBooks());
                } else {
                    int bookId = extractBookIdFromPath(httpPath);
                    if (bookId != -1) LibraryServer.sendHttpResponse(outputStream, 200, LibraryRepository.fetchBookById(bookId));
                    else LibraryServer.sendHttpResponse(outputStream, 404, "{\"error\": \"Book Not Found\"}");
                }
            } else if ("POST".equals(httpMethod)) {
                JSONObject bookData = new JSONObject(requestBody);
                LibraryServer.sendHttpResponse(outputStream, 201, LibraryRepository.addBook(bookData));
            } else if ("PUT".equals(httpMethod)) {
                int bookId = extractBookIdFromPath(httpPath);
                if (bookId != -1) {
                    JSONObject bookData = new JSONObject(requestBody);
                    LibraryServer.sendHttpResponse(outputStream, LibraryRepository.updateBook(bookId, bookData) ? 200 : 404, "{\"message\": \"Book Updated\"}");
                } else {
                    LibraryServer.sendHttpResponse(outputStream, 404, "{\"error\": \"Book Not Found\"}");
                }
            } else if ("DELETE".equals(httpMethod)) {
                int bookId = extractBookIdFromPath(httpPath);
                LibraryServer.sendHttpResponse(outputStream, LibraryRepository.removeBook(bookId) ? 200 : 404, "{\"message\": \"Book Deleted\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LibraryServer.sendHttpResponse(outputStream, 500, "{\"error\": \"Internal Server Error\"}");
        }
    }

    private static int extractBookIdFromPath(String httpPath) {
        try {
            String[] pathSegments = httpPath.split("/");
            if (pathSegments.length > 2) {
                return Integer.parseInt(pathSegments[2]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error extracting book ID from path: " + e.getMessage());
        }
        return -1;
    }
}