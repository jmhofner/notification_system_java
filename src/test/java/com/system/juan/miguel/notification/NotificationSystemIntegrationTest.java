package com.system.juan.miguel.notification;

import com.system.juan.miguel.notification.entity.User;
import com.system.juan.miguel.notification.entity.NotificationLog;
import com.system.juan.miguel.notification.repository.NotificationLogRepository;
import com.system.juan.miguel.notification.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationSystemIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationLogRepository logRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/notifications";
        logRepository.deleteAll();
        userRepository.deleteAll();

        // Create test users
        User user = createTestUser();
        userRepository.save(user);
    }

    @Test
    void sendNotification_ShouldCreateLogsForAllChannels() {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("category", "SPORTS");
        payload.put("message", "Test message");

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/send",
                payload,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify logs were created
        List<NotificationLog> logs = logRepository.findAllByOrderByTimestampDesc();
        assertEquals(2, logs.size()); // One for each channel (EMAIL and SMS)
        assertTrue(logs.stream().allMatch(log -> log.getStatus().equals("SUCCESS")));
    }

    @Test
    void sendNotification_WithInvalidCategory_ShouldReturnBadRequest() {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("category", "INVALID_CATEGORY");
        payload.put("message", "Test message");

        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/send",
                payload,
                Map.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void sendNotification_WithEmptyMessage_ShouldReturnBadRequest() {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("category", "SPORTS");
        payload.put("message", "");

        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/send",
                payload,
                Map.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void getLogs_ShouldReturnAllLogsInDescendingOrder() {
        // Arrange
        createTestLogs();

        // Act
        ResponseEntity<NotificationLog[]> response = restTemplate.getForEntity(
                baseUrl + "/logs",
                NotificationLog[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        NotificationLog[] logs = response.getBody();
        assertTrue(logs.length >= 2);

        // Verify ordering (newest first)
        for (int i = 1; i < logs.length; i++) {
            assertTrue(logs[i-1].getTimestamp().isAfter(logs[i].getTimestamp())
                    || logs[i-1].getTimestamp().equals(logs[i].getTimestamp()));
        }
    }

    @Test
    void getLogs_WithCategory_ShouldReturnFilteredLogs() {
        // Arrange
        createTestLogs();

        // Act
        ResponseEntity<NotificationLog[]> response = restTemplate.getForEntity(
                baseUrl + "/logs?category=SPORTS",
                NotificationLog[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        NotificationLog[] logs = response.getBody();
        assertTrue(Arrays.stream(logs)
                .allMatch(log -> log.getCategory().equals("SPORTS")));
    }

    @Test
    void getUserLogs_ShouldReturnUserSpecificLogs() {
        // Arrange
        User user = userRepository.findAll().get(0);
        createTestLogs();

        // Act
        ResponseEntity<NotificationLog[]> response = restTemplate.getForEntity(
                baseUrl + "/logs/" + user.getId(),
                NotificationLog[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        NotificationLog[] logs = response.getBody();
        assertTrue(Arrays.stream(logs)
                .allMatch(log -> log.getUser().getId().equals(user.getId())));
    }

    private User createTestUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setPhoneNumber("1234567890");
        user.setSubscribed(Arrays.asList("SPORTS", "FINANCE"));
        user.setChannels(Arrays.asList("EMAIL", "SMS"));
        return user;
    }

    private void createTestLogs() {
        User user = userRepository.findAll().get(0);

        NotificationLog log1 = NotificationLog.builder()
                .user(user)
                .category("SPORTS")
                .message("Test message 1")
                .channel("EMAIL")
                .status("SUCCESS")
                .build();

        NotificationLog log2 = NotificationLog.builder()
                .user(user)
                .category("FINANCE")
                .message("Test message 2")
                .channel("SMS")
                .status("SUCCESS")
                .build();

        logRepository.saveAll(Arrays.asList(log1, log2));
    }
}