package model;

import java.util.ArrayList;
import java.util.List;

public class RekamMedis {
    private String nomorRekamMedis;
    private String idPasien;
    private String tanggalBuat;
    private List<String> alergi;
    private List<String> riwayatPenyakit;

    public RekamMedis(String nomorRekamMedis, String idPasien, String tanggalBuat) {
        this.nomorRekamMedis = nomorRekamMedis;
        this.idPasien = idPasien;
        this.tanggalBuat = tanggalBuat;
        this.alergi = new ArrayList<>();
        this.riwayatPenyakit = new ArrayList<>();
    }

    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setIdPasien(String idPasien) { this.idPasien = idPasien; }
    public void setTanggalBuat(String tanggalBuat) { this.tanggalBuat = tanggalBuat; }

    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getIdPasien() { return idPasien; }
    public String getTanggalBuat() { return tanggalBuat; }
    public List<String> getAlergi() { return alergi; }
    public List<String> getRiwayatPenyakit() { return riwayatPenyakit; }

    public void tambahAlergi(String item) { this.alergi.add(item); }
    public void tambahRiwayatPenyakit(String item) { this.riwayatPenyakit.add(item); }

    public String getInfo() {
        return nomorRekamMedis + " | " + idPasien + " | " + tanggalBuat;
    }
}
