package com.shopme.adminapp.encryptpassword;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.util.Predicates.isTrue;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "random_pwd";
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        assertThat(matches).isTrue();
    }
}
