-- Database Schema for Property Rental & Tourism Booking App

DROP DATABASE IF EXISTS property_rental_db;
CREATE DATABASE property_rental_db;
USE property_rental_db;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Host', 'Guest', 'Admin') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Properties (
    property_id INT AUTO_INCREMENT PRIMARY KEY,
    host_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price_per_night DECIMAL(10, 2) NOT NULL,
    location_city VARCHAR(100) NOT NULL,
    cover_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE PropertyImages (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    FOREIGN KEY (property_id) REFERENCES Properties(property_id) ON DELETE CASCADE
);

CREATE TABLE Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT NOT NULL,
    guest_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Confirmed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES Properties(property_id) ON DELETE CASCADE,
    FOREIGN KEY (guest_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_dates CHECK (check_in_date < check_out_date)
);

CREATE TABLE CustomerFeedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bookings_dates ON Bookings(property_id, check_in_date, check_out_date);

-- =========================================================================
-- MOCK DATA FOR TESTING
-- =========================================================================

-- Hosts, Guests, and Admin
INSERT INTO Users (name, email, password_hash, role) VALUES 
('System Admin', 'admin@antigravity.com', 'admin123', 'Admin'),
('Alice Host', 'alice.host@example.com', 'dummy_hash_123', 'Host'),
('Bob Guest', 'bob.guest@example.com', 'dummy_hash_456', 'Guest'),
('Charlie Host', 'charlie.host@example.com', 'dummy_hash_789', 'Host'),
('Diana Guest', 'diana.guest@example.com', 'dummy_hash_101', 'Guest');

-- Properties

INSERT INTO Properties (host_id, title, description, price_per_night, location_city, cover_image_url) VALUES
(4, 'Cozy Penthouse with Ocean View', 'Experience the best of Cape Town in this cozy penthouse with ocean view. Perfect for your next getaway.', 1295.00, 'Cape Town', 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80'),
(4, 'Elegant Studio with Mountain Views', 'Experience the best of Honolulu in this elegant studio with mountain views. Perfect for your next getaway.', 1002.00, 'Honolulu', 'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80'),
(4, 'Vintage Cottage in Historic District', 'Experience the best of Cape Town in this vintage cottage in historic district. Perfect for your next getaway.', 712.00, 'Cape Town', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80'),
(4, 'Vintage Cottage with Mountain Views', 'Experience the best of Los Angeles in this vintage cottage with mountain views. Perfect for your next getaway.', 1268.00, 'Los Angeles', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(4, 'Stunning Chalet with Private Pool', 'Experience the best of Amsterdam in this stunning chalet with private pool. Perfect for your next getaway.', 1384.00, 'Amsterdam', 'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80'),
(2, 'Cozy Beachhouse near the Beach', 'Experience the best of Barcelona in this cozy beachhouse near the beach. Perfect for your next getaway.', 805.00, 'Barcelona', 'https://images.unsplash.com/photo-1464146072230-91cabc968266?auto=format&fit=crop&w=800&q=80'),
(2, 'Elegant Apartment in Historic District', 'Experience the best of Tokyo in this elegant apartment in historic district. Perfect for your next getaway.', 116.00, 'Tokyo', 'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80'),
(4, 'Cozy Loft in City Center', 'Experience the best of Venice in this cozy loft in city center. Perfect for your next getaway.', 1052.00, 'Venice', 'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80'),
(2, 'Rustic Mansion in City Center', 'Experience the best of Vancouver in this rustic mansion in city center. Perfect for your next getaway.', 1263.00, 'Vancouver', 'https://images.unsplash.com/photo-1464146072230-91cabc968266?auto=format&fit=crop&w=800&q=80'),
(4, 'Spacious Loft with Mountain Views', 'Experience the best of Honolulu in this spacious loft with mountain views. Perfect for your next getaway.', 203.00, 'Honolulu', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(4, 'Minimalist Apartment with Mountain Views', 'Experience the best of Rome in this minimalist apartment with mountain views. Perfect for your next getaway.', 1005.00, 'Rome', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(2, 'Vintage Beachhouse with Mountain Views', 'Experience the best of Bali in this vintage beachhouse with mountain views. Perfect for your next getaway.', 924.00, 'Bali', 'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80'),
(2, 'Vintage Cottage in Historic District', 'Experience the best of New York in this vintage cottage in historic district. Perfect for your next getaway.', 858.00, 'New York', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(2, 'Rustic Apartment in City Center', 'Experience the best of Miami in this rustic apartment in city center. Perfect for your next getaway.', 868.00, 'Miami', 'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80'),
(4, 'Serene Loft in City Center', 'Experience the best of Honolulu in this serene loft in city center. Perfect for your next getaway.', 92.00, 'Honolulu', 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80'),
(4, 'Minimalist Loft in Historic District', 'Experience the best of Rome in this minimalist loft in historic district. Perfect for your next getaway.', 797.00, 'Rome', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80'),
(2, 'Luxury Cottage with Private Pool', 'Experience the best of Barcelona in this luxury cottage with private pool. Perfect for your next getaway.', 625.00, 'Barcelona', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(2, 'Rustic Studio with Mountain Views', 'Experience the best of Venice in this rustic studio with mountain views. Perfect for your next getaway.', 1437.00, 'Venice', 'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80'),
(2, 'Modern Beachhouse with Mountain Views', 'Experience the best of Rome in this modern beachhouse with mountain views. Perfect for your next getaway.', 428.00, 'Rome', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(2, 'Serene Loft with Ocean View', 'Experience the best of Tokyo in this serene loft with ocean view. Perfect for your next getaway.', 505.00, 'Tokyo', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(4, 'Vintage Apartment with Private Pool', 'Experience the best of Dubai in this vintage apartment with private pool. Perfect for your next getaway.', 1153.00, 'Dubai', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(2, 'Spacious Beachhouse with Rooftop Terrace', 'Experience the best of Kyoto in this spacious beachhouse with rooftop terrace. Perfect for your next getaway.', 1129.00, 'Kyoto', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80'),
(2, 'Minimalist Beachhouse in City Center', 'Experience the best of Sydney in this minimalist beachhouse in city center. Perfect for your next getaway.', 361.00, 'Sydney', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(2, 'Stunning Studio with Mountain Views', 'Experience the best of Cape Town in this stunning studio with mountain views. Perfect for your next getaway.', 1023.00, 'Cape Town', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(2, 'Elegant Mansion with Rooftop Terrace', 'Experience the best of Toronto in this elegant mansion with rooftop terrace. Perfect for your next getaway.', 1330.00, 'Toronto', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(4, 'Stunning Cabin with Rooftop Terrace', 'Experience the best of Venice in this stunning cabin with rooftop terrace. Perfect for your next getaway.', 358.00, 'Venice', 'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80'),
(2, 'Cozy Cabin with Rooftop Terrace', 'Experience the best of Los Angeles in this cozy cabin with rooftop terrace. Perfect for your next getaway.', 451.00, 'Los Angeles', 'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80'),
(2, 'Serene Loft near Subway Station', 'Experience the best of Honolulu in this serene loft near subway station. Perfect for your next getaway.', 510.00, 'Honolulu', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(2, 'Vintage Chalet with Mountain Views', 'Experience the best of Phuket in this vintage chalet with mountain views. Perfect for your next getaway.', 315.00, 'Phuket', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(4, 'Minimalist Villa with Rooftop Terrace', 'Experience the best of Phuket in this minimalist villa with rooftop terrace. Perfect for your next getaway.', 760.00, 'Phuket', 'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80'),
(4, 'Stunning Cabin in City Center', 'Experience the best of Barcelona in this stunning cabin in city center. Perfect for your next getaway.', 607.00, 'Barcelona', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(2, 'Chic Villa with Mountain Views', 'Experience the best of Los Angeles in this chic villa with mountain views. Perfect for your next getaway.', 482.00, 'Los Angeles', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80'),
(4, 'Elegant Cabin with Private Pool', 'Experience the best of London in this elegant cabin with private pool. Perfect for your next getaway.', 227.00, 'London', 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80'),
(4, 'Elegant Cottage with Rooftop Terrace', 'Experience the best of Dubai in this elegant cottage with rooftop terrace. Perfect for your next getaway.', 1349.00, 'Dubai', 'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80'),
(4, 'Elegant Villa with Rooftop Terrace', 'Experience the best of Bali in this elegant villa with rooftop terrace. Perfect for your next getaway.', 1230.00, 'Bali', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(4, 'Chic Cabin with Rooftop Terrace', 'Experience the best of Vancouver in this chic cabin with rooftop terrace. Perfect for your next getaway.', 173.00, 'Vancouver', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(2, 'Minimalist Apartment in Historic District', 'Experience the best of Sydney in this minimalist apartment in historic district. Perfect for your next getaway.', 302.00, 'Sydney', 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80'),
(4, 'Historic Cottage in City Center', 'Experience the best of Tokyo in this historic cottage in city center. Perfect for your next getaway.', 339.00, 'Tokyo', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(4, 'Vintage Mansion in City Center', 'Experience the best of Sydney in this vintage mansion in city center. Perfect for your next getaway.', 1074.00, 'Sydney', 'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80'),
(2, 'Cozy Beachhouse with Rooftop Terrace', 'Experience the best of New York in this cozy beachhouse with rooftop terrace. Perfect for your next getaway.', 1003.00, 'New York', 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80'),
(4, 'Rustic Penthouse in Historic District', 'Experience the best of Tokyo in this rustic penthouse in historic district. Perfect for your next getaway.', 163.00, 'Tokyo', 'https://images.unsplash.com/photo-1464146072230-91cabc968266?auto=format&fit=crop&w=800&q=80'),
(2, 'Rustic Cottage with Private Pool', 'Experience the best of Sydney in this rustic cottage with private pool. Perfect for your next getaway.', 1377.00, 'Sydney', 'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80'),
(4, 'Rustic Mansion with Rooftop Terrace', 'Experience the best of Venice in this rustic mansion with rooftop terrace. Perfect for your next getaway.', 1433.00, 'Venice', 'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80'),
(2, 'Spacious Treehouse with Private Pool', 'Experience the best of Phuket in this spacious treehouse with private pool. Perfect for your next getaway.', 1456.00, 'Phuket', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(2, 'Modern Loft in City Center', 'Experience the best of Vancouver in this modern loft in city center. Perfect for your next getaway.', 1116.00, 'Vancouver', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(2, 'Minimalist Treehouse with Private Pool', 'Experience the best of New York in this minimalist treehouse with private pool. Perfect for your next getaway.', 377.00, 'New York', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80'),
(2, 'Spacious Loft with Rooftop Terrace', 'Experience the best of Amsterdam in this spacious loft with rooftop terrace. Perfect for your next getaway.', 164.00, 'Amsterdam', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(4, 'Historic Beachhouse with Mountain Views', 'Experience the best of Sydney in this historic beachhouse with mountain views. Perfect for your next getaway.', 85.00, 'Sydney', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(2, 'Minimalist Cottage near the Beach', 'Experience the best of London in this minimalist cottage near the beach. Perfect for your next getaway.', 303.00, 'London', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(2, 'Boutique Treehouse near Subway Station', 'Experience the best of Rome in this boutique treehouse near subway station. Perfect for your next getaway.', 984.00, 'Rome', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(2, 'Serene Penthouse with Ocean View', 'Experience the best of Miami in this serene penthouse with ocean view. Perfect for your next getaway.', 1394.00, 'Miami', 'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80'),
(4, 'Vintage Treehouse with Ocean View', 'Experience the best of Amsterdam in this vintage treehouse with ocean view. Perfect for your next getaway.', 205.00, 'Amsterdam', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80'),
(2, 'Minimalist Beachhouse with Ocean View', 'Experience the best of New York in this minimalist beachhouse with ocean view. Perfect for your next getaway.', 1421.00, 'New York', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80'),
(4, 'Historic Beachhouse in Historic District', 'Experience the best of Phuket in this historic beachhouse in historic district. Perfect for your next getaway.', 457.00, 'Phuket', 'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80'),
(4, 'Historic Treehouse with Private Pool', 'Experience the best of Dubai in this historic treehouse with private pool. Perfect for your next getaway.', 1176.00, 'Dubai', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80');
