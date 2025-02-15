package com.example.restaurant_reservation.domain.reservation.service;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.reservation.repository.ReservationRepository;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.restaurantTable.repository.RestaurantTableRepository;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import com.example.restaurant_reservation.domain.user.repository.UserRepositoryImpl;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RestaurantTableRepository restaurantTableRepository;
    @Mock private ReservationRepository reservationRepository;
    @InjectMocks private ReservationServiceImpl reservationService;

    ReservationRequestDto requestDto;
    Reservation reservation;

    private void setUserId(User user, Long id)throws Exception{
        Field field = user.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(user,id);
    }

    // ID 강제로 주입
    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    @BeforeEach
    void setup() throws Exception {

        User user = User.builder().build();
        setId(user,1L);


        RestaurantTable table = RestaurantTable.builder().build();
        setId(table,1L);


        reservation = new Reservation(
                user,
                table,
                LocalDateTime.now().minusDays(2),
                5,
                ReservationStatus.COMPLETED
        );
        setId(reservation, 1L);

        requestDto =new ReservationRequestDto(
                table.getId(),
                reservation.getReservationTime(),
                reservation.getNumPeople(),
                ReservationStatus.COMPLETED
        );
    }

    @Test
    void createReservation() {
        //given
        User user = User.builder().build();
        RestaurantTable restaurantTable = RestaurantTable.builder().build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(restaurantTable));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        //when

        ReservationResponseDto result = reservationService.createReservation(requestDto,user.getId());


        //then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getRestaurantTableId());
        assertEquals(5, result.getNumPeople());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void findById() {
        //given
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        //when
        ReservationResponseDto result = reservationService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(5,result.getNumPeople());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void searchReservation() {
        //given
        PageRequest pageable = PageRequest.of(0, 10);

        String name = "qwas11";
        String phone = "080-1111-1111";
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        String sortField = "createAt";
        ReservationStatus status = ReservationStatus.COMPLETED;


        Page<ReservationResponseDto> mockReservationPage = mock(Page.class);
        when(reservationRepository.reservationSearch(contains(name),contains(phone),eq(startTime),eq(endTime),eq(sortField),eq(status),eq(pageable))).thenReturn(mockReservationPage);

        //when
        Page<ReservationResponseDto> result = reservationService.searchReservation(name, phone, startTime, endTime, sortField, status, pageable);

        //then
        assertNotNull(result);
        assertSame(mockReservationPage,result);

        verify(reservationRepository, times(1)).reservationSearch(contains(name),contains(phone),eq(startTime),eq(endTime),eq(sortField),eq(status),eq(pageable));

    }

    @Test
    void updateReservation() throws Exception {
        //given
        RestaurantTable restaurantTable = RestaurantTable.builder().build();
        setId(restaurantTable,2L);

        ReservationUpdateDto updateReservation = ReservationUpdateDto.builder()
                .restaurantTableId(2L)
                .reservationTime(LocalDateTime.now().plusDays(5))
                .numPeople(3)
                .status(ReservationStatus.CANCELLED)
                .build();


        when(restaurantTableRepository.findById(2L)).thenReturn(Optional.ofNullable(restaurantTable));
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));

        //when
        ReservationResponseDto result = reservationService.updateReservation(1L, updateReservation);

        //then
        assertNotNull(result);
        assertEquals(updateReservation.getRestaurantTableId(),result.getRestaurantTableId());
        assertEquals(3,result.getNumPeople());

        verify(reservationRepository,times(1)).findById(1L);

    }

    @Test
    void deleteReservation() {
        //given
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        //when
        reservationService.deleteReservation(1L);

        //then
        verify(reservationRepository,times(1)).delete(reservation);
    }
}