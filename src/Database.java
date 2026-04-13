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

    public List<Hotel> getHotelsByCity(String city, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.id, h.name, h.city, h.rooms_total, h.price_per_night, " +
                "(h.rooms_total - COUNT(b.id)) as available_rooms " +
                "FROM hotels h " +
                "LEFT JOIN bookings b ON h.id = b.hotel_id " +
                "AND b.check_in < ? AND b.check_out > ? " +
                "WHERE h.city = ? " +
                "GROUP BY h.id, h.name, h.city, h.rooms_total, h.price_per_night " +
                "HAVING (h.rooms_total - COUNT(b.id)) > 0 " +
                "ORDER BY h.price_per_night";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(checkOut));
            stmt.setDate(2, Date.valueOf(checkIn));
            stmt.setString(3, city);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("rooms_total"),
                        rs.getDouble("price_per_night")
                );
                hotel.setAvailableRooms(rs.getInt("available_rooms"));
                hotels.add(hotel);
            }
        }
        return hotels;
    }

    public boolean isHotelAvailable(int hotelId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT h.rooms_total - COUNT(b.id) as rooms_left " +
                "FROM hotels h " +
                "LEFT JOIN bookings b ON h.id = b.hotel_id " +
                "AND b.check_in < ? AND b.check_out > ? " +
                "WHERE h.id = ? " +
                "GROUP BY h.id, h.rooms_total";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(checkOut));
            stmt.setDate(2, Date.valueOf(checkIn));
            stmt.setInt(3, hotelId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("rooms_left") > 0;
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
            connection.commit();
            System.out.println("Бронирование успешно сохранено в БД");
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
                        rs.getInt("rooms_total"),
                        rs.getDouble("price_per_night")
                );
            }
        }
        return null;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println(" Отключено от PostgreSQL");
        }
    }
}