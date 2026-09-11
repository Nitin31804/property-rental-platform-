import { prisma } from "@/lib/prisma";
import { redirect } from "next/navigation";
import { headers } from "next/headers";
import { checkRateLimit } from "@/lib/rateLimit";
import bcrypt from "bcrypt";

export default function Register() {
  async function registerUser(formData: FormData) {
    "use server";
    
    // Anti-DoS Rate Limiting
    const ip = headers().get("x-forwarded-for") || "unknown";
    if (!checkRateLimit(ip)) {
      redirect("/login?error=Too many attempts. Please try again in 60 seconds.");
    }
    const name = formData.get("name") as string;
    const email = formData.get("email") as string;
    const password = formData.get("password") as string;
    const roleString = formData.get("role") as string;

    const hashedPassword = await bcrypt.hash(password, 10);
    
    try {
      await prisma.users.create({
        data: {
          name,
          email,
          password_hash: hashedPassword,
          role: roleString.includes("Host") ? "Host" : "Guest"
        }
      });
    } catch (error: any) {
      if (error.code === "P2002") {
        // P2002 is Prisma's error code for "Unique constraint failed"
        redirect("/login?error=Email already exists. Please login instead.");
      }
      throw error;
    }
    
    redirect("/login?success=Account created successfully");
  }

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Create an account
        </h2>
      </div>
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6" action={registerUser}>
          <div>
            <label htmlFor="name" className="block text-sm font-medium leading-6 text-gray-900">Full Name</label>
            <div className="mt-2">
              <input id="name" name="name" type="text" required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">Email address</label>
            <div className="mt-2">
              <input id="email" name="email" type="email" required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">Password</label>
            <div className="mt-2">
              <input id="password" name="password" type="password" required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="role" className="block text-sm font-medium leading-6 text-gray-900">I want to...</label>
            <div className="mt-2">
              <select id="role" name="role" className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6">
                <option>Book properties (Guest)</option>
                <option>Host properties (Host)</option>
              </select>
            </div>
          </div>
          <div>
            <button type="submit" className="flex w-full justify-center rounded-md bg-blue-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-blue-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600">
              Register
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
