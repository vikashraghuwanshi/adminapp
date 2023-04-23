package com.shopme.adminapp.service;

import com.shopme.adminapp.models.Role;
import com.shopme.adminapp.models.User;
import com.shopme.adminapp.repository.RoleRepository;
import com.shopme.adminapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    public List<User> listAllUsers() {
        return (List<User>) userRepository.findAll();
    }


    public List<Role> listAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
