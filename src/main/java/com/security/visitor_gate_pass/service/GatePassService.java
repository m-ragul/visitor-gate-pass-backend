package com.security.visitor_gate_pass.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.security.visitor_gate_pass.dto.GatePassRequestDTO;
import com.security.visitor_gate_pass.model.GatePass;
import com.security.visitor_gate_pass.model.GatePassStatus;
import com.security.visitor_gate_pass.model.User;
import com.security.visitor_gate_pass.repository.GatePassRepository;
import com.security.visitor_gate_pass.repository.UserRepository;

@Service
public class GatePassService {

    private final GatePassRepository gatePassRepository;
    private final UserRepository userRepository;

    public GatePassService(GatePassRepository gatePassRepository,
                           UserRepository userRepository) {
        this.gatePassRepository = gatePassRepository;
        this.userRepository = userRepository;
    }

    // ================= USER =================

    public List<GatePass> getMyGatePasses(String username) {
        return gatePassRepository
                .findByUser_UsernameOrderByCreatedAtDesc(username);
    }

    public GatePass createGatePassRequest(String username, GatePassRequestDTO dto) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ❌ Past date check
        if (dto.getVisitDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Visit date cannot be in the past");
        }

        GatePass gatePass = new GatePass();
        gatePass.setUser(user);
        gatePass.setPurpose(dto.getPurpose());
        gatePass.setVisitDate(dto.getVisitDate());
        gatePass.setVisitTime(dto.getVisitTime());
        gatePass.setAdditionalDetails(dto.getAdditionalDetails());

        gatePass.setStatus(GatePassStatus.PENDING);
        gatePass.setQrData(null); // generated only after approval
        gatePass.setActive(true);
        gatePass.setCreatedAt(LocalDateTime.now());

        return gatePassRepository.save(gatePass);
    }



    // ================= ADMIN =================

    public List<GatePass> getPendingPasses() {
        return gatePassRepository.findByStatus(GatePassStatus.PENDING);
    }

    public GatePass approve(Long gatePassId, String adminUsername) {

        GatePass gatePass = gatePassRepository.findById(gatePassId)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        gatePass.setStatus(GatePassStatus.APPROVED);
        gatePass.setApprovedBy(adminUsername);
        gatePass.setApprovedAt(LocalDateTime.now());

        // 🔑 Generate QR ONLY on approval
        gatePass.setQrData("GATEPASS-" + UUID.randomUUID());

        return gatePassRepository.save(gatePass);
    }

    public GatePass reject(Long gatePassId, String adminUsername, String reason) {

        GatePass gatePass = gatePassRepository.findById(gatePassId)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        gatePass.setStatus(GatePassStatus.REJECTED);
        gatePass.setApprovedBy(adminUsername);
        gatePass.setApprovedAt(LocalDateTime.now());
        gatePass.setRejectionReason(reason);

        return gatePassRepository.save(gatePass);
    }
}