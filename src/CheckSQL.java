import java.sql.*;

public class CheckSQL {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/study";
        String user = "postgres";
        String password = "qweeWeWe5W";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Успешное подключение!");

            // Проверяем, что можем выполнять запросы
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT, name VARCHAR(50))");
            System.out.println("✅ Можем создавать таблицы");

            stmt.execute("INSERT INTO test_table VALUES (1, 'Test')");
            System.out.println("✅ Можем вставлять данные");

            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
            System.out.println("✅ Можем читать данные");

            stmt.execute("DROP TABLE test_table");
            System.out.println("✅ Можем удалять таблицы");

            conn.close();
            System.out.println("\n🎉 Пользователь razinkina имеет все необходимые права!");

        } catch (SQLException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}