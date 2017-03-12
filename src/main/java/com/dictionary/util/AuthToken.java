package com.dictionary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by Bae on 2017-02-24.
 */
public class AuthToken {
    final static String secret = "1a2a3a";

    public static String getUser(String token) throws UnsupportedEncodingException {
        if (token != null && authToken(token)) {
            JWT jwt = null;
            jwt = JWT.decode(token);
            return jwt.getIssuer();
        }
        return null;
    }

    public static boolean isEqualFromDB(UserRepository userRepository,String id,String token) throws UnsupportedEncodingException {
        User user= userRepository.findOne(id);
        if(user!=null&&user.getToken().equals(token))
            return true;
        return false;

    }

    public static String getToken(String id) throws UnsupportedEncodingException {
        String token = JWT.create().withIssuer(id)
                .withExpiresAt(new Date(new Date().getTime() + 31536000000L))
                .sign(Algorithm.HMAC256(secret));
        System.out.println("토큰생성:" + token.toString());
        return token.toString();
    }

    public static boolean authToken(String token) throws UnsupportedEncodingException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build(); //Reusable verifier instance
            JWT jwt = (JWT) verifier.verify(token);
            System.out.println("verifier");
            return true;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            System.out.println("failed");
            return false;
        }
    }
}
