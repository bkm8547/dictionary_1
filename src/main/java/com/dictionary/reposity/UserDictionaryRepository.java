package com.dictionary.reposity;

import com.dictionary.domain.UserDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by Bae on 2017-01-10.
 */
public interface UserDictionaryRepository extends JpaRepository<UserDictionary,Long> {
    List<UserDictionary> findByUser_id(String id);
}
