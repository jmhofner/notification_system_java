package com.system.juan.miguel.notification.service;

import com.system.juan.miguel.notification.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public void sendNotification(User user, String message) {
        System.out.println("Sending email to: " + user.getEmail());
        System.out.println("Message: " + message);
    }

    @Override
    public String getChannelType() {
        return "EMAIL";
    }
}