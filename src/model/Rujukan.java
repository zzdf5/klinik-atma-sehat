package model;

public class Rujukan {
    private String idRujukan, idKunjungan, nomorRekamMedis, idDokterPengirim, tujuanRujukan, alasanRujukan, tanggalRujukan, tanggalBerlaku;
    private Status status;

    public enum Status {
        AKTIF, 
        DIGUNAKAN, 
        BATAL
    }

    public Rujukan(String idRujukan, String idKunjungan, String nomorRekamMedis, String idDokterPengirim, String tujuanRujukan, String alasanRujukan,
                   String tanggalRujukan, String tanggalBerlaku) {
        this.idRujukan = idRujukan;
        this.idKunjungan = idKunjungan;
        this.nomorRekamMedis = nomorRekamMedis;
        this.idDokterPengirim = idDokterPengirim;
        this.tujuanRujukan = tujuanRujukan;
        this.alasanRujukan = alasanRujukan;
        this.tanggalRujukan = tanggalRujukan;
        this.tanggalBerlaku = tanggalBerlaku;
        this.status = Status.AKTIF;
    }

    public void setIdRujukan(String idRujukan) { 
        this.idRujukan = idRujukan; 
    }
    
    public void setTujuanRujukan(String tujuanRujukan) { 
        this.tujuanRujukan = tujuanRujukan; 
    }
    
    public void setAlasanRujukan(String alasanRujukan) { 
        this.alasanRujukan = alasanRujukan;
    }
    
    public void setTanggalBerlaku(String tanggalBerlaku) {
        this.tanggalBerlaku = tanggalBerlaku; 
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIdRujukan() { 
        return idRujukan;
    }
    
    public String getIdKunjungan() { 
        return idKunjungan;
    }
    
    public String getNomorRekamMedis() { 
        return nomorRekamMedis;
    }
    
    public String getIdDokterPengirim() {
        return idDokterPengirim;
    }
    
    public String getTujuanRujukan() { 
        return tujuanRujukan;
    }
    
    public String getAlasanRujukan() {
        return alasanRujukan; 
    }
    
    public String getTanggalRujukan() { 
        return tanggalRujukan;
    }
    
    public String getTanggalBerlaku() {
        return tanggalBerlaku;
    }
    
    public Status getStatus() { 
        return status; 
    }

    public String getInfo() {
        return idRujukan + " | " + nomorRekamMedis + " | " + tujuanRujukan + " | " + status;
    }
}
