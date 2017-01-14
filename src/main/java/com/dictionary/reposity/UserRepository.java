package com.dictionary.reposity;

import com.dictionary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Bae on 2017-01-10.
 */
public interface UserRepository extends JpaRepository<User,String> {
}
