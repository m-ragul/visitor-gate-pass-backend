package com.security.visitor_gate_pass.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.visitor_gate_pass.service.GuardService;

@RestController
@RequestMapping("/api/guard")
public class GuardController {

    private final GuardService guardService;

    public GuardController(GuardService guardService) {
        this.guardService = guardService;
    }

    // Scan QR
    @PostMapping("/scan")
    public String scanQr(@RequestParam String qrData) {
        return guardService.validateQr(qrData)
                ? "QR VALID. OTP GENERATED."
                : "INVALID QR";
    }

    // Verify OTP + log entry/exit
    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String qrData,
            @RequestParam String otp,
            @RequestParam String action) {
        System.out.println("🔔 VERIFY REQUEST: QR=" + qrData + ", OTP=" + otp + ", ACTION=" + action);

        return guardService.verifyOtpAndLog(qrData, otp, action)
                ? action + " LOGGED SUCCESSFULLY"
                : "OTP INVALID";
    }
}