package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

/**
 * Created by Bae on 2017-01-09.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
//@RequiredArgsConstructor
public class Meaning {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long no;
    @JsonIgnore
    @ManyToOne
    @NonNull
    private Vocabulary vocabulary;
    @Column(name="word_class")
    @NonNull
    private String word_class;
    @NonNull
    @Column(name="meaning")
    private String voca_meaning;
    public Meaning(Vocabulary voca,String wordClass,String meaning){
        this.vocabulary=voca;
        this.word_class=wordClass;
        this.voca_meaning=meaning;
    }
}
