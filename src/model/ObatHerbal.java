package model;

public class ObatHerbal extends Obat {
    private String bahanUtama;

    public ObatHerbal(String bahanUtama, String idObat, String namaObat, String bentukSediaan, String dosis, String kategori, double hargaSatuan, int stok) {
        super(idObat, namaObat, bentukSediaan, dosis, "Obat Herbal", hargaSatuan, stok);
        this.bahanUtama = bahanUtama;
    }

    public void setBahanUtama(String bahanUtama) { 
        this.bahanUtama = bahanUtama; 
    }
    
    public String getBahanUtama() { 
        return bahanUtama; 
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Bahan Utama: " + bahanUtama;
    }

}
