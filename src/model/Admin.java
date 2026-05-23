package model;

/**
 * Model Admin - extends Pengguna, mengelola data klinik secara keseluruhan
 * @author Sistem Klinik Kesehatan
 */
public class Admin extends Pengguna {
    private String jabatan;
    private boolean isSuperAdmin;

    public Admin(String id, String nama, String tanggalLahir, String jenisKelamin,
                 String noTelepon, String jabatan, boolean isSuperAdmin,
                 String username, String password) {
        super(id, nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.jabatan = jabatan;
        this.isSuperAdmin = isSuperAdmin;
    }

    public Admin(String nama, String tanggalLahir, String jenisKelamin,
                 String noTelepon, String jabatan, String username, String password) {
        super(nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.jabatan = jabatan;
        this.isSuperAdmin = false;
    }

    // Setters
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    public void setSuperAdmin(boolean superAdmin) { isSuperAdmin = superAdmin; }

    // Getters
    public String getJabatan() { return jabatan; }
    public boolean isSuperAdmin() { return isSuperAdmin; }

    public void kelolaDataDokter(String aksi, Dokter dokter) {
        System.out.println("Admin " + getNama() + " melakukan aksi [" + aksi
                + "] pada data dokter: " + dokter.getNama());
    }

    public void kelolaDataPasien(String aksi, Pasien pasien) {
        System.out.println("Admin " + getNama() + " melakukan aksi [" + aksi
                + "] pada data pasien: " + pasien.getNama());
    }

    public void kelolaJadwalDokter(Dokter dokter, String jadwal) {
        dokter.tambahJadwal(jadwal);
        System.out.println("Admin " + getNama() + " menambahkan jadwal Dr. "
                + dokter.getNama() + ": " + jadwal);
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | " + jabatan + (isSuperAdmin ? " [Super Admin]" : "");
    }

    @Override
    public String getPeran() {
        return isSuperAdmin ? "Super Admin" : "Admin";
    }
}