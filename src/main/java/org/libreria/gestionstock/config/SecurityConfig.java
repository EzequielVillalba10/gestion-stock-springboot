package org.libreria.gestionstock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/app.js", "/dashboard.html", "/static/**").permitAll()
                        .requestMatchers("/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/usuarios/me").authenticated()
                        // 🚨 SOLO el Admin puede ver reportes y valor total
                        .requestMatchers("/api/ventas/reporte/**").hasRole("ADMIN")
                        .requestMatchers("/api/stock/valor-total").hasRole("ADMIN")
                        // Los vendedores pueden registrar ventas y ver productos
                        .requestMatchers("/api/ventas/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Este Bean servirá para encriptar las contraseñas en la base de datos
        return new BCryptPasswordEncoder();
    }
}