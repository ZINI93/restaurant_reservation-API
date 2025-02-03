package com.example.restaurant_reservation.domain.payment.dto;

import com.example.restaurant_reservation.domain.payment.entity.PaymentMethod;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.web.ProjectedPayload;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private Long reservationId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    @Builder
    public PaymentRequestDto(Long reservationId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }



}
