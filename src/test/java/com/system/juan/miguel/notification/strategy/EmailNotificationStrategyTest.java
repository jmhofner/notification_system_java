package com.system.juan.miguel.notification.strategy;


import com.system.juan.miguel.notification.entity.User;
import com.system.juan.miguel.notification.service.EmailNotificationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationStrategyTest {

    private EmailNotificationStrategy emailStrategy;

    @BeforeEach
    void setUp() {
        emailStrategy = new EmailNotificationStrategy();
    }

    @Test
    void getChannelType_ShouldReturnEmail() {
        assertEquals("EMAIL", emailStrategy.getChannelType());
    }

    @Test
    void sendNotification_ShouldNotThrowException() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        String message = "Test message";

        // Act & Assert
        assertDoesNotThrow(() -> emailStrategy.sendNotification(user, message));
    }

    @Test
    void sendNotification_WithNullEmail_ShouldThrowException() {
        // Arrange
        User user = new User();
        String message = "Test message";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> emailStrategy.sendNotification(user, message));
    }
}