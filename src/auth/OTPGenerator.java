package auth;

import java.security.SecureRandom;

// Utility class for generating One-Time Password (OTP)
public class OTPGenerator {

    // Generates a 6-digit numeric OTP
    public static String generateOTP() {

        // Use a cryptographically secure random number generator for OTPs.
        SecureRandom r = new SecureRandom();

        // Generate a number between 100000 and 999999 to ensure 6 digits
        return String.valueOf(100000 + r.nextInt(900000));
    }
}
