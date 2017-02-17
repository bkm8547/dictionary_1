package com.dictionary.controller;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bae on 2017-01-09.
 */
@RestController
public class UserController {
    @Autowired
    UserRepository userReposity;

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Object register(User user) {
        if (userReposity.findOne(user.getId()) == null) {
            userReposity.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            Map map = new HashMap();
            map.put("message", "failed");
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @RequestMapping(value = "/user_delete", method = RequestMethod.POST)
    public Object deleteUser(User user) {
        Map map = new HashMap();
        System.out.print("userId : " + user.getId());
        User dbUser = (userReposity.findOne(user.getId()) != null) ? userReposity.findOne(user.getId()) : null;
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            userReposity.delete(dbUser);
            map.put("message", "successed");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("message", "failed");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
