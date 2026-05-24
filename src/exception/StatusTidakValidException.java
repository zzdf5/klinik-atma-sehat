package exception;

public class StatusTidakValidException extends Exception {

    public StatusTidakValidException(String namaEntitas, String statusSaatIni, String operasi) {
        super("Tidak dapat melakukan '" + operasi + "' pada " + namaEntitas
                + " dengan status '" + statusSaatIni + "'.");
    }
}
