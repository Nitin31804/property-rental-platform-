import mysql.connector
import random

city_coords = {
    "New York": (40.7128, -74.0060),
    "Los Angeles": (34.0522, -118.2437),
    "Paris": (48.8566, 2.3522),
    "Tokyo": (35.6762, 139.6503),
    "London": (51.5074, -0.1278),
    "Sydney": (-33.8688, 151.2093),
    "Rome": (41.9028, 12.4964),
    "Dubai": (25.2048, 55.2708),
    "Barcelona": (41.3851, 2.1734),
    "Amsterdam": (52.3676, 4.9041),
    "Miami": (25.7617, -80.1918),
    "Toronto": (43.6510, -79.3470),
    "Vancouver": (49.2827, -123.1207),
    "Bali": (-8.4095, 115.1889),
    "Phuket": (7.9519, 98.3381),
    "Santorini": (36.3932, 25.4615),
    "Kyoto": (35.0116, 135.7681),
    "Venice": (45.4408, 12.3155),
    "Honolulu": (21.3069, -157.8583),
    "Cape Town": (-33.9249, 18.4241),
}

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="NITIN1875",
        database="property_rental_db",
    )
    cursor = conn.cursor()

    # Add columns if they don't exist
    try:
        cursor.execute(
            "ALTER TABLE Properties ADD COLUMN rating DECIMAL(3,2) DEFAULT 0.0"
        )
        cursor.execute("ALTER TABLE Properties ADD COLUMN review_count INT DEFAULT 0")
        cursor.execute(
            "ALTER TABLE Properties ADD COLUMN latitude DECIMAL(9,6) DEFAULT 0.0"
        )
        cursor.execute(
            "ALTER TABLE Properties ADD COLUMN longitude DECIMAL(9,6) DEFAULT 0.0"
        )
    except Exception as e:
        print("Columns might already exist:", e)

    cursor.execute("SELECT property_id, location_city FROM Properties")
    properties = cursor.fetchall()

    for prop in properties:
        prop_id = prop[0]
        city = prop[1]

        # Generate realistic random rating
        rating = round(random.uniform(3.8, 5.0), 1)
        review_count = random.randint(5, 300)

        # Generate coordinates with slight randomization around the city center
        base_lat, base_lng = city_coords.get(city, (0.0, 0.0))
        lat = base_lat + random.uniform(-0.05, 0.05)
        lng = base_lng + random.uniform(-0.05, 0.05)

        cursor.execute(
            "UPDATE Properties SET rating = %s, review_count = %s, latitude = %s, longitude = %s WHERE property_id = %s",
            (rating, review_count, lat, lng, prop_id),
        )

    conn.commit()
    print("Database updated successfully with ratings and coordinates.")

except Exception as e:
    print("Error:", e)
finally:
    if "cursor" in locals():
        cursor.close()
    if "conn" in locals() and conn.is_connected():
        conn.close()
