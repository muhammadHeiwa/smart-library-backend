package com.smartlibrary.smart_library_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.smartlibrary.smart_library_api.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) untuk modul User Management.
 * Memisahkan representasi API dari model database.
 */
public class UserDto {

    // ========== Request DTOs ==========

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Email tidak boleh kosong")
        @Email(message = "Format email tidak valid")
        private String email;

        @NotBlank(message = "Password tidak boleh kosong")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterAnggotaRequest {
        @NotBlank(message = "Nama tidak boleh kosong")
        private String nama;

        @NotBlank(message = "Email tidak boleh kosong")
        @Email(message = "Format email tidak valid")
        private String email;

        @NotBlank(message = "Password tidak boleh kosong")
        @Size(min = 6, message = "Password minimal 6 karakter")
        private String password;

        private String alamat;
        private String noTelepon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAnggotaRequest {
        @NotBlank(message = "Nama tidak boleh kosong")
        private String nama;

        @NotBlank(message = "Email tidak boleh kosong")
        @Email(message = "Format email tidak valid")
        private String email;

        @NotBlank(message = "Password tidak boleh kosong")
        @Size(min = 6, message = "Password minimal 6 karakter")
        private String password;

        private String alamat;
        private String noTelepon;
        private String noAnggota;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAnggotaRequest {
        private String nama;
        private String alamat;
        private String noTelepon;
        private Boolean isActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAdminRequest {
        private String nama;
        private String jabatan;
        private Boolean isActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank(message = "Password lama tidak boleh kosong")
        private String passwordLama;

        @NotBlank(message = "Password baru tidak boleh kosong")
        @Size(min = 6, message = "Password minimal 6 karakter")
        private String passwordBaru;
    }

    // ========== Response DTOs ==========

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String tokenType = "Bearer";
        private UserResponse user;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private String userId;
        private String nama;
        private String email;
        private User.Role role;
        private Boolean isActive;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnggotaResponse {
        private String userId;
        private String nama;
        private String email;
        private String noAnggota;
        private String alamat;
        private String noTelepon;
        private Integer totalPinjamanAktif;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private List<String> daftarPinjamanIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminResponse {
        private String userId;
        private String nama;
        private String email;
        private String jabatan;
        private String noPegawai;
        private Boolean isActive;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private Boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }

        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message, null);
        }
    }
}
