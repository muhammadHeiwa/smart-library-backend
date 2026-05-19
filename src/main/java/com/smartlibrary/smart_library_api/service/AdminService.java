package com.smartlibrary.smart_library_api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlibrary.smart_library_api.dto.UserDto.AdminResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.UpdateAdminRequest;
import com.smartlibrary.smart_library_api.model.Admin;
import com.smartlibrary.smart_library_api.repository.AdminRepository;
import com.smartlibrary.smart_library_api.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    /**
     * Membuat akun Admin baru (hanya bisa dilakukan oleh Admin yang sudah ada).
     */
    @Transactional
    public AdminResponse createAdmin(String nama, String email, String password,
                                     String jabatan, String noPegawai) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email sudah terdaftar");
        }
        if (adminRepository.existsByNoPegawai(noPegawai)) {
            throw new RuntimeException("Nomor pegawai sudah digunakan");
        }

        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Admin admin = new Admin(userId, nama, email,
                passwordEncoder.encode(password), jabatan, noPegawai);
        adminRepository.save(admin);
        return authService.mapToAdminResponse(admin);
    }

    /**
     * Mendapatkan semua admin dengan pagination.
     */
    public Page<AdminResponse> getAllAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nama").ascending());
        return adminRepository.findAll(pageable)
                .map(authService::mapToAdminResponse);
    }

    /**
     * Mendapatkan detail admin berdasarkan ID.
     */
    public AdminResponse getAdminById(String userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan: " + userId));
        return authService.mapToAdminResponse(admin);
    }

    /**
     * Update data admin.
     */
    @Transactional
    public AdminResponse updateAdmin(String userId, UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan: " + userId));

        if (request.getNama() != null) admin.setNama(request.getNama());
        if (request.getJabatan() != null) admin.setJabatan(request.getJabatan());
        if (request.getIsActive() != null) admin.setIsActive(request.getIsActive());

        adminRepository.save(admin);
        return authService.mapToAdminResponse(admin);
    }

    /**
     * Inisialisasi admin default (dijalankan saat startup jika belum ada admin).
     */
    @Transactional
    public void initDefaultAdmin() {
        if (adminRepository.count() == 0) {
            Admin defaultAdmin = new Admin(
                    "USR-ADMIN001",
                    "Administrator",
                    "admin@smartlibrary.com",
                    passwordEncoder.encode("Admin@123"),
                    "Kepala Perpustakaan",
                    "PEG-001"
            );
            adminRepository.save(defaultAdmin);
            System.out.println("[INIT] Default admin dibuat: admin@smartlibrary.com / Admin@123");
        }
    }
}
