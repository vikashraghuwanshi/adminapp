package com.shopme.adminapp.controller;

import com.shopme.adminapp.models.Role;
import com.shopme.adminapp.models.User;
import com.shopme.adminapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAllUsers(Model model) {
        List<User> usersList = userService.listAllUsers();
        model.addAttribute("usersList", usersList);
        return "users";
    }

    @GetMapping("/users/new")
    public String createNewUser(Model model) {
        List<Role> allRolesList = userService.listAllRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("allRolesList", allRolesList);
        return "newuser_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "User has been saved successfully!!!");
        System.out.println(user);
        return "redirect:/users";
    }
}
