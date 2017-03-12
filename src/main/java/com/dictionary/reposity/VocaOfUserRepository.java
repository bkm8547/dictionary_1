package com.dictionary.reposity;

import com.dictionary.domain.UserDictionary;
import com.dictionary.domain.VocaOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Created by Bae on 2017-01-12.
 */
public interface VocaOfUserRepository extends JpaRepository<VocaOfUser, Long> {
    List<VocaOfUser> findByUserDictionary(UserDictionary userDictionary);

    Long deleteByUserDictionary(UserDictionary userDictionary);
}
