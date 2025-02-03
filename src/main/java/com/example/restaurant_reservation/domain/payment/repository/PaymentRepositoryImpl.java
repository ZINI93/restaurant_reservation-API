package com.example.restaurant_reservation.domain.payment.repository;

import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.QPaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.restaurant_reservation.domain.payment.entity.QPayment.*;
import static com.example.restaurant_reservation.domain.reservation.entity.QReservation.*;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PaymentResponseDto> paymentSearch(Long reservationId, PaymentStatus status, Pageable pageable) {

        BooleanExpression predicate = buildPredicate(reservationId,status);

        List<PaymentResponseDto> payments = queryFactory.select(new QPaymentResponseDto(
                        payment.id.as("paymentId"),
                        payment.reservation.id.as("reservationId"),
                        payment.amount,
                        payment.paymentMethod,
                        payment.status)
                ).from(payment)
                .leftJoin(payment.reservation, reservation)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(payment.count())
                .from(payment)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(payments,pageable,total);
    }

    private BooleanExpression buildPredicate(Long reservationId, PaymentStatus status) {
        BooleanExpression predicate = Expressions.TRUE;

        predicate = predicate.and(filterByReservationId(reservationId));
        predicate = predicate.and(filterByStatus(status));

        return predicate;

    }

    private Predicate filterByStatus(PaymentStatus status) {
        return Optional.ofNullable(status)
                .map(payment.status::eq)
                .orElse(Expressions.TRUE);
    }

    private Predicate filterByReservationId(Long reservationId) {
        return Optional.ofNullable(reservationId)
                .map(payment.reservation.id::eq)
                .orElse(Expressions.TRUE);

    }
}
