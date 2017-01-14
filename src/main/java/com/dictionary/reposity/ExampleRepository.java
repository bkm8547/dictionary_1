package com.dictionary.reposity;


import com.dictionary.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Bae on 2017-01-10.
 */
public interface ExampleRepository extends JpaRepository<Example,Long> {
}
