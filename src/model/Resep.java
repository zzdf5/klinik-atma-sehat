package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model Resep - menyimpan data resep obat dari dokter untuk pasien
 * @author Sistem Klinik Kesehatan
 */
public class Resep {
    private String idResep;
    private String idDokter;
    private String nomorRekamMedis;
    private LocalDate tanggalResep;
    private List<ItemResep> daftarObat;
    private String catatan;
    private StatusResep status;

    public enum StatusResep {
        MENUNGGU, DIPROSES, SELESAI, DIBATALKAN
    }

    public Resep(String idDokter, String nomorRekamMedis) {
        this.idDokter = idDokter;
        this.nomorRekamMedis = nomorRekamMedis;
        this.tanggalResep = LocalDate.now();
        this.daftarObat = new ArrayList<>();
        this.status = StatusResep.MENUNGGU;
    }

    public Resep(String idResep, String idDokter, String nomorRekamMedis,
                 LocalDate tanggalResep, String catatan) {
        this.idResep = idResep;
        this.idDokter = idDokter;
        this.nomorRekamMedis = nomorRekamMedis;
        this.tanggalResep = tanggalResep;
        this.catatan = catatan;
        this.daftarObat = new ArrayList<>();
        this.status = StatusResep.MENUNGGU;
    }

    // Setters
    public void setIdResep(String idResep) { this.idResep = idResep; }
    public void setIdDokter(String idDokter) { this.idDokter = idDokter; }
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setTanggalResep(LocalDate tanggalResep) { this.tanggalResep = tanggalResep; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public void setStatus(StatusResep status) { this.status = status; }

    // Getters
    public String getIdResep() { return idResep; }
    public String getIdDokter() { return idDokter; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public LocalDate getTanggalResep() { return tanggalResep; }
    public String getCatatan() { return catatan; }
    public StatusResep getStatus() { return status; }
    public List<ItemResep> getDaftarObat() { return daftarObat; }

    public void tambahObat(ItemResep item) {
        this.daftarObat.add(item);
    }

    public void hapusObat(String namaObat) {
        daftarObat.removeIf(item -> item.getNamaObat().equalsIgnoreCase(namaObat));
    }

    public double hitungTotalHarga() {
        return daftarObat.stream()
                .mapToDouble(item -> item.getHarga() * item.getJumlah())
                .sum();
    }

    public String getInfo() {
        return idResep + " | " + nomorRekamMedis + " | " + tanggalResep + " | " + status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESEP OBAT ===\n");
        sb.append("ID Resep     : ").append(idResep).append("\n");
        sb.append("Dokter       : ").append(idDokter).append("\n");
        sb.append("Pasien (RM)  : ").append(nomorRekamMedis).append("\n");
        sb.append("Tanggal      : ").append(tanggalResep).append("\n");
        sb.append("Status       : ").append(status).append("\n");
        sb.append("Daftar Obat  :\n");
        for (ItemResep item : daftarObat) {
            sb.append("  - ").append(item.toString()).append("\n");
        }
        sb.append("Catatan      : ").append(catatan).append("\n");
        sb.append("Total Harga  : Rp").append(String.format("%.0f", hitungTotalHarga()));
        return sb.toString();
    }
}