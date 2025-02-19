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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id",nullable = false)
    private Reservation reservation;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Builder
    public Payment(Reservation reservation, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, Long ownerId) {
        this.reservation = reservation;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.ownerId = ownerId;
    }


    public PaymentResponseDto toResponse(){
        return PaymentResponseDto.builder()
                .reservationId(this.reservation.getId())
                .amount(this.getAmount())
                .paymentMethod(this.paymentMethod)
                .status(this.status)
                .build();
    }

    public void updateInfo(BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status) {
        if (amount != null) this.amount = amount;
        if (paymentMethod != null) this.paymentMethod = paymentMethod;
        if (status != null) this.status = status;
    }
}
