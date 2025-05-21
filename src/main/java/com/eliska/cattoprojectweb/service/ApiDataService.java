package com.eliska.cattoprojectweb.service;

import com.eliska.cattoprojectweb.config.AppLog;
import com.eliska.cattoprojectweb.model.PostEntity;
import com.eliska.cattoprojectweb.model.PostMediaEntity;
import com.eliska.cattoprojectweb.model.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiDataService {

    @Value("${myapp.apiPath}")
    private String apiPath;

    @Getter
    private String username = "";

    @Getter
    private String password = "";

    @Getter
    @Setter
    private UserEntity loggedUser;


    private RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    public ApiDataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.basicAuthentication(username, password).build();
        //restTemplate.setErrorHandler(new MyErrorHandler());
    }

    //Nastavi BasicSthentication hlavicky pro RestTemplate
    public void setAuth(String username, String password) {
        this.username = username;
        this.password = password;
        this.restTemplate = restTemplateBuilder.basicAuthentication(this.username, this.password).build();
    }

    //Nastavi BasicSthentication hlavicky pro RestTemplate
    public void setAuth(UserEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.restTemplate = restTemplateBuilder.basicAuthentication(this.username, this.password).build();
    }

    //Zavola zabezpecene api, popkud neni uzivatel autentikovan, vyhodi 401 UNAUTHORIZED
    public String checkAuth() {
        ResponseEntity<String> response = restTemplate.getForEntity(apiPath + "/users/testcredentials", String.class);
        return response.getBody();
    }


    //Zjisti jestli uzivatel v databazi existuje nastavi loggedUser
    public UserEntity getUserByUsernameAndSetLoggedUser(String username) {
        ResponseEntity<UserEntity> response = restTemplate.getForEntity(apiPath + "/users/getbyusername/" + username, UserEntity.class);
        this.loggedUser = response.getBody();
        return this.loggedUser;
    }



    public List<PostEntity> getAllPosts() {
        List<PostEntity> posts = new ArrayList<PostEntity>();

        ResponseEntity<PostEntity[]> response = restTemplate.getForEntity(apiPath + "/posts/getall", PostEntity[].class);

        PostEntity[] postsArray = response.getBody();
        if (postsArray != null) {
            posts = Arrays.asList(postsArray);
        }
        return posts;
    }

    public List<PostEntity> getPostsOfUser(Long userId) {
        List<PostEntity> posts = new ArrayList<PostEntity>();
        ResponseEntity<PostEntity[]> response = restTemplate.getForEntity(apiPath + "/posts/getpostsofuser/" + userId, PostEntity[].class);
        PostEntity[] postsArray = response.getBody();
        if (postsArray != null) {
            posts = Arrays.asList(postsArray);
        }
        return posts;
    }

    public PostEntity getPostById(Long postId) {
        ResponseEntity<PostEntity> response = restTemplate.getForEntity(apiPath + "/posts/getpostbypostid/" + postId , PostEntity.class);
        return response.getBody();
    }

    public PostEntity createPost(PostEntity post) {
        ResponseEntity<PostEntity> response = restTemplate.postForEntity(apiPath + "/posts/createpost/" + loggedUser.getId(), post, PostEntity.class);
        return response.getBody();
    }


    public UserEntity createUser(UserEntity user) {
        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<UserEntity> response = restTemplate2.postForEntity(apiPath + "/users/createuser", user, UserEntity.class);
        return response.getBody();
    }

//    public UserEntity getLoggedUser(UserEntity user) {
//        ResponseEntity<UserEntity> response = restTemplate.getForEntity(apiPath + "/users/getbyusername/" + user.getUsername(), UserEntity.class);
//        this.loggedUser = response.getBody();
//        return this.loggedUser;
//    }


    public UserEntity udpateLoggedUser(UserEntity user) {
        ResponseEntity<UserEntity> response = restTemplate.postForEntity(apiPath + "/users/updateuser", user, UserEntity.class);
        this.loggedUser = response.getBody();
        return this.loggedUser;
    }



    //Jen kvuli testovani
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<UserEntity>();

        ResponseEntity<UserEntity[]> response = restTemplate.getForEntity(apiPath + "/users/getall", UserEntity[].class);

        UserEntity[] usersArray = response.getBody();
        if (usersArray != null) {
            users = Arrays.asList(usersArray);
        }
        return users;
    }


    public List<PostMediaEntity> getAllMedia() {
        List<PostMediaEntity> mediaList = new ArrayList<PostMediaEntity>();
        ResponseEntity<PostMediaEntity[]> response = restTemplate.getForEntity(apiPath + "/media/getall", PostMediaEntity[].class);
        PostMediaEntity[] mediaArray = response.getBody();
        if (mediaArray != null) {
            mediaList = Arrays.asList(mediaArray);
        }
        return mediaList;
    }

    public PostMediaEntity getMediaById(Long id) {
        PostMediaEntity media = new PostMediaEntity();
        ResponseEntity<PostMediaEntity> response = restTemplate.getForEntity(apiPath + "/media/getbyid/" + id , PostMediaEntity.class);
        PostMediaEntity mediaResponse = response.getBody();
        return mediaResponse;
    }



    public PostEntity udpatePost(PostEntity post) {
        AppLog.Log("Accessing /updatepost (POST), post.getId():" + post.getId());
        AppLog.Log("Accessing /updatepost (POST), post.getMedia().getId():" + post.getMedia().getId());

        AppLog.Log("Accessing /updatepost (POST), post.getUser().getId():" + post.getUser().getId());
        AppLog.Log("Accessing /updatepost (POST), post.getSharedate():" + post.getSharedate());

        //USER
        post.setUser(this.loggedUser);

        //MEDIA
        post.setMedia(getMediaById(post.getMedia().getId()));
        post.setSharedate(Instant.now());

        if(post.getId() == 0) {
            post.setId(null);
        }

        ResponseEntity<PostEntity> response = restTemplate.postForEntity(apiPath + "/posts/createorsavepost", post, PostEntity.class);

        //return response.getBody();
        return post;
    }
}

