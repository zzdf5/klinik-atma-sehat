package model;

public class Obat {
    private String idObat, namaObat, bentukSediaan, dosis, kategori;
    private double hargaSatuan;
    private int stok;

    public Obat(String idObat, String namaObat, String bentukSediaan, String dosis,
                String kategori, double hargaSatuan, int stok) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.bentukSediaan = bentukSediaan;
        this.dosis = dosis;
        this.kategori = kategori;
        this.hargaSatuan = hargaSatuan;
        this.stok = stok;
    }

    public void setIdObat(String idObat) { 
        this.idObat = idObat; 
    }
    
    public void setNamaObat(String namaObat) { 
        this.namaObat = namaObat; 
    }
    
    public void setBentukSediaan(String bentukSediaan) { 
        this.bentukSediaan = bentukSediaan; 
    }
    
    public void setDosis(String dosis) { 
        this.dosis = dosis; 
    
    }
    
    public void setKategori(String kategori) { 
        this.kategori = kategori; 
    }
    
    public void setHargaSatuan(double hargaSatuan) { 
        this.hargaSatuan = hargaSatuan; 
    }
    
    public void setStok(int stok) { 
        this.stok = stok;
    }

    public String getIdObat() { 
        return idObat; 
    }
    
    public String getNamaObat() {
        return namaObat;
    }
    
    public String getBentukSediaan() { 
        return bentukSediaan; 
    }
    
    public String getDosis() { 
        return dosis;
    }
    
    public String getKategori() { 
        return kategori; 
    }
    
    public double getHargaSatuan() { 
        return hargaSatuan; 
    }
    
    public int getStok() {
        return stok; 
    }

    public String getInfo() {
        return idObat + " | " + namaObat + " " + dosis + " | Stok: " + stok
                + " | Rp" + String.format("%.0f", hargaSatuan);
    }
}
