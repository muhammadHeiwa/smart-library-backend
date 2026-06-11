package com.smartlibrary.smart_library_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartlibrary.smart_library_api.dto.UserDto.AnggotaResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.ApiResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.CreateAnggotaRequest;
import com.smartlibrary.smart_library_api.dto.UserDto.UpdateAnggotaRequest;
import com.smartlibrary.smart_library_api.service.AnggotaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/anggota")
public class AnggotaController {

    @Autowired
    private AnggotaService anggotaService;

    /**
     * GET /api/anggota
     * Daftar semua anggota - hanya Admin.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AnggotaResponse>>> getAllAnggota(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nama") String sortBy) {
        Page<AnggotaResponse> result = anggotaService.getAllAnggota(page, size, sortBy);
        return ResponseEntity.ok(ApiResponse.success("Data anggota berhasil diambil", result));
    }

    /**
     * GET /api/anggota/search?keyword=xxx
     * Cari anggota - hanya Admin.
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AnggotaResponse>>> searchAnggota(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AnggotaResponse> result = anggotaService.searchAnggota(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success("Hasil pencarian anggota", result));
    }

    /**
     * GET /api/anggota/stats
     * Statistik anggota - hanya Admin.
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnggotaService.AnggotaStatsResponse>> getStats() {
        return ResponseEntity.ok(
                ApiResponse.success("Statistik anggota", anggotaService.getStats()));
    }

    /**
     * POST /api/anggota
     * Tambah anggota baru - hanya Admin (OOP: tambahAnggota()).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnggotaResponse>> tambahAnggota(
            @Valid @RequestBody CreateAnggotaRequest request) {
        try {
            AnggotaResponse response = anggotaService.tambahAnggota(request);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Anggota berhasil ditambahkan", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/anggota/{userId}
     * Detail anggota - Bisa diakses oleh Admin atau Anggota itu sendiri secara aman.
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANGGOTA')") 
    public ResponseEntity<ApiResponse<AnggotaResponse>> getAnggotaById(
            @PathVariable String userId,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails currentUser) {
        try {
            // Pengaman tambahan lewat kode program: Anggota tidak boleh intip data ID milik anggota lain
            boolean isAdmin = currentUser.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            // Mengambil data user untuk dicek kecocokan emailnya jika dia bukan admin
            AnggotaResponse response = anggotaService.getAnggotaById(userId);
            
            if (!isAdmin && !currentUser.getUsername().equalsIgnoreCase(response.getEmail())) {
                return ResponseEntity.status(403).body(ApiResponse.error("Anda tidak memiliki hak akses untuk melihat profil ini!"));
            }

            return ResponseEntity.ok(ApiResponse.success("Detail anggota berhasil diambil", response));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/anggota/no/{noAnggota}
     * Detail anggota berdasarkan nomor anggota.
     */
    @GetMapping("/no/{noAnggota}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnggotaResponse>> getAnggotaByNo(
            @PathVariable String noAnggota) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.success("Detail anggota",
                            anggotaService.getAnggotaByNoAnggota(noAnggota)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/anggota/{userId}
     * Update data anggota - Diizinkan untuk Admin atau Anggota itu sendiri
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANGGOTA')") // <--- PASTIKAN role ANGGOTA ada di sini
    public ResponseEntity<ApiResponse<AnggotaResponse>> updateAnggota(
            @PathVariable String userId,
            @Valid @RequestBody UpdateAnggotaRequest request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails currentUser) {
        try {
            // Pengaman tambahan lewat kode program agar Anggota tidak bisa meretas/mengubah data ID Anggota lain
            boolean isAdmin = currentUser.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            // Ambil data lama untuk mencocokkan email jika dia bukan admin
            AnggotaResponse anggotaLama = anggotaService.getAnggotaById(userId);
            
            if (!isAdmin && !currentUser.getUsername().equalsIgnoreCase(anggotaLama.getEmail())) {
                return ResponseEntity.status(403).body(ApiResponse.error("Anda tidak memiliki hak akses untuk mengubah profil ini!"));
            }

            // Eksekusi update ke database
            AnggotaResponse response = anggotaService.updateAnggota(userId, request);
            return ResponseEntity.ok(ApiResponse.success("Profil anggota berhasil diperbarui", response));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PATCH /api/anggota/{userId}/toggle-status
     * Aktifkan/nonaktifkan anggota - hanya Admin.
     */
    @PutMapping("/{userId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnggotaResponse>> toggleStatus(
            @PathVariable String userId) {
        try {
            AnggotaResponse response = anggotaService.toggleStatusAnggota(userId);
            return ResponseEntity.ok(
                    ApiResponse.success("Status anggota berhasil diubah", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/anggota/{userId}/riwayat
     * Lihat riwayat peminjaman anggota (OOP: lihatRiwayat()).
     */
    @GetMapping("/{userId}/riwayat")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<ApiResponse<List<String>>> getRiwayat(
            @PathVariable String userId) {
        try {
            List<String> riwayat = anggotaService.getRiwayatAnggota(userId);
            return ResponseEntity.ok(ApiResponse.success("Riwayat peminjaman", riwayat));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
public ResponseEntity<?> hapusAnggota(@PathVariable String userId) {
    try {
        anggotaService.hapusAnggota(userId); 
        return ResponseEntity.ok(ApiResponse.success("Anggota berhasil dihapus", null));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
}
}

