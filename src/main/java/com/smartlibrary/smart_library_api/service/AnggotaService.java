package com.smartlibrary.smart_library_api.service;

import com.smartlibrary.smart_library_api.dto.UserDto.*;
import com.smartlibrary.smart_library_api.model.Anggota;
import com.smartlibrary.smart_library_api.repository.AnggotaRepository;
import com.smartlibrary.smart_library_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnggotaService {

    @Autowired
    private AnggotaRepository anggotaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    /**
     * Admin menambahkan anggota baru - memanggil tambahAnggota() OOP.
     */
    @Transactional
    public AnggotaResponse tambahAnggota(CreateAnggotaRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String noAnggota = request.getNoAnggota() != null
                ? request.getNoAnggota()
                : generateNoAnggota();

        if (anggotaRepository.existsByNoAnggota(noAnggota)) {
            throw new RuntimeException("Nomor anggota sudah digunakan");
        }

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
        return authService.mapToAnggotaResponse(anggota);
    }

    /**
     * Mendapatkan semua anggota dengan pagination.
     */
    public Page<AnggotaResponse> getAllAnggota(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return anggotaRepository.findAll(pageable)
                .map(authService::mapToAnggotaResponse);
    }

    /**
     * Mencari anggota berdasarkan keyword.
     */
    public Page<AnggotaResponse> searchAnggota(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nama").ascending());
        return anggotaRepository.searchAnggota(keyword, pageable)
                .map(authService::mapToAnggotaResponse);
    }

    /**
     * Mendapatkan detail anggota berdasarkan ID.
     */
    public AnggotaResponse getAnggotaById(String userId) {
        Anggota anggota = anggotaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Anggota tidak ditemukan: " + userId));
        return authService.mapToAnggotaResponse(anggota);
    }

    /**
     * Mendapatkan detail anggota berdasarkan nomor anggota.
     */
    public AnggotaResponse getAnggotaByNoAnggota(String noAnggota) {
        Anggota anggota = anggotaRepository.findByNoAnggota(noAnggota)
                .orElseThrow(() -> new RuntimeException("Anggota tidak ditemukan: " + noAnggota));
        return authService.mapToAnggotaResponse(anggota);
    }

    /**
     * Update data anggota.
     */
    @Transactional
    public AnggotaResponse updateAnggota(String userId, UpdateAnggotaRequest request) {
        Anggota anggota = anggotaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Anggota tidak ditemukan: " + userId));

        if (request.getNama() != null) anggota.setNama(request.getNama());
        if (request.getAlamat() != null) anggota.setAlamat(request.getAlamat());
        if (request.getNoTelepon() != null) anggota.setNoTelepon(request.getNoTelepon());
        if (request.getIsActive() != null) anggota.setIsActive(request.getIsActive());

        anggotaRepository.save(anggota);
        return authService.mapToAnggotaResponse(anggota);
    }

    /**
     * Menonaktifkan/mengaktifkan anggota (soft delete).
     */
    @Transactional
    public AnggotaResponse toggleStatusAnggota(String userId) {
        Anggota anggota = anggotaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Anggota tidak ditemukan: " + userId));
        anggota.setIsActive(!anggota.getIsActive());
        anggotaRepository.save(anggota);
        return authService.mapToAnggotaResponse(anggota);
    }

    /**
     * Menampilkan riwayat peminjaman anggota - memanggil lihatRiwayat() OOP.
     */
    public List<String> getRiwayatAnggota(String userId) {
        Anggota anggota = anggotaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Anggota tidak ditemukan: " + userId));
        // Memanggil method lihatRiwayat() dari kelas Anggota (OOP)
        return anggota.lihatRiwayat();
    }

    /**
     * Statistik anggota untuk dashboard admin.
     */
    public AnggotaStatsResponse getStats() {
        long total = anggotaRepository.count();
        long aktif = anggotaRepository.countByIsActive(true);
        long nonAktif = anggotaRepository.countByIsActive(false);
        return new AnggotaStatsResponse(total, aktif, nonAktif);
    }

    // ===== Helper =====

    private String generateNoAnggota() {
        long count = anggotaRepository.count() + 1;
        return String.format("ANG-%05d", count);
    }

    // ===== Inner DTO =====
    public record AnggotaStatsResponse(long total, long aktif, long nonAktif) {}
}
