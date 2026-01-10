package com.security.visitor_gate_pass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.visitor_gate_pass.model.EntryLog;

public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
    List<EntryLog> findByAction(String action);
}
