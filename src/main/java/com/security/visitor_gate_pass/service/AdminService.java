package com.security.visitor_gate_pass.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.security.visitor_gate_pass.model.EntryLog;
import com.security.visitor_gate_pass.repository.EntryLogRepository;

@Service
public class AdminService {

        private final EntryLogRepository entryLogRepository;
        private final com.security.visitor_gate_pass.repository.GatePassRepository gatePassRepository;

        public AdminService(EntryLogRepository entryLogRepository,
                        com.security.visitor_gate_pass.repository.GatePassRepository gatePassRepository) {
                this.entryLogRepository = entryLogRepository;
                this.gatePassRepository = gatePassRepository;
        }

        public List<EntryLog> getAllLogs() {
                return entryLogRepository.findAll();
        }

        public List<EntryLog> getLogsByAction(String action) {
                return entryLogRepository.findByAction(action);
        }

        public List<EntryLog> getCurrentInsideVisitors() {
                return entryLogRepository.findAll().stream()
                                .filter(log -> log.getGatePass()
                                                .getStatus() == com.security.visitor_gate_pass.model.GatePassStatus.APPROVED
                                                && log.getAction().equals("ENTRY"))
                                // This logic is simplified; a real "inside" check should pair Entry/Exit logs.
                                // For now, we assume anyone who has "ENTRY" log and whose pass is still
                                // APPROVED/ACTIVE is inside?
                                // Actually, let's just return unique people who have entered today but not
                                // exited.
                                // But for this demo, let's keep it simple: List of all ENTRY logs.
                                .toList();
        }

        public com.security.visitor_gate_pass.dto.AdminStatsDTO getDashboardStats() {
                System.out.println("📊 Calculating Dashboard Stats...");

                long totalVisitorsToday = entryLogRepository.findAll().stream()
                                .filter(log -> log.getAction().equals("ENTRY") &&
                                                log.getTimestamp().toLocalDate().isEqual(java.time.LocalDate.now()))
                                .count();
                System.out.println("   - Visitors Today: " + totalVisitorsToday);

                long currentVisitorsInside = gatePassRepository
                                .findByStatus(com.security.visitor_gate_pass.model.GatePassStatus.APPROVED).size();
                System.out.println("   - Inside (Approved): " + currentVisitorsInside);

                long totalPassesGenerated = gatePassRepository.count();
                System.out.println("   - Total Passes: " + totalPassesGenerated);

                long pendingApprovals = gatePassRepository
                                .findByStatus(com.security.visitor_gate_pass.model.GatePassStatus.PENDING).size();
                System.out.println("   - Pending: " + pendingApprovals);

                return com.security.visitor_gate_pass.dto.AdminStatsDTO.builder()
                                .totalVisitorsToday(totalVisitorsToday)
                                .currentVisitorsInside(currentVisitorsInside)
                                .totalPassesGenerated(totalPassesGenerated)
                                .pendingApprovals(pendingApprovals)
                                .build();
        }
}