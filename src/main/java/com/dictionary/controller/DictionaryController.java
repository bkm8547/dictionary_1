package com.dictionary.controller;


import com.dictionary.domain.User;
import com.dictionary.domain.UserDictionary;
import com.dictionary.reposity.UserDictionaryRepository;
import com.dictionary.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Bae on 2017-01-11.
 */
@RestController
@RequestMapping(value="/dic")
public class DictionaryController {
    @Autowired
    UserDictionaryRepository userDictionaryRepository;
    @Autowired
    UserRepository userRepository;
    @RequestMapping(value="{userid}",method = RequestMethod.GET)
    public List get(@PathVariable String userid){
        return userDictionaryRepository.findByUser_id(userid);
    }


    @RequestMapping(value="",method = RequestMethod.POST)
        public String add(User user, UserDictionary userDictionary){
        user=userRepository.findOne(user.getId());
        User dbUser=userRepository.findOne(user.getId());
        if(dbUser.getPassword().equals(user.getPassword())){
            userDictionary.setUser(user);
            userDictionaryRepository.save(userDictionary);
        }
        return "successedd";
    }
    @RequestMapping(value="delete",method = RequestMethod.POST)
            public String delete(User user,UserDictionary userDictionary){
            User dbUser=userRepository.findOne(user.getId());
            if(dbUser.getPassword().equals(user.getPassword())){
                UserDictionary dbUserDictionary=userDictionaryRepository.findOne(userDictionary.getNo());
                if(dbUserDictionary.getUser().getId().equals(user.getId()))
                userDictionaryRepository.delete(userDictionary);
            }
            return "successed";
    }
}
