package com.example.restaurant_reservation.domain.payment.dto;

import com.example.restaurant_reservation.domain.payment.entity.PaymentMethod;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PaymentUpdateDto {


    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;


    @Builder
    public PaymentUpdateDto(BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
}
