package model;

public class Diagnosa {
    private String idDiagnosa;
    private String kodePenyakit;
    private String namaPenyakit;
    private String keterangan;
    private String tanggalDiagnosa;
    private boolean perluRujukan;

    public Diagnosa(String idDiagnosa, String kodePenyakit, String namaPenyakit,
                    String keterangan, String tanggalDiagnosa, boolean perluRujukan) {
        this.idDiagnosa = idDiagnosa;
        this.kodePenyakit = kodePenyakit;
        this.namaPenyakit = namaPenyakit;
        this.keterangan = keterangan;
        this.tanggalDiagnosa = tanggalDiagnosa;
        this.perluRujukan = perluRujukan;
    }

    public void setIdDiagnosa(String idDiagnosa) { this.idDiagnosa = idDiagnosa; }
    public void setKodePenyakit(String kodePenyakit) { this.kodePenyakit = kodePenyakit; }
    public void setNamaPenyakit(String namaPenyakit) { this.namaPenyakit = namaPenyakit; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public void setTanggalDiagnosa(String tanggalDiagnosa) { this.tanggalDiagnosa = tanggalDiagnosa; }
    public void setPerluRujukan(boolean perluRujukan) { this.perluRujukan = perluRujukan; }

    public String getIdDiagnosa() { return idDiagnosa; }
    public String getKodePenyakit() { return kodePenyakit; }
    public String getNamaPenyakit() { return namaPenyakit; }
    public String getKeterangan() { return keterangan; }
    public String getTanggalDiagnosa() { return tanggalDiagnosa; }
    public boolean isPerluRujukan() { return perluRujukan; }

    public String getInfo() {
        return idDiagnosa + " | " + kodePenyakit + " | " + namaPenyakit + " | " + tanggalDiagnosa;
    }
}
