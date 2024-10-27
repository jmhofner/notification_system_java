package com.system.juan.miguel.notification.repositories;

import com.system.juan.miguel.notification.entity.User;
import com.system.juan.miguel.notification.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findBySubscribedContaining_ShouldReturnMatchingUsers() {
        // Arrange
        User user1 = createUser("user1@test.com", Arrays.asList("sports", "finance"));
        User user2 = createUser("user2@test.com", Arrays.asList("movies", "sports"));
        User user3 = createUser("user3@test.com", Arrays.asList("finance"));

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // Act
        List<User> sportsUsers = userRepository.findBySubscribedContaining("sports");
        List<User> financeUsers = userRepository.findBySubscribedContaining("finance");
        List<User> moviesUsers = userRepository.findBySubscribedContaining("movies");

        // Assert
        assertEquals(2, sportsUsers.size());
        assertEquals(2, financeUsers.size());
        assertEquals(1, moviesUsers.size());

        assertTrue(sportsUsers.stream()
                .anyMatch(user -> user.getEmail().equals("user1@test.com")));
        assertTrue(sportsUsers.stream()
                .anyMatch(user -> user.getEmail().equals("user2@test.com")));
    }

    private User createUser(String email, List<String> subscribed) {
        User user = new User();
        user.setName("Test User");
        user.setEmail(email);
        user.setPhoneNumber("1234567890");
        user.setSubscribed(subscribed);
        user.setChannels(Arrays.asList("EMAIL", "SMS"));
        return user;
    }
}