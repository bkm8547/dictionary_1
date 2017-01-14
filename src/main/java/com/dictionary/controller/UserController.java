package com.dictionary.controller;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
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
    @RequestMapping(value="/user",method = RequestMethod.POST)
    public Object register(User user){
        if(userReposity.findOne(user.getId())==null)
            userReposity.save(user);
        else {
            Map map=new HashMap();
            map.put("message", "failed");
            return map;
        }
        return user;
    }
    @Transactional
    @RequestMapping(value="/user_delete",method = RequestMethod.POST)
    public Object deleteUser(User user){
        Map map=new HashMap();
        System.out.print("userId : "+user.getId());
        User dbUser=(userReposity.findOne(user.getId())!=null)?userReposity.findOne(user.getId()):null;
        if(dbUser!=null&&dbUser.getPassword().equals(user.getPassword())){
            userReposity.delete(dbUser);
            map.put("message","successed");
        }
        else{
            map.put("message","failed");
        }
        return map;
    }
}
