package com.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Bae on 2017-01-09.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    private  String id;
    private  String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date register_date=new Date();
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private  Date modified_date=new Date();
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<UserDictionary> userDictionaries;
}