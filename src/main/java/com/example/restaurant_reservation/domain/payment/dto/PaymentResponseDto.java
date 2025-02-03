package com.example.restaurant_reservation.domain.payment.dto;

import com.example.restaurant_reservation.domain.payment.entity.PaymentMethod;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponseDto {

    private Long id;
    private Long reservationId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;


    @Builder
    public PaymentResponseDto(Long reservationId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    @QueryProjection
    public PaymentResponseDto(Long id, Long reservationId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
}
