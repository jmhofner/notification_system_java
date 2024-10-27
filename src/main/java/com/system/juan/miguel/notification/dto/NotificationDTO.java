package com.system.juan.miguel.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotNull(message = "Message is required")
    @NotBlank(message = "Message cannot be empty")
    private String message;
}