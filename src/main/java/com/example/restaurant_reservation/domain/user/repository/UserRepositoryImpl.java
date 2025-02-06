package com.example.restaurant_reservation.domain.user.repository;

import com.example.restaurant_reservation.domain.user.dto.QUserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.restaurant_reservation.domain.user.entity.QUser.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<UserResponseDto> searchUser(String username, String name, String phone, Pageable pageable) {
        //　List<MemberResponseDto> content 조회 부분
        List<UserResponseDto> content = queryFactory
                .select(new QUserResponseDto(
                        user.id.as("userId"),
                        user.username,
                        user.password,
                        user.name,
                        user.email,
                        user.phone
                )).from(user)
                .where(usernameEq(username),
                        phoneCo(phone),
                        nameCo(name))
                .orderBy(user.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total 카운트 조회 부분
        Long total = Optional.ofNullable(queryFactory
                .select(user.id.count())
                .from(user)
                .where(phoneCo(phone),
                        nameCo(name))
//                .orderBy(user.id.desc())  불필요하게 중복되고, count 쿼리는 정렬 순서가 의미가 없음
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content,pageable,total);

    }

    private BooleanExpression nameCo(String name) {

//        if (name == null){
//            return Expressions.TRUE;
//        }
//        return user.name.contains(name);
//    }

        return Optional.ofNullable(name)
                .map(user.name::contains)
                .orElse(Expressions.TRUE);
    }

    private BooleanExpression phoneCo(String phone) {
        return Optional.ofNullable(phone)
                .map(user.phone::contains)
                .orElse(Expressions.TRUE);
    }

        private BooleanExpression usernameEq(String username) {
            return Optional.ofNullable(username)
                    .map(user.username::contains)
                    .orElse(Expressions.TRUE);
        }
}
