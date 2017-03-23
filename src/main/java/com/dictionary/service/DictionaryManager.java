package com.dictionary.service;

import com.dictionary.domain.User;
import com.dictionary.domain.UserDictionary;
import com.dictionary.reposity.UserDictionaryRepository;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Bae on 2017-03-11.
 */
@Service
public class DictionaryManager{
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDictionaryRepository userDictionaryRepository;

    public boolean add(HttpServletRequest httpServletRequest, User user, UserDictionary userDictionary) throws UnsupportedEncodingException {
        String token = httpServletRequest.getHeader("authorization");
        User dbUser;
        if (token != null && AuthToken.authToken(token)) {
            user.setEmail(AuthToken.getUser(token));
            dbUser = userRepository.findOne(user.getEmail());
            if (dbUser.getToken().equals(token)) {
                userDictionary.setUser(user);
                userDictionaryRepository.save(userDictionary);
                return true;
            }
        }
        return false;
    }

    public boolean delete(HttpServletRequest httpServletRequest, Long dicNo) throws UnsupportedEncodingException {
        String token = httpServletRequest.getHeader("authorization");
        String id ;
        User dbUser;

        if (token != null && AuthToken.authToken(token)) {
            id = AuthToken.getUser(token);
            dbUser = userRepository.findOne(id);
            if (dbUser.getToken().equals(token)) {
                UserDictionary dbUserDictionary = userDictionaryRepository.findOne(dicNo);
                if (dbUserDictionary!=null&&dbUserDictionary.getUser().getEmail().equals(id)) {
                    userDictionaryRepository.delete(dbUserDictionary);
                    return true;
                }
            }
        }
        return false;
    }

    public List<UserDictionary> getList(String userId) {
        if (userRepository.findOne(userId) != null) {
            return userDictionaryRepository.findByUser_email(userId);
        }
        return null;
    }
}
