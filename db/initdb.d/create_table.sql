CREATE TABLE `users` (
    `user_id`  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `email`    VARCHAR(255) NOT NULL UNIQUE,
    `phone`    VARCHAR(20)  NOT NULL UNIQUE,
    `name`     VARCHAR(50)  NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role`     VARCHAR(50)  NOT NULL,
    CONSTRAINT `chk_user_role` CHECK (`role` IN ('USER', 'ADMIN'))
);

CREATE TABLE `restaurant_table` (
    `restaurantTable_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tableNumber` VARCHAR(100) NOT NULL,
    `capacity` INT NOT NULL,
    `isAvailable` BOOLEAN NOT NULL
);

CREATE TABLE `reservation` (
    `reservation_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `restaurantTable_id` BIGINT NOT NULL,
    `reservationTime` DATETIME NOT NULL,
    `numPeople` INT NOT NULL,
    `status` VARCHAR(50) NOT NULL,

    CONSTRAINT `fk_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `users`(`user_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT `fk_restaurantTable_id`
        FOREIGN KEY (`restaurantTable_id`)
        REFERENCES `restaurantTable`(`restaurantTable_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT `chk_reservation_status`
        CHECK (`ReservationStatus` IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED'))
);

CREATE TABLE `payment` (
    `payment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `reservation_id` BIGINT NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
    `paymentMethod` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,

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

