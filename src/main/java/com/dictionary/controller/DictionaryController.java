package com.dictionary.controller;


import com.dictionary.domain.User;
import com.dictionary.domain.UserDictionary;
import com.dictionary.service.DictionaryManager;
import com.dictionary.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    DictionaryManager dictionaryManager;

    @GetMapping(path = "",
            produces = {"application/json","application/xml"})
    public Object get(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        Map map = new HashMap();
        String token = httpServletRequest.getHeader("authorization");
        String user = null;
        System.out.println(token);
        if (token != null)
            user = AuthToken.getUser(token);
        if (user != null) {
            System.out.println(user);
            List<UserDictionary> userDictionaries = dictionaryManager.getList(user);
            return userDictionaries;
        }
        map.put("message", "유효하지않은 토큰입니다.토큰을 확인하세요.");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity add(HttpServletRequest httpServletRequest, User user, @RequestParam(value = "dic_name") String dicName, UserDictionary userDictionary) throws UnsupportedEncodingException {
        Map map = new HashMap();
        userDictionary.setName(dicName);
        if (dictionaryManager.add(httpServletRequest, user, userDictionary)) {
            map.put("message", "단어장 생성 성공");
            return new ResponseEntity(map, HttpStatus.ACCEPTED);
        }
        map.put("message", "단어장 생성 실패,ID 또는 TOKEN을 확인하세요.");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{dic_no}", method = RequestMethod.DELETE)
    public ResponseEntity delete(HttpServletRequest httpServletRequest, @PathVariable(value = "dic_no") Long dicNo) throws UnsupportedEncodingException {
        Map map = new HashMap();
        if (dictionaryManager.delete(httpServletRequest, dicNo)) {
            map.put("message", "단어장 삭제성공");
            return new ResponseEntity(map, HttpStatus.ACCEPTED);
        }
        map.put("message", "단어장 삭제실패,TOKEN또는 dic_no를 확인하세요.");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }
}
