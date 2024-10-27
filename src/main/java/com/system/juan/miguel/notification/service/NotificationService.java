package com.system.juan.miguel.notification.service;

import com.system.juan.miguel.notification.entity.NotificationLog;
import com.system.juan.miguel.notification.entity.User;
import com.system.juan.miguel.notification.repository.NotificationLogRepository;
import com.system.juan.miguel.notification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationLogRepository logRepository;
    @Autowired
    private UserRepository userRepository;



    private final Map<String, NotificationStrategy> strategyMap;

    @Autowired
    public NotificationService(List<NotificationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        NotificationStrategy::getChannelType,
                        Function.identity()
                ));
    }

    public void sendNotification(String category, String message) {
        List<User> subscribers = userRepository.findBySubscribedContaining(category);

        for (User user : subscribers) {
            for (String channel : user.getChannels()) {
                try {
                    NotificationStrategy strategy = strategyMap.get(channel.toUpperCase());
                    if (strategy != null) {
                        strategy.sendNotification(user, message);
                        logSuccess(user, category, message, channel);
                    }
                } catch (Exception e) {
                    logError(user, category, message, channel, e.getMessage());
                }
            }
        }
    }

    private void logSuccess(User user, String category, String message, String channel) {
        saveLog(user, category, message, channel, "SUCCESS");
    }

    private void logError(User user, String category, String message, String channel, String error) {
        saveLog(user, category, message, channel, "ERROR: " + error);
    }

    private void saveLog(User user, String category, String message, String channel, String status) {
        NotificationLog log = new NotificationLog();
        log.setUser(user);
        log.setCategory(category);
        log.setMessage(message);
        log.setChannel(channel);
        log.setTimestamp(LocalDateTime.now());
        log.setStatus(status);
        logRepository.save(log);
    }

    public List<NotificationLog> getLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }
}