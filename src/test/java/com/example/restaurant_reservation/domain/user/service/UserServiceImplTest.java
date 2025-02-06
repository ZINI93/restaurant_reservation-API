package com.example.restaurant_reservation.domain.user.service;

import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.user.dto.UserRequestDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserUpdateDto;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import com.example.restaurant_reservation.domain.user.repository.UserRepositoryImpl;
import org.apache.coyote.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;

    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserServiceImpl userService;

    UserRequestDto userRequestDto;
    User user;


    @BeforeEach
    void setup() {



        user = new User(
                "zini1234",
                "1234",
                "Jinhee",
                "qw@gmail.com",
                "080-1234-1234",
                UserRole.USER);


        userRequestDto = new UserRequestDto(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                UserRole.USER);
    }

    @Test
    void createUser() {
        //given
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("1234");
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        UserResponseDto result = userService.createUser(userRequestDto);

        //then
        assertNotNull(result);
        assertEquals("zini1234",result.getUsername());
        assertEquals("Jinhee",result.getName());
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void findById() {

        Long userId = 1L;

        //given
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        //when
        UserResponseDto result = userService.findById(userId);

        //then

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername()); // id검색 확인 필요
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    void searchUser() {
        //given
        PageRequest pageable = PageRequest.of(0, 10);

        String username = "qwas111";
        String name = "zini";
        String phoneNumber = "080-1111-1111";

        Page<UserResponseDto> mockUserPage = mock(Page.class);
        when(userRepository.searchUser(eq(username),eq(name),eq(phoneNumber),eq(pageable))).thenReturn(mockUserPage);



        //when
        Page<UserResponseDto> result = userService.SearchUser(username,name,phoneNumber,pageable);

        //then

        assertNotNull(result);
        assertSame(mockUserPage,result);
        verify(userRepository, times(1)).searchUser(eq(username),eq(name),eq(phoneNumber),eq(pageable));
    }

    @Test
    void updateUser() {

        //given
        UserUpdateDto updateUser = UserUpdateDto.builder()
                .password("789789789")
                .name("kaka")
                .email("kakao@gmail.com")
                .phone("080-1111-1111")
                .build();


        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        //when
        UserResponseDto result = userService.updateUser(1L, updateUser);


        //then
        assertNotNull(result);
        assertEquals(updateUser.getName(),result.getName());
        assertEquals(updateUser.getPhone(),result.getPhone());
    }

    @Test
    void deleteUser() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        //when
        userService.deleteUser(1L);

        //then
        verify(userRepository,times(1)).delete(user);
    }
}