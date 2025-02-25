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
import com.example.restaurant_reservation.domain.reservation.repository.ReservationRepositoryCustom;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.restaurantTable.repository.RestaurantTableRepository;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;


    /**
     * 予約を作成する
     */
    @Override @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto,Long userId) {

        log.info("Creating reservation for user ID: {}", userId);
        //controllerからもらったユーザを使う
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDが見つかりません。"));

        RestaurantTable restaurantTable = restaurantTableRepository.findById(reservationRequestDto.getRestaurantTableId())
                .orElseThrow(() -> new IllegalArgumentException("restaurantTableIdが見つかりません。"));

        // 予約時間が現在の時刻より後かチェック
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (reservationRequestDto.getReservationTime().isBefore(now)){
            throw new IllegalArgumentException("予約時間は現在の時刻より後に設定してください。");
        }
        // 予約時間を1時間単位に制限
        LocalDateTime requestedTime = reservationRequestDto.getReservationTime().truncatedTo(ChronoUnit.HOURS);
        if (!reservationRequestDto.getReservationTime().equals(requestedTime)){
            throw new IllegalArgumentException("予約時間は1時間単位で設定してください。");
        }

        //人数がテーブルの席より多くならないようにチェック
        if (restaurantTable == null || reservationRequestDto.getNumPeople() > restaurantTable.getCapacity()){
            throw new IllegalArgumentException("予約席をご確認をお願いします。");
        }

        // 同じ時間帯に予約があるかチェック
        boolean isAlreadyReserved = reservationRepository.existsByRestaurantTableIdAndReservationTime(restaurantTable.getId(), requestedTime);
        if (isAlreadyReserved){
            throw new IllegalStateException("この時間帯にはすでに予約が入っています。");
        }

        Reservation savedReservation = Reservation.builder()
                .user(user)
                .restaurantTable(restaurantTable)
                .reservationTime(requestedTime)
                .numPeople(reservationRequestDto.getNumPeople())
                .status(ReservationStatus.COMPLETED)
                .build();


        log.info("Created reservation:{}", savedReservation);

        return reservationRepository.save(savedReservation).toResponse();
    }

    /**
     * 管理者ー予約作成
     */
    @Override @Transactional
    public ReservationResponseDto AdminCreateReservation(AdminReservationRequestDto reservationRequestDto) {
        //controllerからもらったユーザを使う
        User user = userRepository.findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDが見つかりません。"));

        RestaurantTable restaurantTable = restaurantTableRepository.findById(reservationRequestDto.getRestaurantTableId())
                .orElseThrow(() -> new IllegalArgumentException("restaurantTableIdが見つかりません。"));

        // 予約時間が現在の時刻より後かチェック
        LocalDateTime now = reservationRequestDto.getReservationTime().truncatedTo(ChronoUnit.MINUTES);
        if (reservationRequestDto.getReservationTime().isBefore(now)){
            throw new IllegalArgumentException("予約時間は現在の時刻より後に設定してください。");
        }

        // 予約時間を1時間単位に制限
        LocalDateTime requestedTime = reservationRequestDto.getReservationTime().truncatedTo(ChronoUnit.HOURS);
        if (reservationRequestDto.getReservationTime().equals(requestedTime)){
            throw new IllegalArgumentException("予約時間は1時間単位で設定してください。");
        }

        //人数がテーブルの席より多くならないようにチェック
        if (restaurantTable == null || reservationRequestDto.getNumPeople() > restaurantTable.getCapacity()){
            throw new IllegalArgumentException("予約席をご確認をお願いします。");
        }

        // 同じ時間帯に予約があるかチェック
        boolean isAlreadyReserved = reservationRepository.existsByRestaurantTableIdAndReservationTime(restaurantTable.getId(), requestedTime);

        if (isAlreadyReserved) {
            throw new IllegalArgumentException("この時間帯にはすでに予約が入っています。");
        }

        Reservation savedReservation = Reservation.builder()
                .user(user)
                .restaurantTable(restaurantTable)
                .reservationTime(reservationRequestDto.getReservationTime())
                .numPeople(reservationRequestDto.getNumPeople())
                .status(ReservationStatus.COMPLETED)
                .build();

        log.info("Created Reservation:{}",savedReservation);

        return reservationRepository.save(savedReservation).toResponse();
    }


    // 予約IDで予約を検索
    @Override
    public ReservationResponseDto findById(Long userId) {
        Reservation reservation = reservationRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当する予約が見つかりません。"));

        if ( reservation.getUser().getId() == null || !Objects.equals(reservation.getUser().getId(), userId)){
            throw new AccessDeniedException("指定されたIDに対するアクセス権がありません。");
        }
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

    @Override
    public ReservationResponseDto findByUuid(String uuid) {
        Reservation reservation = reservationRepository.findByReservationUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当するお支払いが見つかりません。"));

        return reservation.toResponse();
    }

    @Override
    public Page<ReservationResponseDto> findAllByUserId(Long userId, Pageable pageable) {

        log.info("Searching reservation for user Id :{}",userId);

        Page<Reservation> reservations = reservationRepository.findAllByUserId(userId, pageable);

        if (reservations.isEmpty()){
            throw new IllegalArgumentException("予約 ID が見つかりません。");
        }

        return reservations.map(Reservation::toResponse);

    }

    // 予約(名前、電話番号、予約時間、予約のステータスで予約）検索
    @Override
    public Page<ReservationResponseDto> searchReservation(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, String sortField, ReservationStatus status, Pageable pageable) {
        return reservationRepository.reservationSearch(name, phone, startTime, endTime,sortField,status,pageable);
    }

    // 予約をアップデート
    @Override @Transactional
    public ReservationResponseDto updateReservation(String userUuid, String reservationUuid, ReservationUpdateDto updateDto) {

        // 指定された予約IDの予約情報を取得
        Reservation reservation = reservationRepository.findByReservationUuid(reservationUuid)
                .orElseThrow(() -> new IllegalArgumentException("指定された予約IDの予約が見つかりません。"));

        //ユーザーが存在しているかCHECK
        if (reservation.getUser() == null || !Objects.equals(reservation.getUser().getUserUuid(), userUuid)){
            throw new AccessDeniedException("指定されたIDに対するアクセス権がありません。");
        }

        //予約のテーブルをidをCHECK
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
    public void deleteReservation(String uuid) {
        Reservation reservation = reservationRepository.findByReservationUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーIDに該当するお支払いが見つかりません。"));


        reservationRepository.delete(reservation);
    }
}
