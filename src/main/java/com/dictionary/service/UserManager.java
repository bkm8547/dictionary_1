package com.dictionary.service;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import com.dictionary.util.PassBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Created by Bae on 2017-03-11.
 */
@Service
public class UserManager {
    @Autowired
    UserRepository userRepository;
    public boolean create(User user){
           if (user.getId() != null && user.getPassword() != null&&userRepository.findOne(user.getId()) == null) {
                //패스워드 해쉬
                user.setPassword(PassBCrypt.hash(user.getPassword()));
                System.out.println(user.getPassword());
                userRepository.save(user);
                return true;
            }
            return false;
    }
    public boolean delete(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String token=httpServletRequest.getHeader("authorization").trim();
        String id=null;
        String password=null;
        if(token!=null&&AuthToken.authToken(token))
        id = AuthToken.getUser(token);
        password = httpServletRequest.getHeader("password").trim();
        if (id!=null&&password!=null) {
            User user = userRepository.findOne(id);
            //단방향암호화시 해쉬값이매번바뀌모르 brypt에서 제공하는 API사용
            if (user!=null&&PassBCrypt.checkPassword(password,user.getPassword())) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }
}
