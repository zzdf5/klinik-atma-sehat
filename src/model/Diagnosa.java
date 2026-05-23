package model;

import java.time.LocalDate;

/**
 * Model Diagnosa - menyimpan hasil diagnosa dokter terhadap pasien
 * @author Sistem Klinik Kesehatan
 */
public class Diagnosa {
    private String idDiagnosa;
    private String kodePenyakit;   // kode ICD-10
    private String namaPenyakit;
    private String keterangan;
    private String tingkatKeparahan; // Ringan, Sedang, Berat
    private LocalDate tanggalDiagnosa;
    private String tindakan;         // tindakan yang direkomendasikan
    private boolean perluRujukan;

    public Diagnosa(String kodePenyakit, String keterangan, String tingkatKeparahan) {
        this.kodePenyakit = kodePenyakit;
        this.keterangan = keterangan;
        this.tingkatKeparahan = tingkatKeparahan;
        this.tanggalDiagnosa = LocalDate.now();
        this.perluRujukan = false;
    }

    public Diagnosa(String idDiagnosa, String kodePenyakit, String namaPenyakit,
                    String keterangan, String tingkatKeparahan, LocalDate tanggalDiagnosa,
                    String tindakan, boolean perluRujukan) {
        this.idDiagnosa = idDiagnosa;
        this.kodePenyakit = kodePenyakit;
        this.namaPenyakit = namaPenyakit;
        this.keterangan = keterangan;
        this.tingkatKeparahan = tingkatKeparahan;
        this.tanggalDiagnosa = tanggalDiagnosa;
        this.tindakan = tindakan;
        this.perluRujukan = perluRujukan;
    }

    // Setters
    public void setIdDiagnosa(String idDiagnosa) { this.idDiagnosa = idDiagnosa; }
    public void setKodePenyakit(String kodePenyakit) { this.kodePenyakit = kodePenyakit; }
    public void setNamaPenyakit(String namaPenyakit) { this.namaPenyakit = namaPenyakit; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public void setTingkatKeparahan(String tingkatKeparahan) { this.tingkatKeparahan = tingkatKeparahan; }
    public void setTanggalDiagnosa(LocalDate tanggalDiagnosa) { this.tanggalDiagnosa = tanggalDiagnosa; }
    public void setTindakan(String tindakan) { this.tindakan = tindakan; }
    public void setPerluRujukan(boolean perluRujukan) { this.perluRujukan = perluRujukan; }

    // Getters
    public String getIdDiagnosa() { return idDiagnosa; }
    public String getKodePenyakit() { return kodePenyakit; }
    public String getNamaPenyakit() { return namaPenyakit; }
    public String getKeterangan() { return keterangan; }
    public String getTingkatKeparahan() { return tingkatKeparahan; }
    public LocalDate getTanggalDiagnosa() { return tanggalDiagnosa; }
    public String getTindakan() { return tindakan; }
    public boolean isPerluRujukan() { return perluRujukan; }

    public String getInfo() {
        return idDiagnosa + " | " + kodePenyakit + " | " + namaPenyakit
                + " | " + tingkatKeparahan + " | " + tanggalDiagnosa;
    }

    @Override
    public String toString() {
        return "=== DIAGNOSA ===\n"
                + "ID           : " + idDiagnosa + "\n"
                + "Kode ICD-10  : " + kodePenyakit + "\n"
                + "Penyakit     : " + namaPenyakit + "\n"
                + "Keterangan   : " + keterangan + "\n"
                + "Keparahan    : " + tingkatKeparahan + "\n"
                + "Tindakan     : " + tindakan + "\n"
                + "Perlu Rujukan: " + (perluRujukan ? "Ya" : "Tidak") + "\n"
                + "Tanggal      : " + tanggalDiagnosa;
    }
}