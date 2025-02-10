package com.example.restaurant_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RestaurantReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantReservationApplication.class, args);
	}

}
