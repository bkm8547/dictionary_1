package com.dictionary.controller;

import static com.dictionary.domain.QVocabulary.vocabulary;
import static com.dictionary.domain.QVocaOfUser.vocaOfUser;
import com.dictionary.domain.*;
import com.dictionary.reposity.*;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Object addVoca(User user, Vocabulary voca, Meaning meaning, Example example, VocaOfUser vocaOfUser, @RequestParam(value = "dic_no") Long no) {
        Set<Meaning> meanings = new HashSet<>();
        Set<Example> examples = new HashSet<>();
        Set<VocaOfUser> vocaOfUsers = new HashSet<>();
        User dbUser=userRepository.findOne(user.getId());
        UserDictionary userDictionary = userDictionaryRepository.findOne(no);
        Vocabulary dbVoca=vocaRepository.findOneByVoca(voca.getVoca());
        if (dbVoca == null&&dbUser.getPassword().equals(user.getPassword())) {
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
        } else {
            vocaOfUser.setVocabulary(dbVoca);
            vocaOfUser.setUserDictionary(userDictionary);
            vocaOfUserRepository.save(vocaOfUser);
            return "already inserted";
        }
        return "successed";
    }

    @RequestMapping(value = "{dictionary}/{limit}/{page}", method = RequestMethod.GET)
    @Transactional
    public List get(@PathVariable Long dictionary,@PathVariable int limit,@PathVariable int page) {
        JPAQuery<?> query = new JPAQuery<Void>(em);
        List voca = query.select(vocaOfUser).from(vocaOfUser).innerJoin(vocaOfUser.vocabulary,vocabulary).where(vocaOfUser.vocabulary.no.eq(vocabulary.no))
                .where(vocaOfUser.userDictionary.no.eq(dictionary)).limit(limit).offset((page-1)*limit).fetch();
        return voca;

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @Transactional
    public String remove(User user,@RequestParam("no") Long no) {
        User dbUser=userRepository.findOne(user.getId());
        if(dbUser.getPassword().equals(user.getPassword()))
            vocaOfUserRepository.delete(no);
        return "deleted";
    }
}
