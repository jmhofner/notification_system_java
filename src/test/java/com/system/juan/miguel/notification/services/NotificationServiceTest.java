package com.system.juan.miguel.notification.services;

import com.system.juan.miguel.notification.entity.User;
import com.system.juan.miguel.notification.entity.NotificationLog;
import com.system.juan.miguel.notification.repository.NotificationLogRepository;
import com.system.juan.miguel.notification.repository.UserRepository;
import com.system.juan.miguel.notification.service.EmailNotificationStrategy;
import com.system.juan.miguel.notification.service.NotificationService;
import com.system.juan.miguel.notification.service.NotificationStrategy;
import com.system.juan.miguel.notification.service.SMSNotificationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationLogRepository logRepository;

    @Mock
    private EmailNotificationStrategy emailStrategy;

    @Mock
    private SMSNotificationStrategy smsStrategy;

    @Captor
    private ArgumentCaptor<NotificationLog> logCaptor;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        List<NotificationStrategy> strategies = Arrays.asList(emailStrategy, smsStrategy);
        when(emailStrategy.getChannelType()).thenReturn("EMAIL");
        when(smsStrategy.getChannelType()).thenReturn("SMS");

        notificationService = new NotificationService(strategies);
        // Inyectamos los repositorios mock usando reflection
        setField(notificationService, "userRepository", userRepository);
        setField(notificationService, "logRepository", logRepository);
    }

    @Test
    void sendNotification_WhenValidCategoryAndMessage_ShouldNotifySubscribedUsers() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setSubscribed(Arrays.asList("sports"));
        user.setChannels(Arrays.asList("EMAIL", "SMS"));

        when(userRepository.findBySubscribedContaining("sports"))
                .thenReturn(Collections.singletonList(user));

        // Act
        notificationService.sendNotification("sports", "Test message");

        // Assert
        verify(emailStrategy).sendNotification(user, "Test message");
        verify(smsStrategy).sendNotification(user, "Test message");
        verify(logRepository, times(2)).save(logCaptor.capture());

        List<NotificationLog> capturedLogs = logCaptor.getAllValues();
        assertEquals(2, capturedLogs.size());
        assertTrue(capturedLogs.stream()
                .allMatch(log -> log.getStatus().equals("SUCCESS")));
    }

    @Test
    void sendNotification_WhenStrategyFails_ShouldLogError() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setSubscribed(Arrays.asList("sports"));
        user.setChannels(Arrays.asList("EMAIL"));

        when(userRepository.findBySubscribedContaining("sports"))
                .thenReturn(Collections.singletonList(user));

        doThrow(new RuntimeException("Failed to send email"))
                .when(emailStrategy).sendNotification(any(), any());

        // Act
        notificationService.sendNotification("sports", "Test message");

        // Assert
        verify(logRepository).save(logCaptor.capture());
        NotificationLog capturedLog = logCaptor.getValue();
        assertTrue(capturedLog.getStatus().startsWith("ERROR"));
    }

    @Test
    void getLogs_ShouldReturnAllLogsOrderedByTimestamp() {
        // Arrange
        List<NotificationLog> expectedLogs = Arrays.asList(
                createNotificationLog(1L),
                createNotificationLog(2L)
        );
        when(logRepository.findAllByOrderByTimestampDesc())
                .thenReturn(expectedLogs);

        // Act
        List<NotificationLog> actualLogs = notificationService.getLogs();

        // Assert
        assertEquals(expectedLogs, actualLogs);
        verify(logRepository).findAllByOrderByTimestampDesc();
    }

    private NotificationLog createNotificationLog(Long id) {
        NotificationLog log = new NotificationLog();
        log.setId(id);
        return log;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
