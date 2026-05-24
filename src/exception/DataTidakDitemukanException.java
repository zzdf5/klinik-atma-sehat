package exception;

public class DataTidakDitemukanException extends Exception {

    public DataTidakDitemukanException(String namaEntitas, String id) {
        super(namaEntitas + " dengan ID '" + id + "' tidak ditemukan.");
    }
}
