-- Insert dummy users
INSERT INTO users (first_name, last_name, username, password, user_role)
VALUES
    ('John', 'Doe', 'johndoe', 'password123', 'USER'),
    ('Jane', 'Smith', 'admin', 'master', 'ADMIN'),
    ('Alice', 'Johnson', 'alicej', 'testpass', 'TESTER');

-- Insert additional 20 dummy cars
INSERT INTO cars (make, model, year, color, fuel_type, automatic, price_per_day, pickup_location, available)
VALUES
    ('Ford', 'Focus', 2022, 'Blue', 'GAS', true, 50.00, 'Rome Airport', true),
    ('Chevrolet', 'Malibu', 2021, 'Black', 'GAS', true, 55.00, 'Paris Central Station', true),
    ('Volkswagen', 'Golf', 2023, 'White', 'GAS', true, 48.00, 'London Downtown', true),
    ('Mercedes', 'C-Class', 2020, 'Silver', 'GAS', true, 75.00, 'Berlin Central Station', true),
    ('BMW', '3 Series', 2023, 'Blue', 'GAS', true, 80.00, 'Munich Airport', true),
    ('Audi', 'A4', 2022, 'Grey', 'HYBRID', true, 70.00, 'Frankfurt Airport', true),
    ('Hyundai', 'Elantra', 2021, 'Red', 'GAS', false, 42.00, 'Seoul Airport', true),
    ('Kia', 'Optima', 2022, 'White', 'HYBRID', true, 60.00, 'Madrid Airport', true),
    ('Nissan', 'Altima', 2023, 'Black', 'GAS', true, 65.00, 'Tokyo Central', true),
    ('Subaru', 'Outback', 2022, 'Green', 'GAS', false, 58.00, 'Vancouver Downtown', true),
    ('Mazda', 'CX-5', 2023, 'Red', 'GAS', true, 68.00, 'Sydney Airport', true),
    ('Volvo', 'XC90', 2021, 'Silver', 'HYBRID', true, 90.00, 'Stockholm Central', true),
    ('Lexus', 'RX', 2020, 'White', 'GAS', true, 85.00, 'Dubai Downtown', true),
    ('Fiat', '500', 2022, 'Yellow', 'GAS', false, 35.00, 'Milan Central Station', true),
    ('Mini', 'Cooper', 2023, 'Black', 'GAS', true, 77.00, 'London Heathrow', true),
    ('Suzuki', 'Swift', 2022, 'Blue', 'GAS', true, 40.00, 'Barcelona Airport', true),
    ('Opel', 'Astra', 2021, 'Grey', 'GAS', true, 45.00, 'Vienna Central', true),
    ('Peugeot', '308', 2023, 'White', 'GAS', false, 50.00, 'Paris Airport', true),
    ('Renault', 'Clio', 2022, 'Red', 'GAS', true, 42.50, 'Lyon Central', true),
    ('Skoda', 'Octavia', 2023, 'Blue', 'GAS', true, 55.00, 'Prague Airport', true),
    ('Ferrari', '488 Spider', 2023, 'Red', 'GAS', true, 350.00, 'Milan Exclusive Terminal', true),
    ('Lamborghini', 'Huracán Evo', 2022, 'Yellow', 'GAS', true, 400.00, 'Dubai International', true),
    ('Porsche', '911 Turbo S', 2023, 'Silver', 'GAS', true, 320.00, 'Stuttgart Premium', true),
    ('McLaren', '720S', 2021, 'Orange', 'GAS', true, 380.00, 'London VIP Terminal', true),
    ('Aston Martin', 'DB11', 2022, 'British Racing Green', 'GAS', true, 310.00, 'Paris Luxury Terminal', true),
    ('Bugatti', 'Chiron', 2023, 'Blue', 'GAS', false, 1000.00, 'Monaco VIP Terminal', true);

-- Insert dummy bookings
INSERT INTO bookings (booked_by, car_rented, start_date, end_date, total_cost, currency)
VALUES
    (1, 2, '2025-04-10', '2025-04-15', 450.00, 'USD'),
    (2, 3, '2025-04-12', '2025-04-18', 720.00, 'USD'),
    (3, 1, '2025-04-20', '2025-04-25', 225.00, 'USD');
