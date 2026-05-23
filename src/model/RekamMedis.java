package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model RekamMedis - menyimpan seluruh riwayat medis seorang pasien
 * @author Sistem Klinik Kesehatan
 */
public class RekamMedis {
    private String nomorRekamMedis;
    private String idPasien;
    private LocalDate tanggalBuat;
    private List<Kunjungan> riwayatKunjungan;
    private List<String> alergi;
    private List<String> riwayatPenyakit;
    private String catatanUmum;

    public RekamMedis(String nomorRekamMedis, String idPasien) {
        this.nomorRekamMedis = nomorRekamMedis;
        this.idPasien = idPasien;
        this.tanggalBuat = LocalDate.now();
        this.riwayatKunjungan = new ArrayList<>();
        this.alergi = new ArrayList<>();
        this.riwayatPenyakit = new ArrayList<>();
    }

    // Setters
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setIdPasien(String idPasien) { this.idPasien = idPasien; }
    public void setTanggalBuat(LocalDate tanggalBuat) { this.tanggalBuat = tanggalBuat; }
    public void setCatatanUmum(String catatanUmum) { this.catatanUmum = catatanUmum; }

    // Getters
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getIdPasien() { return idPasien; }
    public LocalDate getTanggalBuat() { return tanggalBuat; }
    public List<Kunjungan> getRiwayatKunjungan() { return riwayatKunjungan; }
    public List<String> getAlergi() { return alergi; }
    public List<String> getRiwayatPenyakit() { return riwayatPenyakit; }
    public String getCatatanUmum() { return catatanUmum; }

    public void tambahKunjungan(Kunjungan kunjungan) {
        this.riwayatKunjungan.add(kunjungan);
    }

    public void tambahAlergi(String alergi) {
        this.alergi.add(alergi);
    }

    public void tambahRiwayatPenyakit(String penyakit) {
        this.riwayatPenyakit.add(penyakit);
    }

    public Kunjungan getKunjunganTerakhir() {
        if (riwayatKunjungan.isEmpty()) return null;
        return riwayatKunjungan.get(riwayatKunjungan.size() - 1);
    }

    public int getTotalKunjungan() {
        return riwayatKunjungan.size();
    }

    public String getInfo() {
        return nomorRekamMedis + " | " + idPasien + " | Kunjungan: " + getTotalKunjungan();
    }

    @Override
    public String toString() {
        return "=== REKAM MEDIS ===\n"
                + "No. Rekam Medis : " + nomorRekamMedis + "\n"
                + "ID Pasien       : " + idPasien + "\n"
                + "Tanggal Buat    : " + tanggalBuat + "\n"
                + "Alergi          : " + String.join(", ", alergi) + "\n"
                + "Riwayat Penyakit: " + String.join(", ", riwayatPenyakit) + "\n"
                + "Total Kunjungan : " + getTotalKunjungan();
    }
}