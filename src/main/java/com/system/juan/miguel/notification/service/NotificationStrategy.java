package com.system.juan.miguel.notification.service;


import com.system.juan.miguel.notification.entity.User;

public interface NotificationStrategy {
    void sendNotification(User user, String message);
    String getChannelType();
}