package model;

import java.time.LocalDate;

/**
 * Model Rujukan - surat rujukan pasien ke dokter/RS lain
 * @author Sistem Klinik Kesehatan
 */
public class Rujukan {
    private String idRujukan;
    private String idKunjungan;
    private String nomorRekamMedis;
    private String idDokterPengirim;
    private String tujuanRujukan;       // nama dokter/RS tujuan
    private String spesialisasiTujuan;
    private String alasanRujukan;
    private String diagnosaAwal;
    private LocalDate tanggalRujukan;
    private LocalDate tanggalBerlaku;   // masa berlaku surat rujukan
    private StatusRujukan status;

    public enum StatusRujukan {
        AKTIF, DIGUNAKAN, KADALUARSA, DIBATALKAN
    }

    public Rujukan(String idRujukan, String idKunjungan, String nomorRekamMedis,
                   String idDokterPengirim, String tujuanRujukan, String spesialisasiTujuan,
                   String alasanRujukan, String diagnosaAwal) {
        this.idRujukan = idRujukan;
        this.idKunjungan = idKunjungan;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokterPengirim = idDokterPengirim;
        this.tujuanRujukan = tujuanRujukan;
        this.spesialisasiTujuan = spesialisasiTujuan;
        this.alasanRujukan = alasanRujukan;
        this.diagnosaAwal = diagnosaAwal;
        this.tanggalRujukan = LocalDate.now();
        this.tanggalBerlaku = LocalDate.now().plusDays(30);
        this.status = StatusRujukan.AKTIF;
    }

    // Setters
    public void setIdRujukan(String idRujukan) { this.idRujukan = idRujukan; }
    public void setTujuanRujukan(String tujuanRujukan) { this.tujuanRujukan = tujuanRujukan; }
    public void setAlasanRujukan(String alasanRujukan) { this.alasanRujukan = alasanRujukan; }
    public void setTanggalBerlaku(LocalDate tanggalBerlaku) { this.tanggalBerlaku = tanggalBerlaku; }
    public void setStatus(StatusRujukan status) { this.status = status; }

    // Getters
    public String getIdRujukan() { return idRujukan; }
    public String getIdKunjungan() { return idKunjungan; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getIdDokterPengirim() { return idDokterPengirim; }
    public String getTujuanRujukan() { return tujuanRujukan; }
    public String getSpesialisasiTujuan() { return spesialisasiTujuan; }
    public String getAlasanRujukan() { return alasanRujukan; }
    public String getDiagnosaAwal() { return diagnosaAwal; }
    public LocalDate getTanggalRujukan() { return tanggalRujukan; }
    public LocalDate getTanggalBerlaku() { return tanggalBerlaku; }
    public StatusRujukan getStatus() { return status; }

    public boolean isKadaluarsa() {
        return LocalDate.now().isAfter(tanggalBerlaku);
    }

    public void gunakan() {
        if (isKadaluarsa()) {
            System.out.println("Rujukan sudah kadaluarsa.");
            this.status = StatusRujukan.KADALUARSA;
            return;
        }
        this.status = StatusRujukan.DIGUNAKAN;
        System.out.println("Rujukan " + idRujukan + " telah digunakan.");
    }

    public String getInfo() {
        return idRujukan + " | " + nomorRekamMedis + " | " + tujuanRujukan
                + " | " + spesialisasiTujuan + " | " + status;
    }

    @Override
    public String toString() {
        return "=== SURAT RUJUKAN ===\n"
                + "ID Rujukan    : " + idRujukan + "\n"
                + "Pasien (RM)   : " + nomorRekamMedis + "\n"
                + "Dokter Pengirim: " + idDokterPengirim + "\n"
                + "Tujuan        : " + tujuanRujukan + " (" + spesialisasiTujuan + ")\n"
                + "Alasan        : " + alasanRujukan + "\n"
                + "Diagnosa Awal : " + diagnosaAwal + "\n"
                + "Tgl Rujukan   : " + tanggalRujukan + "\n"
                + "Berlaku s/d   : " + tanggalBerlaku + "\n"
                + "Status        : " + status;
    }
}