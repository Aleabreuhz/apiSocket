package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryRepository {
    public static String fetchAllBooks() throws SQLException {
        PreparedStatement query = DatabaseManager.getConnection().prepareStatement("SELECT * FROM books");
        ResultSet resultSet = query.executeQuery();
        JSONArray booksList = new JSONArray();
        while (resultSet.next()) {
            JSONObject bookDetails = new JSONObject();
            bookDetails.put("id", resultSet.getInt("id"));
            bookDetails.put("title", resultSet.getString("title"));
            bookDetails.put("author", resultSet.getString("author"));
            bookDetails.put("year", resultSet.getInt("year"));
            booksList.put(bookDetails);
        }
        return booksList.toString();
    }

    public static String fetchBookById(int bookId) throws SQLException {
        PreparedStatement query = DatabaseManager.getConnection().prepareStatement("SELECT * FROM books WHERE id = ?");
        query.setInt(1, bookId);
        ResultSet resultSet = query.executeQuery();
        if (resultSet.next()) {
            JSONObject bookDetails = new JSONObject();
            bookDetails.put("id", resultSet.getInt("id"));
            bookDetails.put("title", resultSet.getString("title"));
            bookDetails.put("author", resultSet.getString("author"));
            bookDetails.put("year", resultSet.getInt("year"));
            return bookDetails.toString();
        }
        return "{}";
    }

    public static String addBook(JSONObject bookData) throws SQLException {
        PreparedStatement query = DatabaseManager.getConnection().prepareStatement("INSERT INTO books (title, author, year) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        query.setString(1, bookData.getString("title"));
        query.setString(2, bookData.getString("author"));
        query.setInt(3, bookData.getInt("year"));
        query.executeUpdate();
        return bookData.toString();
    }

    public static boolean updateBook(int bookId, JSONObject bookData) throws SQLException {
        PreparedStatement query = DatabaseManager.getConnection().prepareStatement("UPDATE books SET title=?, author=?, year=? WHERE id=?");
        query.setString(1, bookData.getString("title"));
        query.setString(2, bookData.getString("author"));
        query.setInt(3, bookData.getInt("year"));
        query.setInt(4, bookId);
        return query.executeUpdate() > 0;
    }

    public static boolean removeBook(int bookId) throws SQLException {
        PreparedStatement query = DatabaseManager.getConnection().prepareStatement("DELETE FROM books WHERE id=?");
        query.setInt(1, bookId);
        return query.executeUpdate() > 0;
    }
}