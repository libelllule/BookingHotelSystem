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
    ```sql
    CREATE DATABASE hotel_db;
    -- Run schema from bd.sql to create 'hotels' and 'bookings' tables
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

3.  **Workflow**:
    *   Search by city (e.g., "Moscow") and dates (YYYY-MM-DD).
    *   Select Hotel ID from results and enter guest name.
    *   View active bookings or cached hotel data via the main menu.

## Technical Notes

*   **Protocol Choice**: HTTPS was selected over SFTP for the broader project ecosystem to ensure native Browser/Android compatibility and firewall traversal (Port 443).
*   **Serialization**: If `hotel_state.dat` exists, the system attempts to restore state on startup.
*   **Error Handling**: Comprehensive `SQLException` and `DateTimeParseException` catching.


