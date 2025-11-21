package com.ksvtech.drivingschool.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AadharUtil {

    @Value("${aadhar.salt}")
    private String salt;

    public String hashAadhar(String aadharNumber) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String salted = salt + aadharNumber;
            byte[] hash = digest.digest(salted.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No SHA-256 algorithm", e);
        }
    }

    public String last4(String aadharNumber) {
        if (aadharNumber == null || aadharNumber.length() < 4) return null;
        return aadharNumber.substring(aadharNumber.length() - 4);
    }
}
