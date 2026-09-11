export default function AddProperty() {
  return (
    <div className="bg-white py-12 sm:py-16">
      <div className="mx-auto max-w-2xl px-6 lg:px-8">
        <h2 className="text-3xl font-bold tracking-tight text-gray-900">List Your Property</h2>
        <form className="mt-10 space-y-8">
          <div>
            <label htmlFor="title" className="block text-sm font-medium leading-6 text-gray-900">Property Title</label>
            <div className="mt-2">
              <input type="text" name="title" id="title" className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="price" className="block text-sm font-medium leading-6 text-gray-900">Price per night</label>
            <div className="mt-2">
              <input type="number" name="price" id="price" className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="city" className="block text-sm font-medium leading-6 text-gray-900">City</label>
            <div className="mt-2">
              <input type="text" name="city" id="city" className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="description" className="block text-sm font-medium leading-6 text-gray-900">Description</label>
            <div className="mt-2">
              <textarea name="description" id="description" rows={4} className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6"></textarea>
            </div>
          </div>
          <button type="submit" className="rounded-md bg-blue-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-blue-500">
            Submit Property
          </button>
        </form>
      </div>
    </div>
  );
}
