package com.example.restaurant_reservation.domain.payment.dto;

import com.example.restaurant_reservation.domain.payment.entity.PaymentMethod;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.web.ProjectedPayload;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    @NotNull
    private Long reservationId;

    @NotNull
    private BigDecimal amount;

    @NotBlank(message = "お支払いの方法が必要です。")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "お支払いのステータスが必要です。")
    private PaymentStatus status;

    @Builder
    public PaymentRequestDto(Long reservationId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }



}
