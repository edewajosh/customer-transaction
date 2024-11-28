package com.bank.customertransactions.customer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    private final Logger LOG = LoggerFactory.getLogger(TokenService.class);
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public TokenBody generateToken(Authentication authentication) {
        Instant now = Instant.now(Clock.system(ZoneId.of("Africa/Nairobi")));
        LOG.info("Time: {}", now);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .build();
        return TokenBody.builder()
                .bearer_token(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .expiry_time("3600")
                .token_type("bearer token")
                .build();
    }
}
