-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS notification_system;
USE notification_system;

-- Tabla de usuarios
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla para las suscripciones de usuarios (relación many-to-many)
CREATE TABLE user_subscriptions (
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, category),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla para los canales de notificación de usuarios (relación many-to-many)
CREATE TABLE user_channels (
    user_id BIGINT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, channel),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de logs de notificaciones
CREATE TABLE notification_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    status VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_timestamp (timestamp),
    INDEX idx_user_category (user_id, category)
);

-- Crear índices adicionales para optimizar las búsquedas
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_notification_status ON notification_logs(status);

-- Insertar usuarios de prueba
INSERT INTO users (name, email, phone_number) VALUES
('Juan Pérez', 'juan.perez@email.com', '+521234567890'),
('María García', 'maria.garcia@email.com', '+521234567891'),
('Carlos López', 'carlos.lopez@email.com', '+521234567892'),
('Ana Martínez', 'ana.martinez@email.com', '+521234567893');

-- Insertar suscripciones de usuarios
INSERT INTO user_subscriptions (user_id, category) VALUES
(1, 'SPORTS'),
(1, 'FINANCE'),
(2, 'MOVIES'),
(2, 'SPORTS'),
(3, 'FINANCE'),
(3, 'MOVIES'),
(4, 'SPORTS'),
(4, 'MOVIES'),
(4, 'FINANCE');

-- Insertar canales de notificación preferidos
INSERT INTO user_channels (user_id, channel) VALUES
(1, 'EMAIL'),
(1, 'SMS'),
(2, 'EMAIL'),
(2, 'PUSH'),
(3, 'SMS'),
(3, 'PUSH'),
(4, 'EMAIL'),
(4, 'SMS'),
(4, 'PUSH');

-- Insertar algunos logs de ejemplo
INSERT INTO notification_logs (user_id, category, message, channel, status, timestamp) VALUES
(1, 'SPORTS', 'Nuevo partido programado', 'EMAIL', 'SUCCESS', NOW()),
(1, 'SPORTS', 'Nuevo partido programado', 'SMS', 'SUCCESS', NOW()),
(2, 'MOVIES', 'Nuevo estreno disponible', 'EMAIL', 'SUCCESS', NOW()),
(2, 'MOVIES', 'Nuevo estreno disponible', 'PUSH', 'ERROR: Device not found', NOW()),
(3, 'FINANCE', 'Actualización de mercado', 'SMS', 'SUCCESS', NOW()),
(4, 'SPORTS', 'Resultado del partido', 'EMAIL', 'SUCCESS', NOW());
