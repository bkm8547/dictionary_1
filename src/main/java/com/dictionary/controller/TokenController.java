package com.dictionary.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import com.dictionary.util.PassBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by Bae on 2017-02-24.
 */
@RestController
public class TokenController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "getToken", method = RequestMethod.POST)
    public Object getToen(User user) throws UnsupportedEncodingException {
        User dbUser = userRepository.findOne(user.getId());
        //아이디 비번이 같으면
        if (PassBCrypt.checkPassword(user.getPassword(),dbUser.getPassword())) {
            try {
                String token = JWT.create()
                        .withIssuer(user.getId()).withExpiresAt(new Date(new Date().getTime() + 31536000000L))
                        .sign(Algorithm.HMAC256("1a2a3"));
                System.out.println("토큰생성:" + token.toString());
                dbUser.setToken(token.toString());
                userRepository.save(dbUser);
                return dbUser;
            } catch (JWTCreationException exception) {
                //Invalid Signing configuration / Couldn't convert Claims.
            }
        }
        return null;
    }

    @RequestMapping(value = "authToken", method = RequestMethod.POST)
    public boolean authToken(@Param(value = "id") String id, @Param(value = "token") String token) {
        try{
            if(AuthToken.authToken(id, token))
                return true;
            else
                return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

    }
}
