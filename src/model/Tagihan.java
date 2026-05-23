package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model Tagihan - merepresentasikan tagihan pembayaran pasien
 * @author Sistem Klinik Kesehatan
 */
public class Tagihan {
    private String idTagihan;
    private String idKunjungan;
    private String nomorRekamMedis;
    private LocalDate tanggalTagihan;
    private LocalDateTime tanggalBayar;
    private List<ItemTagihan> daftarItem;
    private double totalTagihan;
    private double jumlahBayar;
    private double kembalian;
    private MetodePembayaran metodePembayaran;
    private StatusTagihan status;

    public enum MetodePembayaran {
        TUNAI, BPJS, ASURANSI, TRANSFER, KARTU_DEBIT, KARTU_KREDIT
    }

    public enum StatusTagihan {
        BELUM_BAYAR, SEBAGIAN, LUNAS, BATAL
    }

    public Tagihan(String idTagihan, String idKunjungan, String nomorRekamMedis) {
        this.idTagihan = idTagihan;
        this.idKunjungan = idKunjungan;
        this.nomorRekamMedis = nomorRekamMedis;
        this.tanggalTagihan = LocalDate.now();
        this.daftarItem = new ArrayList<>();
        this.status = StatusTagihan.BELUM_BAYAR;
    }

    // Setters
    public void setIdTagihan(String idTagihan) { this.idTagihan = idTagihan; }
    public void setIdKunjungan(String idKunjungan) { this.idKunjungan = idKunjungan; }
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setTanggalTagihan(LocalDate tanggalTagihan) { this.tanggalTagihan = tanggalTagihan; }
    public void setMetodePembayaran(MetodePembayaran metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public void setStatus(StatusTagihan status) { this.status = status; }

    // Getters
    public String getIdTagihan() { return idTagihan; }
    public String getIdKunjungan() { return idKunjungan; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public LocalDate getTanggalTagihan() { return tanggalTagihan; }
    public LocalDateTime getTanggalBayar() { return tanggalBayar; }
    public List<ItemTagihan> getDaftarItem() { return daftarItem; }
    public double getTotalTagihan() { return totalTagihan; }
    public double getJumlahBayar() { return jumlahBayar; }
    public double getKembalian() { return kembalian; }
    public MetodePembayaran getMetodePembayaran() { return metodePembayaran; }
    public StatusTagihan getStatus() { return status; }

    public void tambahItem(ItemTagihan item) {
        this.daftarItem.add(item);
        hitungTotal();
    }

    private void hitungTotal() {
        this.totalTagihan = daftarItem.stream()
                .mapToDouble(ItemTagihan::getSubtotal)
                .sum();
    }

    public boolean bayar(double jumlahBayar, MetodePembayaran metode) {
        if (jumlahBayar < totalTagihan) {
            System.out.println("Pembayaran kurang. Kurang: Rp"
                    + String.format("%.0f", totalTagihan - jumlahBayar));
            this.status = StatusTagihan.SEBAGIAN;
            return false;
        }
        this.jumlahBayar = jumlahBayar;
        this.kembalian = jumlahBayar - totalTagihan;
        this.metodePembayaran = metode;
        this.tanggalBayar = LocalDateTime.now();
        this.status = StatusTagihan.LUNAS;
        System.out.println("Pembayaran berhasil. Kembalian: Rp"
                + String.format("%.0f", kembalian));
        return true;
    }

    public void batalkan() {
        this.status = StatusTagihan.BATAL;
    }

    public String getInfo() {
        return idTagihan + " | " + nomorRekamMedis + " | Rp"
                + String.format("%.0f", totalTagihan) + " | " + status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== TAGIHAN ==========\n");
        sb.append("ID Tagihan   : ").append(idTagihan).append("\n");
        sb.append("Kunjungan    : ").append(idKunjungan).append("\n");
        sb.append("Pasien (RM)  : ").append(nomorRekamMedis).append("\n");
        sb.append("Tanggal      : ").append(tanggalTagihan).append("\n");
        sb.append("-----------------------------\n");
        for (ItemTagihan item : daftarItem) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("-----------------------------\n");
        sb.append("TOTAL        : Rp").append(String.format("%.0f", totalTagihan)).append("\n");
        if (status == StatusTagihan.LUNAS) {
            sb.append("Dibayar      : Rp").append(String.format("%.0f", jumlahBayar)).append("\n");
            sb.append("Kembalian    : Rp").append(String.format("%.0f", kembalian)).append("\n");
            sb.append("Metode       : ").append(metodePembayaran).append("\n");
        }
        sb.append("Status       : ").append(status);
        return sb.toString();
    }
}