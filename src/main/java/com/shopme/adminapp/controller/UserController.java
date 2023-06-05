package com.shopme.adminapp.controller;

import com.shopme.adminapp.FileUploadUtil;
import com.shopme.adminapp.exception.UserNotFoundException;
import com.shopme.adminapp.models.Role;
import com.shopme.adminapp.models.User;
import com.shopme.adminapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAllUsers(Model model, Principal principal) {
//        List<User> usersList = userService.listAllUsers();
//        model.addAttribute("usersList", usersList);
//        return "users";
        return listByPage(1,"firstName", "asc", "", model, principal);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model, Principal principal) {
        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> usersList = page.getContent();

        long startCount = (pageNum-1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("usersList", usersList);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("userName", principal.getName());

        return "users";
    }

    @GetMapping("/users/new")
    public String createNewUser(Model model) {
        List<Role> allRolesList = userService.listAllRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New User");
        model.addAttribute("allRolesList", allRolesList);
        return "newuser_form";
    }

    @GetMapping("/users/update/{id}")
    public String updateUser(@PathVariable(name="id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        try{
            User user = userService.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User");
            List<Role> allRolesList = userService.listAllRoles();
            model.addAttribute("allRolesList", allRolesList);
            return "newuser_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }


    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {


        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "User has been saved successfully!!!");
        return "redirect:/users";
    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The User ID: " + id + " has been deleted!!!");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/enable/{id}/enabled/{enabled}")
    public String updateUserEnabledStatus(@PathVariable(name="id") Integer id,
                                          @PathVariable(name="enabled") boolean enabled,
                             RedirectAttributes redirectAttributes) {

        userService.updateUserEnabledStatus(id, enabled);
        if(enabled)
            redirectAttributes.addFlashAttribute("message",
                    "The User ID: " + id + " has been enabled");
        else
            redirectAttributes.addFlashAttribute("message",
                        "The User ID: " + id + " has been disabled");
        return "redirect:/users";
    }
}
