package com.dictionary.controller;


import com.dictionary.domain.User;
import com.dictionary.domain.UserDictionary;
import com.dictionary.reposity.UserDictionaryRepository;
import com.dictionary.reposity.UserRepository;
import com.dictionary.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    public ResponseEntity add(HttpServletRequest httpServletRequest,User user, UserDictionary userDictionary) throws UnsupportedEncodingException {
        String auth=httpServletRequest.getHeader("authorization");
        User dbUser = userRepository.findOne(user.getId());
        Map<String,Object> map = new HashMap();
        if (AuthToken.authToken(user.getId(),auth)&&dbUser.getToken().equals(auth)) {
            userDictionary.setUser(user);
            userDictionaryRepository.save(userDictionary);
            map.put("message", "단어장 생성 성공");
            map.put("dic",userDictionary);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message", "단어장 생성 실패,토큰을 확인하세요.");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{userid}/{dicno}", method = RequestMethod.DELETE)
    public ResponseEntity delete(HttpServletRequest httpServletRequest, @PathVariable String userid, @PathVariable Long dicno) throws UnsupportedEncodingException {
        User dbUser = userRepository.findOne(userid);
        String auth=httpServletRequest.getHeader("authorization");
        Map map = new HashMap();
        if (AuthToken.authToken(userid, auth) && dbUser.getToken().equals(auth)) {
            UserDictionary dbUserDictionary = userDictionaryRepository.findOne(dicno);
            if (dbUserDictionary.getUser().getId().equals(userid))
                userDictionaryRepository.delete(dbUserDictionary);
            map.put("message", "successed");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message", "failed");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
