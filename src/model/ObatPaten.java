package model;

public class ObatPaten extends Obat {
    private String merk;

    public ObatPaten(String merk, String idObat, String namaObat, String bentukSediaan, String dosis, String kategori, double hargaSatuan, int stok) {
        super(idObat, namaObat, bentukSediaan, dosis, "Obat Paten", hargaSatuan, stok);
        this.merk = merk;
    }

    public void setMerk(String merk) { 
        this.merk = merk; 
    }
    
    public String getMerk() { 
        return merk;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Merk: " + merk;
    }

}
