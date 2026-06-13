package model;

import java.util.ArrayList;
import java.util.List;

public class Resep {
    private String idResep;
    private String idDokter;
    private String nomorRekamMedis;
    private String tanggalResep;
    private Status status;
    private List<ItemResep> daftarObat;

    public enum Status {
        DIPROSES, SELESAI, BATAL
    }

    public Resep(String idResep, String idDokter, String nomorRekamMedis, String tanggalResep) {
        this.idResep = idResep;
        this.idDokter = idDokter;
        this.nomorRekamMedis = nomorRekamMedis;
        this.tanggalResep = tanggalResep;
        this.status = Status.DIPROSES;
        this.daftarObat = new ArrayList<>();
    }

    public void setIdResep(String idResep) { 
        this.idResep = idResep; 
    }
    public void setIdDokter(String idDokter) {
        this.idDokter = idDokter; 
    }
    public void setNomorRekamMedis(String nomorRekamMedis) {
        this.nomorRekamMedis = nomorRekamMedis;
    }
    public void setTanggalResep(String tanggalResep) {
        this.tanggalResep = tanggalResep; 
    }
    public void setStatus(Status status) { 
        this.status = status; 
    }

    public String getIdResep() { 
        return idResep;
    }
    public String getIdDokter() { 
        return idDokter; 
    }
    public String getNomorRekamMedis() { 
        return nomorRekamMedis;
    }
    public String getTanggalResep() { 
        return tanggalResep;
    }
    public Status getStatus() {
        return status; 
    }
    public List<ItemResep> getDaftarObat() { 
        return daftarObat;
    }

    public void tambahObat(ItemResep item) { 
        this.daftarObat.add(item); 
    }

    public String getInfo() {
        return idResep + " | " + nomorRekamMedis + " | " + tanggalResep + " | " + status;
    }

    public static class ItemResep {
        private Obat obat;
        private int jumlah;
        private String aturanPakai;

        public ItemResep(Obat obat, int jumlah, String aturanPakai) {
            this.obat = obat;
            this.jumlah = jumlah;
            this.aturanPakai = aturanPakai;
        }

        public void setObat(Obat obat) {
            this.obat = obat;
        }
        public void setJumlah(int jumlah) { 
            this.jumlah = jumlah;
        }
        public void setAturanPakai(String aturanPakai) {
            this.aturanPakai = aturanPakai; 
        }

        public Obat getObat() {
            return obat; 
        }
        public int getJumlah() {
            return jumlah; 
        }
        public String getAturanPakai() {
            return aturanPakai;
        }
        public String getNamaObat() {
            return obat.getNamaObat(); 
        }
        public double getHarga() {
            return obat.getHargaSatuan(); 
        }

        @Override
        public String toString() {
            return obat.getNamaObat() + " x" + jumlah + " | " + aturanPakai;
        }
    }
}
