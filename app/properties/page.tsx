import { prisma } from "@/lib/prisma";
import Link from "next/link";
import Image from "next/image";

export const dynamic = "force-dynamic";

export default async function PropertiesPage() {
  const properties = await prisma.properties.findMany({
    include: {
      Users: { select: { name: true } }
    },
    take: 20
  });

  return (
    <div className="bg-white py-12 sm:py-16">
      <div className="mx-auto max-w-7xl px-6 lg:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">Available Properties</h2>
          <p className="mt-2 text-lg leading-8 text-gray-600">
            Find the perfect place for your next trip.
          </p>
        </div>
        <div className="mx-auto mt-16 grid max-w-2xl grid-cols-1 gap-x-8 gap-y-20 lg:mx-0 lg:max-w-none lg:grid-cols-3">
          {properties.map((property) => (
            <article key={property.property_id} className="flex flex-col items-start justify-between bg-white rounded-2xl shadow-sm ring-1 ring-gray-200 overflow-hidden">
              <div className="relative w-full">
                {property.cover_image_url ? (
                  <img
                    src={property.cover_image_url}
                    alt=""
                    className="aspect-[16/9] w-full object-cover sm:aspect-[2/1] lg:aspect-[3/2]"
                  />
                ) : (
                  <div className="aspect-[16/9] w-full bg-gray-100 flex items-center justify-center">
                    <span className="text-gray-400">No image</span>
                  </div>
                )}
              </div>
              <div className="p-6 max-w-xl">
                <div className="flex items-center gap-x-4 text-xs">
                  <span className="text-gray-500">{property.location_city}</span>
                  <span className="relative z-10 rounded-full bg-blue-50 px-3 py-1.5 font-medium text-blue-600">
                    ${property.price_per_night.toString()} / night
                  </span>
                </div>
                <div className="group relative">
                  <h3 className="mt-3 text-lg font-semibold leading-6 text-gray-900 group-hover:text-gray-600">
                    <Link href={`/properties/${property.property_id}`}>
                      <span className="absolute inset-0" />
                      {property.title}
                    </Link>
                  </h3>
                  <p className="mt-5 line-clamp-3 text-sm leading-6 text-gray-600">{property.description}</p>
                </div>
                <div className="relative mt-8 flex items-center gap-x-4">
                  <div className="text-sm leading-6">
                    <p className="font-semibold text-gray-900">
                      Host: {property.Users?.name || "Unknown"}
                    </p>
                  </div>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </div>
  );
}
