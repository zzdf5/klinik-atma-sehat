package control;

import dao.AdminDAO;
import exception.DataTidakDitemukanException;
import exception.InputKosongException;
import java.util.List;
import model.Admin;

public class AdminControl {
    private final AdminDAO dao = new AdminDAO();

    public Admin masuk(String username, String password) throws InputKosongException, DataTidakDitemukanException {
        if (username == null || username.isBlank()) throw new InputKosongException("Username");
        if (password == null || password.isBlank()) throw new InputKosongException("Password");

        Admin admin = dao.searchByCredential(username, hash(password));
        if (admin == null) throw new DataTidakDitemukanException("Admin", username);

        return admin;
    }

    public void daftar(String id, String nama, String noTelepon, String username, String password, Admin.Jabatan jabatan)
            throws InputKosongException {
        if (id == null || id.isBlank()) throw new InputKosongException("ID");
        if (nama == null || nama.isBlank()) throw new InputKosongException("Nama");
        if (username == null || username.isBlank()) throw new InputKosongException("Username");
        if (password == null || password.isBlank()) throw new InputKosongException("Password");

        dao.insert(new Admin(id, nama, noTelepon, username, hash(password), jabatan));
    }

    public void insert(Admin data) { dao.insert(data); }
    public void update(Admin data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Admin> showData() { return dao.showData(); }
    public Admin search(String id) { return dao.search(id); }

    private static String hash(String input) {
        long result = 0;
        for (int i = 0; i < input.length(); i++) {
            result = (result + input.charAt(i) * (i + 1)) % 1_000_000_007;
        }
        return String.valueOf(result);
    }
}
