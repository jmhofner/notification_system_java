package com.system.juan.miguel.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogDTO {
    private Long id;
    private String userName;
    private String category;
    private String message;
    private String channel;
    private String status;
    private LocalDateTime timestamp;
}