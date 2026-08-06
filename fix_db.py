import random

with open('schema.sql', 'rb') as f:
    content = f.read()

text = content.decode('utf-8', errors='ignore').replace('\0', '')
clean_text = text.split('INSERT INTO Properties (host_id, title, description, price_per_night, location_city, cover_image_url) VALUES')[0].strip()

cities = ['New York', 'Los Angeles', 'Paris', 'Tokyo', 'London', 'Sydney', 'Rome', 'Dubai', 'Barcelona', 'Amsterdam', 'Miami', 'Toronto', 'Vancouver', 'Bali', 'Phuket', 'Santorini', 'Kyoto', 'Venice', 'Honolulu', 'Cape Town']
adjectives = ['Luxury', 'Cozy', 'Modern', 'Rustic', 'Chic', 'Stunning', 'Spacious', 'Minimalist', 'Elegant', 'Vintage', 'Serene', 'Boutique', 'Historic']
types = ['Penthouse', 'Villa', 'Cabin', 'Loft', 'Apartment', 'Mansion', 'Cottage', 'Studio', 'Chalet', 'Treehouse', 'Beachhouse']
features = ['with Ocean View', 'in City Center', 'with Private Pool', 'near the Beach', 'with Rooftop Terrace', 'in Historic District', 'with Mountain Views', 'near Subway Station']

images = [
    'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1510798831971-661eb04b3739?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1502672260266-1c1de242d5cb?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1501183638710-841dd1904471?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1464146072230-91cabc968266?auto=format&fit=crop&w=800&q=80'
]

sql_statements = []
for i in range(55):
    city = random.choice(cities)
    adj = random.choice(adjectives)
    ptype = random.choice(types)
    feat = random.choice(features)
    title = f"{adj} {ptype} {feat}"
    desc = f"Experience the best of {city} in this {title.lower()}. Perfect for your next getaway."
    price = random.randint(80, 1500)
    image = random.choice(images)
    host_id = random.choice([2, 4])
    sql = f"({host_id}, '{title}', '{desc}', {price}.00, '{city}', '{image}')"
    sql_statements.append(sql)

final_sql = clean_text + "\n\nINSERT INTO Properties (host_id, title, description, price_per_night, location_city, cover_image_url) VALUES\n"
final_sql += ",\n".join(sql_statements) + ";\n"

with open('schema.sql', 'w', encoding='utf-8') as f:
    f.write(final_sql)
