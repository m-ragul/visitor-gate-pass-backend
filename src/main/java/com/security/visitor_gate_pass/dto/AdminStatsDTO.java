package com.security.visitor_gate_pass.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatsDTO {
    private long totalVisitorsToday;
    private long currentVisitorsInside;
    private long totalPassesGenerated; // Total lifetime
    private long pendingApprovals;
}
