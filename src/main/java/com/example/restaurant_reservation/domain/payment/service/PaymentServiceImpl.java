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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    /**
     * お支払い作成
     */
    @Override @Transactional
    public PaymentResponseDto createPayment(Long userId, PaymentRequestDto requestDto) {

        log.info("Creating payment for user ID : {}", userId);


        // 予約情報を検索
        Reservation reservation = reservationRepository.findById(requestDto.getReservationId())
                .orElseThrow(() ->  new IllegalArgumentException("予約IDが見つかりません。"));

        // ユーザーIDが一致するか確認
        if (reservation.getUser().getId() == null || !Objects.equals(reservation.getUser().getId(),userId)){
            throw new AccessDeniedException("指定されたIDに対するアクセス権がありません。");
        }

        //お支払い情報を作成
        Payment savedPayment = Payment.builder()
                .reservation(reservation)
                .amount(requestDto.getAmount())
                .paymentMethod(requestDto.getPaymentMethod())
                .status(PaymentStatus.COMPLETED)
                .ownerId(userId)
                .build();

        log.info("Created Payment :{}", savedPayment);

        return paymentRepository.save(savedPayment).toResponse();
    }

    /**
     * お支払いIDを検索
     */

    @Override
    public PaymentResponseDto findById(Long userId) {
        Payment payment = paymentRepository.findByOwnerId(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当するお支払いが見つかりません。"));

        // ユーザーIDが一致するか確認
        if (payment.getOwnerId() == null || !Objects.equals(payment.getOwnerId(),userId)){
            throw new AccessDeniedException("指定されたIDに対するアクセス権がありません。");
        }

        return payment.toResponse();
    }

    @Override
    public PaymentResponseDto findByUuid(String uuid) {
        Payment payment = paymentRepository.findByPaymentUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーUUIDに該当するお支払いが見つかりません。"));

        return payment.toResponse();
    }

    /**
     * ユーザーの支払いの情報を照会
     */
    @Override
    public List<PaymentResponseDto> findByOwnerId(Long userId) {

        log.info("Searching payment for user Id: {}", userId);

        List<Payment> payments = paymentRepository.findAllByOwnerId(userId);

        if (payments.isEmpty()){
            throw  new IllegalArgumentException("お支払いの情報がありません。");
        }

        return payments.stream()
                .map(Payment::toResponse)
                .collect(Collectors.toList());
    }


    /**
     * お支払い（予約番号、ステータス）で検索
     */

    @Override
    public Page<PaymentResponseDto> paymentSearch(Long reservationId, PaymentStatus status, Pageable pageable) {
        return paymentRepository.paymentSearch(reservationId,status,pageable);
    }


    /**
     * お支払いをアップデート
     */
    @Override @Transactional
    public PaymentResponseDto updatePayment(Long userId, PaymentUpdateDto updateDto) {

        log.info("updating Payment for user ID :{}",userId);
        Payment payment = paymentRepository.findByOwnerId(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当するお支払いが見つかりません。"));

        // ユーザーIDが一致するか確認
        if (payment.getOwnerId() == null || !Objects.equals(payment.getOwnerId(), userId)){
            throw new AccessDeniedException("指定されたIDに対するアクセス権がありません。");
        }

        payment.updateInfo(updateDto.getAmount(),
                updateDto.getPaymentMethod(),
                updateDto.getStatus());

        log.info("updated payment for user ID {}:{}", userId, payment);
        return payment.toResponse();
    }

    /**
     *  お支払い削除
     */
    @Override @Transactional
    public void deletePayment(String uuid) {

        log.info("deleting payment for user Id:{}",uuid);

        Payment payment = paymentRepository.findByPaymentUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当するお支払いが見つかりません。"));

        

        paymentRepository.delete(payment);
        log.info("Successfully deleted payment for user ID: {}", uuid);
    }
}
