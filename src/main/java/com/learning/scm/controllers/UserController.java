package com.learning.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

    // user dashboard page
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userDasbboard() {
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile() {
        return "user/profile";
    }
    // user add contact page

    // user view contacts page

    // user edit contact page

    // user delete contact page

    // user search contact page
}