package com.smartlibrary.smart_library_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartlibrary.smart_library_api.model.Anggota;

@Repository
public interface AnggotaRepository extends JpaRepository<Anggota, String> {

    Optional<Anggota> findByNoAnggota(String noAnggota);

    boolean existsByNoAnggota(String noAnggota);

    @Query("SELECT a FROM Anggota a WHERE " +
           "LOWER(a.nama) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.noAnggota) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Anggota> searchAnggota(@Param("keyword") String keyword, Pageable pageable);

    long countByIsActive(Boolean isActive);
}
