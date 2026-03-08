# Quick Share 

**Quick Share** — это гибридное решение для быстрого обмена файлами между устройствами в одной локальной сети (Wi-Fi) без доступа к интернету.

## Описание проекта
Проект решает проблему передачи данных в условиях блокировок мессенджеров и отсутствия стабильной сети (например, в университетах). Мы исключаем необходимость использования флешек или облачных хранилищ, обеспечивая прямую передачу данных внутри LAN.

##  Ключевые функции
*   **Временные «Комнаты»**: Создание сессии с уникальным ID для мгновенного подключения участников.
*   **Загрузка**: Возможность для любого участника загрузить файлы в общую «комнату» для мгновенного доступа к ним других пользователей.
*   **Скачивание**: Участники могут просматривать список доступных файлов в сессии и скачивать нужные им объекты напрямую на свои устройства.
*   **Прямая передача по LAN**: Максимальная скорость обмена внутри Wi-Fi сети без расхода интернет-трафика.
*   **Мультизагрузка**: Одновременная загрузка и скачивание нескольких файлов.
*   **Безопасность и конфиденциальность**: Шифрование трафика (HTTPS) и полное удаление файлов с сервера после завершения сессии.
*   **Кроссплатформенность**: Доступ через веб-интерфейс для любых ОС и нативное Android-приложение.

##  Технический стек
*   **Backend**: `Rust` + `Docker` (высокая производительность и легкое развертывание).
*   **Frontend (Web)**: `HTML` / `JavaScript` (доступ через любой современный браузер).
*   **Mobile**: `Kotlin` (нативное приложение для Android 8.0+).
*   **Протоколы**: `HTTPS` с самоподписанными сертификатами для защиты локального трафика.

##  Команда проекта (Team Quick Share)


| Участник | Роль | Зона ответственности |
| :--- | :--- | :--- |
| **Бокарев Роман** | **Team Lead** | Backend Developer, архитектура сервера, координация, разработка нативного приложения |
| **Каневских Павел** | **Backend Developer** | Разработка серверной логики на Rust |
| **Косулин Никита** | **QA Engineer** | Тестировщик, параллельное и финальное тестирование |
| **Федосов Владислав** | **Designer & QA** | UI/UX дизайн, тестирование функций и финальная проверка |
| **Разинкина Анна** | **PM & System Analyst** | Управление проектом, проектирование системы (BRD/SRS) |


# Hotel Booking System (PostgreSQL + File Serialization)

Console-based hotel reservation system with a dual-layer storage strategy. Features a PostgreSQL backend for primary data and automatic state serialization for session persistence.

## Architecture

1.  **Booking System Core (BookingSystem.java)**:
    *   Central logic layer coordinating DB and File I/O.
    *   Handles hotel search, availability checks, and reservation flow.
    *   Triggers automatic serialization to `hotel_state.dat` on every change.

2.  **Database Layer (Database.java)**:
    *   PostgreSQL interface using JDBC.
    *   Manages transactional bookings (INSERT + UPDATE) with rollback support.
    *   Handles city-based filtering and room inventory.

3.  **Persistence Layer (File Storage)**:
    *   Binary serialization of `Hotel` and `Booking` POJO classes.
    *   Enables state recovery if the database connection is interrupted.

## Features

*   **Smart Search**: Filter hotels by city with real-time room availability validation.
*   **Date Validation**: Strict checking for past dates and checkout-after-checkin logic.
*   **Transactional Safety**: Atomic booking operations with automatic SQL rollback on failure.
*   **Hybrid Storage**: Concurrent writing to SQL and local binary state file.
*   **Input Sanitization**: RegEx validation for city names and date format parsing.
*   **Caching**: In-memory storage of recent search results for performance.

## Project Structure

*   `Main.java` - Console UI and command-line menu.
*   `BookingSystem.java` - Core business logic and coordination.
*   `Database.java` - PostgreSQL connection and SQL execution.
*   `Hotel.java` - Serializable model for hotel entities.
*   `Booking.java` - Serializable model for booking records.
*   `CheckSQL.java` - Database connectivity diagnostic tool.
*   `bd.sql` - Database schema and initial seed data.

## System Requirements

### Platform
*   **Java**: JDK 8 or higher.
*   **Database**: PostgreSQL 12+.

### Dependencies
*   PostgreSQL JDBC Driver (`postgresql-42.x.x.jar`).

## Installation

1.  **Database Setup**:
    Run the following SQL commands in your PostgreSQL console:
    ```sql
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

    -- Seed Data
    INSERT INTO hotels (name, city, rooms_available, price_per_night) VALUES
        ('Grand Hotel', 'Moscow', 5, 5000),
        ('City Inn', 'Moscow', 3, 3500),
        ('Sea Breeze', 'Sochi', 8, 4500);
    ```

2.  **Configuration**:
    Update credentials in `Database.java`:
    ```java
    String url = "jdbc:postgresql://localhost:5432/hotel_db";
    String user = "postgres";
    String password = "your_password";
    ```

3.  **Build**:
    ```bash
    javac *.java
    ```

## Usage

1.  **Verify Connection**:
    ```bash
    java CheckSQL
    ```

2.  **Run Application**:
    ```bash
    java Main
    ```
