package com.shopme.adminapp.controller;

import com.shopme.adminapp.models.User;
import com.shopme.adminapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("fullName", user.getFullName());
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

}
