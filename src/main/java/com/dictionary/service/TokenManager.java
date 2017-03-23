package com.dictionary.service;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import com.dictionary.util.PassBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

/**
 * Created by Bae on 2017-03-11.
 */
@Service
public class TokenManager {
    @Autowired
    UserRepository userRepository;

    public String getToken(User user) throws UnsupportedEncodingException {
        User dbUser;
        System.out.println("userre:"+userRepository);
        if (user.getEmail() != null && user.getPassword() != null) {
            dbUser = userRepository.findOne(user.getEmail());
            if (dbUser!=null&&PassBCrypt.checkPassword(user.getPassword(), dbUser.getPassword())) {
                String token = AuthToken.getToken(user.getEmail());
                dbUser.setToken(token.toString());
                userRepository.save(dbUser);
                return token.toString();
            }
        }
        return null;
    }
}
