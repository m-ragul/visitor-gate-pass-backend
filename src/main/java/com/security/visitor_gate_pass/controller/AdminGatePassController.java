package com.security.visitor_gate_pass.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.visitor_gate_pass.dto.RejectGatePassRequest;
import com.security.visitor_gate_pass.model.GatePass;
import com.security.visitor_gate_pass.service.GatePassService;

@RestController
@RequestMapping("/api/admin/gate-passes")
public class AdminGatePassController {

    private final GatePassService gatePassService;

    public AdminGatePassController(GatePassService gatePassService) {
        this.gatePassService = gatePassService;
    }

    // 1️⃣ View all pending requests
    @GetMapping("/pending")
    public List<GatePass> getPending() {
        return gatePassService.getPendingPasses();
    }

    // 2️⃣ Approve
    @PostMapping("/{id}/approve")
    public GatePass approve(@PathVariable Long id, Authentication auth) {
        return gatePassService.approve(id, auth.getName());
    }

    // 3️⃣ Reject
    @PostMapping("/{id}/reject")
    public GatePass reject(
            @PathVariable Long id,
            @RequestBody RejectGatePassRequest request,
            Authentication auth
    ) {
        return gatePassService.reject(id, auth.getName(), request.getReason());
    }
}