package com.shopme.adminapp.controller;

import com.shopme.adminapp.FileUploadUtil;
import com.shopme.adminapp.models.User;
import com.shopme.adminapp.security.ShopmeUserDetails;
import com.shopme.adminapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,
                              Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Your Account Details");
        model.addAttribute("fullName", user.getFullName());

        return "update_acnt_details_form";
    }


    @PostMapping("/account/update")
    public String updateAccountDetails(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {


        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccountDetails(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccountDetails(user);
        }

        redirectAttributes.addFlashAttribute("message",
                "Your account details have been updated successfully!!!");

        return "redirect:/account";
    }

}
