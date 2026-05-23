package model;

public class Admin extends Pengguna {
    private boolean statusAktif;
    private Jabatan jabatan;

    public enum Jabatan { KARYAWAN, MANAGER }

    public Admin(String id, String nama, String noTelepon,
                 String username, String password, Jabatan jabatan) {
        super(id, nama, noTelepon, username, password);
        this.jabatan = jabatan;
        this.statusAktif = true;
    }

    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }
    public void setJabatan(Jabatan jabatan) { this.jabatan = jabatan; }

    public boolean isStatusAktif() { return statusAktif; }
    public Jabatan getJabatan() { return jabatan; }

    @Override
    public String getPeran() { return "Admin"; }
}
