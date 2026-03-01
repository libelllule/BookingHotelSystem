# 🏨 Hotel Booking System

Консольное приложение для бронирования отелей на Java. Система поддерживает работу с реляционной базой данных (PostgreSQL) и обеспечивает отказоустойчивость за счет дублирующей сериализации состояния в файл.

## 🏗 Архитектура

Проект построен на четком разделении ответственности:

*   **BookingSystem**: Ядро системы. Координирует бизнес-логику, взаимодействие с БД и файловым хранилищем.
*   **Database Layer**: Интерфейс к PostgreSQL. Инкапсулирует SQL-запросы, управление транзакциями и подключениями.
*   **File Storage**: Механизм автоматической сериализации состояния в `hotel_state.dat` для быстрого восстановления данных.
*   **Models**: Сериализуемые POJO-классы (`Hotel`, `Booking`) с переопределенными методами `equals/hashCode`.

## ✨ Основные возможности

*   🔍 **Умный поиск**: Поиск по городам с проверкой фактического наличия свободных мест.
*   📅 **Валидация дат**: Контроль корректности (даты не в прошлом, выезд позже заезда).
*   💰 **Авторасчет**: Калькуляция полной стоимости проживания на основе количества ночей.
*   🛡 **Транзакционность**: Атомарное создание брони с автоматическим откатом (Rollback) при сбоях.
*   💾 **Гибридное хранение**: Одновременная запись в SQL и бинарный файл.
*   ⚡ **Кэширование**: Хранение последних результатов поиска для ускорения работы.

## 📂 Структура проекта

```text
src/
├── Main.java           # Точка входа, консольное меню
├── BookingSystem.java  # Сервис управления логикой
├── Database.java       # DAO-слой для PostgreSQL
├── Hotel.java          # Сущность "Отель"
├── Booking.java        # Сущность "Бронирование"
├── CheckSQL.java       # Утилита для диагностики БД
└── bd.sql              # SQL-скрипты инициализации


🛠 Требования и настройка
Системные требования
Java: JDK 8 или выше.
БД: PostgreSQL 12+.
Драйвер: JDBC PostgreSQL Driver.
Развертывание базы данных
Выполните скрипты из файла bd.sql или поочередно:
sql
CREATE DATABASE hotel_db;

CREATE TABLE hotels (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    rooms_available INTEGER NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    hotel_id INTEGER REFERENCES hotels(id),
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);


Конфигурация
В файле Database.java укажите ваши данные для подключения:
java
String url = "jdbc:postgresql://localhost:5432/hotel_db";
String user = "postgres";
String password = "your_password";


🚀 Запуск и использование
Компиляция и диагностика:
bash
javac *.java
java CheckSQL # Проверка связи с БД


Запуск приложения:
bash
java Main


