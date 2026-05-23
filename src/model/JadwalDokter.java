package model;

import java.time.LocalTime;

/**
 * Model JadwalDokter - merepresentasikan jadwal praktik seorang dokter
 * @author Sistem Klinik Kesehatan
 */
public class JadwalDokter {
    private String idJadwal;
    private String idDokter;
    private String idPoliklinik;
    private HariKerja hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private int kuotaPasien;
    private int pasienTerdaftar;
    private boolean statusAktif;

    public enum HariKerja {
        SENIN, SELASA, RABU, KAMIS, JUMAT, SABTU
    }

    public JadwalDokter(String idJadwal, String idDokter, String idPoliklinik,
                        HariKerja hari, LocalTime jamMulai, LocalTime jamSelesai,
                        int kuotaPasien) {
        this.idJadwal = idJadwal;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.kuotaPasien = kuotaPasien;
        this.pasienTerdaftar = 0;
        this.statusAktif = true;
    }

    // Setters
    public void setIdJadwal(String idJadwal) { this.idJadwal = idJadwal; }
    public void setIdDokter(String idDokter) { this.idDokter = idDokter; }
    public void setIdPoliklinik(String idPoliklinik) { this.idPoliklinik = idPoliklinik; }
    public void setHari(HariKerja hari) { this.hari = hari; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }
    public void setJamSelesai(LocalTime jamSelesai) { this.jamSelesai = jamSelesai; }
    public void setKuotaPasien(int kuotaPasien) { this.kuotaPasien = kuotaPasien; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    // Getters
    public String getIdJadwal() { return idJadwal; }
    public String getIdDokter() { return idDokter; }
    public String getIdPoliklinik() { return idPoliklinik; }
    public HariKerja getHari() { return hari; }
    public LocalTime getJamMulai() { return jamMulai; }
    public LocalTime getJamSelesai() { return jamSelesai; }
    public int getKuotaPasien() { return kuotaPasien; }
    public int getPasienTerdaftar() { return pasienTerdaftar; }
    public boolean isStatusAktif() { return statusAktif; }

    public int getSisaKuota() {
        return kuotaPasien - pasienTerdaftar;
    }

    public boolean isMasihAdaKuota() {
        return pasienTerdaftar < kuotaPasien;
    }

    public boolean daftarPasien() {
        if (!isMasihAdaKuota()) {
            System.out.println("Kuota jadwal penuh!");
            return false;
        }
        this.pasienTerdaftar++;
        return true;
    }

    public void batalkanPasien() {
        if (pasienTerdaftar > 0) {
            this.pasienTerdaftar--;
        }
    }

    public String getInfo() {
        return idJadwal + " | " + hari + " " + jamMulai + "-" + jamSelesai
                + " | Kuota: " + pasienTerdaftar + "/" + kuotaPasien
                + " | " + (statusAktif ? "Aktif" : "Nonaktif");
    }

    @Override
    public String toString() {
        return hari + " | " + jamMulai + " - " + jamSelesai
                + " | Sisa Kuota: " + getSisaKuota() + "/" + kuotaPasien;
    }
}