package com.smartlibrary.smart_library_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Kelas User - Parent class untuk semua pengguna sistem.
 * Menyimpan data dasar pengguna dan menyediakan fungsi login dan logout.
 *
 * Konsep OOP yang digunakan:
 * - Inheritance: Kelas ini menjadi parent bagi Admin dan Anggota
 * - Encapsulation: Atribut private, diakses via getter/setter (Lombok)
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Method login - representasi OOP dari proses autentikasi.
     * Implementasi sesungguhnya ada di AuthService via Spring Security.
     */
    public void login() {
        System.out.println("[User] " + this.nama + " melakukan login.");
    }

    /**
     * Method logout - representasi OOP dari proses logout.
     * Implementasi sesungguhnya ada di AuthService (invalidate token).
     */
    public void logout() {
        System.out.println("[User] " + this.nama + " melakukan logout.");
    }

    /**
     * Enum Role untuk menentukan jenis pengguna.
     */
    public enum Role {
        ADMIN,
        ANGGOTA
    }
}
