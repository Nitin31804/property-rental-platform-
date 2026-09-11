import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

async function main() {
  console.log("Start seeding...");
  
  const admin = await prisma.users.create({
    data: {
      name: "System Admin",
      email: "admin@antigravity.com",
      password_hash: "admin123",
      role: "Admin",
    },
  });

  const host = await prisma.users.create({
    data: {
      name: "Alice Host",
      email: "alice.host@example.com",
      password_hash: "dummy_hash_123",
      role: "Host",
    },
  });

  const property = await prisma.properties.create({
    data: {
      host_id: host.user_id,
      title: "Cozy Penthouse with Ocean View",
      description: "Experience the best of Cape Town in this cozy penthouse.",
      price_per_night: 1295.00,
      location_city: "Cape Town",
      cover_image_url: "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",
    },
  });

  console.log(`Created admin user with id: ${admin.user_id}`);
  console.log(`Created property with id: ${property.property_id}`);
  console.log("Seeding finished.");
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });

