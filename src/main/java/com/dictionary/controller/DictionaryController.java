package com.dictionary.controller;


import com.dictionary.domain.User;
import com.dictionary.domain.UserDictionary;
import com.dictionary.reposity.UserDictionaryRepository;
import com.dictionary.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bae on 2017-01-11.
 */
@RestController
@RequestMapping(value = "/dic")
public class DictionaryController {
    @Autowired
    UserDictionaryRepository userDictionaryRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "{userid}", method = RequestMethod.GET)
    public List get(@PathVariable String userid) {
        return userDictionaryRepository.findByUser_id(userid);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity add(User user, UserDictionary userDictionary) {
        user = userRepository.findOne(user.getId());
        User dbUser = userRepository.findOne(user.getId());
        Map map = new HashMap();
        if (dbUser.getPassword().equals(user.getPassword())) {
            userDictionary.setUser(user);
            userDictionaryRepository.save(userDictionary);
            map.put("message", "successed");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message", "failed");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseEntity delete(User user, UserDictionary userDictionary) {
        User dbUser = userRepository.findOne(user.getId());
        Map map = new HashMap();
        if (dbUser.getPassword().equals(user.getPassword())) {
            UserDictionary dbUserDictionary = userDictionaryRepository.findOne(userDictionary.getNo());
            if (dbUserDictionary.getUser().getId().equals(user.getId()))
                userDictionaryRepository.delete(userDictionary);
            map.put("message", "successed");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message", "failed");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
