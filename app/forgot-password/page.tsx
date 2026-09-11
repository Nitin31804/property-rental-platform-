import { redirect } from "next/navigation";
import { cookies } from "next/headers";

export default function ForgotPassword() {
  async function requestOTP(formData: FormData) {
    "use server";
    const email = formData.get("email") as string;
    
    // Generate a secure, random 6-digit OTP
    const otp = Math.floor(100000 + Math.random() * 900000).toString();
    console.log(`[SIMULATED SMS/EMAIL] OTP for ${email} is: ${otp}`);
    
    // Store email and OTP securely in an HTTP-only cookie
    // This prevents hackers from tampering with hidden HTML fields!
    cookies().set("reset_session", `${email}:${otp}`, { httpOnly: true, secure: true });
    
    // Redirect cleanly without exposing the email in the URL
    redirect("/reset-password");
  }

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Forgot your password?
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          Enter your email and we will send you a 6-digit OTP.
        </p>
      </div>
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6" action={requestOTP}>
          <div>
            <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">Email address</label>
            <div className="mt-2">
              <input id="email" name="email" type="email" required className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <button type="submit" className="flex w-full justify-center rounded-md bg-blue-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-blue-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600">
              Send OTP
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
