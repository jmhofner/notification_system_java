package com.system.juan.miguel.notification.service;

import com.system.juan.miguel.notification.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SMSNotificationStrategy implements NotificationStrategy {

    @Override
    public void sendNotification(User user, String message) {
        // Aquí iría la implementación real del envío de SMS
        System.out.println("Sending SMS to: " + user.getPhoneNumber());
        System.out.println("Message: " + message);
    }

    @Override
    public String getChannelType() {
        return "SMS";
    }
}