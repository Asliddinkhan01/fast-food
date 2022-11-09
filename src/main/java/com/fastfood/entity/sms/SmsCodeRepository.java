package com.fastfood.entity.sms;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    Optional<SmsCode> findByPhoneNumber(String phoneNumber);
}
