package com.security.visitor_gate_pass.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gate_passes")
public class GatePass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 Each gate pass belongs to ONE user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GatePassStatus status;

    private String purpose;

    @Column(nullable = false)
    private LocalDate visitDate;

    @Column(nullable = false)
    private LocalTime visitTime;

    @Column(columnDefinition = "TEXT")
    private String additionalDetails;

    private String qrData;
    private boolean active = true;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    private String approvedBy;
    private String rejectionReason;

    // 🔑 SECURE OTP STORAGE
    private String currentOtp;
    private LocalDateTime otpExpiry;
}