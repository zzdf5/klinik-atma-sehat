package exception;

public class InputKosongException extends Exception {
    public InputKosongException() {
        super("Input tidak boleh kosong.");
    }
}
