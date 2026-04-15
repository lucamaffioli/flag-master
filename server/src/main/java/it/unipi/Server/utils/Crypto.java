package it.unipi.Server.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
/**
 * Classe per hashing password.
 * @author lucamaffioli
 */
public class Crypto { 
    /**
     * La funzione sfrutta l'algoritmo SHA-256 per l'hashing senza salt. 
     * Per poter salvare il risultato nel db sottoforma di stringa i byte 
     * vengono convertiti in esadecimale.
     * @param plaintxt password in chiaro.
     * @return password hasata sottoforma di stringa in esadecimale.
     */
    public static String sha256(String plaintxt) {
        try {  
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(plaintxt.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error during hashing" + e);
        }
    }
}
