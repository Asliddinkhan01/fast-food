package com.fastfood.entity.sms;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "sms_codes")
public class SmsCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private int code;

    private LocalDateTime expirationDate;

    public SmsCode(String phoneNumber, int code, LocalDateTime expirationDate) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expirationDate = expirationDate;
    }
}
