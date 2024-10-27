package com.system.juan.miguel.notification.controllers;


import com.system.juan.miguel.notification.controller.NotificationController;
import com.system.juan.miguel.notification.entity.NotificationLog;
import com.system.juan.miguel.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void sendNotification_WithValidPayload_ShouldReturnOk() {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("category", "sports");
        payload.put("message", "Test message");

        doNothing().when(notificationService)
                .sendNotification(payload.get("category"), payload.get("message"));

        // Act
        ResponseEntity<?> response = notificationController.sendNotification(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notificationService).sendNotification("sports", "Test message");
    }

    @Test
    void getLogs_ShouldReturnAllLogs() {
        // Arrange
        List<NotificationLog> expectedLogs = Arrays.asList(
                createNotificationLog(1L, "sports", "Test message 1"),
                createNotificationLog(2L, "finance", "Test message 2")
        );
        when(notificationService.getLogs()).thenReturn(expectedLogs);

        // Act
        ResponseEntity<List<NotificationLog>> response = notificationController.getLogs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLogs, response.getBody());
        verify(notificationService).getLogs();
    }

    private NotificationLog createNotificationLog(Long id, String category, String message) {
        NotificationLog log = new NotificationLog();
        log.setId(id);
        log.setCategory(category);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());
        log.setStatus("SUCCESS");
        return log;
    }
}