package model;

public class JadwalDokter {
    private String idJadwal, idDokter, idPoliklinik, jamMulai, jamSelesai;
    private int kuotaPasien;

    public JadwalDokter(String idJadwal, String idDokter, String idPoliklinik, String jamMulai, String jamSelesai, int kuotaPasien) {
        this.idJadwal = idJadwal;
        this.idDokter = idDokter;
        this.idPoliklinik = idPoliklinik;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.kuotaPasien = kuotaPasien;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal; 
    }
    
    public void setIdDokter(String idDokter) {
        this.idDokter = idDokter; 
    }
    
    public void setIdPoliklinik(String idPoliklinik) { 
        this.idPoliklinik = idPoliklinik; 
    }
    
    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai; 
    }
    
    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai; 
    }
    
    public void setKuotaPasien(int kuotaPasien) {
        this.kuotaPasien = kuotaPasien; 
    }

    public String getIdJadwal() {
        return idJadwal; 
    }
    
    public String getIdDokter() { 
        return idDokter; 
    }
    
    public String getIdPoliklinik() {
        return idPoliklinik; 
    }
    
    public String getJamMulai() {
        return jamMulai;
    }
    
    public String getJamSelesai() {
        return jamSelesai; 
    }
    
    public int getKuotaPasien() {
        return kuotaPasien; 
    }

    public String getInfo() {
        return idJadwal + " | " + jamMulai + "-" + jamSelesai + " | Kuota: " + kuotaPasien;
    }
}
