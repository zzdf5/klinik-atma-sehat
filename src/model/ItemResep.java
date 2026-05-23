package model;

/**
 * Model ItemResep - merepresentasikan satu item obat dalam resep
 * @author Sistem Klinik Kesehatan
 */
public class ItemResep {
    private String namaObat;
    private int jumlah;
    private String aturanPakai;   // contoh: "3x1 sesudah makan"
    private String bentukSediaan; // tablet, kapsul, sirup, dll
    private double harga;

    public ItemResep(String namaObat, int jumlah, String aturanPakai,
                     String bentukSediaan, double harga) {
        this.namaObat = namaObat;
        this.jumlah = jumlah;
        this.aturanPakai = aturanPakai;
        this.bentukSediaan = bentukSediaan;
        this.harga = harga;
    }

    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setAturanPakai(String aturanPakai) { this.aturanPakai = aturanPakai; }
    public void setBentukSediaan(String bentukSediaan) { this.bentukSediaan = bentukSediaan; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getNamaObat() { return namaObat; }
    public int getJumlah() { return jumlah; }
    public String getAturanPakai() { return aturanPakai; }
    public String getBentukSediaan() { return bentukSediaan; }
    public double getHarga() { return harga; }

    public double getSubtotal() {
        return harga * jumlah;
    }

    @Override
    public String toString() {
        return namaObat + " (" + bentukSediaan + ") x" + jumlah
                + " | " + aturanPakai + " | Rp" + String.format("%.0f", getSubtotal());
    }
}