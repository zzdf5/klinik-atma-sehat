package exception;

public class InputSpecialAttributeException extends Exception {

    public InputSpecialAttributeException(String namaField) {
        super("Field '" + namaField + "' mengandung karakter yang tidak diizinkan.");
    }
}
