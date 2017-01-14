package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString
@NoArgsConstructor
public class Vocabulary {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long no;
    @Column(name = "voca",unique = true)
    private String voca;
    @Column(name = "voice_url")
    private String voice_url;
    //    @ManyToMany
    @JsonIgnore
    @OneToMany(mappedBy = "vocabulary",cascade = CascadeType.PERSIST)
    Set<VocaOfUser> vocaOfUsers;
    //    @JsonIgnore
//    @JsonIgnore
    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL)
    private Set<Meaning> meanings;
//    @JsonIgnore
    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL)
    private Set<Example> examples;

}
