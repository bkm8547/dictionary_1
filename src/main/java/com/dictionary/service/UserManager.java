package com.dictionary.service;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import com.dictionary.util.PassBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bae on 2017-03-11.
 */
@Service
public class UserManager {
    @Autowired
    UserRepository userRepository;

    public boolean create(User user) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(user.getEmail());
        if (user.getEmail() != null && user.getPassword() != null &&m.matches()&& userRepository.findOne(user.getEmail()) == null) {
            //패스워드 해쉬
            user.setPassword(PassBCrypt.hash(user.getPassword()));
            System.out.println(user.getPassword());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean delete(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String token = httpServletRequest.getHeader("authorization").trim();
        String id = null;
        User user;
        if (token != null && AuthToken.authToken(token))
            id = AuthToken.getUser(token);
        user = userRepository.findOne(id);
        if (id != null && user != null) {
            //단방향암호화시 해쉬값이매번바뀌모르 brypt에서 제공하는 API사용
            if (token.equals(user.getToken())) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }
}
