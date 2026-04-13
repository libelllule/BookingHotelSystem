import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BookingSystem {
    private List<Hotel> hotels = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private Database db;
    private List<Hotel> lastSearchResults = new ArrayList<>(); //Кэш последнего поиска

    public BookingSystem() throws Exception { //пробрасываем исключения в мэин
        this.db = new Database();
        loadFromFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(  // Создаем поток для записи
                new FileOutputStream("hotel_state.dat"))) { //бинарный файл
            oos.writeObject(hotels);
            oos.writeObject(bookings);
            System.out.println("Состояние автоматически сохранено");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File("hotel_state.dat");  //Создает объект File, который представляет путь к файлу, а не сам файл
        if (!file.exists()) {
            System.out.println("Файл состояния не найден, создаем новый");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("hotel_state.dat"))) {
            hotels = (List<Hotel>) ois.readObject();
            bookings = (List<Booking>) ois.readObject();
            System.out.println("Загружено: " + hotels.size() +
                    " отелей, " + bookings.size() + " бронирований");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }

    public void SearchHotels(String city, LocalDate checkIn, LocalDate checkOut) {
        try {
            System.out.println("   Поиск отелей в городе: " + city.toUpperCase());
            System.out.println("Даты: " + checkIn + " - " + checkOut +
                    " (" + ChronoUnit.DAYS.between(checkIn, checkOut) + " дней)");

            lastSearchResults.clear();

            List<Hotel> availableHotels = db.getHotelsByCity(city, checkIn, checkOut);
            if (availableHotels.isEmpty()) {
                System.out.println("\nВ городе " + city + " нет доступных отелей");
                return;
            }
            lastSearchResults.addAll(availableHotels);

            for (Hotel hotel : availableHotels) {
                if (!hotels.contains(hotel)) {
                    hotels.add(hotel);
                }
            }

            System.out.println("\n Найдено доступных отелей: " + availableHotels.size());

            for (int i = 0; i < availableHotels.size(); i++) {
                Hotel hotel = availableHotels.get(i);
                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                double total = hotel.getPrice() * days;

                System.out.println("\n" + (i+1) + ". " + hotel.getName());
                System.out.println("   ID: " + hotel.getId());
                System.out.println("   Город: " + hotel.getCity());
                System.out.println("   Цена за ночь: " + hotel.getPrice() + " руб");
                System.out.println("   Свободных номеров: " + hotel.getAvailableRooms());                System.out.println("   Общая стоимость: " + total + " руб (" + days + " ночей)");
            }

        } catch (Exception e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
    }

    public List<Hotel> getLastSearchResults() { //возвращает копию списка lastSearchResults
        return new ArrayList<>(lastSearchResults);
    }

    public boolean bookHotel(int hotelId, String guest, LocalDate checkIn, LocalDate checkOut) {
        try {
            Hotel hotel = db.getHotelById(hotelId);
            if (hotel == null) {
                System.out.println("Отель с ID " + hotelId + " не найден");
                return false;
            }

            if (!db.isHotelAvailable(hotelId, checkIn, checkOut)) {
                System.out.println("В отеле нет свободных номеров на эти даты!");
                return false;
            }

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            double total = hotel.getPrice() * days;

            db.createBooking(guest, hotelId, checkIn, checkOut, total);
            Booking booking = new Booking(
                    bookings.size() + 1,
                    guest,
                    hotelId,
                    checkIn,
                    checkOut,
                    total
            );

            bookings.add(booking);

            boolean hotelAdded = false;
            if (!hotels.contains(hotel)) {
                hotels.add(hotel);
                hotelAdded = true;
            }

            // Обновляем отель в локальном списке
            int index = hotels.indexOf(hotel);
            if (index != -1) {
                hotels.set(index, hotel); // Заменяем на обновленный
            } else {
                hotels.add(hotel);
            }

            saveToFile();

            System.out.println("   БРОНИРОВАНИЕ УСПЕШНО СОЗДАНО!");
            System.out.println("   Гость: " + guest);
            System.out.println("   Отель: " + hotel.getName());
            System.out.println("   Город: " + hotel.getCity());
            System.out.println("   Даты: " + checkIn + " → " + checkOut);
            System.out.println("   Ночей: " + days);
            System.out.println("   Стоимость: " + total + " руб");
            System.out.println("   ID бронирования: " + booking.getId());


            return true;

        } catch (Exception e) {
            System.out.println("Ошибка бронирования: " + e.getMessage());
            return false;
        }
    }

    public void ShowAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("Нет бронирований");
            return;
        }

        System.out.println("   ВСЕ БРОНИРОВАНИЯ (" + bookings.size() + ")");

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            System.out.println("\n" + (i+1) + ". " + booking.getGuestName());
            System.out.println("   Отель ID: " + booking.getHotelId());
            System.out.println("   Даты: " + booking.getCheckIn() + " → " + booking.getCheckOut());
            System.out.println("   Стоимость: " + booking.getTotalPrice() + " руб");
        }

    }

    public void ShowAllHotels() {
        if (hotels.isEmpty()) {
            System.out.println("Нет отелей в памяти");
            return;
        }

        System.out.println("   ВСЕ ОТЕЛИ В ПАМЯТИ (" + hotels.size() + ")");
        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            System.out.println("\n" + (i+1) + ". " + hotel.getName());
            System.out.println("   Город: " + hotel.getCity());
            System.out.println("   Цена: " + hotel.getPrice() + " руб/ночь");
            System.out.println("   Всего номеров: " + hotel.getRoomsTotal());            System.out.println("   ID: " + hotel.getId());
        }
    }
}