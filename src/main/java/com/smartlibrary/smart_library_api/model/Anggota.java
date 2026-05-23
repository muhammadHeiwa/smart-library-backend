package com.smartlibrary.smart_library_api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Kelas Anggota - Turunan dari User (Inheritance).
 * Merepresentasikan pengguna perpustakaan yang dapat melakukan
 * peminjaman dan pengembalian buku serta melihat riwayat transaksi.
 *
 * Konsep OOP yang digunakan:
 * - Inheritance: extends User
 * - Encapsulation: daftarPinjaman adalah private ArrayList
 */
@Entity
@Table(name = "tb_anggota")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Anggota extends User {

    @Column(name = "no_anggota", unique = true, length = 50)
    private String noAnggota;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "no_telepon", length = 20)
    private String noTelepon;

    @Column(name = "total_pinjaman_aktif")
    private Integer totalPinjamanAktif = 0;

    /**
     * daftarPinjaman: ArrayList<Peminjaman> - sesuai spesifikasi kelas OOP.
     * Disimpan sebagai relasi ke tabel Peminjaman (modul lain).
     * Di sini hanya disimpan ID peminjaman sebagai referensi.
     */
    @ElementCollection
    @CollectionTable(name = "tb_anggota_daftar_pinjaman",
                     joinColumns = @JoinColumn(name = "anggota_id"))
    @Column(name = "peminjaman_id")
    private List<String> daftarPinjamanIds = new ArrayList<>();

    public Anggota(String userId, String nama, String email, String password,
                   String noAnggota, String alamat, String noTelepon) {
        super(userId, nama, email, password, Role.ANGGOTA,
              null, null, true);
        this.noAnggota = noAnggota;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
    }

    /**
     * Anggota meminjam buku.
     * Implementasi penuh ada di PeminjamanService (modul lain).
     * @param idBuku ID buku yang akan dipinjam
     */
    public void pinjamBuku(String idBuku) {
        System.out.println("[Anggota] " + this.getNama() + " meminjam buku ID: " + idBuku);
    }

    /**
     * Anggota mengembalikan buku.
     * @param idBuku ID buku yang akan dikembalikan
     */
    public void kembalikanBuku(String idBuku) {
        System.out.println("[Anggota] " + this.getNama() + " mengembalikan buku ID: " + idBuku);
    }

    /**
     * Anggota melihat riwayat transaksi.
     * Mengembalikan daftar ID peminjaman yang dimiliki anggota.
     */
    public List<String> lihatRiwayat() {
        System.out.println("[Anggota] " + this.getNama() + " melihat riwayat peminjaman.");
        return this.daftarPinjamanIds;
    }

    /**
     * Menambahkan ID peminjaman ke daftar pinjaman anggota.
     */
    public void tambahPinjaman(String peminjamanId) {
        this.daftarPinjamanIds.add(peminjamanId);
        this.totalPinjamanAktif++;
    }

    /**
     * Menghapus ID peminjaman dari daftar pinjaman aktif anggota.
     */
    public void hapusPinjaman(String peminjamanId) {
        this.daftarPinjamanIds.remove(peminjamanId);
        if (this.totalPinjamanAktif > 0) this.totalPinjamanAktif--;
    }
}
