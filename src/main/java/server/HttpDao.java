package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HttpDao {  public static String getAllBooks() throws SQLException {
    PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement("SELECT * FROM books");
    ResultSet rs = stmt.executeQuery();
    JSONArray books = new JSONArray();
    while (rs.next()) {
        JSONObject book = new JSONObject();
        book.put("id", rs.getInt("id"));
        book.put("title", rs.getString("title"));
        book.put("author", rs.getString("author"));
        book.put("year", rs.getInt("year"));
        books.put(book);
    }
    return books.toString();
}

    public static String getBookById(int id) throws SQLException {
        PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement("SELECT * FROM books WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            JSONObject book = new JSONObject();
            book.put("id", rs.getInt("id"));
            book.put("title", rs.getString("title"));
            book.put("author", rs.getString("author"));
            book.put("year", rs.getInt("year"));
            return book.toString();
        }
        return "{}";
    }

    public static String createBook(JSONObject json) throws SQLException {
        PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement("INSERT INTO books (title, author, year) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, json.getString("title"));
        stmt.setString(2, json.getString("author"));
        stmt.setInt(3, json.getInt("year"));
        stmt.executeUpdate();
        return json.toString();
    }

    public static boolean updateBook(int id, JSONObject json) throws SQLException {
        PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement("UPDATE books SET title=?, author=?, year=? WHERE id=?");
        stmt.setString(1, json.getString("title"));
        stmt.setString(2, json.getString("author"));
        stmt.setInt(3, json.getInt("year"));
        stmt.setInt(4, id);
        return stmt.executeUpdate() > 0;
    }

    public static boolean deleteBook(int id) throws SQLException {
        PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement("DELETE FROM books WHERE id=?");
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    }
}