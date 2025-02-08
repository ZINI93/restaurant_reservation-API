package com.example.restaurant_reservation.domain.payment.entity;


import com.example.restaurant_reservation.domain.TimeStamp;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends TimeStamp {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id",nullable = false)
    private Reservation reservation;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    @Builder
    public Payment(Reservation reservation, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.reservation = reservation;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }


    public PaymentResponseDto toResponse(){
        return PaymentResponseDto.builder()
                .reservationId(this.reservation.getId())
                .amount(this.getAmount())
                .paymentMethod(this.paymentMethod)
                .status(this.status)
                .build();
    }

    public void UpdateInfo(BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        if (amount != null) this.amount = amount;
        if (paymentMethod != null) this.paymentMethod = paymentMethod;
        if (status != null) this.status = status;
    }
}
