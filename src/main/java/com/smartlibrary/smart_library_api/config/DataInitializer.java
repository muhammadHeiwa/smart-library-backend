package com.smartlibrary.smart_library_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartlibrary.smart_library_api.service.AdminService;

/**
 * Inisialisasi data default saat aplikasi pertama kali dijalankan.
 * Membuat akun admin default jika belum ada.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Override
    public void run(String... args) {
        adminService.initDefaultAdmin();
    }
}
