package com.eliska.cattoprojectweb.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class PostEntity {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String Text;


    @Getter
    @Setter
    private Instant Sharedate;

    @Getter
    @Setter
    private Long Likecount;

    @Getter
    @Setter
    private UserEntity user;

//    @Getter
//    @Setter
//    private List<PostMediaEntity> media = new ArrayList<>();

    @Getter
    @Setter
    private PostMediaEntity media;


}
