package com.security.visitor_gate_pass.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.visitor_gate_pass.model.EntryLog;
import com.security.visitor_gate_pass.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/logs")
    public List<EntryLog> getAllLogs() {
        return adminService.getAllLogs();
    }

    @GetMapping("/logs/filter")
    public List<EntryLog> getLogsByAction(@RequestParam String action) {
        return adminService.getLogsByAction(action);
    }

    @GetMapping("/inside")
    public List<EntryLog> getInsideVisitors() {
        return adminService.getCurrentInsideVisitors();
    }

    @GetMapping("/stats")
    public com.security.visitor_gate_pass.dto.AdminStatsDTO getStats() {
        return adminService.getDashboardStats();
    }

}
