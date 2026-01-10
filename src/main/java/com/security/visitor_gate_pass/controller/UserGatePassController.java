package com.security.visitor_gate_pass.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.security.visitor_gate_pass.dto.GatePassRequestDTO;
import com.security.visitor_gate_pass.model.GatePass;
import com.security.visitor_gate_pass.service.GatePassService;

@RestController
@RequestMapping("/api/user")
public class UserGatePassController {

    private final GatePassService gatePassService;

    public UserGatePassController(GatePassService gatePassService) {
        this.gatePassService = gatePassService;
    }

    /**
     * 🔐 USER → Get all my gate passes
     * GET /api/user/gate-passes
     */
    @GetMapping("/gate-passes")
    public List<GatePass> getMyGatePasses(Authentication auth) {
        return gatePassService.getMyGatePasses(auth.getName());
    }

    /**
     * 🔐 USER → Request new gate pass
     * POST /api/user/gate-passes
     */
    @PostMapping("/gate-passes")
    public ResponseEntity<GatePass> requestGatePass(
            Authentication auth,
            @Valid @RequestBody GatePassRequestDTO dto
    ) {

        GatePass created = gatePassService.createGatePassRequest(
                auth.getName(),
                dto
        );

        return ResponseEntity.ok(created);
    }
}