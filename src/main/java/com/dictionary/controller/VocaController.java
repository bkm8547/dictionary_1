package com.dictionary.controller;

import static com.dictionary.domain.QVocabulary.vocabulary;
import static com.dictionary.domain.QVocaOfUser.vocaOfUser;

import com.dictionary.domain.*;
import com.dictionary.service.VocaManager;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Bae on 2017-01-09.
 */
@RestController
@RequestMapping("/voca")
public class VocaController {
    @Autowired
    VocaManager vocaManager;

    @PersistenceContext
    EntityManager em;
    final static int NEW_VOCA = 1;
    final static int EXISTING_VOCA = 2;
    final static int FALSE_TOKEN = 3;

    @Transactional
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addVoca(HttpServletRequest httpServletRequest, Vocabulary vocabulary, Meaning meaning, Example example, VocaOfUser vocaOfUser, @RequestParam(value = "dic_no") Long no) throws UnsupportedEncodingException {
        Map map = new HashMap();
        switch (vocaManager.add(httpServletRequest, vocabulary, meaning, example, vocaOfUser, no)) {
            case NEW_VOCA:
                map.put("message", "단어를 등록했습니다.");
                return new ResponseEntity(map, HttpStatus.OK);
            case EXISTING_VOCA:
                map.put("message", "단어가이미 존재해서 단어장에 추가했습니다.");
                return new ResponseEntity(map, HttpStatus.OK);
            case FALSE_TOKEN:
                map.put("message", "단어등록실패, 입력정보,토큰을 확인하세요.");
                return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
            default:
                return null;
        }
    }

    @RequestMapping(value = "{dic_no}/{limit}/{page}", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity get(@PathVariable(value = "dic_no") Long dicNo, @PathVariable int limit, @PathVariable int page) {
        Map map = new HashMap();
        JPAQuery<?> query = new JPAQuery<Void>(em);
        //단어장 단어리스트
        List voca = query.select(vocaOfUser).from(vocaOfUser).innerJoin(vocaOfUser.vocabulary, vocabulary).where(vocaOfUser.vocabulary.no.eq(vocabulary.no))
                .where(vocaOfUser.userDictionary.no.eq(dicNo)).limit(limit).offset((page - 1) * limit).fetch();
        if (voca != null)
            return new ResponseEntity(voca, HttpStatus.OK);
        map.put("message", "단어리스트가없습니다.");
        return new ResponseEntity(voca, HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(value = "{voca_no}", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity remove(HttpServletRequest httpServletRequest, @PathVariable(value="voca_no")Long vocaNo) throws UnsupportedEncodingException {
        Map map=new HashMap();
        if (vocaManager.delete(httpServletRequest,vocaNo)) {
            map.put("message", "삭제성공");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        map.put("message", "삭제실패 토큰,입력정보를 확인하세요.");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
