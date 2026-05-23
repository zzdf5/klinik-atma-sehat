package model;

/**
 * Model Obat - menyimpan data obat yang tersedia di klinik
 * @author Sistem Klinik Kesehatan
 */
public class Obat {
    private String idObat;
    private String namaObat;
    private String namaGenerik;
    private String bentukSediaan;    // tablet, kapsul, sirup, injeksi, dll
    private String dosis;            // contoh: "500mg"
    private KategoriObat kategori;
    private double hargaSatuan;
    private int stok;
    private int stokMinimum;
    private String keterangan;
    private boolean membutuhkanResep;

    public enum KategoriObat {
        ANTIBIOTIK, ANALGESIK, ANTIHIPERTENSI, ANTIDIABETIK,
        VITAMIN, ANTIHISTAMIN, ANTASIDA, LAINNYA
    }

    public Obat(String idObat, String namaObat, String namaGenerik, String bentukSediaan,
                String dosis, KategoriObat kategori, double hargaSatuan, int stok,
                boolean membutuhkanResep) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.namaGenerik = namaGenerik;
        this.bentukSediaan = bentukSediaan;
        this.dosis = dosis;
        this.kategori = kategori;
        this.hargaSatuan = hargaSatuan;
        this.stok = stok;
        this.stokMinimum = 10;
        this.membutuhkanResep = membutuhkanResep;
    }

    // Setters
    public void setIdObat(String idObat) { this.idObat = idObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public void setNamaGenerik(String namaGenerik) { this.namaGenerik = namaGenerik; }
    public void setBentukSediaan(String bentukSediaan) { this.bentukSediaan = bentukSediaan; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public void setKategori(KategoriObat kategori) { this.kategori = kategori; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
    public void setStok(int stok) { this.stok = stok; }
    public void setStokMinimum(int stokMinimum) { this.stokMinimum = stokMinimum; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public void setMembutuhkanResep(boolean membutuhkanResep) { this.membutuhkanResep = membutuhkanResep; }

    // Getters
    public String getIdObat() { return idObat; }
    public String getNamaObat() { return namaObat; }
    public String getNamaGenerik() { return namaGenerik; }
    public String getBentukSediaan() { return bentukSediaan; }
    public String getDosis() { return dosis; }
    public KategoriObat getKategori() { return kategori; }
    public double getHargaSatuan() { return hargaSatuan; }
    public int getStok() { return stok; }
    public int getStokMinimum() { return stokMinimum; }
    public String getKeterangan() { return keterangan; }
    public boolean isMembutuhkanResep() { return membutuhkanResep; }

    public boolean isStokHabis() {
        return stok <= 0;
    }

    public boolean isStokMenipis() {
        return stok <= stokMinimum;
    }

    public boolean kurangiStok(int jumlah) {
        if (jumlah > stok) {
            System.out.println("Stok " + namaObat + " tidak mencukupi.");
            return false;
        }
        this.stok -= jumlah;
        if (isStokMenipis()) {
            System.out.println("PERINGATAN: Stok " + namaObat + " menipis! Sisa: " + stok);
        }
        return true;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
        System.out.println("Stok " + namaObat + " bertambah " + jumlah + ". Total: " + stok);
    }

    public String getInfo() {
        return idObat + " | " + namaObat + " | " + dosis + " | Stok: " + stok
                + " | Rp" + String.format("%.0f", hargaSatuan);
    }

    @Override
    public String toString() {
        return "[" + idObat + "] " + namaObat + " " + dosis + " (" + bentukSediaan + ")"
                + " | " + kategori + " | Stok: " + stok
                + " | Rp" + String.format("%.0f", hargaSatuan)
                + (membutuhkanResep ? " [Resep]" : "");
    }
}