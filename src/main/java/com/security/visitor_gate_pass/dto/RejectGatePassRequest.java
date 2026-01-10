package com.security.visitor_gate_pass.dto;

import lombok.Data;

@Data
public class RejectGatePassRequest {
    private String reason;
}