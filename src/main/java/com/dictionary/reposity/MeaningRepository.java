package com.dictionary.reposity;

import com.dictionary.domain.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Bae on 2017-01-10.
 */
public interface MeaningRepository extends JpaRepository<Meaning,Long> {
}
