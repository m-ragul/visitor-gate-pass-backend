package com.security.visitor_gate_pass.service;

import com.security.visitor_gate_pass.model.EntryLog;
import com.security.visitor_gate_pass.model.GatePass;
import com.security.visitor_gate_pass.repository.EntryLogRepository;
import com.security.visitor_gate_pass.repository.GatePassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class GuardService {

    private final GatePassRepository gatePassRepository;
    private final EntryLogRepository entryLogRepository;

    public GuardService(GatePassRepository gatePassRepository,
            EntryLogRepository entryLogRepository) {
        this.gatePassRepository = gatePassRepository;
        this.entryLogRepository = entryLogRepository;
    }

    // 1) Scan QR & Generate OTP (Persisted in DB)
    public boolean validateQr(String qrData) {
        Optional<GatePass> gp = gatePassRepository.findByQrData(qrData);

        if (gp.isPresent()) {
            GatePass gatePass = gp.get();

            try {
                // Verify User existence early to avoid crashes later
                if (gatePass.getUser() == null || gatePass.getUser().getUsername() == null) {
                    System.out.println("❌ Scan rejected. Corrupted GatePass (User missing). ID: " + gatePass.getId());
                    return false;
                }
            } catch (Exception e) {
                System.out.println("❌ Scan rejected. Corrupted GatePass (User lookup failed). ID: " + gatePass.getId());
                return false;
            }

            // 🛡️ VALIDATION: Only allow APPROVED passes
            if (gatePass.getStatus() != com.security.visitor_gate_pass.model.GatePassStatus.APPROVED) {
                System.out.println("❌ Scan rejected. Status: " + gatePass.getStatus());
                return false;
            }

            generateOtpForPass(gatePass);
            return true;
        }
        return false;
    }

    // 2) Generate OTP (Persisted)
    private void generateOtpForPass(GatePass gatePass) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        gatePass.setCurrentOtp(otp);
        gatePass.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // Valid for 5 mins
        gatePassRepository.save(gatePass);

        System.out.println("🔐 OTP FOR GUARD VERIFICATION (" + gatePass.getUser().getUsername() + "): " + otp);
    }

    // 3) Verify OTP + mark ENTRY/EXIT
    public boolean verifyOtpAndLog(String qrData, String otp, String action) {
        GatePass gatePass = gatePassRepository.findByQrData(qrData)
                .orElse(null);

        if (gatePass == null)
            return false;

        // check if OTP matches and is not expired
        if (gatePass.getCurrentOtp() == null || !gatePass.getCurrentOtp().equals(otp)) {
            return false;
        }

        if (gatePass.getOtpExpiry() != null && gatePass.getOtpExpiry().isBefore(LocalDateTime.now())) {
            System.out.println("❌ OTP Expired");
            return false;
        }

        // Consume OTP
        gatePass.setCurrentOtp(null);
        gatePass.setOtpExpiry(null);

        // 🛡️ LIFECYCLE MANAGEMENT
        if ("EXIT".equalsIgnoreCase(action)) {
            gatePass.setStatus(com.security.visitor_gate_pass.model.GatePassStatus.COMPLETED);
            gatePass.setActive(false);
        }

        gatePassRepository.save(gatePass);

        EntryLog log = new EntryLog();
        log.setAction(action);
        log.setGatePass(gatePass);
        entryLogRepository.save(log);

        return true;
    }
}