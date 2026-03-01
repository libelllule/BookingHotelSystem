import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public Database() throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/hotel_db";
        String user = "postgres";
        String password = "qweeWeWe5W";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println(" Подключено к PostgreSQL: hotel_db");
    }


    public List<Hotel> getHotelsByCity(String city) throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE city = ? AND rooms_available > 0 ORDER BY price_per_night";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("rooms_available"),
                        rs.getDouble("price_per_night")
                );
                hotels.add(hotel);
            }
        }
        return hotels;
    }


    public boolean isHotelAvailable(int hotelId) throws SQLException {
        String sql = "SELECT rooms_available FROM hotels WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("rooms_available") > 0;
            }
        }
        return false;
    }


    public void createBooking(String guest, int hotelId, LocalDate checkIn,
                              LocalDate checkOut, double total) throws SQLException {

        connection.setAutoCommit(false);

        try {
            String sql = "INSERT INTO bookings (guest_name, hotel_id, check_in, check_out, total_price) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, guest);
                stmt.setInt(2, hotelId);
                stmt.setDate(3, Date.valueOf(checkIn));
                stmt.setDate(4, Date.valueOf(checkOut));
                stmt.setDouble(5, total);
                stmt.executeUpdate();
            }

            updateRoomCount(hotelId, -1);

            connection.commit();
            System.out.println("Бронирование создано");

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }


    public Hotel getHotelById(int hotelId) throws SQLException {
        String sql = "SELECT * FROM hotels WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Hotel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("rooms_available"),
                        rs.getDouble("price_per_night")
                );
            }
        }
        return null;
    }

    private void updateRoomCount(int hotelId, int delta) throws SQLException {
        String sql = "UPDATE hotels SET rooms_available = rooms_available + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, hotelId);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println(" Отключено от PostgreSQL");
        }
    }
}