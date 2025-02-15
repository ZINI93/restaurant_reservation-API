package com.example.restaurant_reservation.domain.payment.service;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.entity.Payment;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.payment.repository.PaymentRepository;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.example.restaurant_reservation.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    // お支払い作成
    @Override @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {

        Reservation reservation = reservationRepository.findById(requestDto.getReservationId())
                .orElseThrow(() ->  new IllegalArgumentException("予約IDが見つかりません。"));

        Payment savedPayment = Payment.builder()
                .reservation(reservation)
                .amount(requestDto.getAmount())
                .paymentMethod(requestDto.getPaymentMethod())
                .status(PaymentStatus.COMPLETED)
                .build();

        return paymentRepository.save(savedPayment).toResponse();
    }

    //　お支払いIDを検索
    @Override
    public PaymentResponseDto findById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("お支払いIDが見つかりません。"));

        return payment.toResponse();
    }

    // お支払い（予約番号、ステータス）で検索
    @Override
    public Page<PaymentResponseDto> paymentSearch(Long reservationId, PaymentStatus status, Pageable pageable) {
        return paymentRepository.paymentSearch(reservationId,status,pageable);
    }

    //　お支払いをアップデート
    @Override @Transactional
    public PaymentResponseDto updatePayment(Long paymentId, PaymentUpdateDto updateDto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("お支払いIDが見つかりません。"));

        payment.UpdateInfo(updateDto.getAmount(),
                updateDto.getPaymentMethod(),
                updateDto.getStatus());

        return payment.toResponse();
    }

    //お支払い削除
    @Override @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("お支払いIDが見つかりません。"));

        paymentRepository.delete(payment);
    }
}
