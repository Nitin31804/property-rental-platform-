import mysql.connector
import random
import json

types = [
    "Hotel",
    "Villa",
    "Cabin",
    "Loft",
    "Apartment",
    "Mansion",
    "Cottage",
    "Studio",
    "Chalet",
    "Resort",
    "Guesthouse",
]
amenities_pool = [
    "Pool",
    "WiFi",
    "Pet Friendly",
    "Free Parking",
    "Air Conditioning",
    "Kitchen",
    "Gym",
    "Hot Tub",
    "Breakfast Included",
    "Balcony",
    "Ocean View",
]

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="NITIN1875",
        database="property_rental_db",
    )
    cursor = conn.cursor()

    try:
        cursor.execute(
            "ALTER TABLE Properties ADD COLUMN property_type VARCHAR(50) DEFAULT 'Apartment'"
        )
        cursor.execute(
            "ALTER TABLE Properties ADD COLUMN amenities VARCHAR(500) DEFAULT ''"
        )
    except Exception as e:
        print("Columns might already exist:", e)

    cursor.execute("SELECT property_id FROM Properties")
    properties = cursor.fetchall()

    for prop in properties:
        prop_id = prop[0]
        ptype = random.choice(types)

        # Pick 3 to 6 random amenities
        num_amenities = random.randint(3, 6)
        prop_amenities = random.sample(amenities_pool, num_amenities)
        amenities_str = ",".join(prop_amenities)

        cursor.execute(
            "UPDATE Properties SET property_type = %s, amenities = %s WHERE property_id = %s",
            (ptype, amenities_str, prop_id),
        )

    conn.commit()
    print("Database updated successfully with property types and amenities.")

except Exception as e:
    print("Error:", e)
finally:
    if "cursor" in locals():
        cursor.close()
    if "conn" in locals() and conn.is_connected():
        conn.close()
