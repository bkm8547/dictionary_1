package com.dictionary.service;

import com.dictionary.domain.*;
import com.dictionary.reposity.UserDictionaryRepository;
import com.dictionary.reposity.UserRepository;
import com.dictionary.reposity.VocaOfUserRepository;
import com.dictionary.reposity.VocaRepository;
import com.dictionary.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bae on 2017-03-11.
 */
@Service
public class VocaManager {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VocaRepository vocaRepository;
    @Autowired
    UserDictionaryRepository userDictionaryRepository;
    @Autowired
    VocaOfUserRepository vocaOfUserRepository;
    @Autowired
    VocaManager vocaManager;

    final static int NEW_VOCA=1;
    final static int EXISTING_VOCA=2;
    final static int FALSE_TOKEN=3;


    public int add(HttpServletRequest httpServletRequest, Vocabulary vocabulary, Meaning meaning, Example example, VocaOfUser vocaOfUser, Long no) throws UnsupportedEncodingException {
        String token = httpServletRequest.getHeader("authorization");
        String id;
        Set<Meaning> meanings = new HashSet<>();
        Set<Example> examples = new HashSet<>();
        if (token != null && AuthToken.authToken(token)) {
            id = AuthToken.getUser(token);
            Set<VocaOfUser> vocaOfUsers = new HashSet<>();
            User dbUser = userRepository.findOne(id);
            UserDictionary userDictionary = userDictionaryRepository.findOne(no);
            Vocabulary dbVoca = vocaRepository.findOneByVoca(vocabulary.getVoca());
            if (dbVoca == null && dbUser.getToken().equals(token)&&userDictionary!=null) { //DB에 단어가없는경우 단어를 생성하고 단어장에 추가
                vocabulary.setMeanings(meanings);
                vocabulary.setExamples(examples);
                meaning.setVocabulary(vocabulary);
                meanings.add(meaning);
                example.setVocabulary(vocabulary);
                examples.add(example);
                vocabulary.setVocaOfUsers(vocaOfUsers);
                vocaOfUser.setVocabulary(vocabulary);
                vocaOfUser.setUserDictionary(userDictionary);
                vocaOfUsers.add(vocaOfUser);
                vocaRepository.save(vocabulary);
                return NEW_VOCA;
            } else if (dbVoca != null && dbUser.getToken().equals(token)&&userDictionary!=null) { //DB에 단어가이미있는경우 개인단어장에 바로추가
                vocaOfUser.setVocabulary(dbVoca);
                vocaOfUser.setUserDictionary(userDictionary);
                vocaOfUserRepository.save(vocaOfUser);
                return EXISTING_VOCA;
            }
        }
        return FALSE_TOKEN;
    }

    public boolean delete(HttpServletRequest httpServletRequest,Long vocaNo) throws UnsupportedEncodingException {
        String token = httpServletRequest.getHeader("authorization");
//        String dicNo = httpServletRequest.getHeader("voca_no");
        String id;
        if(token!=null&&AuthToken.authToken(token)){
            id=AuthToken.getUser(token);
            if(AuthToken.isEqualFromDB(userRepository,id,token)&&vocaNo!=0L){
                vocaOfUserRepository.delete((vocaNo));
                return true;
            }
        }
        return false;
    }
}
