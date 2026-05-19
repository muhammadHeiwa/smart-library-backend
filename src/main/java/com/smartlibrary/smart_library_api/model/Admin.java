package com.smartlibrary.smart_library_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Kelas Admin - Turunan dari User (Inheritance).
 * Admin memiliki hak akses untuk mengelola data buku dan anggota dalam sistem.
 *
 * Konsep OOP yang digunakan:
 * - Inheritance: extends User
 * - Polymorphism: override/extend behaviour dari parent class
 */
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    @Column(name = "jabatan", length = 100)
    private String jabatan;

    @Column(name = "no_pegawai", unique = true, length = 50)
    private String noPegawai;

    public Admin(String userId, String nama, String email, String password,
                 String jabatan, String noPegawai) {
        super(userId, nama, email, password, Role.ADMIN,
              null, null, true);
        this.jabatan = jabatan;
        this.noPegawai = noPegawai;
    }

    /**
     * Admin menambahkan buku ke sistem.
     * Implementasi sesungguhnya ada di BukuService (modul lain).
     * @param idBuku ID buku yang akan ditambahkan
     */
    public void tambahBuku(String idBuku) {
        System.out.println("[Admin] " + this.getNama() + " menambahkan buku ID: " + idBuku);
    }

    /**
     * Admin mengubah data buku.
     * @param idBuku ID buku yang akan diubah
     */
    public void ubahBuku(String idBuku) {
        System.out.println("[Admin] " + this.getNama() + " mengubah buku ID: " + idBuku);
    }

    /**
     * Admin menghapus buku dari sistem.
     * @param idBuku ID buku yang akan dihapus
     */
    public void hapusBuku(String idBuku) {
        System.out.println("[Admin] " + this.getNama() + " menghapus buku ID: " + idBuku);
    }

    /**
     * Admin menambahkan anggota baru.
     * @param anggota objek Anggota yang akan ditambahkan
     */
    public void tambahAnggota(Anggota anggota) {
        System.out.println("[Admin] " + this.getNama() +
                " menambahkan anggota: " + anggota.getNama());
    }
}
