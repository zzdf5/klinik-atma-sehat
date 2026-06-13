package model;

public class Dokter extends Pengguna {
    private String nomorSTR;
    private String spesialisasi;
    private double tarifKonsultasi;
    private boolean statusAktif;

    public Dokter(String id, String nomorSTR, String nama, String noTelepon,
                  String spesialisasi, double tarifKonsultasi,
                  String username, String password) {
        super(id, nama, noTelepon, username, password);
        this.nomorSTR = nomorSTR;
        this.spesialisasi = spesialisasi;
        this.tarifKonsultasi = tarifKonsultasi;
        this.statusAktif = true;
    }

    public void setNomorSTR(String nomorSTR) {
        this.nomorSTR = nomorSTR; 
    }
    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi; 
    }
    public void setTarifKonsultasi(double tarifKonsultasi) {
        this.tarifKonsultasi = tarifKonsultasi; 
    }
    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getNomorSTR() {
        return nomorSTR; 
    }
    public String getSpesialisasi() {
        return spesialisasi;
    }
    public double getTarifKonsultasi() {
        return tarifKonsultasi; 
    }
    public boolean isStatusAktif() {
        return statusAktif;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | " + spesialisasi + " | STR: " + nomorSTR;
    }

    @Override
    public String getPeran() { return "Dokter"; }
}
