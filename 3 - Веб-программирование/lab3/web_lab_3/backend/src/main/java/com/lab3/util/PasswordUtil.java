package com.lab3.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hash) {
        if (password == null || hash == null) return false;
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }
}