package com.system.juan.miguel.notification.service;

import com.system.juan.miguel.notification.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationStrategy implements NotificationStrategy {

    @Override
    public void sendNotification(User user, String message) {
        // Aquí iría la implementación real del envío de notificación push
        System.out.println("Sending Push Notification to user: " + user.getId());
        System.out.println("Message: " + message);
    }

    @Override
    public String getChannelType() {
        return "PUSH";
    }
}