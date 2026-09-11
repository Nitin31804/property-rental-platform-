import { prisma } from "@/lib/prisma";
import { notFound } from "next/navigation";
import Link from "next/link";

export const dynamic = "force-dynamic";

export default async function PropertyDetail({ params }: { params: { id: string } }) {
  const propertyId = parseInt(params.id);
  
  if (isNaN(propertyId)) {
    return notFound();
  }

  const property = await prisma.properties.findUnique({
    where: { property_id: propertyId },
    include: {
      Users: true,
      PropertyImages: true
    }
  });

  if (!property) return notFound();

  return (
    <div className="bg-white">
      <div className="pt-6">
        <div className="mx-auto max-w-2xl px-4 sm:px-6 lg:max-w-7xl lg:px-8">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold tracking-tight text-gray-900 sm:text-3xl">{property.title}</h1>
            <p className="text-3xl tracking-tight text-gray-900">${property.price_per_night.toString()} / night</p>
          </div>
          <p className="mt-2 text-lg text-gray-500">{property.location_city}</p>
        </div>

        <div className="mx-auto mt-6 max-w-2xl sm:px-6 lg:max-w-7xl lg:px-8">
          {property.cover_image_url && (
            <div className="aspect-h-4 aspect-w-3 overflow-hidden rounded-lg lg:block">
              <img
                src={property.cover_image_url}
                alt={property.title}
                className="h-full w-full object-cover object-center max-h-[500px]"
              />
            </div>
          )}
        </div>

        <div className="mx-auto max-w-2xl px-4 pb-16 pt-10 sm:px-6 lg:grid lg:max-w-7xl lg:grid-cols-3 lg:grid-rows-[auto,auto,1fr] lg:gap-x-8 lg:px-8 lg:pb-24 lg:pt-16">
          <div className="lg:col-span-2 lg:border-r lg:border-gray-200 lg:pr-8">
            <div>
              <h3 className="sr-only">Description</h3>
              <div className="space-y-6">
                <p className="text-base text-gray-900">{property.description}</p>
              </div>
            </div>
            
            <div className="mt-10">
              <h2 className="text-lg font-medium text-gray-900">Host Information</h2>
              <div className="mt-4 space-y-6">
                <p className="text-sm text-gray-600">Hosted by {property.Users?.name}</p>
              </div>
            </div>
          </div>

          <div className="mt-4 lg:row-span-3 lg:mt-0">
            <h2 className="sr-only">Booking information</h2>
            <form className="mt-10">
              <button
                type="submit"
                className="mt-10 flex w-full items-center justify-center rounded-md border border-transparent bg-blue-600 px-8 py-3 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Book Now
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
