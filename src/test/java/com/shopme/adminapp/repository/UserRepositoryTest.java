package com.shopme.adminapp.repository;

import com.shopme.adminapp.models.Role;
import com.shopme.adminapp.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User user = new User("vikash@gmail.com", "temppwd", "Vikash", "Raghuwanshi");
        user.addRole(roleAdmin);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
        User user = userRepository.findById(1).get();
        System.out.println(user.toString());
        assertThat(user).isNotNull();
    }


    @Test
    public void testUpdateUserDetails() {
        User user = userRepository.findById(1).get();
        user.setEnabled(true);
        user.setEmail("changed@gmail.com");
        userRepository.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = userRepository.findById(1).get();
        user.getRoles().remove(new Role(3));
        user.addRole(new Role(2));
        userRepository.save(user);
    }


    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "random_email@gmail.com";
        User user = userRepository.getUserByEmail((email));

        assertThat(user).isNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;

        assertThat(userRepository.countById(id)).isNotZero();
    }

    @Test
    public void testDisableUser() {
        Integer id = 13;
        userRepository.updateUserEnableStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 13;
        userRepository.updateUserEnableStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber=0;
        int pageSize=4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "vikash";

        int pageNumber=0;
        int pageSize=4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
