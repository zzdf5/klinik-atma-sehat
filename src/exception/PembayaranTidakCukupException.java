package exception;

public class PembayaranTidakCukupException extends Exception {

    public PembayaranTidakCukupException(double jumlahBayar, double totalTagihan) {
        super("Jumlah bayar Rp" + String.format("%.0f", jumlahBayar)
                + " kurang dari total tagihan Rp" + String.format("%.0f", totalTagihan) + ".");
    }
}
