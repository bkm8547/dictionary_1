package com.dictionary.reposity;

import com.dictionary.domain.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Bae on 2017-01-10.
 */
public interface VocaRepository extends JpaRepository<Vocabulary, Long> {
    public Vocabulary findOneByVoca(String voca);
//    Page<User> findByLastname(String lastname, Pageable pageable);
}
