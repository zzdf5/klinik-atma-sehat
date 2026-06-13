package model;

import java.util.ArrayList;
import java.util.List;

public class Tagihan {
    private String idTagihan;
    private String idKunjungan;
    private String tanggalTagihan;
    private double totalTagihan;
    private double jumlahBayar;
    private double kembalian;
    private MetodePembayaran metodePembayaran;
    private Status status;
    private List<ItemTagihan> daftarItem;

    public enum MetodePembayaran {
        TUNAI, BPJS, TRANSFER, DEBIT
    }

    public enum Status {
        BELUM_BAYAR, LUNAS
    }

    public Tagihan(String idTagihan, String idKunjungan, String tanggalTagihan) {
        this.idTagihan = idTagihan;
        this.idKunjungan = idKunjungan;
        this.tanggalTagihan = tanggalTagihan;
        this.status = Status.BELUM_BAYAR;
        this.daftarItem = new ArrayList<>();
    }

    public void setIdTagihan(String idTagihan) { 
        this.idTagihan = idTagihan;
    }
    public void setIdKunjungan(String idKunjungan) {
        this.idKunjungan = idKunjungan; 
    }
    public void setTanggalTagihan(String tanggalTagihan) { 
        this.tanggalTagihan = tanggalTagihan; 
    }
    public void setMetodePembayaran(MetodePembayaran metodePembayaran) {
        this.metodePembayaran = metodePembayaran; 
    }
    public void setStatus(Status status) { 
        this.status = status;
    }
    public void setJumlahBayar(double jumlahBayar) { 
        this.jumlahBayar = jumlahBayar; 
    }
    public void setKembalian(double kembalian) { 
        this.kembalian = kembalian;
    }
    public void setTotalTagihan(double totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public String getIdTagihan() {
        return idTagihan;
    }
    public String getIdKunjungan() { 
        return idKunjungan; 
    }
    public String getTanggalTagihan() { 
        return tanggalTagihan; 
    }
    public double getTotalTagihan() { 
        return totalTagihan;
    }
    public double getJumlahBayar() {
        return jumlahBayar; 
    }
    public double getKembalian() {
        return kembalian; 
    }
    public MetodePembayaran getMetodePembayaran() {
        return metodePembayaran; 
    }
    public Status getStatus() {
        return status;
    }
    public List<ItemTagihan> getDaftarItem() {
        return daftarItem; 
    }

    public void tambahItem(ItemTagihan item) {
        this.daftarItem.add(item);
    }

    public String getInfo() {
        return idTagihan + " | Rp" + String.format("%.0f", totalTagihan) + " | " + status;
    }

    public static class ItemTagihan {
        private String namaItem;
        private int jumlah;
        private double hargaSatuan;

        public ItemTagihan(String namaItem, int jumlah, double hargaSatuan) {
            this.namaItem = namaItem;
            this.jumlah = jumlah;
            this.hargaSatuan = hargaSatuan;
        }

        public void setNamaItem(String namaItem) { 
            this.namaItem = namaItem; 
        }
        public void setJumlah(int jumlah) { 
            this.jumlah = jumlah; 
        }
        public void setHargaSatuan(double hargaSatuan) {
            this.hargaSatuan = hargaSatuan; 
        }

        public String getNamaItem() {
            return namaItem; 
        }
        public int getJumlah() { 
            return jumlah; 
        }
        public double getHargaSatuan() {
            return hargaSatuan;
        }

        @Override
        public String toString() {
            return String.format("%-25s x%d  Rp%.0f", namaItem, jumlah, hargaSatuan);
        }
    }
}
