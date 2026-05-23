package com.smartlibrary.smart_library_api.controller;

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

import com.smartlibrary.smart_library_api.dto.UserDto.AdminResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.ApiResponse;
import com.smartlibrary.smart_library_api.dto.UserDto.UpdateAdminRequest;
import com.smartlibrary.smart_library_api.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminResponse>>> getAllAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminResponse> result = adminService.getAllAdmin(page, size);
        return ResponseEntity.ok(ApiResponse.success("Data admin berhasil diambil", result));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminResponse>> getAdminById(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.success("Detail admin", adminService.getAdminById(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminResponse>> createAdmin(
            @RequestParam String nama,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "Staff Perpustakaan") String jabatan,
            @RequestParam String noPegawai) {
        try {
            AdminResponse response = adminService.createAdmin(
                    nama, email, password, jabatan, noPegawai);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Admin berhasil dibuat", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminResponse>> updateAdmin(
            @PathVariable String userId,
            @RequestBody UpdateAdminRequest request) {
        try {
            AdminResponse response = adminService.updateAdmin(userId, request);
            return ResponseEntity.ok(
                    ApiResponse.success("Data admin berhasil diperbarui", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ─── Endpoint Toggle Status Admin Menggunakan PUT ───
    @PutMapping("/{userId}/toggle-status")
    public ResponseEntity<?> toggleStatus(@PathVariable String userId) {
        try {
            adminService.toggleStatus(userId);
            return ResponseEntity.ok(ApiResponse.success("Status admin berhasil diubah", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> hapusAdmin(@PathVariable String userId) {
        try {
            adminService.hapusAdmin(userId);
            return ResponseEntity.ok(ApiResponse.success("Admin berhasil dihapus", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}