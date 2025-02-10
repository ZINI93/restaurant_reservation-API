CREATE TABLE `users` (
    `user_id`  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `name`     VARCHAR(50)  NOT NULL,
    `email`    VARCHAR(255) NOT NULL UNIQUE,
    `phone`    VARCHAR(20)  NOT NULL UNIQUE,
    `role`     VARCHAR(50)  NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `chk_user_role` CHECK (`role` IN ('USER', 'ADMIN'))
);

CREATE TABLE `restaurant_table` (

    `restaurant_table_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `table_number` VARCHAR(100) NOT NULL,
    `capacity` INT NOT NULL,
    `is_available` BOOLEAN NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

CREATE TABLE `reservation` (
    `reservation_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `restaurant_table_id` BIGINT NOT NULL,
    `reservation_time` DATETIME NOT NULL,
    `num_people` INT UNSIGNED NOT NULL,
    `status` ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL,

    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_restaurant_table_id` FOREIGN KEY(`restaurant_table_id`) REFERENCES `restaurant_table`(`restaurant_table_id`) ON DELETE CASCADE
);

CREATE TABLE `payment` (
    `payment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `reservation_id` BIGINT NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
    `paymentMethod` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_reservation_id`
        FOREIGN KEY (`reservation_id`)
        REFERENCES `reservation`(`reservation_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT `chk_paymentMethod`
        CHECK (`paymentMethod` IN ('CREDIT_CARD', 'lINE_PAY', 'PAY_PAY', 'CASH')),

    CONSTRAINT `chk_status`
        CHECK (`status` IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED'))
);

