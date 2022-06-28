package com.beTheDonor.repository;
import com.beTheDonor.entity.ApplicationUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("Testing save user method in repository layer")
    @Rollback(value = false)
    public void saveUserTest() {
        ApplicationUser applicationUser = ApplicationUser.builder()
                .firstname("Dharmik").lastname("Soni").email("dhsoni2510@gmail.com").enabled(false).phone_number("9029892923").password("Dharmik").applicationUserRole("Donor").build();

        userRepository.save(applicationUser);
        Assertions.assertThat(applicationUser.getId()).isGreaterThan(0);

    }

    @Test
    @Order(2)
    @DisplayName("Testing findEmail() method in repository layer")
    @Rollback(value = false)
    public void emailTest() {
        ApplicationUser appUser = null;
        Optional<ApplicationUser> applicationUser = userRepository.findByEmail("dhsoni2510@gmail.com");

        if (applicationUser.isPresent()) {
            appUser = applicationUser.get();
        }
        Assertions.assertThat(appUser.getEmail()).isEqualTo("dhsoni2510@gmail.com");
    }


    @Test
    @Order(3)
    @DisplayName("Testing enableApplicationUser() method in repository layer")
    @Rollback(value = false)
    void enableApplicationUserTest()
    {
        ApplicationUser applicationUser = userRepository.findById(1L).get();
        int value =  userRepository.enableApplicationUser(applicationUser.getEmail());
        Assertions.assertThat(value).isEqualTo(1);

    }
}