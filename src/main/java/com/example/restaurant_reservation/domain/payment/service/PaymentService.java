package com.example.restaurant_reservation.domain.payment.service;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto createPayment(Long userId, PaymentRequestDto requestDto);
    PaymentResponseDto findById(Long userId);
    PaymentResponseDto findByUuid(String uuid);
    List<PaymentResponseDto> findByOwnerId(Long userId);
    Page<PaymentResponseDto> paymentSearch(Long reservationId, PaymentStatus status , Pageable pageable);
    PaymentResponseDto updatePayment(Long paymentId, PaymentUpdateDto updateDto);
    void deletePayment(String uuid);
}
