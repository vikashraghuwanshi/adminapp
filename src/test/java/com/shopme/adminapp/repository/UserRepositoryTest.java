package com.shopme.adminapp.repository;

import com.shopme.adminapp.models.Role;
import com.shopme.adminapp.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User user = new User("vikash@gmail.com", "temppwd", "Vikash", "Raghuwanshi");
        user.addRole(roleAdmin);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {

        User userRavi = new User("ravi@gmail.com", "tempravi", "Ravi", "Kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = userRepository.save(userRavi);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }


    @Test
    public void testListAllUsers() {
        Iterable<User> usersList = userRepository.findAll();
        usersList.forEach(user -> System.out.println(user.toString()));
    }


    @Test
    public void testGetUserById() {
        User user = userRepository.findById(2).get();
        System.out.println(user.toString());
        assertThat(user).isNotNull();
    }


    @Test
    public void testUpdateUserDetails() {
        User user = userRepository.findById(2).get();
        user.setEnabled(true);
        user.setEmail("changed@gmail.com");
        userRepository.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = userRepository.findById(2).get();
        user.getRoles().remove(new Role(3));
        user.addRole(new Role(2));
        userRepository.save(user);
    }


    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }
}
