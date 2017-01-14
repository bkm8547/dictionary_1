package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by Bae on 2017-01-09.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Example {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long no;
    @ManyToOne
    @JoinColumn(name="voca_no")
    @JsonIgnore
    private Vocabulary vocabulary;
    @Column(name="example")
    private String example_santence;
    @Column(name="meaning")
    private String example_meaning;
}
