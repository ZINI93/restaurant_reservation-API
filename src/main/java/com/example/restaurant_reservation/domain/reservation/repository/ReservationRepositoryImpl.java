package com.example.restaurant_reservation.domain.reservation.repository;

import com.example.restaurant_reservation.domain.reservation.dto.QReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.example.restaurant_reservation.domain.reservation.entity.QReservation.*;
import static com.example.restaurant_reservation.domain.user.entity.QUser.*;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReservationResponseDto> reservationSearch(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, String sortField,ReservationStatus status, Pageable pageable) {

        //조건식 생성
        BooleanExpression predicate = buildPredicate(name, phone, startTime, endTime,status);

        //정렬처리
        OrderSpecifier<?> sortReservation = getSortReservation(sortField);

        List<ReservationResponseDto> reservations = queryFactory.select(new QReservationResponseDto(
                        reservation.id.as("reservationId"),
                        reservation.user.id.as("userId"),
                        reservation.restaurantTable.id.as("restaurantTableId"),
                        reservation.reservationTime,
                        reservation.numPeople,
                        reservation.status)
                ).from(reservation)
                .leftJoin(reservation.user, user)
                .where(predicate)
                .orderBy(sortReservation)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(reservations,pageable,total);
      }

      private static final Map<String, Supplier<OrderSpecifier<?>>> SORT_FIELDS = Map.of(
              "reservationTime", () -> reservation.reservationTime.desc()
      );

    private OrderSpecifier<?> getSortReservation(String sortField) {
        return SORT_FIELDS.getOrDefault(sortField.toLowerCase(), () -> reservation.reservationTime.desc()).get();
    }

    private BooleanExpression buildPredicate(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status) {

        BooleanExpression predicate = Expressions.TRUE;

        predicate = predicate.and(filterByStatus(status));
        predicate = predicate.and(filterByName(name));
        predicate = predicate.and(filterByPhone(phone));
        predicate = predicate.and(filterByDateRange(startTime,endTime));

        return predicate;
    }

    private BooleanExpression filterByDateRange(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime == null && endTime == null) {
            return Expressions.TRUE;
        } else if (startTime == null) {
            return reservation.reservationTime.isNotNull().and(reservation.reservationTime.loe(endTime));
        } else if (endTime == null) {
            return reservation.reservationTime.isNotNull().and(reservation.reservationTime.goe(startTime));
        } else {
            return reservation.reservationTime.isNotNull().and(reservation.reservationTime.between(startTime, endTime));
        }
    }
    private BooleanExpression filterByPhone(String phone) {
        return Optional.ofNullable(phone)
                .map(reservation.user.phone::contains)
                .orElse(Expressions.TRUE);
    }

    private BooleanExpression filterByName(String name) {
        return Optional.ofNullable(name)
                .map(reservation.user.name::contains)
                .orElse(Expressions.TRUE);
    }

    private BooleanExpression filterByStatus(ReservationStatus status) {
        return Optional.ofNullable(status)
                .map(reservation.status::eq)
                .orElse(Expressions.TRUE);
    }
}
