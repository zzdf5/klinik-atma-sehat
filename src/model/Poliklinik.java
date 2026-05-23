package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Poliklinik - merepresentasikan ruangan/departemen pelayanan di klinik
 * @author Sistem Klinik Kesehatan
 */
public class Poliklinik {
    private String idPoliklinik;
    private String namaPoliklinik;
    private String deskripsi;
    private String lokasiRuangan;
    private List<String> idDokter;     // daftar ID dokter yang bertugas
    private List<String> idPerawat;    // daftar ID perawat yang bertugas
    private boolean statusBuka;
    private String jamOperasional;     // contoh: "08:00 - 16:00"

    public Poliklinik(String idPoliklinik, String namaPoliklinik, String lokasiRuangan,
                      String jamOperasional) {
        this.idPoliklinik = idPoliklinik;
        this.namaPoliklinik = namaPoliklinik;
        this.lokasiRuangan = lokasiRuangan;
        this.jamOperasional = jamOperasional;
        this.idDokter = new ArrayList<>();
        this.idPerawat = new ArrayList<>();
        this.statusBuka = true;
    }

    // Setters
    public void setIdPoliklinik(String idPoliklinik) { this.idPoliklinik = idPoliklinik; }
    public void setNamaPoliklinik(String namaPoliklinik) { this.namaPoliklinik = namaPoliklinik; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setLokasiRuangan(String lokasiRuangan) { this.lokasiRuangan = lokasiRuangan; }
    public void setStatusBuka(boolean statusBuka) { this.statusBuka = statusBuka; }
    public void setJamOperasional(String jamOperasional) { this.jamOperasional = jamOperasional; }

    // Getters
    public String getIdPoliklinik() { return idPoliklinik; }
    public String getNamaPoliklinik() { return namaPoliklinik; }
    public String getDeskripsi() { return deskripsi; }
    public String getLokasiRuangan() { return lokasiRuangan; }
    public List<String> getIdDokter() { return idDokter; }
    public List<String> getIdPerawat() { return idPerawat; }
    public boolean isStatusBuka() { return statusBuka; }
    public String getJamOperasional() { return jamOperasional; }

    public void tambahDokter(String idDokter) {
        if (!this.idDokter.contains(idDokter)) {
            this.idDokter.add(idDokter);
            System.out.println("Dokter " + idDokter + " ditambahkan ke " + namaPoliklinik);
        }
    }

    public void hapusDokter(String idDokter) {
        this.idDokter.remove(idDokter);
    }

    public void tambahPerawat(String idPerawat) {
        if (!this.idPerawat.contains(idPerawat)) {
            this.idPerawat.add(idPerawat);
        }
    }

    public void hapusPerawat(String idPerawat) {
        this.idPerawat.remove(idPerawat);
    }

    public void buka() {
        this.statusBuka = true;
        System.out.println(namaPoliklinik + " dibuka.");
    }

    public void tutup() {
        this.statusBuka = false;
        System.out.println(namaPoliklinik + " ditutup.");
    }

    public String getInfo() {
        return idPoliklinik + " | " + namaPoliklinik + " | " + lokasiRuangan
                + " | " + (statusBuka ? "Buka" : "Tutup");
    }

    @Override
    public String toString() {
        return "Poliklinik: " + namaPoliklinik
                + " | Lokasi: " + lokasiRuangan
                + " | Jam: " + jamOperasional
                + " | Dokter: " + idDokter.size()
                + " | Status: " + (statusBuka ? "Buka" : "Tutup");
    }
}