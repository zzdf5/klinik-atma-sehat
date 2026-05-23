package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model Kunjungan - merepresentasikan satu sesi kunjungan pasien ke dokter
 * @author Sistem Klinik Kesehatan
 */
public class Kunjungan {
    private String idKunjungan;
    private String nomorRekamMedis;
    private String idDokter;
    private String idPoliklinik;
    private LocalDate tanggal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String keluhanUtama;
    private String hasilPemeriksaan;    // pemeriksaan fisik (tekanan darah, suhu, dll)
    private Diagnosa diagnosa;
    private Resep resep;
    private String tindakan;
    private double biayaKonsultasi;
    private StatusKunjungan status;

    public enum StatusKunjungan {
        TERJADWAL, BERLANGSUNG, SELESAI, BATAL
    }

    public Kunjungan(String idKunjungan, String nomorRekamMedis, String idDokter,
                     String idPoliklinik, LocalDate tanggal, String keluhanUtama) {
        this.idKunjungan = idKunjungan;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.tanggal = tanggal;
        this.keluhanUtama = keluhanUtama;
        this.status = StatusKunjungan.TERJADWAL;
    }

    // Setters
    public void setIdKunjungan(String idKunjungan) { this.idKunjungan = idKunjungan; }
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setIdDokter(String idDokter) { this.idDokter = idDokter; }
    public void setIdPoliklinik(String idPoliklinik) { this.idPoliklinik = idPoliklinik; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }
    public void setJamSelesai(LocalTime jamSelesai) { this.jamSelesai = jamSelesai; }
    public void setKeluhanUtama(String keluhanUtama) { this.keluhanUtama = keluhanUtama; }
    public void setHasilPemeriksaan(String hasilPemeriksaan) { this.hasilPemeriksaan = hasilPemeriksaan; }
    public void setDiagnosa(Diagnosa diagnosa) { this.diagnosa = diagnosa; }
    public void setResep(Resep resep) { this.resep = resep; }
    public void setTindakan(String tindakan) { this.tindakan = tindakan; }
    public void setBiayaKonsultasi(double biayaKonsultasi) { this.biayaKonsultasi = biayaKonsultasi; }
    public void setStatus(StatusKunjungan status) { this.status = status; }

    // Getters
    public String getIdKunjungan() { return idKunjungan; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getIdDokter() { return idDokter; }
    public String getIdPoliklinik() { return idPoliklinik; }
    public LocalDate getTanggal() { return tanggal; }
    public LocalTime getJamMulai() { return jamMulai; }
    public LocalTime getJamSelesai() { return jamSelesai; }
    public String getKeluhanUtama() { return keluhanUtama; }
    public String getHasilPemeriksaan() { return hasilPemeriksaan; }
    public Diagnosa getDiagnosa() { return diagnosa; }
    public Resep getResep() { return resep; }
    public String getTindakan() { return tindakan; }
    public double getBiayaKonsultasi() { return biayaKonsultasi; }
    public StatusKunjungan getStatus() { return status; }

    public void mulai() {
        this.jamMulai = LocalTime.now();
        this.status = StatusKunjungan.BERLANGSUNG;
    }

    public void selesai() {
        this.jamSelesai = LocalTime.now();
        this.status = StatusKunjungan.SELESAI;
    }

    public double getTotalBiaya() {
        double totalObat = (resep != null) ? resep.hitungTotalHarga() : 0;
        return biayaKonsultasi + totalObat;
    }

    public String getInfo() {
        return idKunjungan + " | " + tanggal + " | " + idDokter + " | " + status;
    }

    @Override
    public String toString() {
        return "=== KUNJUNGAN ===\n"
                + "ID Kunjungan : " + idKunjungan + "\n"
                + "Tanggal      : " + tanggal + "\n"
                + "Dokter       : " + idDokter + "\n"
                + "Keluhan      : " + keluhanUtama + "\n"
                + "Pemeriksaan  : " + hasilPemeriksaan + "\n"
                + "Status       : " + status + "\n"
                + "Biaya Konsul : Rp" + String.format("%.0f", biayaKonsultasi) + "\n"
                + "Total Biaya  : Rp" + String.format("%.0f", getTotalBiaya());
    }
}