package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by Bae on 2017-01-12.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VocaOfUser {
    @Id
    @GeneratedValue
    @Column(name="no")
    Long no;
    @JsonIgnore
    @ManyToOne
    UserDictionary userDictionary;
//    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    Vocabulary vocabulary;


}
