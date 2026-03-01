import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("СИСТЕМА БРОНИРОВАНИЯ ОТЕЛЕЙ");

        BookingSystem system = null;

        try {
            system = new BookingSystem();
            Scanner scanner = new Scanner(System.in); //поток ввода с клавиатуры
            boolean running = true;

            while (running) {
                printMenu();
                System.out.print("Ваш выбор: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        SearchAndBook(system, scanner);
                        break;
                    case "2":
                        system.ShowAllBookings();
                        break;
                    case "3":
                        system.ShowAllHotels();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }

            scanner.close();
            System.out.println("\nСистема завершает работу...");
            System.out.println("Все данные сохранены");

        } catch (Exception e) {
            System.out.println("Сообщение: " + e.getMessage());
            if (e.getMessage().contains("PostgreSQL") ||
                    e.getMessage().contains("connection") ||
                    e.getMessage().contains("database") ||
                    e instanceof SQLException) {

                System.out.println("\nВозможные причины:");
                System.out.println("1. PostgreSQL не запущен");
                System.out.println("2. Неправильные логин/пароль в Database.java");
                System.out.println("3. База данных 'hotel_db' не создана");
            }
        }
    }

    private static void printMenu() {
        System.out.println("            ГЛАВНОЕ МЕНЮ");
        System.out.println("1. Найти и забронировать отель");
        System.out.println("2. Показать все бронирования");
        System.out.println("3. Показать все отели");
        System.out.println("0. Выход");
    }

    private static void SearchAndBook(BookingSystem system, Scanner scanner) {
        try {
            System.out.println("   ПОИСК И БРОНИРОВАНИЕ");
            String city;
            while (true) {
                System.out.print("Введите город: ");
                city = scanner.nextLine().trim();

                if (city.isEmpty()) {
                    System.out.println("Город не может быть пустым");
                    continue;
                }

                boolean isValid = true;
                for (char c : city.toCharArray()) {
                    if (!Character.isLetter(c) && c != ' ' && c != '-') {
                        isValid = false;
                        break;
                    }
                }

                if (!isValid) {
                    System.out.println("Город должен содержать только буквы, пробелы и дефисы");
                    continue;
                }

                break; // Все проверки пройдены
            }

            System.out.print("Дата заезда (гггг-мм-дд): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine().trim()); //LocalDate.parse преобразует строку в объект LocalDate

            System.out.print("Дата выезда (гггг-мм-дд): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine().trim());

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                System.out.println("Дата выезда должна быть позже даты заезда");
                return;
            }

            if (checkIn.isBefore(LocalDate.now())) {
                System.out.println("Дата заезда не может быть в прошлом");
                return;
            }

            system.SearchHotels(city, checkIn, checkOut);

            List<Hotel> foundHotels = system.getLastSearchResults();

            if (foundHotels == null || foundHotels.isEmpty()) {
                System.out.println("\nБронирование невозможно: нет доступных отелей");
                System.out.println("Возврат в главное меню...");
                return;
            }
            System.out.print("\n Введите ID отеля для бронирования (или 0 для отмены): ");
            String input = scanner.nextLine().trim();

            if (!input.equals("0")) {
                try {
                    int hotelId = Integer.parseInt(input);

                    System.out.print("Введите ваше имя: ");
                    String guestName = scanner.nextLine().trim();

                    if (guestName.isEmpty()) {
                        System.out.println("Имя не может быть пустым");
                        return;
                    }

                    System.out.print("Подтвердить бронирование? (да/нет): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();

                    if (confirm.equals("да") || confirm.equals("yes") || confirm.equals("Да")) {
                        system.bookHotel(hotelId, guestName, checkIn, checkOut);
                    } else {
                        System.out.println("Бронирование отменено");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат ID. Введите число.");
                }
            } else {
                System.out.println("Поиск завершен");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты. Используйте гггг-мм-дд");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

//RuntimeException → ошибки программиста (можно избежать проверками)
//Проверяемые Exception → ошибки окружения (файлы, сеть, БД)