package com.example.restaurant_reservation.domain.reservation.service;

import com.example.restaurant_reservation.domain.payment.entity.Payment;
import com.example.restaurant_reservation.domain.payment.repository.PaymentRepository;
import com.example.restaurant_reservation.domain.reservation.dto.AdminReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.reservation.repository.ReservationRepository;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.restaurantTable.repository.RestaurantTableRepository;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.desktop.PrintFilesEvent;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;


    // 予約を作成する
    @Override @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto,Long userId) {

        //controllerからもらったユーザを使う
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDが見つかりません。"));

        RestaurantTable restaurantTable = restaurantTableRepository.findById(reservationRequestDto.getRestaurantTableId())
                .orElseThrow(() -> new IllegalArgumentException("restaurantTableIdが見つかりません。"));

        Reservation savedReservation = Reservation.builder()
                .user(user)
                .restaurantTable(restaurantTable)
                .reservationTime(reservationRequestDto.getReservationTime())
                .numPeople(reservationRequestDto.getNumPeople())
                .status(ReservationStatus.COMPLETED)
                .build();

        return reservationRepository.save(savedReservation).toResponse();
    }

    @Override
    public ReservationResponseDto AdminCreateReservation(AdminReservationRequestDto reservationRequestDto) {
        //controllerからもらったユーザを使う
        User user = userRepository.findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDが見つかりません。"));

        RestaurantTable restaurantTable = restaurantTableRepository.findById(reservationRequestDto.getRestaurantTableId())
                .orElseThrow(() -> new IllegalArgumentException("restaurantTableIdが見つかりません。"));

        Reservation savedReservation = Reservation.builder()
                .user(user)
                .restaurantTable(restaurantTable)
                .reservationTime(reservationRequestDto.getReservationTime())
                .numPeople(reservationRequestDto.getNumPeople())
                .status(ReservationStatus.COMPLETED)
                .build();

        return reservationRepository.save(savedReservation).toResponse();
    }


    // 予約IDで予約を検索
    @Override
    public ReservationResponseDto findById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("予約 ID が見つかりません。"));
        return reservation.toResponse();
    }

    @Override
    public ReservationResponseDto findByPaymentId(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("支払いidがありません。"));

        // payment null check
        if (payment.getReservation() == null){
            throw new IllegalArgumentException("この支払いには対応する予約がありません。");
        }
        return payment.getReservation().toResponse();
    }

    // 予約(名前、電話番号、予約時間、予約のステータスで予約）検索
    @Override
    public Page<ReservationResponseDto> searchReservation(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, String sortField, ReservationStatus status, Pageable pageable) {
        return reservationRepository.reservationSearch(name, phone, startTime, endTime,sortField,status,pageable);
    }

    // 予約をアップデート
    @Override @Transactional
    public ReservationResponseDto updateReservation(Long reservationId, ReservationUpdateDto updateDto) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("予約 ID が見つかりません。"));

        RestaurantTable restaurantTable = restaurantTableRepository.findById(updateDto.getRestaurantTableId())
                .orElseThrow(() -> new IllegalArgumentException("レストランテーブル ID が見つかりません。"));


        reservation.updateInfo(restaurantTable,
                updateDto.getReservationTime(),
                updateDto.getNumPeople(),
                updateDto.getStatus());

        return reservation.toResponse();
    }

    // 予約を削除
    @Override @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("予約 ID が見つかりません。"));

        reservationRepository.delete(reservation);
    }
}
