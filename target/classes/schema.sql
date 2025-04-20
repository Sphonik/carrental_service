DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS cars CASCADE;

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       user_role VARCHAR(10) NOT NULL CHECK (user_role IN ('ADMIN', 'USER', 'TESTER'))
);

CREATE TABLE IF NOT EXISTS cars (
                      id SERIAL PRIMARY KEY,
                      make VARCHAR(255) NOT NULL,
                      model VARCHAR(255) NOT NULL,
                      year INTEGER NOT NULL,
                      color VARCHAR(255) NOT NULL,
                      fuel_type VARCHAR(10) NOT NULL CHECK (fuel_type IN ('GAS', 'ELECTRIC', 'HYBRID')),
                      automatic BOOLEAN NOT NULL,
                      price_per_day DECIMAL(10,2) NOT NULL,
                      pickup_location VARCHAR(255) NOT NULL,
                      available BOOLEAN NOT NULL
);

CREATE TABLE bookings (
                          id SERIAL PRIMARY KEY,
                          booked_by INTEGER NOT NULL,
                          car_rented INTEGER NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE NOT NULL,
                          total_cost NUMERIC(10, 2) NOT NULL,
                          currency VARCHAR(10) NOT NULL,

    -- Optional: Foreign Keys, wenn du Beziehungen definierst
                          CONSTRAINT fk_booked_by FOREIGN KEY (booked_by) REFERENCES users(id),
                          CONSTRAINT fk_car_rented FOREIGN KEY (car_rented) REFERENCES cars(id)
);
