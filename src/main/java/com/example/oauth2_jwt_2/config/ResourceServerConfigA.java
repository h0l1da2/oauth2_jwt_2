package com.example.oauth2_jwt_2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

/**
 * OAuth 프로젝트 없이 대칭 키 이용
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigA extends WebSecurityConfigurerAdapter {

    @Value("${jwt.key}")
    private String jwtKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(
                        c -> c.jwt(
                                j -> j.decoder(jwtDecoder())
                        )
                );
    }

    /**
     * 앱이 토큰을 검증하는 데 필요한 세부 정보 구성
     * 대칭 키니까
     * 같은 클래스에 대칭 키의 값을 제공하는 jwtDecoder 만들고
     * 디코더 설정함
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] key = jwtKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(key, 0, key.length, "AES");

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey).build();

        return jwtDecoder;
    }
}
