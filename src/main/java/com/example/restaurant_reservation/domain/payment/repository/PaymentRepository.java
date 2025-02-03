package com.example.restaurant_reservation.domain.payment.repository;

import com.example.restaurant_reservation.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>,PaymentRepositoryCustom {
}