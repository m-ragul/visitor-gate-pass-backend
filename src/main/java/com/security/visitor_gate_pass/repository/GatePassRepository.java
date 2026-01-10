package com.security.visitor_gate_pass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.visitor_gate_pass.model.GatePass;
import com.security.visitor_gate_pass.model.GatePassStatus;

public interface GatePassRepository extends JpaRepository<GatePass, Long> {

    List<GatePass> findByUser_UsernameOrderByCreatedAtDesc(String username);

    // 🔑 ADMIN
    List<GatePass> findByStatus(GatePassStatus status);

    Optional<GatePass> findByQrData(String qrData);
}
