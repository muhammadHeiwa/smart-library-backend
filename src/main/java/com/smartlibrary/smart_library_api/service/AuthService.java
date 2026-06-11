package com.smartlibrary.smart_library_api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlibrary.smart_library_api.dto.UserDto.AdminResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.AnggotaResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.LoginRequest;
import com.smartlibrary.smart_library_api.dto.UserDto.LoginResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.RegisterAnggotaRequest;
import com.smartlibrary.smart_library_api.dto.UserDto.UserResponse;
import com.smartlibrary.smart_library_api.model.Admin;
import com.smartlibrary.smart_library_api.model.Anggota;
import com.smartlibrary.smart_library_api.model.User;
import com.smartlibrary.smart_library_api.repository.AdminRepository;
import com.smartlibrary.smart_library_api.repository.AnggotaRepository;
import com.smartlibrary.smart_library_api.repository.UserRepository;
import com.smartlibrary.smart_library_api.security.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnggotaRepository anggotaRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Login user - memanggil method login() pada objek User (OOP).
     */
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Memanggil method login() dari kelas User (OOP)
        user.login();

        UserResponse userResponse = new UserResponse(
                user.getUserId(), user.getNama(), user.getEmail(),
                user.getRole(), user.getIsActive(), user.getCreatedAt()
        );

        return new LoginResponse(jwt, "Bearer", userResponse);
    }

    /**
     * Register anggota baru (self-registration).
     */
    @Transactional
    public AnggotaResponse registerAnggota(RegisterAnggotaRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String noAnggota = generateNoAnggota();

        Anggota anggota = new Anggota(
                userId,
                request.getNama(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                noAnggota,
                request.getAlamat(),
                request.getNoTelepon()
        );

        anggotaRepository.save(anggota);
        return mapToAnggotaResponse(anggota);
    }

    /**
     * Mendapatkan data user saat ini berdasarkan email dari JWT.
     */
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        return new UserResponse(
                user.getUserId(), user.getNama(), user.getEmail(),
                user.getRole(), user.getIsActive(), user.getCreatedAt()
        );
    }

    // ===== Helper Methods =====

    private String generateNoAnggota() {
        long count = anggotaRepository.count() + 1;
        return String.format("ANG-%05d", count);
    }

    public AnggotaResponse mapToAnggotaResponse(Anggota a) {
        return new AnggotaResponse(
                a.getUserId(), a.getNama(), a.getEmail(),
                a.getNoAnggota(), a.getAlamat(), a.getNoTelepon(),
                a.getTotalPinjamanAktif(), a.getIsActive(), a.getCreatedAt(),
                a.getDaftarPinjamanIds()
        );
    }

    public AdminResponse mapToAdminResponse(Admin a) {
        return new AdminResponse(
                a.getUserId(), a.getNama(), a.getEmail(),
                a.getJabatan(), a.getNoPegawai(), a.getIsActive(), a.getCreatedAt()
        );
    }
}
