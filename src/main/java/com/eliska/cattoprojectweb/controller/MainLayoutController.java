package com.eliska.cattoprojectweb.controller;

import com.eliska.cattoprojectweb.config.AppLog;
import com.eliska.cattoprojectweb.model.PostEntity;
import com.eliska.cattoprojectweb.model.PostMediaEntity;
import com.eliska.cattoprojectweb.model.UserEntity;
import com.eliska.cattoprojectweb.service.ApiDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Controller
public class MainLayoutController {

    @Autowired
    private ApiDataService apiDataService;

    @GetMapping("/")
    public ModelAndView mainLayout( RedirectAttributes redirectAttributes ) {

        AppLog.Log("Accessing / (home) ...");

        ModelAndView mav = new ModelAndView("MainLayout");

        try {
            mav.addObject("posts", apiDataService.getAllPosts());
            mav.addObject("user", apiDataService.getLoggedUser());
            return mav;
        } catch(Exception e) {
            //return null;
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
            //return mav;
        }

    }

    @GetMapping("/profile")
    public ModelAndView profileLayout(RedirectAttributes redirectAttributes ) {

        AppLog.Log("Accessing /profile...");

        ModelAndView mav = new ModelAndView("ProfileLayout");

        try {
            mav.addObject("user", apiDataService.getLoggedUser());
            mav.addObject("posts", apiDataService.getPostsOfUser(apiDataService.getLoggedUser().getId()));
            return mav;
        } catch(Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }

    @GetMapping("/editprofile")
    public ModelAndView editProfileLayout(RedirectAttributes redirectAttributes ) {
        AppLog.Log("Accessing /editprofile");

        try {
            apiDataService.checkAuth();
            ModelAndView mav = new ModelAndView("Editprofile");
            mav.addObject("user", apiDataService.getLoggedUser());
            return mav;
        } catch(Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }

    }

    @GetMapping("/editpost")
    public ModelAndView editPost(@RequestParam(required = false) Long postId, RedirectAttributes redirectAttributes ) {
        AppLog.Log("Accessing /editpost");
        ModelAndView mav = new ModelAndView("EditPost");
        try {
            mav.addObject("user", apiDataService.getLoggedUser());
            if(postId == null) {
                PostEntity newPost = new PostEntity();
                PostMediaEntity newPostMedia = new PostMediaEntity();
                newPostMedia.setId(1L);
                newPostMedia.setFilepath("/images/cat0.png");
                newPost.setLikecount(0L);
                newPost.setMedia(newPostMedia);
                newPost.setUser(apiDataService.getLoggedUser());
                //newPost.setSharedate(Instant.now());
                newPost.setId(0L);
                mav.addObject("post", newPost);
            } else {
                mav.addObject("post", apiDataService.getPostById(postId));
            }
            mav.addObject("media", apiDataService.getAllMedia());
            return mav;
        } catch(Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }

    @PostMapping("/updatepost")
    public ModelAndView updatePost(@ModelAttribute("post") PostEntity post, RedirectAttributes redirectAttributes) {
        AppLog.Log("Accessing /updatepost (POST), post with ID:" + post.getId());
        try{
            post = apiDataService.udpatePost(post);
            redirectAttributes.addAttribute("postId", post.getId());
            return new ModelAndView("redirect:/profile");
            //return new ModelAndView("redirect:/editpost");
        } catch (Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }


    @GetMapping("/registerpage")
    public ModelAndView registerLayout() {
        AppLog.Log("Accessing /registerpage ...");
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new UserEntity());
        return mav;
    }

    @PostMapping("/registeruser")
    public ModelAndView submitUser(@ModelAttribute("user") UserEntity user, RedirectAttributes redirectAttributes) {
        try{
            AppLog.Log("Accessing /registeruser (POST)...");
            UserEntity newUser = apiDataService.createUser(user);
            apiDataService.setAuth(newUser);
            redirectAttributes.addAttribute("error", "Registered, please log in");
            return new ModelAndView("login"); //"redirect:/login";

        } catch (Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }

    @PostMapping("/updateprofile")
    public ModelAndView updateProfile(@ModelAttribute("user") UserEntity user, RedirectAttributes redirectAttributes) {
        AppLog.Log("Accessing /updateprofile (POST), user with ID:" + user.getId().toString());
        try{
            apiDataService.udpateLoggedUser(user);
            return new ModelAndView("redirect:/profile");
        } catch (Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }


    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String errorFromParam, String error) {
        AppLog.Log("Accessing /login ...");
        var mav = new ModelAndView("login");

        mav.addObject("user", new UserEntity());

        if(errorFromParam != null) {
            mav.addObject("error", errorFromParam);
        } else if(error != null) {
            mav.addObject("error", error);
        } else {
            mav.addObject("error", "Please log in");
        }
        return mav;
    }

    @PostMapping("/loginuser")
    public ModelAndView loginUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        AppLog.Log("Accessing /loginuser (POST) ...");
        apiDataService.setAuth(username, password); // 1) nastavi headers s Username a Password

        try {
            apiDataService.getUserByUsernameAndSetLoggedUser(username); // 2) pokud nevyhodi chybu, jmeno a heslo je OK
            return new ModelAndView("redirect:/");
        } catch(Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }

    @GetMapping("/logout")
    public String logout(String error, RedirectAttributes redirectAttributes) {
        AppLog.Log("Accessing /logout ...");
        apiDataService.setAuth("___", "___"); //nastavi schvalne neplatne hlavicky
        redirectAttributes.addAttribute("error", "Successfully logged off"); //vytvori /login?error=
        return "redirect:/login";
    }


    @GetMapping("/error")
    public ModelAndView error() {
        AppLog.Log("Accessing /error...");
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "--no error--");
        return mav;
    }

    private ModelAndView handleExceptionAndRedirectMAV(Exception e, RedirectAttributes redirectAttributes ) {
        AppLog.Log("Accessing handleExceptionAndRedirectMAV...");
        String errorMessageForUser = "";
        ModelAndView mav = new ModelAndView("error");
        AppLog.Log(e.getMessage());
        if(e instanceof HttpClientErrorException) {
            if(((HttpClientErrorException) e).getStatusCode().toString().contains("401")) {  //if 401 Unauthorized
                mav.setViewName("redirect:/login");
                redirectAttributes.addAttribute("error", "You are not authorized, please log in");
                return mav;
            } else {
                errorMessageForUser = ((HttpClientErrorException) e).getResponseBodyAsString();
                redirectAttributes.addFlashAttribute("errorMessageForUser", errorMessageForUser);
            }
        }
        mav.setViewName("redirect:/error");
        return mav;
    }


    //jen jako test
    @GetMapping("/allusers")
    public ModelAndView allusers( RedirectAttributes redirectAttributes ) {

        AppLog.Log("Accessing /allusers...");

        try {
            ModelAndView mav = new ModelAndView("allusers");
            mav.addObject("users", apiDataService.getAllUsers());
            return mav;
        } catch(Exception e) {
            return handleExceptionAndRedirectMAV(e, redirectAttributes);
        }
    }

    @GetMapping("/settings")
    public ModelAndView settingsLayout() {
        AppLog.Log("Accessing Settings...");
        return new ModelAndView("SettingsLayout");
    }


//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
//        AppLog.Log("--------------------------------------------");
//        ModelAndView mav = new ModelAndView();
//        // Do stuff and redirect to your error view.
//
//        return mav;
//    }


}
