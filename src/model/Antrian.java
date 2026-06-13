package model;

public class Antrian {
    private int idAntrian, nomorUrut;
    private String idPasien, idDokter, idPoliklinik, tanggal;
    private Status status;
    private JenisKunjungan jenisKunjungan;

    public enum Status {
        MENUNGGU, 
        DALAM_PEMERIKSAAN, 
        SELESAI, 
        BATAL
    }

    public enum JenisKunjungan {
        BARU, 
        KONTROL, 
        RUJUKAN
    }

    public Antrian(int idAntrian, int nomorUrut, String idPasien, String idDokter,
                   String idPoliklinik, String tanggal, JenisKunjungan jenisKunjungan) {
        this.idAntrian = idAntrian;
        this.nomorUrut = nomorUrut;
        this.idPasien = idPasien;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.tanggal = tanggal;
        this.jenisKunjungan = jenisKunjungan;
        this.status = Status.MENUNGGU;
    }

    public void setIdAntrian(int idAntrian) { 
        this.idAntrian = idAntrian; 
    }
    
    public void setNomorUrut(int nomorUrut) {
        this.nomorUrut = nomorUrut; 
    }
    
    public void setIdPasien(String idPasien) { 
        this.idPasien = idPasien;
    }
    
    public void setIdDokter(String idDokter) { 
        this.idDokter = idDokter;
    }
    
    public void setIdPoliklinik(String idPoliklinik) { 
        this.idPoliklinik = idPoliklinik; 
    }
    
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal; 
    }
    
    public void setStatus(Status status) {
        this.status = status; 
    }
    
    public void setJenisKunjungan(JenisKunjungan jenisKunjungan) {
        this.jenisKunjungan = jenisKunjungan; 
    }

    public int getIdAntrian() {
        return idAntrian; 
    }
    
    public int getNomorUrut() {
        return nomorUrut; 
    }
    
    public String getIdPasien() {
        return idPasien; 
    }
    
    public String getIdDokter() {
        return idDokter; 
    }
    
    public String getIdPoliklinik() {
        return idPoliklinik; 
    }
    
    public String getTanggal() { 
        return tanggal; 
    }
    
    public Status getStatus() {
        return status; 
    }
    
    public JenisKunjungan getJenisKunjungan() {
        return jenisKunjungan; 
    }

    public String getInfo() {
        return idAntrian + " | No." + nomorUrut + " | " + jenisKunjungan + " | " + status;
    }
}
