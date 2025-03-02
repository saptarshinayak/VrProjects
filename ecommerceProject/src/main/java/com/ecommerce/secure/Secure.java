package com.ecommerce.secure;

public class Secure {
	
	
	public static String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (char ch : input.toCharArray()) {
            encrypted.append((char) (ch + 2));  // Shift each character by +2
        }
        return encrypted.toString();
    }

    // Decryption method (-2 character shift)
    public static String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (char ch : input.toCharArray()) {
            decrypted.append((char) (ch - 2));  // Shift each character by -2
        }
        return decrypted.toString();
    }
}