package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Bae on 2017-01-09.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDictionary {
    @Id
    @GeneratedValue
    private Long no;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "userDictionary",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    Set<VocaOfUser> vocaOfUsers;

}
