import mysql.connector
import random

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="NITIN1875",
    database="property_rental_db"
)
cursor = db.cursor(dictionary=True)

cursor.execute("DELETE FROM PropertyImages")
db.commit()

# High quality Unsplash URLs for Exteriors (Cover)
exteriors = [
    "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80", # Resort pool
    "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80", # Luxury resort
    "https://images.unsplash.com/photo-1542314831-c6a4d27ce6a2?auto=format&fit=crop&w=1200&q=80", # Hotel exterior
    "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80", # Hotel pool day
    "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=1200&q=80", # Beach resort
    "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?auto=format&fit=crop&w=1200&q=80", # Beach house
    "https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=1200&q=80", # Cabin
    "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1200&q=80", # Mansion exterior
]

# High quality Unsplash URLs for Interiors (Gallery)
interiors = [
    "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=800&q=80", # Bed
    "https://images.unsplash.com/photo-1522771731478-44eb10e52850?auto=format&fit=crop&w=800&q=80", # Bedroom
    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=800&q=80", # Living room
    "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?auto=format&fit=crop&w=800&q=80", # Bathroom
    "https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=800&q=80", # Modern living
    "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=800&q=80", # Minimalist
    "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=800&q=80", # Dining
    "https://images.unsplash.com/photo-1595526114101-11910fb90bc1?auto=format&fit=crop&w=800&q=80", # Living area
    "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=800&q=80", # Kitchen
    "https://images.unsplash.com/photo-1583847268964-b28ce8f31586?auto=format&fit=crop&w=800&q=80", # Small bedroom
    "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?auto=format&fit=crop&w=800&q=80", # Modern space
]

cursor.execute("DELETE FROM PropertyImages")
db.commit()

cursor.execute("SELECT property_id FROM Properties")
properties = cursor.fetchall()

update_prop_sql = "UPDATE Properties SET cover_image_url = %s WHERE property_id = %s"
insert_img_sql = "INSERT INTO PropertyImages (property_id, image_url) VALUES (%s, %s)"

for p in properties:
    pid = p['property_id']
    # 1 cover
    cover = random.choice(exteriors)
    cursor.execute(update_prop_sql, (cover, pid))
    
    # 6 interiors
    imgs = random.sample(interiors, 6)
    for img in imgs:
        cursor.execute(insert_img_sql, (pid, img))

db.commit()
print("Successfully assigned high-res Unsplash interiors and exteriors to all properties!")
