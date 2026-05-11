package com.moa.auth.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.moa.dto.auth.TokenResponse;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider(
            "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=",
            60_000L,
            120_000L);

    @Test
    void generateAndRefreshTokenKeepsUserAndProvider() {
        var authentication = new UsernamePasswordAuthenticationToken(
                "user@example.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        TokenResponse token = jwtProvider.generateToken(authentication, "google");
        TokenResponse refreshed = jwtProvider.refresh(token.getRefreshToken());

        assertThat(jwtProvider.validateToken(token.getAccessToken())).isTrue();
        assertThat(jwtProvider.validateToken(refreshed.getAccessToken())).isTrue();
        assertThat(jwtProvider.getAuthentication(refreshed.getAccessToken()).getName()).isEqualTo("user@example.com");
        assertThat(jwtProvider.getProviderFromToken(refreshed.getAccessToken())).isEqualTo("google");
    }
}
