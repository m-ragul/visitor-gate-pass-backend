package com.security.visitor_gate_pass.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

public class JwtConstants {

    public static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(
                "this-is-a-very-secure-and-long-secret-key-256bits!"
                .getBytes()
            );

    public static final long EXPIRATION = 1000 * 60 * 60; // 1 hour
}