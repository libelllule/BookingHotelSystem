DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS hotels;

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

INSERT INTO hotels (name, city, rooms_total, price_per_night)
VALUES ('Ocean View', 'Sochi', 5, 5000.00),
       ('Mountain Lodge', 'Rosa Khutor', 10, 8000.00);