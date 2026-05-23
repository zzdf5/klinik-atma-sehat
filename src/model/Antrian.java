package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model Antrian - merepresentasikan antrian pasien di klinik
 * @author Sistem Klinik Kesehatan
 */
public class Antrian {
    private String idAntrian;
    private String nomorUrut;
    private String idPasien;
    private String nomorRekamMedis;
    private String idDokter;
    private String idPoliklinik;
    private LocalDate tanggal;
    private LocalTime jamDaftar;
    private LocalTime jamDipanggil;
    private StatusAntrian status;
    private JenisKunjungan jenisKunjungan;

    public enum StatusAntrian {
        MENUNGGU, DIPANGGIL, DALAM_PEMERIKSAAN, SELESAI, BATAL
    }

    public enum JenisKunjungan {
        BARU, KONTROL, RUJUKAN
    }

    public Antrian(String idPasien, String nomorRekamMedis, String idDokter,
                   String idPoliklinik, JenisKunjungan jenisKunjungan) {
        this.idPasien = idPasien;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.jenisKunjungan = jenisKunjungan;
        this.tanggal = LocalDate.now();
        this.jamDaftar = LocalTime.now();
        this.status = StatusAntrian.MENUNGGU;
    }

    public Antrian(String idAntrian, String nomorUrut, String idPasien,
                   String nomorRekamMedis, String idDokter, String idPoliklinik,
                   LocalDate tanggal, JenisKunjungan jenisKunjungan) {
        this.idAntrian = idAntrian;
        this.nomorUrut = nomorUrut;
        this.idPasien = idPasien;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.tanggal = tanggal;
        this.jamDaftar = LocalTime.now();
        this.jenisKunjungan = jenisKunjungan;
        this.status = StatusAntrian.MENUNGGU;
    }

    // Setters
    public void setIdAntrian(String idAntrian) { this.idAntrian = idAntrian; }
    public void setNomorUrut(String nomorUrut) { this.nomorUrut = nomorUrut; }
    public void setIdPasien(String idPasien) { this.idPasien = idPasien; }
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setIdDokter(String idDokter) { this.idDokter = idDokter; }
    public void setIdPoliklinik(String idPoliklinik) { this.idPoliklinik = idPoliklinik; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public void setJamDaftar(LocalTime jamDaftar) { this.jamDaftar = jamDaftar; }
    public void setJamDipanggil(LocalTime jamDipanggil) { this.jamDipanggil = jamDipanggil; }
    public void setStatus(StatusAntrian status) { this.status = status; }
    public void setJenisKunjungan(JenisKunjungan jenisKunjungan) { this.jenisKunjungan = jenisKunjungan; }

    // Getters
    public String getIdAntrian() { return idAntrian; }
    public String getNomorUrut() { return nomorUrut; }
    public String getIdPasien() { return idPasien; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getIdDokter() { return idDokter; }
    public String getIdPoliklinik() { return idPoliklinik; }
    public LocalDate getTanggal() { return tanggal; }
    public LocalTime getJamDaftar() { return jamDaftar; }
    public LocalTime getJamDipanggil() { return jamDipanggil; }
    public StatusAntrian getStatus() { return status; }
    public JenisKunjungan getJenisKunjungan() { return jenisKunjungan; }

    public void panggil() {
        this.jamDipanggil = LocalTime.now();
        this.status = StatusAntrian.DIPANGGIL;
        System.out.println("Memanggil antrian nomor: " + nomorUrut);
    }

    public void mulaiPemeriksaan() {
        this.status = StatusAntrian.DALAM_PEMERIKSAAN;
    }

    public void selesai() {
        this.status = StatusAntrian.SELESAI;
    }

    public void batalkan() {
        this.status = StatusAntrian.BATAL;
    }

    public String getInfo() {
        return idAntrian + " | No." + nomorUrut + " | " + nomorRekamMedis
                + " | " + jenisKunjungan + " | " + status;
    }
}