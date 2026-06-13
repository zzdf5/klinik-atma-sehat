package model;

public class Pasien {
    private String id, nomorRekamMedis, nama, tanggalLahir, jenisKelamin, noTelepon, alamat;

    public Pasien(String id, String nomorRekamMedis, String nama, String tanggalLahir, String jenisKelamin, String noTelepon, String alamat) {
        this.id = id;
        this.nomorRekamMedis = nomorRekamMedis;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
    }

    public void setId(String id) { 
        this.id = id; 
    }
    
    public void setNomorRekamMedis(String nomorRekamMedis) {
        this.nomorRekamMedis = nomorRekamMedis; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    
    public void setTanggalLahir(String tanggalLahir) { 
        this.tanggalLahir = tanggalLahir; 
    }
    
    public void setJenisKelamin(String jenisKelamin) { 
        this.jenisKelamin = jenisKelamin; 
    }
    
    public void setNoTelepon(String noTelepon) { 
        this.noTelepon = noTelepon;
    }
    
    public void setAlamat(String alamat) { 
        this.alamat = alamat; 
    }

    public String getId() { 
        return id; 
    }
    
    public String getNomorRekamMedis() { 
        return nomorRekamMedis; 
    }
    
    public String getNama() {
        return nama;
    }
    
    public String getTanggalLahir() {
        return tanggalLahir; 
    }
    
    public String getJenisKelamin() {
        return jenisKelamin;
    }
    
    public String getNoTelepon() { 
        return noTelepon; 
    }
    
    public String getAlamat() { 
        return alamat;
    }

    public String getInfo() {
        return id + " | " + nomorRekamMedis + " | " + nama + " | " + jenisKelamin;
    }
}
