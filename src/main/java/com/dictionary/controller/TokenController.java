package com.dictionary.controller;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.service.TokenManager;
import com.dictionary.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bae on 2017-02-24.
 */
@RestController
public class TokenController {
    @Autowired
    TokenManager tokenManager;
    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "token", method = RequestMethod.POST)
    public Object getToen(User user) throws UnsupportedEncodingException {
        HashMap<String,Object> map=new HashMap();
        String token;
        //아이디 비번이 같으면
        token=tokenManager.getToken(user);
        if(token!=null){
            map.put("token",token);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message","토큰발행 실패,아이디나 패스워드를 확인하세요.");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "token/verify", method = RequestMethod.GET)
    public Object authToken(@RequestParam(value = "token") String token) {
        Map map=new HashMap();
        try{
            if(token!=null&&AuthToken.authToken(token)) {
                User dbUser=userRepository.findOne(AuthToken.getUser(token));
                if(dbUser!=null&&dbUser.getToken().equals(token)) {
                    map.put("message", "true");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("message","토큰이잘못되었거나 만료되었습니다.");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
