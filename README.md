# Hotel Booking System (Java + JDBC + PostgreSQL)

Профессиональная система бронирования отелей, реализованная на Java. Основная особенность проекта — динамический расчет доступности номеров на стороне базы данных, что исключает ошибки при одновременном бронировании на разные даты.

## Ключевые особенности

* **Advanced Availability Logic**: В отличие от простых систем с декрементом счетчика, здесь используется SQL-логика пересечения временных интервалов. Система проверяет занятость конкретных номеров именно в выбранный период.
* **Database-Driven**: Полная интеграция с PostgreSQL через JDBC.
* **ACID Compliance**: Использование транзакций для обеспечения целостности данных при оформлении бронирования.
* **Validation**: Строгая валидация входных данных: проверка корректности городов, форматов дат и логики (дата выезда не может быть раньше даты заезда).
* **State Management**: Комбинированный подход — хранение бизнес-сущностей в БД и поддержка состояния сессии в бинарных файлах (`.dat`).

## Технический стек

* **Language:** Java (JDK 15+)
* **Database:** PostgreSQL
* **Library:** JDBC (PostgreSQL Driver)
* **Architecture:** Layered Architecture (UI -> Service -> Database)

## Логика расчета пересечения дат

Сердцем системы является SQL-запрос, который находит конфликтующие бронирования. Математически это выражено условием:
`WHERE (check_in < :new_check_out) AND (check_out > :new_check_in)`

Если количество пересекающихся броней меньше общего количества комнат в отеле (`rooms_total`), отель считается доступным.

## Установка и запуск

### 1. Подготовка базы данных
Создайте базу данных `hotel_db` и выполните скрипт из файла `bd.sql`:
~~~bash
```sql
CREATE TABLE hotels (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    rooms_total INTEGER NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    hotel_id INTEGER REFERENCES hotels(id),
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL
);
~~~
2. Настройка подключения
В файле Database.java укажите свои данные для подключения:
~~~bash
String url = "jdbc:postgresql://localhost:5432/hotel_db";
String user = "postgres";
String password = "your_password";
~~~
3. Сборка и запуск
Скомпилируйте проект в IntelliJ IDEA.

Запустите класс Main.java.

Следуйте инструкциям в консольном меню.

 Структура проекта
Main.java — Точка входа, консольный интерфейс.

BookingSystem.java — Сервисный слой, обработка бизнес-логики.

Database.java — Слой доступа к данным (DAO), работа с SQL.

Hotel.java / Booking.java — Модели данных.

bd.sql — Схема базы данных и начальные данные.
