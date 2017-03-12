package com.dictionary.controller;

import com.dictionary.domain.User;
import com.dictionary.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bae on 2017-01-09.
 */
@RestController
public class UserController {

    @Autowired
    UserManager userManager;

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Object create(User user) {
        Map<String, String> map = new HashMap();
        if (userManager.create(user)) {
            map.put("message", "유저생성 성공");
            return new ResponseEntity(map, HttpStatus.OK);
        }
        map.put("message", "유저생성 실패, 이미있는계정이거나 입력값이 유효하지않습니다.");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public Object deleteUser(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap();
        if (userManager.delete(httpServletRequest)) {
            map.put("message", "유저삭제 성공");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        System.out.println("유저삭제실패");
        map.put("message", "유저삭제실패,TOKEN 또는 입력값을 확인하세요");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
