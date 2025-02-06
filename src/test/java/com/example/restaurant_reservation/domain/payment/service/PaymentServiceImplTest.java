package com.example.restaurant_reservation.domain.payment.service;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.entity.Payment;
import com.example.restaurant_reservation.domain.payment.entity.PaymentMethod;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.payment.repository.PaymentRepository;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.example.restaurant_reservation.domain.reservation.repository.ReservationRepository;
import org.hibernate.sql.ast.tree.expression.Any;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock PaymentRepository paymentRepository;
    @Mock ReservationRepository reservationRepository;
    @InjectMocks PaymentServiceImpl paymentService;

    Payment payment;

    PaymentRequestDto requestDto;

    Reservation reservation;

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity,id);
    }
    @BeforeEach
    void setup() throws Exception {

        reservation = Reservation.builder().build();
        setId(reservation,1L);

        payment = new Payment(
                reservation,
                new BigDecimal(10.10),
                PaymentMethod.CASH,
                PaymentStatus.COMPLETED
        );

        requestDto = PaymentRequestDto.builder()
                .reservationId(reservation.getId())
                .amount(payment.getAmount())
                .paymentMethod(PaymentMethod.CASH)
                .status(PaymentStatus.COMPLETED)
                .build();
        }

    @Test
    void createPayment() {
        //given
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        //when
        PaymentResponseDto result = paymentService.createPayment(requestDto);

        //then
        assertNotNull(result);
        assertEquals(payment.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(payment.getStatus(),result.getStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));

    }

    @Test
    void findById() {
        //given
        when(paymentRepository.findById(1L)).thenReturn(Optional.ofNullable(payment));

        //when

        PaymentResponseDto result = paymentService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(payment.getPaymentMethod(),result.getPaymentMethod());

        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void paymentSearch() {
        //given
        //when
        //then
    }

    @Test
    void updatePayment() {
        //given
        PaymentUpdateDto updateDto = PaymentUpdateDto.builder()
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .amount(new BigDecimal(100.11))
                .status(PaymentStatus.REFUNDED)
                .build();

        when(paymentRepository.findById(1L)).thenReturn(Optional.ofNullable(payment));
        //when
        PaymentResponseDto result = paymentService.updatePayment(1L, updateDto);
        //then
        assertNotNull(result);
        assertEquals(PaymentMethod.CREDIT_CARD, result.getPaymentMethod());
        assertEquals(new BigDecimal(100.11), result.getAmount());
    }

    @Test
    void deletePayment() {
        //given
        when(paymentRepository.findById(1L)).thenReturn(Optional.ofNullable(payment));

        //when
        paymentService.deletePayment(1L);

        //then

        verify(paymentRepository, times(1)).delete(payment);
    }
}