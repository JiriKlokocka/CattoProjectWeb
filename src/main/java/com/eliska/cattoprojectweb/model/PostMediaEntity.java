package com.eliska.cattoprojectweb.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class PostMediaEntity {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String filepath;


//    @Getter
//    @Setter
//    @JsonBackReference
//    private PostEntity post;

    @Getter
    @Setter
    private List<PostEntity> posts = new ArrayList<>();
}
