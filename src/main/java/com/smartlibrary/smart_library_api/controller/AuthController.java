package com.smartlibrary.smart_library_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlibrary.smart_library_api.dto.UserDto.*;
import com.smartlibrary.smart_library_api.dto.UserDto.AnggotaResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.ApiResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.LoginRequest;
import com.smartlibrary.smart_library_api.dto.UserDto.LoginResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.RegisterAnggotaRequest;
import com.smartlibrary.smart_library_api.dto.UserDto.UserResponse;
import com.smartlibrary.smart_library_api.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/login
     * Endpoint login untuk semua user (Admin & Anggota).
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login berhasil", response));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Email atau password salah"));
        }
    }

    /**
     * POST /api/auth/register
     * Endpoint registrasi mandiri untuk Anggota baru.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AnggotaResponse>> register(
            @Valid @RequestBody RegisterAnggotaRequest request) {
        try {
            AnggotaResponse response = authService.registerAnggota(request);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Registrasi berhasil", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/auth/me
     * Mendapatkan informasi user yang sedang login.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserResponse response = authService.getCurrentUser(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Data user berhasil diambil", response));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/auth/logout
     * Logout (client-side: hapus token dari localStorage).
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout berhasil. Hapus token di sisi client.", null));
    }
}
