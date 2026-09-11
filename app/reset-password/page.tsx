import { prisma } from "@/lib/prisma";
import { redirect } from "next/navigation";
import { cookies } from "next/headers";
import bcrypt from "bcrypt";

export default function ResetPassword() {
  async function resetPassword(formData: FormData) {
    "use server";
    const userOtp = formData.get("otp") as string;
    const newPassword = formData.get("password") as string;

    const sessionCookie = cookies().get("reset_session")?.value;
    if (!sessionCookie) {
      redirect("/forgot-password?error=Session expired. Please request a new OTP.");
    }

    const [targetEmail, validOtp] = sessionCookie.split(":");

    if (userOtp !== validOtp) {
      redirect("/reset-password?error=Invalid OTP");
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10);

    // Update the password in the database
    await prisma.users.update({
      where: { email: targetEmail },
      data: { password_hash: hashedPassword }
    });

    // Destroy the session cookie so the OTP cannot be reused
    cookies().delete("reset_session");

    redirect("/login?success=Password reset successfully");
  }

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Reset Password
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          Enter the 6-digit OTP we sent to your email.
        </p>
      </div>
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6" action={resetPassword}>
          <div>
            <label htmlFor="otp" className="block text-sm font-medium leading-6 text-gray-900">6-Digit OTP</label>
            <div className="mt-2">
              <input id="otp" name="otp" type="text" maxLength={6} required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">New Password</label>
            <div className="mt-2">
              <input id="password" name="password" type="password" required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <button type="submit" className="flex w-full justify-center rounded-md bg-blue-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-blue-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600">
              Reset Password
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
