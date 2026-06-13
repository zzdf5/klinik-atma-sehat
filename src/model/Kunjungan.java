package model;

public class Kunjungan {
    private String idKunjungan;
    private String nomorRekamMedis;
    private String idDokter;
    private String tanggal;
    private String jam;
    private String keluhanUtama;
    private String hasilPemeriksaan;
    private String idDiagnosa;
    private String idResep;
    private double biayaKonsultasi;
    private Status status;

    public enum Status {
        BELUM_DILAKUKAN, SELESAI, BATAL
    }

    public Kunjungan(String idKunjungan, String nomorRekamMedis,
                     String tanggal, String jam, String keluhanUtama) {
        this.idKunjungan = idKunjungan;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokter = null;
        this.tanggal = tanggal;
        this.jam = jam;
        this.keluhanUtama = keluhanUtama;
        this.status = Status.BELUM_DILAKUKAN;
    }

    public void setIdKunjungan(String idKunjungan) {
        this.idKunjungan = idKunjungan; 
    }
    public void setNomorRekamMedis(String nomorRekamMedis) {
        this.nomorRekamMedis = nomorRekamMedis;
    }
    public void setIdDokter(String idDokter) { 
        this.idDokter = idDokter; 
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal; 
    }
    public void setJam(String jam) {
        this.jam = jam; 
    }
    public void setKeluhanUtama(String keluhanUtama) {
        this.keluhanUtama = keluhanUtama; 
    }
    public void setHasilPemeriksaan(String hasilPemeriksaan) {
        this.hasilPemeriksaan = hasilPemeriksaan; 
    }
    public void setIdDiagnosa(String idDiagnosa) {
        this.idDiagnosa = idDiagnosa; 
    }
    public void setIdResep(String idResep) {
        this.idResep = idResep; 
    }
    public void setBiayaKonsultasi(double biayaKonsultasi) {
        this.biayaKonsultasi = biayaKonsultasi; 
    }
    public void setStatus(Status status) {
        this.status = status; 
    }

    public String getIdKunjungan() {
        return idKunjungan; 
    }
    public String getNomorRekamMedis() { 
        return nomorRekamMedis;
    }
    public String getIdDokter() { 
        return idDokter; 
    }
    public String getTanggal() {
        return tanggal; 
    }
    public String getJam() {
        return jam; 
    }
    public String getKeluhanUtama() { 
        return keluhanUtama;
    }
    public String getHasilPemeriksaan() { 
        return hasilPemeriksaan;
    }
    public String getIdDiagnosa() {
        return idDiagnosa; 
    }
    public String getIdResep() { 
        return idResep;
    }
    public double getBiayaKonsultasi() {
        return biayaKonsultasi; 
    }
    public Status getStatus() { 
        return status; 
    }

    public String getInfo() {
        return idKunjungan + " | " + tanggal + " " + jam + " | " + status;
    }
}
