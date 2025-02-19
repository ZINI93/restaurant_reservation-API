package com.example.restaurant_reservation.domain.payment.repository;

import com.example.restaurant_reservation.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {

    Optional<Payment> findByOwnerId(Long userId);

    List<Payment> findAllByOwnerId(Long userId);



}