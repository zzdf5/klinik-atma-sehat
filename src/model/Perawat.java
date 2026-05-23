package model;

/**
 * Model Perawat - extends Pengguna, bertugas membantu dokter dan mengelola antrian
 * @author Sistem Klinik Kesehatan
 */
public class Perawat extends Pengguna {
    private String nomorSIP;       // Surat Izin Praktik
    private String idPoliklinik;
    private boolean statusAktif;

    public Perawat(String id, String nomorSIP, String nama, String tanggalLahir,
                   String jenisKelamin, String noTelepon, String idPoliklinik,
                   String username, String password) {
        super(id, nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.nomorSIP = nomorSIP;
        this.idPoliklinik = idPoliklinik;
        this.statusAktif = true;
    }

    public Perawat(String nomorSIP, String nama, String tanggalLahir,
                   String jenisKelamin, String noTelepon, String idPoliklinik,
                   String username, String password) {
        super(nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.nomorSIP = nomorSIP;
        this.idPoliklinik = idPoliklinik;
        this.statusAktif = true;
    }

    // Setters
    public void setNomorSIP(String nomorSIP) { this.nomorSIP = nomorSIP; }
    public void setIdPoliklinik(String idPoliklinik) { this.idPoliklinik = idPoliklinik; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    // Getters
    public String getNomorSIP() { return nomorSIP; }
    public String getIdPoliklinik() { return idPoliklinik; }
    public boolean isStatusAktif() { return statusAktif; }

    public void panggilPasien(Antrian antrian) {
        antrian.panggil();
        System.out.println("Perawat " + getNama() + " memanggil pasien no. " + antrian.getNomorUrut());
    }

    public void ukurTandaVital(Pasien pasien, String hasilPengukuran) {
        System.out.println("Perawat " + getNama() + " mengukur tanda vital pasien "
                + pasien.getNama() + ": " + hasilPengukuran);
    }

    public void bantuDokter(Dokter dokter) {
        System.out.println("Perawat " + getNama() + " membantu Dr. " + dokter.getNama());
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Perawat | SIP: " + nomorSIP;
    }

    @Override
    public String getPeran() {
        return "Perawat";
    }

    public String getString() {
        return getId() + " | " + getNama() + " | SIP: " + nomorSIP + " | Poli: " + idPoliklinik;
    }
}