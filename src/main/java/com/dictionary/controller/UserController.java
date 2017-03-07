package com.dictionary.controller;

import com.dictionary.domain.User;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import com.dictionary.util.PassBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
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
        Map<String,String> map = new HashMap();
        if (userReposity.findOne(user.getId()) == null) {
            //hash password
            user.setPassword(PassBCrypt.hash(user.getPassword()));
            System.out.println(user.getPassword());
            userReposity.save(user);
            map.put("message", "successed");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("message", "failed");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.DELETE)
    public Object deleteUser(HttpServletRequest httpServletRequest, @PathVariable String userid) throws UnsupportedEncodingException {
        Enumeration<String> headers=httpServletRequest.getHeaderNames();
        String auth=httpServletRequest.getHeader("authorization").trim();
        System.out.println(userid+auth);
        Map<String,String> map=new HashMap();
        while(headers.hasMoreElements()){
            String headersNmae=headers.nextElement();
            System.out.println(headersNmae+":"+httpServletRequest.getHeader(headersNmae));
        }
        if(AuthToken.authToken(userid,auth)){
            User user=userReposity.findOne(userid);
            if(user.getToken().equals(auth)){
            userReposity.delete(userid);
                System.out.println("유저삭제성공");
                map.put("message", "유저삭제 성공");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("message", "갱신된 토큰이 아닙니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else{
            System.out.println("유저삭제실패");
            map.put("message", "유저삭제실패,토큰을 확인하세요");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
