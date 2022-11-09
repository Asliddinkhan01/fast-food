package com.fastfood.entity.sms;


import com.fastfood.common.ApiResponse;
import com.fastfood.entity.user.User;
import com.fastfood.entity.user.UserRepository;
import com.messagebird.MessageBirdClient;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsCodeRepository smsCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final MessageBirdClient messageBirdClient;

    public Integer sendSms(String phoneNumber) {

//      creating code
        int code = (int) ((Math.random() * (999999 - 100000)) + 100000);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(50);


//        sending sms VIA MESSAGEBIRD
        List<BigInteger> phones = new ArrayList<>();
        phones.add(new BigInteger(phoneNumber));

        try {
            MessageResponse response = messageBirdClient.sendMessage("FootZone", "Your code is: " + code, phones);
        } catch (UnauthorizedException | GeneralException ex) {
            ex.printStackTrace();
            return null;
        }

//      saving code to db
        Optional<SmsCode> optionalByPhoneNumber = smsCodeRepository.findByPhoneNumber(phoneNumber);
        if (optionalByPhoneNumber.isPresent()) {
            SmsCode smsCode = optionalByPhoneNumber.get();
            smsCode.setCode(code);
            smsCode.setExpirationDate(expirationDate);
            smsCodeRepository.save(smsCode);
        } else {
            smsCodeRepository.save(
                    new SmsCode(
                            phoneNumber,
                            code,
                            expirationDate
                    )
            );
        }
        return code;
    }

    public ApiResponse validateSmsCode(String phoneNumber, String code) {
        Optional<SmsCode> optionalByPhoneNumber = smsCodeRepository.findByPhoneNumber(phoneNumber);

        if (optionalByPhoneNumber.isEmpty()) return new ApiResponse("There is no code with this number", false);

        SmsCode smsCode = optionalByPhoneNumber.get();

        if (smsCode.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ApiResponse("This code is invalid", false);

        if (Integer.parseInt(code) == smsCode.getCode())
            return new ApiResponse("Success", true);

        return new ApiResponse("Wrong code", false);
    }

    public HttpEntity<?> sendSmsForUserRegistration(String phoneNumber) {

        boolean phoneNumberExists = userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists)
            return ResponseEntity.status(400).body(new ApiResponse("This phone number already exists", false));

        Integer smsCode = sendSms(phoneNumber);
        if (smsCode == null)
            return ResponseEntity.status(409).body(new ApiResponse("Something went wrong !!!, Please try again", false));
        return ResponseEntity.status(200).body(new ApiResponse("SMS successfully sent", true));

    }


    public HttpEntity<?> sendSmsForUserLogin(String phoneNumber) {

        boolean phoneNumberExists = userRepository.existsByPhoneNumber(phoneNumber);
        if (!phoneNumberExists)
            return ResponseEntity.status(404).body(new ApiResponse("User with this phone number does not exist", false));

        Integer smsCode = sendSms(phoneNumber);
        if (smsCode == null)
            return ResponseEntity.status(409).body(new ApiResponse("Something went wrong !!!, Please try again", false));

        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isPresent()) {
            optionalUser.get().setSmsCode(passwordEncoder.encode(smsCode.toString()));
            userRepository.save(optionalUser.get());
        }
        return ResponseEntity.status(200).body(new ApiResponse("SMS successfully sent", true, smsCode.toString()));
    }

    public HttpEntity<?> validateSmsForUserRegistration(SmsDto smsDto) {
        String codeSent = smsDto.getCodeSent();
        ApiResponse apiResponse = validateSmsCode(smsDto.getPhoneNumber(), codeSent);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

}
