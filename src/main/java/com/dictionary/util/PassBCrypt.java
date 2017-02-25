package com.dictionary.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by Bae on 2017-02-24.
 */
public class PassBCrypt {
    public static String hash(String password) {
        // Sample code for jBCrypt is a Java
        // gensalt is work factor and the default is 10
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        return hashed;
    }
    public static boolean checkPassword(String candidate,String hashed){
        // Check that an unencrypted password matches one that has
        // previously been hashed
        if (BCrypt.checkpw(candidate, hashed)){
            System.out.println("It matches");
        return  true;}
        else {
            System.out.println("It does not match");
        return false;
        }
        }

}
