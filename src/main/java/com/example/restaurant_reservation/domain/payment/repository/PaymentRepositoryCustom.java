package com.example.restaurant_reservation.domain.payment.repository;

import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PaymentRepositoryCustom {


    Page<PaymentResponseDto> paymentSearch(Long reservationId, PaymentStatus status ,Pageable pageable);
}
