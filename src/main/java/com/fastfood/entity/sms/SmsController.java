package com.fastfood.entity.sms;




import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${app.domain}/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @GetMapping("/send/forRegister/{phoneNumber}")
    public HttpEntity<?> sendSmsForUserRegistration(@PathVariable String phoneNumber) {
        return smsService.sendSmsForUserRegistration(phoneNumber);
    }

    @GetMapping("/send/forLogin/{phoneNumber}")
    public HttpEntity<?> sendSmsForUserLogin(@PathVariable String phoneNumber) {
        return smsService.sendSmsForUserLogin(phoneNumber);
    }

    @PostMapping("/validate/forRegister")
    public HttpEntity<?> validateSmsForUserRegistration(@Valid @RequestBody SmsDto smsDto) {
        return smsService.validateSmsForUserRegistration(smsDto);
    }


}
