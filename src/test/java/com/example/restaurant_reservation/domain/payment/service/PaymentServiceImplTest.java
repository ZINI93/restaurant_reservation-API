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
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock PaymentRepository paymentRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock UserRepository userRepository;
    @InjectMocks PaymentServiceImpl paymentService;

    Payment payment;

    PaymentRequestDto requestDto;

    Reservation reservation;

    User user;

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity,id);
    }
    @BeforeEach
    void setup() throws Exception {

        user = User.builder().build();
        setId(user,1L);


        reservation = Reservation.builder().user(user).build();
        setId(reservation,1L);

        payment = new Payment(
                reservation,
                new BigDecimal(10.10),
                PaymentMethod.CASH,
                PaymentStatus.COMPLETED,
                1L,
                UUID.randomUUID().toString()
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

        Long ownerId = user.getId();

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.ofNullable(reservation));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);


        //when
        PaymentResponseDto result = paymentService.createPayment(ownerId,requestDto);

        //then
        assertNotNull(result);
        assertEquals(payment.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(payment.getStatus(),result.getStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));

    }

//    @Test
//    void findByOwnerId() {
//        //given
//
//        when(paymentRepository.findByOwnerId())
//
//        //when
//        paymentService.findByOwnerId(user.getId());
//
//        //then
//        assertNotNull(result);
//        assertEquals(payment.getPaymentMethod(),result.getPaymentMethod());
//
//        verify(paymentRepository, times(1)).findByOwnerId(1L);
//    }

    @Test
    void paymentSearch() {
        //given
        PageRequest pageable = PageRequest.of(0, 10);

        Long reservationId =1L;
        PaymentStatus status = PaymentStatus.COMPLETED;

        Page<PaymentResponseDto> mockPaymentPage = mock(Page.class);
        when(paymentRepository.paymentSearch(eq(reservationId),eq(status),eq(pageable))).thenReturn(mockPaymentPage);

        //when
        Page<PaymentResponseDto> result = paymentService.paymentSearch(reservationId, status, pageable);

        //then
        assertNotNull(result);
        assertSame(mockPaymentPage, result);

        verify(paymentRepository, times(1)).paymentSearch(eq(reservationId),eq(status),eq(pageable));
    }

    @Test
    void updatePayment() {
        //given
        PaymentUpdateDto updateDto = PaymentUpdateDto.builder()
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .amount(new BigDecimal(100.11))
                .status(PaymentStatus.REFUNDED)
                .build();

        when(paymentRepository.findByOwnerId(1L)).thenReturn(Optional.ofNullable(payment));
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

        String uuid = UUID.randomUUID().toString();

        when(paymentRepository.findByPaymentUuid(uuid)).thenReturn(Optional.ofNullable(payment));

        //when
        paymentService.deletePayment(uuid);

        //then

        verify(paymentRepository, times(1)).delete(payment);
    }
}