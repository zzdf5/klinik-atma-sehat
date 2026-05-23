package model;

/**
 * Model ItemTagihan - satu baris item dalam tagihan
 * @author Sistem Klinik Kesehatan
 */
public class ItemTagihan {
    private String namaItem;
    private KategoriItem kategori;
    private int jumlah;
    private double hargaSatuan;

    public enum KategoriItem {
        KONSULTASI, OBAT, TINDAKAN, LABORATORIUM, ADMINISTRASI, LAINNYA
    }

    public ItemTagihan(String namaItem, KategoriItem kategori, int jumlah, double hargaSatuan) {
        this.namaItem = namaItem;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }
    public void setKategori(KategoriItem kategori) { this.kategori = kategori; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }

    public String getNamaItem() { return namaItem; }
    public KategoriItem getKategori() { return kategori; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }

    public double getSubtotal() {
        return hargaSatuan * jumlah;
    }

    @Override
    public String toString() {
        return String.format("%-25s [%s] x%d  Rp%.0f",
                namaItem, kategori, jumlah, getSubtotal());
    }
}