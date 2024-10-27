## Notification System with Frontend and Backend

This project is a comprehensive notification system designed to handle message distribution across multiple channels based on user preferences. The system includes both a frontend (built with React) and a backend (developed using Java 8 and Spring Boot).

### Key Features:

- **Message Categories:** Users receive notifications based on three predefined categories: Sports, Finance, and Movies. Notifications are sent only if the message category matches the user's subscriptions.
- **Notification Channels:** Supports three types of notification channels: SMS, Email, and Push Notification. Each channel has its own class to manage the sending logic independently.
- **User Configuration:** Users are pre-populated with details such as ID, Name, Email, Phone Number, Subscribed Categories, and Preferred Notification Channels (SMS, Email, Push).
- **Architecture:** The backend architecture follows best practices in OOP, SOLID principles, and design patterns. It employs a well-structured approach with controllers, services, repositories, DTOs, and interfaces to ensure scalability and ease of maintenance.
- **Database Integration:** Utilizes JPA with MySQL, including support for migrations, foreign keys, and indexing. The backend stores all relevant information to track message deliveries, including message type, user data, and timestamps.
- **Frontend:** Built with React, the user interface consists of:
  1. A submission form where users can choose a category and enter a message.
  2. A log history that displays all past messages, sorted by date.
- **Scalability:** Designed to add more notification channels with minimal changes to the core logic.
- **Unit Testing:** Comprehensive tests for each service and function, ensuring reliability and robustness.

This project provides a solid foundation for a scalable, multi-channel notification system. Although actual message delivery is not implemented, the architecture is structured to accommodate future integration with third-party services.
