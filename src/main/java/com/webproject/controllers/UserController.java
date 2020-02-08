package com.webproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/index")
public class UserController {

    @GetMapping(value = "/")
    public String getHomeForUser() {
        return "home";
    }

}
