package com.fastfood.entity.admin;


import com.fastfood.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.domain}/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("makeChef/{phoneNumber}")
    public HttpEntity<?> make_UnmakeChef(@PathVariable String phoneNumber) {
        return adminService.changeRole(phoneNumber, AppConstants.CHEF);
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/makeDeliverer/{phoneNumber}")
    public HttpEntity<?> make_UnmakeDeliverer(@PathVariable String phoneNumber) {
        return adminService.changeRole(phoneNumber, AppConstants.DELIVERER);
    }


}
