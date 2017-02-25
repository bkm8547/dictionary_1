package com.dictionary.controller;

import static com.dictionary.domain.QVocabulary.vocabulary;
import static com.dictionary.domain.QVocaOfUser.vocaOfUser;

import com.dictionary.domain.*;
import com.dictionary.reposity.*;
import com.dictionary.util.AuthToken;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Bae on 2017-01-09.
 */
@RestController
@RequestMapping("/voca/")
public class VocaController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VocaRepository vocaRepository;
    @Autowired
    UserDictionaryRepository userDictionaryRepository;
    @Autowired
    MeaningRepository meaningRepository;
    @Autowired
    ExampleRepository exampleRepository;
    @Autowired
    VocaOfUserRepository vocaOfUserRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addVoca(User user, Vocabulary voca, Meaning meaning, Example example, VocaOfUser vocaOfUser, @RequestParam(value = "dic_no") Long no) throws UnsupportedEncodingException {
        Set<Meaning> meanings = new HashSet<>();
        Set<Example> examples = new HashSet<>();
        Set<VocaOfUser> vocaOfUsers = new HashSet<>();
        User dbUser = userRepository.findOne(user.getId());
        UserDictionary userDictionary = userDictionaryRepository.findOne(no);
        Vocabulary dbVoca = vocaRepository.findOneByVoca(voca.getVoca());
        Map map = new HashMap();
        if (dbVoca == null) {
            if (AuthToken.authToken(user.getId(), user.getToken()) && dbUser.getToken().equals(user.getToken())) {
                voca.setMeanings(meanings);
                voca.setExamples(examples);
                meaning.setVocabulary(voca);
                meanings.add(meaning);
                example.setVocabulary(voca);
                examples.add(example);
                voca.setVocaOfUsers(vocaOfUsers);
                vocaOfUser.setVocabulary(voca);
                vocaOfUser.setUserDictionary(userDictionary);
                vocaOfUsers.add(vocaOfUser);
                vocaRepository.save(voca);
                map.put("message", "inserted");
            }
            else{
                map.put("message", "wrong token or expired");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            vocaOfUser.setVocabulary(dbVoca);
            vocaOfUser.setUserDictionary(userDictionary);
            vocaOfUserRepository.save(vocaOfUser);
            map.put("message", "already inserted");
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{dictionary}/{limit}/{page}", method = RequestMethod.GET)
    @Transactional
    public List get(@PathVariable Long dictionary, @PathVariable int limit, @PathVariable int page) {
        JPAQuery<?> query = new JPAQuery<Void>(em);
        List voca = query.select(vocaOfUser).from(vocaOfUser).innerJoin(vocaOfUser.vocabulary, vocabulary).where(vocaOfUser.vocabulary.no.eq(vocabulary.no))
                .where(vocaOfUser.userDictionary.no.eq(dictionary)).limit(limit).offset((page - 1) * limit).fetch();
        return voca;

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity remove(User user, @RequestParam("no") Long no) throws UnsupportedEncodingException {
        User dbUser = userRepository.findOne(user.getId());
        Map map = new HashMap();
        if (AuthToken.authToken(user.getId(), user.getToken()) && dbUser.getToken().equals(user.getToken())&&dbUser.getToken().equals(user.getToken())) {
            vocaOfUserRepository.delete(no);
            map.put("message", "deleted");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("message", "password not correct");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
