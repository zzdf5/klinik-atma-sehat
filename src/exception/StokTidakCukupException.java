package exception;

public class StokTidakCukupException extends Exception {

    public StokTidakCukupException(String namaObat, int diminta, int tersedia) {
        super("Stok obat '" + namaObat + "' tidak cukup. Diminta: " + diminta + ", tersedia: " + tersedia + ".");
    }
}
