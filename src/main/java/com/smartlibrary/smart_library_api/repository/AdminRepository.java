package com.smartlibrary.smart_library_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartlibrary.smart_library_api.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByNoPegawai(String noPegawai);
    boolean existsByNoPegawai(String noPegawai);
}
