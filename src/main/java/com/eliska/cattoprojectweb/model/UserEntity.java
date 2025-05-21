package com.eliska.cattoprojectweb.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserEntity {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    @Size(min = 1, max = 5)
    private String Fname;


    @Getter
    @Setter
    private String Lname;


    @Getter
    @Setter
    //@Column(name = "lastlogon")
    private Instant Lastlogon;

    @Getter
    @Setter
    private String Password;

    @Getter
    @Setter
    private String Phonenum;

    @Getter
    @Setter
    private String Email;

    @Getter
    @Setter
    private String BAN;

    @Getter
    @Setter
    private Long IdAddress;

    @Getter
    @Setter
    private Long IdUsertype;

//    @Getter
//    @Setter
//    @OneToMany(mappedBy = "user")
//    private List<PostEntity> posts = new ArrayList<>();
}
